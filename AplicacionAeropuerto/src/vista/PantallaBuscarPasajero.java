package vista;

import javax.swing.*;

import controlador.ControladorPasajero;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaBuscarPasajero extends JFrame {

	private static final long serialVersionUID = 1L;
	private static boolean instanciaAbierta = false;

	public PantallaBuscarPasajero(ControladorPasajero controlador) {
		if (instanciaAbierta) {
			return;
		}

		setTitle("Buscar Pasajero");
		setSize(600, 400);
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

		JLabel nombreLabel = new JLabel("DNI del pasajero:");
		nombreLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		nombreLabel.setForeground(Color.DARK_GRAY);
		nombreLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JTextField nombreField = new JTextField(20);

		JButton mostrarButton = new JButton("Buscar");
		mostrarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		mostrarButton.setForeground(Color.WHITE);
		mostrarButton.setBackground(Color.DARK_GRAY);
		mostrarButton.setHorizontalAlignment(SwingConstants.CENTER);

		JTextArea outputArea = new JTextArea(10, 50);
		outputArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputArea);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		panel.add(volverButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		panel.add(nombreLabel, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		panel.add(nombreField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		panel.add(mostrarButton, gbc);

		gbc.gridy = 3;
		gbc.gridwidth = 4;
		panel.add(scrollPane, gbc);

		add(panel);

		// ACTION LISTENERS
		mostrarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dNI = nombreField.getText().trim();
				if (!dNI.isEmpty()) {
					outputArea.setText("");
					controlador.readPasajero(dNI, outputArea);
				} else {
					outputArea.setText("Por favor, ingrese el nombre del aeropuerto para mostrar.\n");
				}
			}
		});

		volverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instanciaAbierta = false;
				dispose();
				new PantallaGestionarPasajero();
			}
		});

		setVisible(true);

	}

}
