/*The Driver Manager class  manages the establishment of connections*/
/*Queries are connected with MySQL server*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Types;

public class LoadDriver {

    public static void main(String[] args) {
        Statement stmt = null;
        ResultSet rs = null;
        connection conn = null;
        try {
            stmt = conn.createStatement();
            stmt = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT foo FROM bar");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" +
                    "user=minty&password=greatsqldb");
            CallableStatement cStmt = conn.prepareCall("{call demoSp(?, ?)}");

            if (stmt.execute("SELECT foo FROM bar")) {
                rs = stmt.getResultSet();
            }

            stmt.executeUpdate("DROP TABLE IF EXISTS autoIncTutorial");
            stmt.executeUpdate(
                    "CREATE TABLE autoIncTutorial ("
                            + "priKey INT NOT NULL AUTO_INCREMENT, "
                            + "dataField VARCHAR(64), PRIMARY KEY (priKey))");

            stmt.executeUpdate(
                    "INSERT INTO autoIncTutorial (dataField) "
                            + "values ('Can I Get the Auto Increment Field?')",
                    Statement.RETURN_GENERATED_KEYS);

            rs = stmt.executeQuery("SELECT priKey, dataField "
                    + "FROM autoIncTutorial");

            rs.moveToInsertRow();

            rs.updateString("dataField", "AUTO INCREMENT here?");
            rs.insertRow();

            int autoIncKeyFromApi = -1;

            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                autoIncKeyFromApi = rs.getInt(1);
            } else {

                // throw an exception from here
            }

            int autoIncKeyFromRS = rs.getInt("priKey");

            System.out.println("Key returned for inserted row: "
                    + autoIncKeyFromRS);

            System.out.println("Key returned from getGeneratedKeys():"
                    + autoIncKeyFromApi);

            cStmt.registerOutParameter(2, Types.INTEGER);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }
    }
}