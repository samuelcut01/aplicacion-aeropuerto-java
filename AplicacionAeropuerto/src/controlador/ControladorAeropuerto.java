package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import ajustes.Conexion;
import modelo.Aeropuerto;
import modelo.Vuelo;

public class ControladorAeropuerto {

	public static void crearTablaAeropuerto() {
		String sql = "CREATE TABLE IF NOT EXISTS `aeropuertos` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
				+ "  `nombre` varchar(100) NOT NULL, " + "  `ciudad` varchar(100) NOT NULL, " + "  PRIMARY KEY (`id`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

		try (Connection con = Conexion.getConnection();
				Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stm.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Integer createAeropuerto(Aeropuerto aeropuerto) {
		String nombre = aeropuerto.getNombre();
		String ciudad = aeropuerto.getCiudad();

		if (nombre == null || nombre.trim().isEmpty() || ciudad == null || ciudad.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre y la ciudad del aeropuerto no pueden estar vacíos.");
		}

		if (existeAeropuertoParaAeropuerto(aeropuerto.getNombre(), aeropuerto.getCiudad())) {
			throw new IllegalArgumentException("El aeropuerto ya existe. No se puede volver a añadir.");
		}

		String sql = "INSERT INTO aeropuertos (nombre, ciudad) VALUES (?, ?)";

		try (Connection con = Conexion.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {

			ps.setString(1, aeropuerto.getNombre());
			ps.setString(2, aeropuerto.getCiudad());

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteAeropuertoPorIdAeropuerto(Integer id_aeropuerto) {
		String sqlSeleccionarVueloId = "SELECT id_vuelo FROM aeropuertos_vuelos WHERE id_aeropuerto = ?";
		String sqlEliminarVueloPasajero = "DELETE FROM vuelos_pasajeros WHERE id_vuelo = ?";
		String sqlEliminarAeropuertoVuelo = "DELETE FROM aeropuertos_vuelos WHERE id_aeropuerto = ?";
		String sqlEliminarVuelo = "DELETE FROM vuelos WHERE id = ?";
		String sqlEliminarAeropuerto = "DELETE FROM aeropuertos WHERE id = ?";

		List<Integer> idVuelos = new ArrayList<Integer>();

		Connection con = null;
		Savepoint savePoint = null;
		ResultSet rs = null;

		try {

			con = Conexion.getConnection();
			con.setAutoCommit(false);

			// Crear el Savepoint
			savePoint = con.setSavepoint("eliminarAeropuerto");

			PreparedStatement psSeleccionarVuelo_id = con.prepareStatement(sqlSeleccionarVueloId,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			// SELECCIONAR Y AÑADIR EN UN ARRAYLIST LOS VUELO_ID
			psSeleccionarVuelo_id.setInt(1, id_aeropuerto);
			rs = psSeleccionarVuelo_id.executeQuery();

			while (rs.next()) {
				idVuelos.add(rs.getInt("id_vuelo"));
			}

			rs.close();
			psSeleccionarVuelo_id.close();

			// RECORRER LA LISTA Y ELIMINAR EN VUELOS_PASAJEROS SI TIENE EL VUELO_ID
			PreparedStatement psEliminarVuelos_pasajeros = con.prepareStatement(sqlEliminarVueloPasajero,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			for (Integer elemento : idVuelos) {
				psEliminarVuelos_pasajeros.setInt(1, elemento);
				psEliminarVuelos_pasajeros.executeUpdate();
			}

			psEliminarVuelos_pasajeros.close();

			// ELIMINAR EN AEROPUERTO_VUELO
			PreparedStatement psEliminarAeropuertoVuelo = con.prepareStatement(sqlEliminarAeropuertoVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			psEliminarAeropuertoVuelo.setInt(1, id_aeropuerto);
			psEliminarAeropuertoVuelo.executeUpdate();

			psEliminarAeropuertoVuelo.close();

			// ELIMINAR EN VUELO
			PreparedStatement psEliminarVuelo = con.prepareStatement(sqlEliminarVuelo, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			for (Integer elemento : idVuelos) {
				psEliminarVuelo.setInt(1, elemento);
				psEliminarVuelo.executeUpdate();
			}

			psEliminarVuelo.close();

			// ELIMINAR EN AEROPUERTO
			PreparedStatement psEliminarAeropuerto = con.prepareStatement(sqlEliminarAeropuerto,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			psEliminarAeropuerto.setInt(1, id_aeropuerto);
			Integer columnasAfectadasAeropuertos = psEliminarAeropuerto.executeUpdate();

			psEliminarAeropuerto.close();

			if (columnasAfectadasAeropuertos > 0) {
				con.commit();
				System.out.println("Aeropuerto junto a sus vuelos eliminados éxitosamente.");
			} else {
				con.rollback();
				System.out.println("No se encontró ningún aeropuerto con el código proporcionado.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			if (savePoint != null) {
				try {
					// Revertir la transacción hasta el Savepoint
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
					System.err.println("Error al cerrar la conexión con la base de datos.");
				}
			}
			if (rs != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					System.err.println("Error al cerrar ResultSet.");
				}
			}
		}
	}

	public void deleteAeropuertoPorNombreYCiudad(String nombre, String ciudad) {
		String sqlSeleccionarIdAeropuerto = "SELECT id FROM aeropuertos WHERE nombre = ? AND ciudad = ?";
		String sqlSeleccionarIdVuelo = "SELECT id_vuelo FROM aeropuertos_vuelos WHERE id_aeropuerto = ?";
		String sqlEliminarVueloPasajero = "DELETE FROM vuelos_pasajeros WHERE id_vuelo = ?";
		String sqlEliminarAeropuertoVuelo = "DELETE FROM aeropuertos_vuelos WHERE id_aeropuerto = ?";
		String sqlEliminarVuelo = "DELETE FROM vuelos WHERE id = ?";
		String sqlEliminarAeropuerto = "DELETE FROM aeropuertos WHERE id = ?";

		List<Integer> idVuelos = new ArrayList<Integer>();
		Integer idAeropuerto = 0;

		Connection con = null;
		Savepoint savePoint = null;
		ResultSet rs = null;

		try {
			con = Conexion.getConnection();
			con.setAutoCommit(false);
			savePoint = con.setSavepoint("eliminarAeropuerto");

			// GUARDAR EL ID DEL AEROPUERTO A TRAVÉS DE SU NOMBRE Y CIUDAD
			try (PreparedStatement psSeleccionarIdAeropuerto = con.prepareStatement(sqlSeleccionarIdAeropuerto,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				psSeleccionarIdAeropuerto.setString(1, nombre);
				psSeleccionarIdAeropuerto.setString(2, ciudad);
				rs = psSeleccionarIdAeropuerto.executeQuery();

				while (rs.next()) {
					idAeropuerto = rs.getInt("id");
				}
			}

			// GUARDAR LOS ID DE VUELOS EN LISTA
			try (PreparedStatement psSeleccionarIdVuelo = con.prepareStatement(sqlSeleccionarIdVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				psSeleccionarIdVuelo.setInt(1, idAeropuerto);
				rs = psSeleccionarIdVuelo.executeQuery();

				while (rs.next()) {
					idVuelos.add(rs.getInt("id_vuelo"));
				}
			}

			// ELIMINAR EN TABLA VUELOS_PASAJEROS
			try (PreparedStatement psEliminarVueloPasajero = con.prepareStatement(sqlEliminarVueloPasajero,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				for (Integer elemento : idVuelos) {
					psEliminarVueloPasajero.setInt(1, elemento);
					psEliminarVueloPasajero.executeUpdate();
				}
			}

			// ELIMINAR EN TABLA AEROPUERTOS_VUELOS
			try (PreparedStatement psEliminarAeropuertoVuelo = con.prepareStatement(sqlEliminarAeropuertoVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				psEliminarAeropuertoVuelo.setInt(1, idAeropuerto);
				psEliminarAeropuertoVuelo.executeUpdate();
			}

			// ELIMINAR EN TABLA VUELOS
			try (PreparedStatement psEliminarVuelo = con.prepareStatement(sqlEliminarVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				for (Integer elemento : idVuelos) {
					psEliminarVuelo.setInt(1, elemento);
					psEliminarVuelo.executeUpdate();
				}
			}

			// ELIMINAR EN TABLA AEROPUERTO
			try (PreparedStatement psEliminarAeropuerto = con.prepareStatement(sqlEliminarAeropuerto,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				psEliminarAeropuerto.setInt(1, idAeropuerto);
				int filasEliminadas = psEliminarAeropuerto.executeUpdate();

				if (filasEliminadas > 0) {
					con.commit();
					System.out.println("Aeropuerto eliminado exitosamente.");
				} else {
					System.out.println("No se encontró ningún aeropuerto con el nombre y ciudad especificados.");
				}
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
					System.err.println("Error al cerrar la conexión con la base de datos.");
				}
			}
			if (rs != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					System.err.println("Error al cerrar ResultSet.");
				}
			}
		}
	}

	public void readAeropuerto(String nombre, JTextArea outputArea) {
		String sqlAeropuertos = "SELECT * FROM aeropuertos WHERE nombre = ?";
		String sqlObtenerVuelos = "SELECT DISTINCT v.* FROM vuelos v "
				+ "JOIN aeropuertos_vuelos av ON av.id_vuelo = v.id " + "JOIN aeropuertos a ON a.id = av.id_aeropuerto "
				+ "WHERE (v.origen = a.ciudad OR v.destino = a.ciudad) " + "AND a.nombre = ?";

		List<Vuelo> vuelos = new ArrayList<>();

		try (Connection con = Conexion.getConnection();
				PreparedStatement ps = con.prepareStatement(sqlAeropuertos, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {

			ps.setString(1, nombre);

			ResultSet rsAeropuerto = ps.executeQuery();

			if (rsAeropuerto.next()) {
				outputArea.append("Datos del aeropuerto:\n");
				for (int i = 1; i <= rsAeropuerto.getMetaData().getColumnCount(); i++) {
					outputArea.append(
							rsAeropuerto.getMetaData().getColumnName(i) + ": " + rsAeropuerto.getString(i) + "\t\t");
				}
				outputArea.append("\n");

				// SELECCIONAMOS TODOS LOS VUELOS QUE TENGAN COMO ORIGEN O DESTINO EL AEROPUERTO
				try (PreparedStatement psObtenerVuelos = con.prepareStatement(sqlObtenerVuelos)) {
					psObtenerVuelos.setString(1, nombre);
					ResultSet rsVuelo = psObtenerVuelos.executeQuery();

					while (rsVuelo.next()) {
						Vuelo vuelo = new Vuelo();

						vuelo.setId(rsVuelo.getInt("id"));
						vuelo.setCodigo(rsVuelo.getString("codigo"));
						vuelo.setOrigen(rsVuelo.getString("origen"));
						vuelo.setDestino(rsVuelo.getString("destino"));

						Timestamp fechaSalida = rsVuelo.getTimestamp("fechaSalida");
						if (fechaSalida != null) {
							vuelo.setFechaSalida(fechaSalida.toLocalDateTime());
						}

						Timestamp fechaLlegada = rsVuelo.getTimestamp("fechaLlegada");
						if (fechaLlegada != null) {
							vuelo.setFechaLlegada(fechaLlegada.toLocalDateTime());
						}

						Time duracion = rsVuelo.getTime("duracion");
						if (duracion != null) {
							vuelo.setDuracion(duracion.toLocalTime());
						}

						vuelo.setNumeroPlazas(rsVuelo.getInt("numeroPlazas"));
						vuelo.setNumeroPasajeros(rsVuelo.getInt("numeroPasajeros"));
						vuelo.setCompleto(rsVuelo.getBoolean("completo"));

						vuelos.add(vuelo);
					}

					// IMPRIMIR LA LISTA DE VUELOS
					outputArea.append("Vuelos asociados:\n");
					for (Vuelo vuelo : vuelos) {
						outputArea.append(vuelo.toString() + "\n");
					}
				}
			} else {
				outputArea.append("No se encontró ningún aeropuerto con el nombre proporcionado.\n");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			outputArea.append("Error al mostrar los aeropuertos.\n");
		}
	}

	public static Boolean existeAeropuertoParaVuelo(String ciudad) {
		String sql = "SELECT COUNT(*) FROM aeropuertos WHERE ciudad = ?";

		try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, ciudad);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static Boolean existeAeropuertoParaAeropuerto(String nombre, String ciudad) {
		String sql = "SELECT * FROM aeropuertos WHERE nombre = ? AND ciudad = ?";

		try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nombre);
			ps.setString(2, ciudad);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<String> obtenerCiudadesDisponibles() {
		List<String> ciudades = new ArrayList<>();
		String sql = "SELECT DISTINCT ciudad FROM aeropuertos";
		try (Connection con = Conexion.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				ciudades.add(rs.getString("ciudad"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ciudades;
	}

}
