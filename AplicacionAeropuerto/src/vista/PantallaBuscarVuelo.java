package vista;

import controlador.ControladorVuelo;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PantallaBuscarVuelo extends JFrame {

	private static final long serialVersionUID = 1L;
	private static boolean instanciaAbierta = false;

	public PantallaBuscarVuelo() {
		if (instanciaAbierta) {
			return;
		}

		ControladorVuelo controlador = new ControladorVuelo();

		setTitle("Buscar Vuelo por Código");
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

		JLabel vueloLabel = new JLabel("Seleccione el vuelo:");
		vueloLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		vueloLabel.setForeground(Color.DARK_GRAY);
		vueloLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JComboBox<String> vueloComboBox = new JComboBox<>();
		vueloComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		vueloComboBox.setBackground(Color.WHITE);

		JButton mostrarButton = new JButton("Buscar");
		mostrarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		mostrarButton.setForeground(Color.WHITE);
		mostrarButton.setBackground(Color.DARK_GRAY);
		mostrarButton.setHorizontalAlignment(SwingConstants.CENTER);

		JTextArea outputArea = new JTextArea(10, 50);
		outputArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputArea);

		// Llenar el JComboBox con los códigos de vuelo disponibles
		List<String> codigosVuelo = controlador.obtenerCodigosVueloDisponibles();
		for (String codigo : codigosVuelo) {
			vueloComboBox.addItem(codigo);
		}

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		panel.add(volverButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		panel.add(vueloLabel, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		panel.add(vueloComboBox, gbc);

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
				String codigo = (String) vueloComboBox.getSelectedItem();
				if (codigo != null) {
					outputArea.setText("");
					controlador.readVueloPorCodigo(codigo, outputArea);
				} else {
					outputArea.setText("Por favor, seleccione un vuelo para buscar.\n");
				}
			}
		});

		volverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instanciaAbierta = false;
				dispose();
				new PantallaGestionarVuelo();
			}
		});

		setVisible(true);
	}
}