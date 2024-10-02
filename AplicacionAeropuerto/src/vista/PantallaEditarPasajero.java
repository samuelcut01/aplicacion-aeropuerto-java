package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controlador.ControladorPasajero;
import modelo.Pasajero;

public class PantallaEditarPasajero extends JFrame {

	private static final long serialVersionUID = 1L;

	public PantallaEditarPasajero(ControladorPasajero controlador, String dNI) {

		setTitle("Editar Pasajero");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel nombreLabel = new JLabel("Nuevo nombre:");
		nombreLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		nombreLabel.setForeground(Color.DARK_GRAY);
		nombreLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JTextField nombreField = new JTextField(20);

		JLabel apellidosLabel = new JLabel("Nuevos apellidos:");
		apellidosLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		apellidosLabel.setForeground(Color.DARK_GRAY);
		apellidosLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JTextField apellidosField = new JTextField(20);

		JButton guardarButton = new JButton("Guardar Cambios");
		guardarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		guardarButton.setForeground(Color.WHITE);
		guardarButton.setBackground(Color.DARK_GRAY);
		guardarButton.setHorizontalAlignment(SwingConstants.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		panel.add(nombreLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panel.add(nombreField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		panel.add(apellidosLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panel.add(apellidosField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 3;
		panel.add(guardarButton, gbc);

		add(panel);

		// ACTION LISTENERS
		guardarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = nombreField.getText().trim();
				String apellidos = apellidosField.getText().trim();
				if (!nombre.isEmpty() && !apellidos.isEmpty()) {
					Pasajero pasajero = new Pasajero(nombre, apellidos, dNI);
					controlador.updatePasajero(dNI, pasajero);
					JOptionPane.showMessageDialog(null, "Los cambios se han guardado correctamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		setVisible(true);
	}
}
