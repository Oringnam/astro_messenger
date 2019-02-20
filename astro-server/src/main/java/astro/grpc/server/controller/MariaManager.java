package astro.grpc.server.controller;

import astro.grpc.server.Server;
import astro.grpc.server.dao.MariaDAO;
import astro.grpc.server.domain.AstroMessage;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.text.SimpleDateFormat;

public class MariaManager {
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private MariaDAO dao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private PreparedStatement sql = null;
    private ResultSet resultSet = null;

    private String driver = "org.mariadb.jdbc.Driver";
    private String ip = "localhost";
    private String port = "3306";

    private String database = "sample";

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

        logger.info("------------------\n");
    }

    public boolean connect() {
        //DB 연결과정
        try {
            String filePath = "mybatis_config.xml";
            Reader reader = Resources.getResourceAsReader(filePath);

            if(sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                logger.info("DBServer is connected");
            }
        } catch (FileNotFoundException fnfe) {
            logger.error("Config file not found");

            return false;
        } catch (IOException ie) {
            logger.error("DB connection fail");

            return false;
        }

        sqlSession = sqlSessionFactory.openSession(true);

        return true;
    }


    public boolean store(astro.com.message.AstroMessage value) {
        if (isFull()) {
            logger.info("Storage is full"); //저장공간 꽉참
            return false;
        }

        dao = sqlSession.getMapper(MariaDAO.class);
        String table = selectTable(value.getTopic());

        createTable(table);

        try {
            AstroMessage storingMessage = setMessage(value);
            dao.insert(table, storingMessage);
            storeDisplay(storingMessage.getDatetime());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public AstroMessage setMessage(astro.com.message.AstroMessage value) {
        String dateTime = dateTransform(value.getDatetime());

        AstroMessage astroMessage = new AstroMessage();

        astroMessage.setUuid(value.getUuid());
        astroMessage.setDatetime(dateTime);
        astroMessage.setIndex(value.getIndex());
        astroMessage.setTopic(value.getTopic());
        astroMessage.setMessage(value.getMessage());

        return astroMessage;
    }

    private boolean createTable(String table) {
        try {
            dao.createTable(table);
        } catch(Exception e) {
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

    private String selectTable(String topic) {
        String tableName = topic;

        return tableName;
    }
}
