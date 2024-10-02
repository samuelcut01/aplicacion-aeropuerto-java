package ajustes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	public static Connection getConnection() throws SQLException {
		String url = "";
		String user = "";
		String password = "";
		return DriverManager.getConnection(url, user, password);
	}
}
