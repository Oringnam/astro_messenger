import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.text.SimpleDateFormat;

public class StoringManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private PreparedStatement sql = null;
    private ResultSet resultSet = null;

    public boolean store(Connection dbConnector, Object value) {
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

            String query = "insert into " + table + " values ('" + uuid + "', '" + dateTime + "', " + index + ", '" + topic + "', '" + message + "');";   //자료형 : string, datetime, int, string, string 순

            sql = dbConnector.prepareStatement(query);
            resultSet = sql.executeQuery();
            logger.info("Message stored");
        } catch(SQLException e) {
            logger.error("Query error");
        } catch(Exception e) {
            logger.error("Storing error");
        }

        return true;
    }

    private boolean isFull() {
        //저장 실패시 return true
        return false;
    }

}
