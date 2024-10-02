package vista;

import javax.swing.*;
import controlador.ControladorVueloPasajero;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaAgregarPasajeroVuelo extends JFrame {

	private static final long serialVersionUID = 1L;

	public PantallaAgregarPasajeroVuelo(ControladorVueloPasajero controlador) {

		setTitle("Agregar Pasajero a Vuelo");
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel vueloLabel = new JLabel("Seleccionar Vuelo:");
		JComboBox<String> vueloComboBox = new JComboBox<>(controlador.obtenerVuelosDisponibles());

		JLabel dNILabel = new JLabel("DNI del Pasajero:");
		JTextField dNIField = new JTextField(20);

		JButton agregarButton = new JButton("Agregar Pasajero al Vuelo");
		agregarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		agregarButton.setForeground(Color.WHITE);
		agregarButton.setBackground(Color.DARK_GRAY);
		agregarButton.setHorizontalAlignment(SwingConstants.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		panel.add(vueloLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(vueloComboBox, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		panel.add(dNILabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		panel.add(dNIField, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		panel.add(agregarButton, gbc);

		agregarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String codigoVuelo = (String) vueloComboBox.getSelectedItem();
				String dNI = dNIField.getText().trim();
				if (!codigoVuelo.isEmpty() && !dNI.isEmpty()) {
					controlador.createVueloPasajero(codigoVuelo, dNI);
					JOptionPane.showMessageDialog(null, "Pasajero agregado al vuelo correctamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		add(panel);
		setVisible(true);
	}

	public static void main(String[] args) {
		ControladorVueloPasajero cp = new ControladorVueloPasajero();
		new PantallaAgregarPasajeroVuelo(cp);
	}
}
