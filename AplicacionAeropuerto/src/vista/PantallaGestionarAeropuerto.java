package vista;

import javax.swing.*;

import controlador.ControladorAeropuerto;
import modelo.Aeropuerto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaGestionarAeropuerto extends JFrame {

	private static final long serialVersionUID = 1L;
	private static boolean instanciaAbierta = false;
	private ControladorAeropuerto controlador;
	private JTextField nombreField;
	private JTextField ciudadField;
	private JTextArea outputArea;

	public PantallaGestionarAeropuerto() {
		if (instanciaAbierta) {
			return;
		}

		instanciaAbierta = true;

		controlador = new ControladorAeropuerto();

		setTitle("Gestionar Aeropuerto");
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel nombreLabel = new JLabel("Nombre del aeropuerto:");
		nombreLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		nombreLabel.setForeground(Color.DARK_GRAY);
		nombreLabel.setHorizontalAlignment(SwingConstants.LEFT);

		nombreField = new JTextField(20);

		JLabel ciudadLabel = new JLabel("Ciudad del aeropuerto:");
		ciudadLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		ciudadLabel.setForeground(Color.DARK_GRAY);
		ciudadLabel.setHorizontalAlignment(SwingConstants.LEFT);

		ciudadField = new JTextField(20);

		JButton agregarButton = new JButton("Agregar Aeropuerto");
		agregarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		agregarButton.setForeground(Color.WHITE);
		agregarButton.setBackground(Color.DARK_GRAY);
		agregarButton.setHorizontalAlignment(SwingConstants.CENTER);

		JButton eliminarButton = new JButton("Eliminar Aeropuerto");
		eliminarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		eliminarButton.setForeground(Color.WHITE);
		eliminarButton.setBackground(Color.DARK_GRAY);
		eliminarButton.setHorizontalAlignment(SwingConstants.CENTER);

		JButton volverButton = new JButton("Volver");
		volverButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		volverButton.setForeground(Color.WHITE);
		volverButton.setBackground(Color.DARK_GRAY);
		volverButton.setHorizontalAlignment(SwingConstants.CENTER);

		outputArea = new JTextArea(5, 30);
		outputArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputArea);

		JLabel buscarLabel = new JLabel("Buscar información de un aeropuerto");
		buscarLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		buscarLabel.setForeground(Color.DARK_GRAY);
		buscarLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JButton buscarAeropuertoButton = new JButton("Buscar Aeropuerto");
		buscarAeropuertoButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		buscarAeropuertoButton.setForeground(Color.WHITE);
		buscarAeropuertoButton.setBackground(Color.DARK_GRAY);
		buscarAeropuertoButton.setHorizontalAlignment(SwingConstants.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		panel.add(volverButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		panel.add(nombreLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panel.add(nombreField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		panel.add(ciudadLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panel.add(ciudadField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		panel.add(agregarButton, gbc);

		gbc.gridy = 4;
		panel.add(eliminarButton, gbc);

		gbc.gridy = 5;
		panel.add(scrollPane, gbc);

		gbc.gridy = 6;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

		gbc.gridy = 7;
		panel.add(buscarLabel, gbc);

		gbc.gridy = 8;
		panel.add(buscarAeropuertoButton, gbc);

		add(panel);

		// ACTION LISTENERS
		agregarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = nombreField.getText().trim();
				String ciudad = ciudadField.getText().trim();

				if (nombre.isEmpty() || ciudad.isEmpty()) {
					outputArea.setText("Asegúrate de rellenar todos los campos del aeropuerto.\n");
					return;
				}

				Aeropuerto aeropuerto = new Aeropuerto(nombre, ciudad);
				try {
					controlador.createAeropuerto(aeropuerto);
					outputArea.setText("Aeropuerto agregado exitosamente.\n");
				} catch (IllegalArgumentException ex) {
					outputArea.setText(ex.getMessage() + "\n");
				}
			}
		});

		eliminarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = nombreField.getText().trim();
				String ciudad = ciudadField.getText().trim();
				if (!nombre.isEmpty() && !ciudad.isEmpty()) {
					controlador.deleteAeropuertoPorNombreYCiudad(nombre, ciudad);
					outputArea.setText("Aeropuerto eliminado exitosamente.\n");
				} else {
					outputArea.setText("Por favor, ingrese el nombre y la ciudad del aeropuerto para eliminar.\n");
				}
			}
		});

		volverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instanciaAbierta = false;
				new PantallaMenu();
				dispose();
			}
		});

		buscarAeropuertoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaBuscarAeropuerto(controlador);
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		new PantallaGestionarAeropuerto();
	}
}
