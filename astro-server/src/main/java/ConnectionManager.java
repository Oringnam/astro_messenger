import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private io.grpc.Server server = null;
    public Connection dbConnector = null;

    public boolean clientCoennect(MessageImplementation messageImplementation, int port) {
        try {
            server = ServerBuilder.forPort(port).addService(messageImplementation).build().start();
        } catch(IOException e) {
            logger.info("Server open fail");
            return false;
        }

        logger.info("Server is ready");
        return true;
    }

    public boolean databaseConnect() {
        String driver = "org.mariadb.jdbc.Driver";
        String url = "jdbc:mariadb://localhost:3316/db이름";   //DB명 : 변경사항
        String id = "root";
        String password = "비밀번호";      //변경사항

        //DB 연결과정
        try {
            Class.forName(driver);
            dbConnector = DriverManager.getConnection(url, id, password);

            if(dbConnector != null) {
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

}
