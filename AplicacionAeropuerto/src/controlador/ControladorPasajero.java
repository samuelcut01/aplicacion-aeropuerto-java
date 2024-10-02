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
import modelo.Pasajero;
import modelo.Vuelo;

public class ControladorPasajero {

	public void crearTablaPasajero() {
		String sql = "CREATE TABLE `pasajeros` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
				+ "  `nombre` varchar(100) NOT NULL, " + "  `apellidos` varchar(100) NOT NULL, "
				+ "  `dNI` varchar(100) NOT NULL, " + "  PRIMARY KEY (`id`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

		try (Connection con = Conexion.getConnection();
				Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stm.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Integer createPasajero(Pasajero pasajero) {

		if (existePasajero(pasajero.getdNI())) {
			throw new IllegalArgumentException("El pasajero ya existe. No se puede volver a añadir.");
		}

		String sql = "INSERT INTO pasajeros (nombre, apellidos, dNI) VALUES (?, ? ,?)";
		Connection con = null;
		Savepoint savePoint = null;
		Integer generatedKey = null;

		try {
			con = Conexion.getConnection();
			con.setAutoCommit(false);
			savePoint = con.setSavepoint("crearPasajero");

			try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, pasajero.getNombre());
				ps.setString(2, pasajero.getApellidos());
				ps.setString(3, pasajero.getdNI());

				ps.executeUpdate();

				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						generatedKey = rs.getInt(1);
					}
				}
				con.commit();
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
		}
		return generatedKey;
	}

	public void readPasajero(String dNI, JTextArea outputArea) {
		String sqlPasajeros = "SELECT * FROM pasajeros WHERE dNI = ?";
		String sqlObtenerVuelos = "SELECT DISTINCT v.* FROM vuelos v "
				+ "JOIN vuelos_pasajeros ap ON ap.id_vuelo = v.id " + "JOIN pasajeros p ON p.id = ap.id_pasajero "
				+ "WHERE p.dNI = ?";

		List<Vuelo> vuelos = new ArrayList<>();

		try (Connection con = Conexion.getConnection();
				PreparedStatement psPasajeros = con.prepareStatement(sqlPasajeros, ResultSet.CONCUR_READ_ONLY,
						ResultSet.TYPE_SCROLL_SENSITIVE);) {

			psPasajeros.setString(1, dNI);

			ResultSet rsPasajero = psPasajeros.executeQuery();

			if (rsPasajero.next()) {
				outputArea.append("Datos del pasajero:\n");
				for (int i = 1; i <= rsPasajero.getMetaData().getColumnCount(); i++) {
					outputArea.append(
							rsPasajero.getMetaData().getColumnName(i) + ": " + rsPasajero.getString(i) + "\t\t");
				}
				outputArea.append("\n");

				// SELECCIONAMOS TODOS LOS VUELOS QUE APAREZCAN EN LA TABLA VUELOS_PASAJEROS
				try (PreparedStatement psObtenerVuelos = con.prepareStatement(sqlObtenerVuelos)) {
					psObtenerVuelos.setString(1, dNI);
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
				}

				// IMPRIMIR LA LISTA DE VUELOS
				outputArea.append("Vuelos asociados:\n");
				for (Vuelo vuelo : vuelos) {
					outputArea.append(vuelo.toString() + "\n");
				}
			} else {
				outputArea.append("No se encontró ningún pasajero con el DNI proporcionado.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			outputArea.append("Error al mostrar los aeropuertos.\n");
		}
	}

	public void updatePasajero(String dNI, Pasajero pasajero) {
		String sql = "UPDATE pasajeros SET nombre = ?, apellidos = ?, dNI = ? WHERE dNI = ?";

		try (Connection con = Conexion.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			ps.setString(1, pasajero.getNombre());
			ps.setString(2, pasajero.getApellidos());
			ps.setString(3, pasajero.getdNI());
			ps.setString(4, dNI);

			int columnasAfectadas = ps.executeUpdate();

			if (columnasAfectadas > 0) {
				System.out.println("Se ha actualizado el pasajero correctamente.");
			} else {
				System.out.println("No se ha encontrado ningún pasajero con el ID proporcionado.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deletePasajero(String dNI) {
		String sqlSeleccionarIdPasajero = "SELECT id FROM pasajeros WHERE dNI = ?";
		String sqlEliminarVueloPasajero = "DELETE FROM vuelos_pasajeros WHERE id_pasajero = ?";
		String sqlEliminarPasajero = "DELETE FROM pasajeros WHERE id = ?";

		Integer idPasajero = 0;

		Connection con = null;
		Savepoint savePoint = null;
		ResultSet rs = null;

		try {
			con = Conexion.getConnection();
			con.setAutoCommit(false);
			savePoint = con.setSavepoint("eliminarPasajero");

			// GUARDAR EL ID DEL PASAJERO A TRAVÉS DE SU DNI
			try (PreparedStatement psSeleccionarIdPasajero = con.prepareStatement(sqlSeleccionarIdPasajero,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				psSeleccionarIdPasajero.setString(1, dNI);
				rs = psSeleccionarIdPasajero.executeQuery();

				while (rs.next()) {
					idPasajero = rs.getInt("id");
				}
			}

			// ELIMINAR EN TABLA VUELOS_PASAJEROS
			try (PreparedStatement psEliminarVueloPasajero = con.prepareStatement(sqlEliminarVueloPasajero,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				psEliminarVueloPasajero.setInt(1, idPasajero);
				psEliminarVueloPasajero.executeUpdate();

			}

			// ELIMINAR EN TABLA PASAJEROS
			try (PreparedStatement psEliminarPasajero = con.prepareStatement(sqlEliminarPasajero,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
				psEliminarPasajero.setInt(1, idPasajero);
				int filasEliminadas = psEliminarPasajero.executeUpdate();

				if (filasEliminadas > 0) {
					con.commit();
					System.out.println("El pasajero se ha eliminado correctamente.");
				} else {
					System.out.println("No se encontró ningún pasajero con el DNI especificado.");
				}
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

	public static Boolean existePasajero(String dNI) {
		String sql = "SELECT * FROM pasajeros WHERE dNI = ?";

		try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, dNI);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
