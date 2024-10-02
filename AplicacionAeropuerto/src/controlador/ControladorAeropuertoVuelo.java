package controlador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ajustes.Conexion;

public class ControladorAeropuertoVuelo {

	public void crearTablaAeropuertosVuelos() {
		String sql = "CREATE TABLE `aeropuertos_vuelos` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
				+ "  `id_aeropuerto` int NOT NULL, " + "  `id_vuelo` int NOT NULL, " + "  PRIMARY KEY (`id`), "
				+ "  KEY `aeropuertos_vuelos_aeropuertos_FK` (`id_aeropuerto`), "
				+ "  KEY `aeropuertos_vuelos_vuelos_FK` (`id_vuelo`), "
				+ "  CONSTRAINT `aeropuertos_vuelos_aeropuertos_FK` FOREIGN KEY (`id_aeropuerto`) REFERENCES `aeropuertos` (`id`) ON DELETE CASCADE, "
				+ "  CONSTRAINT `aeropuertos_vuelos_vuelos_FK` FOREIGN KEY (`id_vuelo`) REFERENCES `vuelos` (`id`) ON DELETE CASCADE "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

		try (Connection con = Conexion.getConnection();
				Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stm.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
