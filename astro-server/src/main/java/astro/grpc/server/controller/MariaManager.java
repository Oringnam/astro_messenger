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

        initDisplay();
    }

    private void initDisplay() {
        logger.info("maria db init --------");
        logger.info("dirver : {} ", driver);
        logger.info("ip : {} ", ip);
        logger.info("port : {} ", port);
        logger.info("database : {} ", database);
        logger.info("url : {}", url);
        logger.info("id : {} ", id);
        logger.info("password : {} ", password);
//        logger.info("table : {} ", id);

        logger.info("------------------\n");
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
            logger.info("Database doesn't exist. Create database...");

            boolean connectSwitch = createDatabase(database);

            if(connectSwitch) {
                logger.info("Database created : {}", database);
                try {
                    url = "jdbc:mariadb://" + ip + ":" + port + "/" + database;
                    dbConnector = DriverManager.getConnection(url, id, password);
                } catch(SQLException se) {
                    logger.error("Connection error");
                }
            }
            return false;
        }

        return true;
    }

    private boolean createDatabase(String dataBase) {
        try {
            url = "jdbc:mariadb://" + ip + ":" + port;
            dbConnector = DriverManager.getConnection(url, id, password);

            String query = "CREATE DATABASE " + "`" + dataBase + "`";
            sql = dbConnector.prepareStatement(query);
            resultSet = sql.executeQuery();
        } catch(SQLException e) {
            logger.error("Database creation error");

            return false;
        }

        return true;
    }

    public boolean store(astro.com.message.AstroMessage value) {
        if (isFull()) {
            logger.info("Storage is full");
            return false;
        }

        String table = database;

        try {
            sql = setInsertQuery(table, value);
            resultSet = sql.executeQuery();
            storeDisplay(dateTransform(value.getDatetime()));
        } catch (Exception e) {
            logger.error("Storing error");
            logger.info("Table is not on DB. Create Table...");

            boolean createSwitch = createTable(table);
            if(createSwitch) {
                logger.info("Table created");
            }
        }

        return true;
    }

    public PreparedStatement setInsertQuery(String table, astro.com.message.AstroMessage value) {
        String query = null;
        String dateTime = dateTransform(value.getDatetime());

        query = "insert into " + "`" + table + "`" + " values (?, ?, ?, ?, ?)";

        try {
            sql = dbConnector.prepareStatement(query);
            sql.setString(1, value.getUuid());
            sql.setString(2, dateTime);
            sql.setInt(3, value.getIndex());
            sql.setString(4, value.getTopic());
            sql.setString(5, value.getMessage());
        } catch (SQLException e) {
            logger.error("SQL error");

            return null;
        }

        return sql;
    }

    private boolean createTable(String table) {
        try {
            String query = "create table" + "`" + table + "`"
                    + "(`uuid` char(50) not null,"
                    + "`datetime` datetime,"
                    + "`index` int(11),"
                    + "`topic` char(50),"
                    + "`message` char(50),"
                    + "primary key (`uuid`));";

            sql = dbConnector.prepareStatement(query);
            resultSet = sql.executeQuery();
        } catch(SQLException e) {
            logger.error("Table creation error");
            return false;
        }
        return true;
    }

    private String dateTransform(long time) {
        long serverTime = time;

        SimpleDateFormat dayTime = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        String dateTime = dayTime.format(new Date(serverTime));

        return dateTime;
    }

    private void storeDisplay(String dateTime) {
        logger.info("Message stored : {}...", dateTime);
    }


    private boolean isFull() {
        //저장 실패시 return true
        return false;
    }
}
