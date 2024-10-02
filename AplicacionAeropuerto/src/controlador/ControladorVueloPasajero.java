package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ajustes.Conexion;

public class ControladorVueloPasajero {

	public void crearTablaVuelosPasajeros() {
		String sql = "CREATE TABLE `vuelos_pasajeros` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
				+ "  `id_pasajero` int NOT NULL, " + "  `id_vuelo` int NOT NULL, " + "  PRIMARY KEY (`id`), "
				+ "  KEY `vuelos_pasajeros_vuelos_FK` (`id_vuelo`), "
				+ "  KEY `vuelos_pasajeros_pasajeros_FK` (`id_pasajero`), "
				+ "  CONSTRAINT `vuelos_pasajeros_pasajeros_FK` FOREIGN KEY (`id_pasajero`) REFERENCES `pasajeros` (`id`), "
				+ "  CONSTRAINT `vuelos_pasajeros_vuelos_FK` FOREIGN KEY (`id_vuelo`) REFERENCES `vuelos` (`id`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

		try (Connection con = Conexion.getConnection();
				Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stm.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createVueloPasajero(String codigo, String dNI) {
		if (!ControladorPasajero.existePasajero(dNI) && !ControladorVuelo.existeVueloParaVuelosPasajeros(codigo)) {
			throw new IllegalArgumentException("DNI o código del vuelo incorrecto.");
		}

		String sqlObtenerIdPasajero = "SELECT id FROM pasajeros WHERE dNI = ?";
		String sqlObtenerIdVuelo = "SELECT id FROM vuelos WHERE codigo = ?";
		String sqlCrearVueloPasajero = "INSERT INTO vuelos_pasajeros(id_pasajero, id_vuelo) VALUES (?, ?)";

		Connection con = null;
		Savepoint savePoint = null;

		Integer idVuelo = null;
		Integer idPasajero = null;

		try {
			con = Conexion.getConnection();
			con.setAutoCommit(false);
			savePoint = con.setSavepoint("crearVueloPasajero");

			// OBTENER EL ID DE PASAJERO
			try (PreparedStatement psObtenerIdPasajero = con.prepareStatement(sqlObtenerIdPasajero)) {
				psObtenerIdPasajero.setString(1, dNI);
				ResultSet rs = psObtenerIdPasajero.executeQuery();

				if (rs.next()) {
					idPasajero = rs.getInt("id");
				}
				rs.close();
			}

			// OBTENER EL ID DE VUELO
			try (PreparedStatement psObtenerIdVuelo = con.prepareStatement(sqlObtenerIdVuelo)) {
				psObtenerIdVuelo.setString(1, codigo);
				ResultSet rs = psObtenerIdVuelo.executeQuery();

				if (rs.next()) {
					idVuelo = rs.getInt("id");
				}
				rs.close();
			}

			// CREAR VUELOPASAJERO
			try (PreparedStatement psCrearVueloPasajero = con.prepareStatement(sqlCrearVueloPasajero)) {
				psCrearVueloPasajero.setInt(1, idPasajero);
				psCrearVueloPasajero.setInt(2, idVuelo);
				psCrearVueloPasajero.executeUpdate();

				con.commit();
				System.out.println("Pasajero añadido al vuelo correctamente.");

				// ACTUALIZAR EL NÚMERO DE PASAJERO DE UN VUELO
				ControladorVuelo.actualizarNumeroPasajerosVuelo(idVuelo, true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if (savePoint != null) {
				try {
					con.rollback(savePoint);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					System.err.println("Error al cerrar la conexión a la base de datos.");
				}
			}
		}
	}

	public String[] obtenerVuelosDisponibles() {
		List<String> vuelosDisponibles = new ArrayList<>();

		String sql = "SELECT codigo FROM vuelos WHERE completo = 0";

		try (Connection conn = Conexion.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				vuelosDisponibles.add(rs.getString("codigo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vuelosDisponibles.toArray(new String[0]);
	}
}
