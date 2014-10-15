package tv.icntv.consumer;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Consumer extends Thread{
  private Logger logger = LoggerFactory.getLogger(Consumer.class);
    private KafkaStream stream;

    public KafkaStream getStream() {
        return stream;
    }

    public void setStream(KafkaStream stream) {
        this.stream = stream;
    }

    public Consumer(KafkaStream stream){
      this.stream=stream;
  }
  @Override
  public void run() {
      ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
      while (iterator.hasNext()) {
          MessageAndMetadata<byte[],byte[]> mm=iterator.next();
          logger.info(new String(mm.message()) + " \t " + new String(mm.topic()));

      }
  }

}