package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import ajustes.Conexion;
import modelo.Vuelo;

public class ControladorVuelo {

	public void crearTablaVuelos() {
		String sql = "CREATE TABLE IF NOT EXISTS `vuelos` ( " + "  `id` int NOT NULL AUTO_INCREMENT, "
				+ "  `origen` varchar(100) NOT NULL, " + "  `destino` varchar(100) NOT NULL, "
				+ "  `fechaSalida` timestamp NOT NULL, " + "  `fechaLlegada` timestamp NOT NULL, "
				+ "  `duracion` time NOT NULL, " + "  `numeroPlazas` int NOT NULL, "
				+ "  `numeroPasajeros` int NOT NULL, " + "  `completo` tinyint NOT NULL, " + "  PRIMARY KEY (`id`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

		try (Connection con = Conexion.getConnection();
				Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stm.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createVuelo(Vuelo vuelo) {

		if (!ControladorAeropuerto.existeAeropuertoParaVuelo(vuelo.getOrigen())
				&& !ControladorAeropuerto.existeAeropuertoParaVuelo(vuelo.getDestino())) {
			throw new IllegalArgumentException("El origen o destino no es un aeropuerto válido.");
		}

		if (existeVuelo(vuelo.getFechaSalida(), vuelo.getOrigen())) {
			throw new IllegalArgumentException("El vuelo que deseas crear ya existe.");
		}

		String sqlCrearVuelo = "INSERT INTO vuelos (origen, destino, fechaSalida, fechaLlegada, duracion, numeroPlazas, numeroPasajeros, completo, codigo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String sqlObtenerIdAeropuerto = "SELECT id FROM aeropuertos WHERE ciudad = ?";
		String sqlCrearAeropuertoVuelo = "INSERT INTO aeropuertos_vuelos (id_aeropuerto, id_vuelo) VALUES (?, ?)";

		Connection con = null;
		Savepoint savePoint = null;

		try {
			con = Conexion.getConnection();
			con.setAutoCommit(false);
			savePoint = con.setSavepoint("crearVuelo");

			Integer idAeropuertoOrigen;
			Integer idAeropuertoDestino;

			// INSERTAR EL VUELO EN VUELOS
			try (PreparedStatement psCrearVuelo = con.prepareStatement(sqlCrearVuelo,
					PreparedStatement.RETURN_GENERATED_KEYS);) {
				psCrearVuelo.setString(1, vuelo.getOrigen());
				psCrearVuelo.setString(2, vuelo.getDestino());
				psCrearVuelo.setTimestamp(3, Timestamp.valueOf(vuelo.getFechaSalida()));
				psCrearVuelo.setTimestamp(4, Timestamp.valueOf(vuelo.getFechaLlegada()));
				psCrearVuelo.setTime(5, Time.valueOf(vuelo.getDuracion()));
				psCrearVuelo.setInt(6, vuelo.getNumeroPlazas());
				psCrearVuelo.setInt(7, vuelo.getNumeroPasajeros());
				psCrearVuelo.setBoolean(8, vuelo.getCompleto());
				psCrearVuelo.setString(9, vuelo.getCodigo());

				Integer filasAfectadas = psCrearVuelo.executeUpdate();

				if (filasAfectadas == 0) {
					throw new SQLException("No se puedo crear el vuelo.");
				}

				// OBTENER EL ID DEL VUELO CREADO Y SETEARLO
				try (ResultSet generatedKeys = psCrearVuelo.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						int vueloId = generatedKeys.getInt(1);
						vuelo.setId(vueloId);
					} else {
						throw new SQLException("No se pudo crear el vuelo, no se obtuvo el ID.");
					}
				}
			}

			// OBTENER EL ID DEL AEROPUERTO POR ORIGEN
			try (PreparedStatement psObtenerIdAeropuertoPorOrigen = con.prepareStatement(sqlObtenerIdAeropuerto)) {

				psObtenerIdAeropuertoPorOrigen.setString(1, vuelo.getOrigen());

				ResultSet rs = psObtenerIdAeropuertoPorOrigen.executeQuery();

				if (rs.next()) {
					idAeropuertoOrigen = rs.getInt("id");
				} else {
					throw new SQLException("Error al obtener el aeropuerto de origen.");
				}

			}

			// OBTENER EL ID DEL AEROPUERTO POR DESTINO
			try (PreparedStatement psObtenerIdAeropuertoPorDestino = con.prepareStatement(sqlObtenerIdAeropuerto)) {
				psObtenerIdAeropuertoPorDestino.setString(1, vuelo.getDestino());

				ResultSet rs = psObtenerIdAeropuertoPorDestino.executeQuery();

				if (rs.next()) {
					idAeropuertoDestino = rs.getInt("id");
				} else {
					throw new SQLException("Error al obtener el aeropuerto de destino.");
				}
			}

			// INSERTAR LA RELACIÓN EN AEROPUERTOS_VUELOS DE ORIGEN
			try (PreparedStatement psCrearAeropuertoVueloOrigen = con.prepareStatement(sqlCrearAeropuertoVuelo)) {
				psCrearAeropuertoVueloOrigen.setInt(1, idAeropuertoOrigen);
				psCrearAeropuertoVueloOrigen.setInt(2, vuelo.getId());

				psCrearAeropuertoVueloOrigen.executeUpdate();
			}

			// INSERTAR LA RELACIÓN EN AROPUERTO_VUELOS DE DESTINO
			try (PreparedStatement psCrearAeropuertoVueloDestino = con.prepareStatement(sqlCrearAeropuertoVuelo)) {
				psCrearAeropuertoVueloDestino.setInt(1, idAeropuertoDestino);
				psCrearAeropuertoVueloDestino.setInt(2, vuelo.getId());

				psCrearAeropuertoVueloDestino.executeUpdate();
			}

			con.commit();
			System.out.println("Vuelo creado exitosamente.");

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

	public void deleteVueloPorCodigo(String codigo) {
		String sqlSeleccionarIdVuelo = "SELECT id FROM vuelos WHERE codigo = ?";
		String sqlEliminarVueloPasajero = "DELETE FROM vuelos_pasajeros WHERE id_vuelo = ?";
		String sqlEliminarAeropuertoVuelo = "DELETE FROM aeropuertos_vuelos WHERE id_vuelo = ?";
		String sqlEliminarVuelo = "DELETE FROM vuelos WHERE codigo = ?";

		List<Integer> idVuelos = new ArrayList<>();

		Connection con = null;
		Savepoint savePoint = null;
		ResultSet rs = null;

		try {
			con = Conexion.getConnection();
			con.setAutoCommit(false);
			savePoint = con.setSavepoint("eliminarVuelo");

			// GUARDAR LOS ID DE VUELO EN LISTA
			try (PreparedStatement psSeleccionarIdVuelo = con.prepareStatement(sqlSeleccionarIdVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				psSeleccionarIdVuelo.setString(1, codigo);
				rs = psSeleccionarIdVuelo.executeQuery();

				while (rs.next()) {
					idVuelos.add(rs.getInt("id"));
				}
			}

			if (idVuelos.isEmpty()) {
				System.out.println("No se encontró ningún vuelo con el código especificado.");
				JOptionPane.showMessageDialog(null, "No se encontró ningún vuelo con el código especificado.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// ELIMINAR EN TABLA VUELOS_PASAJEROS
			try (PreparedStatement psEliminarVueloPasajero = con.prepareStatement(sqlEliminarVueloPasajero,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				for (Integer elemento : idVuelos) {
					psEliminarVueloPasajero.setInt(1, elemento);
					psEliminarVueloPasajero.executeUpdate();
				}
			}

			// ELIMINAR EN TABLA AEROPUERTOS_VUELOS
			try (PreparedStatement psEliminarAeropuertoVuelo = con.prepareStatement(sqlEliminarAeropuertoVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				for (Integer elemento : idVuelos) {
					psEliminarAeropuertoVuelo.setInt(1, elemento);
					psEliminarAeropuertoVuelo.executeUpdate();
				}
			}

			// ELIMINAR EN TABLA VUELOS
			try (PreparedStatement psEliminarVuelo = con.prepareStatement(sqlEliminarVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				psEliminarVuelo.setString(1, codigo);
				int filasEliminadas = psEliminarVuelo.executeUpdate();

				if (filasEliminadas > 0) {
					con.commit();
					System.out.println("Vuelo eliminado exitosamente.");
					JOptionPane.showMessageDialog(null, "Vuelo eliminado exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					System.out.println("No se encontró ningún vuelo con el código especificado.");
					JOptionPane.showMessageDialog(null, "No se encontró ningún vuelo con el código especificado.",
							"Error", JOptionPane.ERROR_MESSAGE);
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
					System.err.println("Error al cerrar la conexión a la base de datos.");
				}
			}
		}
	}

	public void deleteVuelosPorOrigen(String origen) {
		String sqlSeleccionarIdVuelo = "SELECT id FROM vuelos WHERE origen = ?";
		String sqlEliminarVueloPasajero = "DELETE FROM vuelos_pasajeros WHERE id_vuelo = ?";
		String sqlEliminarAeropuertoVuelo = "DELETE FROM aeropuertos_vuelos WHERE id_vuelo = ?";
		String sqlEliminarVuelo = "DELETE FROM vuelos WHERE origen = ?";

		List<Integer> idVuelos = new ArrayList<>();

		Connection con = null;
		Savepoint savePoint = null;
		ResultSet rs = null;

		try {
			con = Conexion.getConnection();
			con.setAutoCommit(false);
			savePoint = con.setSavepoint("eliminarVuelo");

			// GUARDAR LOS ID DE VUELO EN LISTA
			try (PreparedStatement psSeleccionarIdVuelo = con.prepareStatement(sqlSeleccionarIdVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				psSeleccionarIdVuelo.setString(1, origen);
				rs = psSeleccionarIdVuelo.executeQuery();

				while (rs.next()) {
					idVuelos.add(rs.getInt("id"));
				}
			}

			if (idVuelos.isEmpty()) {
				JOptionPane.showMessageDialog(null, "No se encontraron vuelos con el origen especificado.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// ELIMINAR EN TABLA VUELOS_PASAJEROS
			try (PreparedStatement psEliminarVueloPasajero = con.prepareStatement(sqlEliminarVueloPasajero,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				for (Integer elemento : idVuelos) {
					psEliminarVueloPasajero.setInt(1, elemento);
					psEliminarVueloPasajero.executeUpdate();
				}
			}

			// ELIMINAR EN TABLA AEROPUERTOS_VUELOS
			try (PreparedStatement psEliminarAeropuertoVuelo = con.prepareStatement(sqlEliminarAeropuertoVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				for (Integer elemento : idVuelos) {
					psEliminarAeropuertoVuelo.setInt(1, elemento);
					psEliminarAeropuertoVuelo.executeUpdate();
				}
			}

			// ELIMINAR EN TABLA VUELOS
			try (PreparedStatement psEliminarVuelo = con.prepareStatement(sqlEliminarVuelo,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				psEliminarVuelo.setString(1, origen);
				int filasEliminadas = psEliminarVuelo.executeUpdate();

				if (filasEliminadas > 0) {
					con.commit();
					JOptionPane.showMessageDialog(null, "Vuelos eliminados exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "No se encontraron vuelos con el origen especificado.", "Error",
							JOptionPane.ERROR_MESSAGE);
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
					System.err.println("Error al cerrar la conexión a la base de datos.");
				}
			}
		}
	}

	public void readVueloPorCodigo(String codigo, JTextArea outputArea) {
		String sqlVuelo = "SELECT * FROM vuelos WHERE codigo = ?";
		String sqlSeleccionarPasajeros = "SELECT id_pasajero FROM vuelos_pasajeros WHERE id_vuelo = ?";
		String sqlPasajero = "SELECT * FROM pasajeros WHERE id = ?";

		try (Connection con = Conexion.getConnection();
				PreparedStatement psVuelo = con.prepareStatement(sqlVuelo, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {

			psVuelo.setString(1, codigo);
			ResultSet rsVuelo = psVuelo.executeQuery();

			if (rsVuelo.next()) {
				outputArea.append("Datos del vuelo:\n");
				for (int i = 1; i <= rsVuelo.getMetaData().getColumnCount(); i++) {
					outputArea.append(rsVuelo.getMetaData().getColumnName(i) + ": " + rsVuelo.getString(i) + "\t\t");
				}
				outputArea.append("\n");

				// OBTENER Y MOSTRAR LA LISTA DE PASAJEROS
				int idVuelo = rsVuelo.getInt("id");
				try (PreparedStatement psSeleccionarPasajeros = con.prepareStatement(sqlSeleccionarPasajeros)) {
					psSeleccionarPasajeros.setInt(1, idVuelo);
					ResultSet rsPasajeros = psSeleccionarPasajeros.executeQuery();

					int contador = 1;
					while (rsPasajeros.next()) {
						int idPasajero = rsPasajeros.getInt("id_pasajero");
						try (PreparedStatement psPasajero = con.prepareStatement(sqlPasajero)) {
							psPasajero.setInt(1, idPasajero);
							ResultSet rs = psPasajero.executeQuery();

							outputArea.append(contador + ". Pasajero:\n");
							while (rs.next()) {
								for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
									outputArea
											.append(rs.getMetaData().getColumnName(j) + ": " + rs.getString(j) + "\n");
								}
							}
							outputArea.append("\n");
							contador++;
						}
					}
				}
			} else {
				outputArea.append("No se encontró ningún vuelo con el código proporcionado.\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void listarVuelosPorOrigen(String origen) {
		String sql = "SELECT * FROM vuelos WHERE origen = ?";
		List<Vuelo> listaVuelosEncontrados = new ArrayList<Vuelo>();

		try (Connection con = Conexion.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			ps.setString(1, origen);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					Vuelo vuelo = new Vuelo();

					vuelo.setId(rs.getInt("id"));
					vuelo.setCodigo(rs.getString("codigo"));
					vuelo.setOrigen(rs.getString("origen"));
					vuelo.setDestino(rs.getString("destino"));

					Timestamp fechaSalida = rs.getTimestamp("fechaSalida");
					if (fechaSalida != null) {
						vuelo.setFechaSalida(fechaSalida.toLocalDateTime());
					}

					Timestamp fechaLlegada = rs.getTimestamp("fechaLlegada");
					if (fechaLlegada != null) {
						vuelo.setFechaLlegada(fechaLlegada.toLocalDateTime());
					}

					Time duracion = rs.getTime("duracion");
					if (duracion != null) {
						vuelo.setDuracion(duracion.toLocalTime());
					}

					vuelo.setNumeroPlazas(rs.getInt("numeroPlazas"));
					vuelo.setNumeroPasajeros(rs.getInt("numeroPasajeros"));
					vuelo.setCompleto(rs.getBoolean("completo"));

					listaVuelosEncontrados.add(vuelo);
				}

				for (Vuelo elemento : listaVuelosEncontrados) {
					System.out.println(elemento.toString());
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static Boolean existeVuelo(LocalDateTime fechaSalida, String origen) {
		String sql = "SELECT COUNT(*) FROM vuelos WHERE fechaSalida = ? AND origen = ?";

		try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			Timestamp fechaSalidaTimestamp = Timestamp.valueOf(fechaSalida);
			ps.setTimestamp(1, fechaSalidaTimestamp);
			ps.setString(2, origen);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean existeVueloParaVuelosPasajeros(String codigo) {
		String sql = "SELECT * FROM vuelos WHERE codigo = ?";

		try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, codigo);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void actualizarNumeroPasajerosVuelo(int idVuelo, boolean incrementar) {
		String sqlActualizarNumeroPasajeros = "UPDATE vuelos SET numeroPasajeros = numeroPasajeros + ? WHERE id = ?";

		try (Connection con = Conexion.getConnection();
				PreparedStatement psActualizarNumeroPasajeros = con.prepareStatement(sqlActualizarNumeroPasajeros)) {

			psActualizarNumeroPasajeros.setInt(1, incrementar ? 1 : -1);
			psActualizarNumeroPasajeros.setInt(2, idVuelo);
			psActualizarNumeroPasajeros.executeUpdate();

			System.out.println("Número de pasajeros del vuelo actualizado correctamente.");

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al actualizar el número de pasajeros del vuelo.");
		}
	}

	public List<String> obtenerCodigosVueloDisponibles() {
		List<String> codigosVuelo = new ArrayList<>();
		String sql = "SELECT codigo FROM vuelos";

		try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String codigo = rs.getString("codigo");
				codigosVuelo.add(codigo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return codigosVuelo;
	}

}
