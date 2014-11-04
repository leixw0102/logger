package tv.icntv.consumer;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class Consumer extends Thread {
    protected Logger logger = LoggerFactory.getLogger(Consumer.class);
    private KafkaStream stream;

    public KafkaStream getStream() {
        return stream;
    }

    public void setStream(KafkaStream stream) {
        this.stream = stream;
    }

    public Consumer(KafkaStream stream) {
        this.stream = stream;
    }

    @Override
    public void run() {


        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

        while (iterator.hasNext()) {
            MessageAndMetadata<byte[], byte[]> mm = iterator.next();
            try {
                execute(new String(mm.message()));
            } catch (Exception e) {

                logger.error("consumer msg error ! ",e);
            }
        }
    }

   public  abstract void execute(String msg) throws Exception;

}