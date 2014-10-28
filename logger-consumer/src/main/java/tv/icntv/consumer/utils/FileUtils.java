package tv.icntv.consumer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.icntv.logger.common.PropertiesLoaderUtils;

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


public class FileUtils implements Constant{

	Properties pro;
	private Logger logger = LoggerFactory.getLogger(FileUtils.class);
	private String path=null;

	private String fileFormat=null;
	private int rollInterval=1;
	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
	private String hdfs=null;
	private String lzoPath=null;
	public String fileName="";
    private String charset= null;
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileUtils (){
        try {
            pro = PropertiesLoaderUtils.loadAllProperties("icntvStb.properties");
        } catch (IOException e) {
            logger.error("load properties name ={},but null" ,"icntvStb.properties");
        }

        //init
        path = pro.getProperty(SOURCE_PATH,"");
        fileFormat = pro.getProperty(SOURCE_FILE_NAME);
        rollInterval = Integer.parseInt(pro.getProperty(ROLL_INTERVAL, "1"));
        hdfs = pro.getProperty(HDFS_PATH,"hdfs://icntv/icntv/log/stb");
        lzoPath = pro.getProperty(COMPRESSED_PATH,"/data/hadoop/icntv/log/lzoData");
        charset = pro.getProperty("icntv.stb.charset", "utf-8");
        //
		if(!new File(path).exists()){
			new File(path).mkdirs();
		}
		scheduler.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				String tmp=path+File.separator+getCurrentDate();
				File tmpFile=new File(tmp);
				if(!tmpFile.exists()){
					tmpFile.mkdirs();
				}
				String tempName=String.format(fileFormat, getCurrentDate(), new Date());

				setFileName(tmp+File.separator+tempName); //baseFileName+getCurrentDate()+"-"+String.format("%tH",new Date()) +suffix
				logger.info("set basename from "+currentFile+" to "+getFileName());
			}
		}, 0, rollInterval, TimeUnit.HOURS);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};

    }
	
    private String currentFile="";
	public FileChannel getOutChannel(){
		try {
			
				if(!currentFile.equals(getFileName())){
					if(!currentFile.equals("")){
						logger.info("start compress and send to hdfs");
						//new ClientThread(currentFile,lzoPath+File.separator+day(),hdfs+File.separator+day()).start();;
					}
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
