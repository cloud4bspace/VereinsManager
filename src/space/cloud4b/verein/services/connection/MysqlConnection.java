package space.cloud4b.verein.services.connection;

import space.cloud4b.verein.einstellungen.Einstellung;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {

        private java.sql.Connection conn = null;
        private String dbURL;
        private String user;
        private String pw;

        public MysqlConnection() {
            dbURL = Einstellung.getdbURL();
            //System.out.println("dbURL: " + dbURL);
            /*dbURL = "jdbc:mysql://144.hosttech.eu:3306/usr_web116_1?useUnicode=true&"
                    + "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";*/
            user = "web116";
            pw = "524680_Ab";
            try {
                conn = DriverManager.getConnection(dbURL, user, pw);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public java.sql.Connection getConnection() {
            return conn;
        }

}
