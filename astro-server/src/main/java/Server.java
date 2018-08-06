import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import message.AstroMessage;
import monitor.AstroMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import astro.com.message.*;

public class Server {
    private ExecutorService service = Executors.newScheduledThreadPool(10);
    private LinkedBlockingQueue<AstroMessage> queue = new LinkedBlockingQueue<>();
    private AtomicBoolean opener = new AtomicBoolean(true);
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private AstroMonitor astromonitor = new AstroMonitor();

    private io.grpc.Server server;
    private MessageImplementation messageImplementation = new MessageImplementation();

    public Server(int port) {
        boolean openSwitch = connect(port);

        if(openSwitch){
            threadPool();
        } else {
            logger.error("Server Error");
        }
    }

    /*** Grpc 수신 ***/

    public boolean connect(int port) {
        try {
            server = ServerBuilder.forPort(8080).addService(messageImplementation).build().start();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Server is ready");
        return true;
    }

    private void threadPool()  {
        service.submit(()-> {
            while(opener.get()) {
                try {
                    Object value = messageImplementation.queue.poll(100, TimeUnit.MILLISECONDS);
                    if(value == null) {
                        continue;
                    } else {
                        astromonitor.increaseMessagecCount();
                    }

                    int index = ((astro.com.message.AstroMessage) value).getIndex();
                    long time = ((astro.com.message.AstroMessage) value).getDatetime();
                    String topic = ((astro.com.message.AstroMessage) value).getTopic();
                    String message = ((astro.com.message.AstroMessage) value).getMessage();
                    String uuid = ((astro.com.message.AstroMessage) value).getUuid();

                    System.out.println(index + " " + time + " " + topic + " " + message + " " + uuid + " ");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 저장 방식 정해지면, 구현해야함. db를 쓸것인지, 파일로 저장할 것인지
    private boolean store(Object value) {
        if(isFull()) {
            logger.info("Storage is full");
            return false;
        }
        logger.info("message : {} ", value.toString());

        return true;
    }

    private boolean isFull() {
        //저장 실패시 return true
        return false;
    }

    public void close() {
        opener.set(false);
        service.shutdown();
    }

    /**
     * getRegistry() 수행 시, gc가 registry 정리해버림.
     * createRegistry(port)로 수행할 것
     */
    public static void main(String[] args) {
        Server server = new Server(8080);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //server.close();
    }

    class MessageImplementation extends TransportGrpc.TransportImplBase {
        public LinkedBlockingQueue<astro.com.message.AstroMessage> queue = new LinkedBlockingQueue<astro.com.message.AstroMessage>();

        @Override
        public void sendMessage(astro.com.message.AstroMessage message, StreamObserver<Return> responseObserver) {
            if (message == null) {
                astromonitor.failedTransferMessageCount(message.getIndex());
                Return result = Return.newBuilder().setReturnCode(1).build();
                responseObserver.onNext(result);
                responseObserver.onCompleted();
                return;
            }

            try {
                queue.put(message);
                astromonitor.increaseTransferMessageCount();
                Return result = Return.newBuilder().setReturnCode(0).build();
                responseObserver.onNext(result);
                responseObserver.onCompleted();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }
}
