package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class PantallaInicio extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PantallaInicio() {
		setTitle("Aplicación Aeropuerto");
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel inicioPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel inicioLabel = new JLabel("Bienvenido");
		inicioLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		inicioLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inicioLabel.setForeground(Color.DARK_GRAY);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		inicioPanel.add(inicioLabel, gbc);

		JButton botonEntrar = new JButton("Entrar");
		botonEntrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		botonEntrar.setForeground(Color.WHITE);
		botonEntrar.setBackground(Color.DARK_GRAY);
		botonEntrar.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 2;
		inicioPanel.add(botonEntrar, gbc);

		botonEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaMenu();
				dispose();
			}
		});

		add(inicioPanel);
		setVisible(true);
	}

	public static void main(String[] args) {
		Font defaultFont = new Font("Tahoma", Font.PLAIN, 14);
		UIManager.put("Label.font", defaultFont);
		UIManager.put("Button.font", defaultFont);
		UIManager.put("TextField.font", defaultFont);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PantallaInicio();
			}
		});
	}
}
