package astro.grpc.server.controller;

import astro.grpc.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.SimpleDateFormat;

public class MariaManager {
    public Connection dbConnector = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private PreparedStatement sql = null;
    private ResultSet resultSet = null;

    private String driver = "org.mariadb.jdbc.Driver";
    private String ip = "localhost";
    private String port = "3306";

    private String database = "myfirstdb";

    private String id = "root";
    private String password = "";

    private String url = "jdbc:mariadb://localhost:3306/sample";

    public void init() {
        try {
            driver = Server.config.get("maria.driver");
            id = Server.config.get("maria.id");
            password = Server.config.get("maria.pw");
            ip = Server.config.get("maria.ip");
            port = Server.config.get("maria.port");
            database = Server.config.get("maria.database");

            url = "jdbc:mariadb://" + ip + ":" + port + "/" + database;
        } catch (NullPointerException e) {
            logger.warn("config getter is failed : {}", e.getMessage());
        }
    }


    public boolean connect() {
        //DB 연결과정
        try {
            Class.forName(driver);
            dbConnector = DriverManager.getConnection(url, id, password);

            if (dbConnector != null) {
                logger.info("DBServer is connected");
            }
        } catch (ClassNotFoundException e) {
            logger.error("Driver load fail");
            return false;
        } catch (SQLException e) {
            logger.error("DB connection fail");
            return false;
        }

        return true;
    }

    public boolean store(Object value) {
        if (isFull()) {
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

            String query = "insert into " + table + " values ('" + uuid + "', '" + dateTime + "', " + index + ", '" +
                    topic + "', '" + message + "');";   //자료형 : string, datetime, int, string, string 순

            sql = dbConnector.prepareStatement(query);
            resultSet = sql.executeQuery();
            logger.info("Message stored");
        } catch (SQLException e) {
            logger.error("Query error");
        } catch (Exception e) {
            logger.error("Storing error");
        }

        return true;
    }

    private boolean isFull() {
        //저장 실패시 return true
        return false;
    }
}
