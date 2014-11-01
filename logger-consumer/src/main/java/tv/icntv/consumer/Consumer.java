package tv.icntv.consumer;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public abstract class Consumer extends Thread {
    protected Logger logger = LoggerFactory.getLogger(Consumer.class);
    private KafkaStream stream;

    public KafkaStream getStream() {
        return stream;
    }
    private boolean isBatch=true;

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    public void setStream(KafkaStream stream) {
        this.stream = stream;
    }

    public Consumer(KafkaStream stream) {
        this(stream, true);
    }
    public Consumer(KafkaStream stream,boolean isBatch){
        this.stream = stream;
        this.isBatch = isBatch;
    }
    @Override
    public void run() {
        List<MessageAndMetadata<byte[],byte[]>> lists=(List<MessageAndMetadata<byte[],byte[]>>)stream.toList();
        if(isBatch()){
            try {
                executeBatch(lists);
            } catch (Exception e) {
                logger.error("error ", e);
            }
            return;
        }

        for(MessageAndMetadata<byte[],byte[]> mm:lists){
            try {
                execute(mm);
            } catch (Exception e) {
                logger.error("error ",e);
                continue;
            }
        }
//        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
//        Lists.transform((List<MessageAndMetadata>) iterator.toList(),new Function<MessageAndMetadata, Object>() {
//            @Override
//            public Object apply(MessageAndMetadata input) {
//                return null;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//        while (iterator.hasNext()) {
//            MessageAndMetadata<byte[], byte[]> mm = iterator.next();
//            logger.info(new String(mm.message()) + " \t " + new String(mm.topic()));
//
//            try {
//                execute(mm);
//            } catch (Exception e) {
//
//                logger.error("consumer msg error ! ",e);
//            }
//        }
    }

    public abstract void executeBatch(List<MessageAndMetadata<byte[],byte[]>> msgs) throws Exception;

    public  abstract void execute(MessageAndMetadata<byte[],byte[]> msg) throws Exception;

}