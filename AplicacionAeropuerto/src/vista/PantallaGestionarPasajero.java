package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controlador.ControladorPasajero;
import modelo.Pasajero;

public class PantallaGestionarPasajero extends JFrame {

	private static final long serialVersionUID = 1L;
	private ControladorPasajero controlador;
	private static boolean instanciaAbierta = false;

	public PantallaGestionarPasajero() {
		if (instanciaAbierta) {
			return;
		}

		instanciaAbierta = true;

		controlador = new ControladorPasajero();

		setTitle("Gestionar Pasajero");
		setSize(800, 700);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel nombreLabel = new JLabel("Nombre del pasajero:");
		nombreLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		nombreLabel.setForeground(Color.DARK_GRAY);
		nombreLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JTextField nombreField = new JTextField(20);

		JLabel apellidosLabel = new JLabel("Apellidos del pasajero:");
		apellidosLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		apellidosLabel.setForeground(Color.DARK_GRAY);
		apellidosLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JTextField apellidosField = new JTextField(20);

		JLabel dNILabel = new JLabel("DNI del pasajero:");
		dNILabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		dNILabel.setForeground(Color.DARK_GRAY);
		dNILabel.setHorizontalAlignment(SwingConstants.LEFT);

		JTextField dNIField = new JTextField(20);

		JButton agregarButton = new JButton("Agregar Pasajero");
		agregarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		agregarButton.setForeground(Color.WHITE);
		agregarButton.setBackground(Color.DARK_GRAY);
		agregarButton.setHorizontalAlignment(SwingConstants.CENTER);

		JButton eliminarButton = new JButton("Eliminar Pasajero");
		eliminarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		eliminarButton.setForeground(Color.WHITE);
		eliminarButton.setBackground(Color.DARK_GRAY);
		eliminarButton.setHorizontalAlignment(SwingConstants.CENTER);

		JButton actualizarButton = new JButton("Actualizar Pasajero");
		actualizarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		actualizarButton.setForeground(Color.WHITE);
		actualizarButton.setBackground(Color.DARK_GRAY);
		actualizarButton.setHorizontalAlignment(SwingConstants.CENTER);

		JButton buscarButton = new JButton("Buscar Pasajero");
		buscarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		buscarButton.setForeground(Color.WHITE);
		buscarButton.setBackground(Color.DARK_GRAY);
		buscarButton.setHorizontalAlignment(SwingConstants.CENTER);

		JButton volverButton = new JButton("Volver");
		volverButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		volverButton.setForeground(Color.WHITE);
		volverButton.setBackground(Color.DARK_GRAY);
		volverButton.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel buscarLabel = new JLabel("Buscar información de un pasajero");
		buscarLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		buscarLabel.setForeground(Color.DARK_GRAY);
		buscarLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel otrasOpcionesLabel = new JLabel("Más opciones");
		otrasOpcionesLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		otrasOpcionesLabel.setForeground(Color.DARK_GRAY);
		otrasOpcionesLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JTextArea outputArea = new JTextArea(3, 20);
		outputArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputArea);

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
		panel.add(apellidosLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panel.add(apellidosField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		panel.add(dNILabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panel.add(dNIField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		panel.add(agregarButton, gbc);

		gbc.gridy = 5;
		panel.add(scrollPane, gbc);

		gbc.gridy = 6;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

		gbc.gridy = 7;
		panel.add(otrasOpcionesLabel, gbc);

		gbc.gridy = 8;
		panel.add(eliminarButton, gbc);

		gbc.gridy = 9;
		panel.add(actualizarButton, gbc);

		gbc.gridy = 10;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

		gbc.gridy = 11;
		panel.add(buscarLabel, gbc);

		gbc.gridy = 12;
		panel.add(buscarButton, gbc);

		add(panel);

		// ACTION LISTENERS
		volverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instanciaAbierta = false;
				new PantallaMenu();
				dispose();
			}
		});

		agregarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = nombreField.getText().trim();
				String apellidos = apellidosField.getText().trim();
				String dNI = dNIField.getText().trim();

				if (nombre.isEmpty() || apellidos.isEmpty() || dNI.isEmpty()) {
					outputArea.setText("Asegúrate de rellenar todos los campos del pasajero.\n");
					return;
				}
				try {
					Pasajero pasajero = new Pasajero(nombre, apellidos, dNI);
					controlador.createPasajero(pasajero);
					outputArea.setText("Pasajero agregado exitosamente.\n");
				} catch (IllegalArgumentException ex) {
					outputArea.setText(ex.getMessage() + "\n");
				}
			}
		});

		eliminarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaEliminarPasajero();
			}
		});

		actualizarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaActualizarPasajero(controlador);
			}
		});

		buscarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaBuscarPasajero(controlador);
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		new PantallaGestionarPasajero();
	}
}
