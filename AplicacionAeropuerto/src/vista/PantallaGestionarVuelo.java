package vista;

import javax.swing.*;
import controlador.ControladorVueloPasajero;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PantallaGestionarVuelo extends JFrame {

	private static final long serialVersionUID = 1L;
	private static boolean instanciaAbierta = false;

	public PantallaGestionarVuelo() {
		if (instanciaAbierta) {
			return;
		}

		instanciaAbierta = true;

		setTitle("Gestionar Vuelo");
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JButton volverButton = new JButton("Volver");
		volverButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		volverButton.setForeground(Color.WHITE);
		volverButton.setBackground(Color.DARK_GRAY);
		volverButton.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnCrearVuelo = new JButton("Crear Vuelo");
		btnCrearVuelo.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCrearVuelo.setForeground(Color.WHITE);
		btnCrearVuelo.setBackground(Color.DARK_GRAY);
		btnCrearVuelo.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnEliminarVuelo = new JButton("Eliminar un Vuelo");
		btnEliminarVuelo.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEliminarVuelo.setForeground(Color.WHITE);
		btnEliminarVuelo.setBackground(Color.DARK_GRAY);
		btnEliminarVuelo.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnEliminarVariosVuelos = new JButton("Eliminar más de un Vuelo");
		btnEliminarVariosVuelos.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEliminarVariosVuelos.setForeground(Color.WHITE);
		btnEliminarVariosVuelos.setBackground(Color.DARK_GRAY);
		btnEliminarVariosVuelos.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnListarPorOrigen = new JButton("Buscar Vuelos");
		btnListarPorOrigen.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnListarPorOrigen.setForeground(Color.WHITE);
		btnListarPorOrigen.setBackground(Color.DARK_GRAY);
		btnListarPorOrigen.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel buscarLabel = new JLabel("Buscar información de un vuelo");
		buscarLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		buscarLabel.setForeground(Color.DARK_GRAY);
		buscarLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel anyadirOEliminarLabel = new JLabel("Añadir o eliminar vuelos");
		anyadirOEliminarLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		anyadirOEliminarLabel.setForeground(Color.DARK_GRAY);
		anyadirOEliminarLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel anyadirPasajerosLabel = new JLabel("Añadir pasajero a un vuelo");
		anyadirPasajerosLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		anyadirPasajerosLabel.setForeground(Color.DARK_GRAY);
		anyadirPasajerosLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnAñadirPasajerosAVuelo = new JButton("Añadir Pasajero a un Vuelo");
		btnAñadirPasajerosAVuelo.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAñadirPasajerosAVuelo.setForeground(Color.WHITE);
		btnAñadirPasajerosAVuelo.setBackground(Color.DARK_GRAY);
		btnAñadirPasajerosAVuelo.setHorizontalAlignment(SwingConstants.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		panel.add(volverButton, gbc);

		gbc.gridy = 1;
		panel.add(anyadirOEliminarLabel, gbc);

		gbc.gridy = 2;
		panel.add(btnCrearVuelo, gbc);

		gbc.gridy = 3;
		panel.add(btnEliminarVuelo, gbc);

		gbc.gridy = 4;
		panel.add(btnEliminarVariosVuelos, gbc);

		gbc.gridy = 5;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

		gbc.gridy = 6;
		panel.add(anyadirPasajerosLabel, gbc);

		gbc.gridy = 7;
		panel.add(btnAñadirPasajerosAVuelo, gbc);

		gbc.gridy = 8;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

		gbc.gridy = 9;
		panel.add(buscarLabel, gbc);

		gbc.gridy = 10;
		panel.add(btnListarPorOrigen, gbc);

		add(panel);

		// ACTION LISTENERS
		volverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instanciaAbierta = false;
				new PantallaMenu();
				dispose();
			}
		});

		btnCrearVuelo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PantallaCrearVuelo pantallaCrearVuelo = new PantallaCrearVuelo();
				pantallaCrearVuelo.setVisible(true);
			}
		});

		btnEliminarVuelo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PantallaEliminarVuelo pantallaEliminarVuelo = new PantallaEliminarVuelo();
				pantallaEliminarVuelo.setVisible(true);
			}
		});

		btnEliminarVariosVuelos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PantallaEliminarVariosVuelos pantallaEliminarVariosVuelos = new PantallaEliminarVariosVuelos();
				pantallaEliminarVariosVuelos.setVisible(true);
			}
		});

		btnListarPorOrigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PantallaBuscarVuelo pantallaBuscarVuelo = new PantallaBuscarVuelo();
				pantallaBuscarVuelo.setVisible(true);
			}
		});

		btnAñadirPasajerosAVuelo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ControladorVueloPasajero controladorVueloPasajero = new ControladorVueloPasajero();
				PantallaAgregarPasajeroVuelo pantallaAgregarPasajeroVuelo = new PantallaAgregarPasajeroVuelo(
						controladorVueloPasajero);
				pantallaAgregarPasajeroVuelo.setVisible(true);
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				instanciaAbierta = false;
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		new PantallaGestionarVuelo();
	}
}
