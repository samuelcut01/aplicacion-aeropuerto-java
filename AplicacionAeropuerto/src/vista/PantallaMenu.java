package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private static boolean instanciaAbierta = false;

	public PantallaMenu() {
		if (instanciaAbierta) {
			return;
		}
		
		instanciaAbierta = true;

		setTitle("Menú");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel menuPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel menuLabel = new JLabel("¿Qué deseas realizar?");
		menuLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		menuLabel.setForeground(Color.DARK_GRAY);
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		menuPanel.add(menuLabel, gbc);

		JButton gestionarAeropuertoButton = new JButton("Gestionar Aeropuerto");
		gestionarAeropuertoButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		gestionarAeropuertoButton.setForeground(Color.WHITE);
		gestionarAeropuertoButton.setBackground(Color.DARK_GRAY);
		gestionarAeropuertoButton.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		menuPanel.add(gestionarAeropuertoButton, gbc);

		JButton gestionarVueloButton = new JButton("Gestionar Vuelo");
		gestionarVueloButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		gestionarVueloButton.setForeground(Color.WHITE);
		gestionarVueloButton.setBackground(Color.DARK_GRAY);
		gestionarVueloButton.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 2;
		menuPanel.add(gestionarVueloButton, gbc);

		JButton gestionarPasajeroButton = new JButton("Gestionar Pasajero");
		gestionarPasajeroButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		gestionarPasajeroButton.setForeground(Color.WHITE);
		gestionarPasajeroButton.setBackground(Color.DARK_GRAY);
		gestionarPasajeroButton.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 3;
		menuPanel.add(gestionarPasajeroButton, gbc);

		add(menuPanel);

		// ACTION LISTENERS
		gestionarAeropuertoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaGestionarAeropuerto();
			}
		});
		
		gestionarPasajeroButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaGestionarPasajero();
			}
		});
		
		gestionarVueloButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaGestionarVuelo();
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		new PantallaMenu();
	}
}
