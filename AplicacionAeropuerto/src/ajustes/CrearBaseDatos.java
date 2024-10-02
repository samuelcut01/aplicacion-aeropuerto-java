package ajustes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CrearBaseDatos {
	static String nombrePuerto = "";

	public void crearBaseDatos() {
		try {
			Connection cnx = Conexion.getConnection();
			Statement stm = cnx.createStatement();
			ResultSet rs = stm.executeQuery("SHOW DATABASES LIKE 'aeropuerto'");
			if (rs.next()) {
				nombrePuerto = "aeropuerto";
			} else {
				stm.addBatch("CREATE DATABASE aeropuerto;");
				stm.addBatch("USE aeropuerto");
				stm.executeBatch();
				stm.close();
				nombrePuerto = "aeropuerto";
				createTablas();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void createTablas() {
		try {
			Connection cnx = Conexion.getConnection();
			Statement stm = cnx.createStatement();
			stm.addBatch("CREATE TABLE IF NOT EXISTS `aeropuertos` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
					+ "  `nombre` varchar(100) NOT NULL, " + "  `ciudad` varchar(100) NOT NULL, "
					+ "  PRIMARY KEY (`id`) " + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
			stm.addBatch("CREATE TABLE IF NOT EXISTS `vuelos` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
					+ "  `origen` varchar(100) NOT NULL, " + "  `destino` varchar(100) NOT NULL, "
					+ "  `fechaSalida` timestamp NOT NULL, " + "  `fechaLlegada` timestamp NOT NULL, "
					+ "  `duracion` time NOT NULL, " + "  `numeroPlazas` int NOT NULL, "
					+ "  `numeroPasajeros` int NOT NULL, " + "  `completo` tinyint NOT NULL, " + "  PRIMARY KEY (`id`) "
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
			stm.addBatch("CREATE TABLE `aeropuertos_vuelos` (\r\n" + "  `id` int NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `id_aeropuerto` int NOT NULL,\r\n" + "  `id_vuelo` int NOT NULL,\r\n"
					+ "  PRIMARY KEY (`id`),\r\n" + "  KEY `aeropuertos_vuelos_aeropuertos_FK` (`id_aeropuerto`),\r\n"
					+ "  KEY `aeropuertos_vuelos_vuelos_FK` (`id_vuelo`),\r\n"
					+ "  CONSTRAINT `aeropuertos_vuelos_aeropuertos_FK` FOREIGN KEY (`id_aeropuerto`) REFERENCES `aeropuertos` (`id`) ON DELETE CASCADE,\r\n"
					+ "  CONSTRAINT `aeropuertos_vuelos_vuelos_FK` FOREIGN KEY (`id_vuelo`) REFERENCES `vuelos` (`id`) ON DELETE CASCADE\r\n"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");

			stm.addBatch("CREATE TABLE `pasajeros` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
					+ "  `nombre` varchar(100) NOT NULL, " + "  `apellidos` varchar(100) NOT NULL, "
					+ "  `dNI` varchar(100) NOT NULL, " + "  PRIMARY KEY (`id`) "
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
			stm.addBatch("CREATE TABLE `vuelos_pasajeros` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
					+ "  `id_pasajero` int NOT NULL, " + "  `id_vuelo` int NOT NULL, " + "  PRIMARY KEY (`id`), "
					+ "  KEY `vuelos_pasajeros_vuelos_FK` (`id_vuelo`), "
					+ "  KEY `vuelos_pasajeros_pasajeros_FK` (`id_pasajero`), "
					+ "  CONSTRAINT `vuelos_pasajeros_pasajeros_FK` FOREIGN KEY (`id_pasajero`) REFERENCES `pasajeros` (`id`), "
					+ "  CONSTRAINT `vuelos_pasajeros_vuelos_FK` FOREIGN KEY (`id_vuelo`) REFERENCES `vuelos` (`id`) "
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
			stm.executeBatch();
			stm.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
