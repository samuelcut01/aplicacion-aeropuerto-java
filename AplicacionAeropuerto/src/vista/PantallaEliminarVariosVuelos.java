package vista;

import javax.swing.*;
import controlador.ControladorVuelo;
import java.awt.*;
import java.awt.event.*;

public class PantallaEliminarVariosVuelos extends JFrame {

	private static final long serialVersionUID = 1L;
	private ControladorVuelo controlador;

	public PantallaEliminarVariosVuelos() {
		controlador = new ControladorVuelo();

		setTitle("Eliminar Vuelos por Origen");
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel origenLabel = new JLabel("Introduce el origen de los vuelos:");
		origenLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		origenLabel.setForeground(Color.DARK_GRAY);
		origenLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JTextField origenField = new JTextField(20);

		JButton eliminarButton = new JButton("Eliminar Vuelos");
		eliminarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		eliminarButton.setForeground(Color.WHITE);
		eliminarButton.setBackground(Color.DARK_GRAY);
		eliminarButton.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(origenLabel, gbc);

		gbc.gridy = 1;
		panel.add(origenField, gbc);

		gbc.gridy = 2;
		panel.add(eliminarButton, gbc);

		add(panel);

		// ACTION LISTENERS
		eliminarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String origen = origenField.getText().trim();

				if (origen.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Por favor, introduce un origen válido.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				controlador.deleteVuelosPorOrigen(origen);
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		new PantallaEliminarVariosVuelos();
	}
}
