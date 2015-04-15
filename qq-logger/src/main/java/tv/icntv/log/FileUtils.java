package tv.icntv.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.SchedulerListener;
import it.sauronsoftware.cron4j.TaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.icntv.log.common.PropertiesUtils;
import tv.icntv.sender.HdfsModule;


public class FileUtils {

	Properties pro = PropertiesUtils.getProperties();
	private Logger logger = LoggerFactory.getLogger(FileUtils.class);
	private String path=pro.getProperty("icntv.stb.log.path","/data/hadoop/icntv/log/data/");

	private String baseFileName="qqStb-";
	
	private String suffix=pro.getProperty("icntv.stb.log.file.suffix", ".log");
	private String hdfs=pro.getProperty("icntv.stb.hdfs.url","hdfs://icntv/icntv/log/stb");
	private String lzoPath=pro.getProperty("icntv.stb.compress.log.path","/data/hadoop/icntv/log/lzoData");
	public String fileName="";
    /**
     * 生成文件时间
     */
    private String generateFileCron=pro.getProperty("icntv.stb.file.cron","0 * * * *");

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileUtils (){
		if(!new File(path).exists()){
			new File(path).mkdirs();
		}
        if(Strings.isNullOrEmpty(getFileName())){
            generateFile();
            currentFile=getFileName();
            logger.info("init file name = "+currentFile);
        }
        Scheduler scheculer = new Scheduler();
        scheculer.addSchedulerListener(new SchedulerListener() {
            @Override
            public void taskLaunching(TaskExecutor executor) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void taskSucceeded(TaskExecutor executor) {
                logger.info("set basename from "+currentFile+" to "+getFileName());
            }

            @Override
            public void taskFailed(TaskExecutor executor, Throwable exception) {
                //To change body of implemented methods use File | Settings | File Templates.
                logger.error("set basename error from "+currentFile+" to "+getFileName());
            }
        });
        scheculer.schedule(generateFileCron,new Runnable(){
            @Override
            public void run() {
               generateFile();
            }
        });
        scheculer.start();

	}

    private void generateFile() {
        String tmp=path+File.separator+getCurrentDate();
        File tmpFile=new File(tmp);
        if(!tmpFile.exists()){
            tmpFile.mkdirs();
        }
        String file=tmp+File.separator+baseFileName+getCurrentDate()+"-"+String.format("%tH",new Date()) +suffix;
        setFileName(file);
    }

    private String currentFile="";
	public FileChannel getOutChannel(){
		try {

				if(!currentFile.equals(getFileName())){
						logger.info("start compress and send to hdfs");
//						new ClientThread(currentFile,lzoPath+File.separator+day(),hdfs+File.separator+day()).start();
//                        HdfsModule module = new HdfsModule(new String[]{currentFile,lzoPath+File.separator+day(),"LZO_COMPRESS","UN_COMPRESS_NONE"});
//                        Injector injector = Guice.createInjector(module);//(new String[]{"d:\\douban\\error.txt","d:\\",""}));
//
//                        Runnable client=injector.getInstance(Runnable.class);
//                        new Thread(client).start();
//					}
					currentFile=getFileName();
				}
					
			return new RandomAccessFile(currentFile,"rw").getChannel();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	};
	
	private String getCurrentDate(){
		return String.format("%tF", new Date());
	}
	
	private String day(){
		String tmp = currentFile.substring(currentFile.lastIndexOf(File.separator)+1);
		tmp=tmp.replace(baseFileName, "").replace(suffix, "");
		return tmp.substring(0,tmp.lastIndexOf("-"));
	}
	private String charset=pro.getProperty("icntv.stb.charset", "utf-8");
	public void write(String message){
		FileChannel channel=getOutChannel();
		try {
			channel.position(channel.size());
			channel.write(ByteBuffer.wrap(message.getBytes(charset)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
