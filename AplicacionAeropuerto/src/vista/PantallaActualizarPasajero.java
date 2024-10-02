package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controlador.ControladorPasajero;

public class PantallaActualizarPasajero extends JFrame {

	private static final long serialVersionUID = 1L;

	public PantallaActualizarPasajero(ControladorPasajero controlador) {

		setTitle("Actualizar Pasajero");
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		JLabel dNILabel = new JLabel("Introduce el DNI del pasajero:");
		dNILabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		dNILabel.setForeground(Color.DARK_GRAY);
		dNILabel.setHorizontalAlignment(SwingConstants.LEFT);

		JTextField dNIField = new JTextField(20);

		JButton actualizarButton = new JButton("Actualizar Pasajero");
		actualizarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		actualizarButton.setForeground(Color.WHITE);
		actualizarButton.setBackground(Color.DARK_GRAY);
		actualizarButton.setHorizontalAlignment(SwingConstants.CENTER);

		gbc.gridx = 0; 
		gbc.gridy = 1; 
		gbc.gridwidth = 3; 
		panel.add(dNILabel, gbc);

		
		gbc.gridx = 0; 
		gbc.gridy = 2; 
		gbc.gridwidth = 3;
		panel.add(dNIField, gbc);

		
		gbc.gridx = 0;
		gbc.gridy = 3; 
		gbc.gridwidth = 3; 
		panel.add(actualizarButton, gbc);

		add(panel);

		// ACTION LISTENERS
		actualizarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dNI = dNIField.getText().trim();
				if (!dNI.isEmpty()) {
					new PantallaEditarPasajero(controlador, dNI);
				} else {
					JOptionPane.showMessageDialog(null, "Por favor, introduce el DNI del pasajero.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		setVisible(true);
	}
}
