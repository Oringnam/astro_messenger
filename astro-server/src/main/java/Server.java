import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
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

    private Connection dbConnection = null;
    private PreparedStatement query = null;
    private ResultSet resultSet = null;

    public Server(int port) {
        boolean openSwitch = connect(port);
        connectDB();

        if(openSwitch){
            threadPool();
        } else {
            logger.error("Server Error");
        }
    }

    /*** Grpc 수신 ***/

    private boolean connect(int port) {
        try {
            server = ServerBuilder.forPort(8080).addService(messageImplementation).build().start();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Server is ready");
        return true;
    }

    private boolean connectDB() {
        String driver = "org.mariadb.jdbc.Driver";
        String url = "jdbc:mariadb://localhost:3316/DB명";   //DB명 : 변경사항
        String id = "root";
        String password = "비밀번호";      //변경사항

        //DB 연결과정
        try {
            Class.forName(driver);
            dbConnection = DriverManager.getConnection(url, id, password);

            if(dbConnection != null) {
                logger.info("DBServer is connected");
            }
        } catch(ClassNotFoundException cnfe) {
            logger.error("Driver load fail");
            return false;
        } catch(SQLException e) {
            logger.error("DB connection fail");
            return false;
        }

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

                    store(value);

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

        try {
            String uuid = ((astro.com.message.AstroMessage) value).getUuid();
            long serverTime = ((astro.com.message.AstroMessage) value).getDatetime();
            int index = ((astro.com.message.AstroMessage) value).getIndex();
            String topic = ((astro.com.message.AstroMessage) value).getTopic();
            String message = ((astro.com.message.AstroMessage) value).getMessage();

            String table = "`Message Database`";
            SimpleDateFormat dayTime = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
            String dateTime = dayTime.format(new Date(serverTime));

            System.out.println(dateTime);

            String sql = "insert into " + table + " values ('" + uuid + "', '" + dateTime + "', " + index + ", '" + topic + "', '" + message + "');";   //자료형 : string, datetime, int, string, string 순

            query = dbConnection.prepareStatement(sql);
            resultSet = query.executeQuery();
            logger.info("Message stored");
        } catch(SQLException e) {
            logger.error("Query error");
        }

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
