package vista;

import javax.swing.*;
import controlador.ControladorAeropuerto;
import controlador.ControladorVuelo;
import modelo.Vuelo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PantallaCrearVuelo extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField txtFechaSalida;
	private JTextField txtFechaLlegada;
	private JTextField txtNumeroPlazas;
	private JComboBox<String> cmbCiudadesOrigen;
	private JComboBox<String> cmbCiudadesDestino;
	private JTextArea txtAreaMensaje;

	public PantallaCrearVuelo() {
		setTitle("Crear Vuelo");
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblTitulo = new JLabel("Crear Vuelo");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitulo.setForeground(Color.DARK_GRAY);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblOrigen = new JLabel("Origen:");
		lblOrigen.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblOrigen.setForeground(Color.DARK_GRAY);
		lblOrigen.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblDestino = new JLabel("Destino:");
		lblDestino.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDestino.setForeground(Color.DARK_GRAY);
		lblDestino.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblFechaSalida = new JLabel("Fecha de Salida (YYYY-MM-DD HH:MM):");
		lblFechaSalida.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFechaSalida.setForeground(Color.DARK_GRAY);
		lblFechaSalida.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblFechaLlegada = new JLabel("Fecha de Llegada (YYYY-MM-DD HH:MM):");
		lblFechaLlegada.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFechaLlegada.setForeground(Color.DARK_GRAY);
		lblFechaLlegada.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblNumeroPlazas = new JLabel("Número de Plazas:");
		lblNumeroPlazas.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNumeroPlazas.setForeground(Color.DARK_GRAY);
		lblNumeroPlazas.setHorizontalAlignment(SwingConstants.LEFT);

		cmbCiudadesOrigen = new JComboBox<>();
		cmbCiudadesDestino = new JComboBox<>();
		txtFechaSalida = new JTextField(20);
		txtFechaLlegada = new JTextField(20);
		txtNumeroPlazas = new JTextField(20);
		txtAreaMensaje = new JTextArea(2, 20);
		txtAreaMensaje.setEditable(false);
		txtAreaMensaje.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(txtAreaMensaje);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JButton btnCrear = new JButton("Crear");
		btnCrear.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCrear.setForeground(Color.WHITE);
		btnCrear.setBackground(Color.DARK_GRAY);
		btnCrear.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(lblTitulo, gbc);

		gbc.gridy = 1;
		panel.add(lblOrigen, gbc);

		gbc.gridy = 2;
		panel.add(lblDestino, gbc);

		gbc.gridy = 3;
		panel.add(lblFechaSalida, gbc);

		gbc.gridy = 4;
		panel.add(lblFechaLlegada, gbc);

		gbc.gridy = 5;
		panel.add(lblNumeroPlazas, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(cmbCiudadesOrigen, gbc);

		gbc.gridy = 2;
		panel.add(cmbCiudadesDestino, gbc);

		gbc.gridy = 3;
		panel.add(txtFechaSalida, gbc);

		gbc.gridy = 4;
		panel.add(txtFechaLlegada, gbc);

		gbc.gridy = 5;
		panel.add(txtNumeroPlazas, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		panel.add(scrollPane, gbc);

		gbc.gridy = 7;
		gbc.gridwidth = 2;
		panel.add(btnCrear, gbc);

		add(panel);

		List<String> ciudadesDisponibles = ControladorAeropuerto.obtenerCiudadesDisponibles();
		for (String ciudad : ciudadesDisponibles) {
			cmbCiudadesOrigen.addItem(ciudad);
			cmbCiudadesDestino.addItem(ciudad);
		}

		// ACTION LISTENERS
		btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String origen = (String) cmbCiudadesOrigen.getSelectedItem();
					String destino = (String) cmbCiudadesDestino.getSelectedItem();
					LocalDateTime fechaSalida = LocalDateTime.parse(txtFechaSalida.getText(),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
					LocalDateTime fechaLlegada = LocalDateTime.parse(txtFechaLlegada.getText(),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
					int numeroPlazas = Integer.parseInt(txtNumeroPlazas.getText());

					if (origen.isEmpty() || destino.isEmpty() || txtFechaSalida.getText().isEmpty()
							|| txtFechaLlegada.getText().isEmpty() || txtNumeroPlazas.getText().isEmpty()) {
						txtAreaMensaje.setText("Asegúrate de rellenar todos los campos.\n");
						return;
					}

					if (origen.equals(destino)) {
						txtAreaMensaje.setText("El origen y el destino no pueden ser la misma ciudad.\n");
						return;
					}

					Vuelo vuelo = new Vuelo(origen, destino, fechaSalida, fechaLlegada, numeroPlazas);
					ControladorVuelo controlador = new ControladorVuelo();
					controlador.createVuelo(vuelo);
					txtAreaMensaje.setText("Vuelo creado exitosamente.\n");
				} catch (DateTimeParseException dtpe) {
					txtAreaMensaje.setText("Formato de fecha incorrecto. Por favor usa el formato YYYY-MM-DD HH:MM.\n");
				} catch (NumberFormatException nfe) {
					txtAreaMensaje.setText("Número de plazas incorrecto. Por favor ingresa un número válido.\n");
				} catch (IllegalArgumentException ex) {
					txtAreaMensaje.setText(ex.getMessage() + "\n");
				} catch (Exception ex) {
					txtAreaMensaje.setText("Ocurrió un error al crear el vuelo: " + ex.getMessage() + "\n");
				}
			}
		});

		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new PantallaCrearVuelo();
	}
}
