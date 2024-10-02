package vista;

import javax.swing.*;

import controlador.ControladorPasajero;

import java.awt.*;
import java.awt.event.*;

public class PantallaEliminarPasajero extends JFrame {

    private static final long serialVersionUID = 1L;
    private ControladorPasajero controlador;

    public PantallaEliminarPasajero() {
        controlador = new ControladorPasajero();

        setTitle("Eliminar Pasajero");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel dNILabel = new JLabel("Introduce el DNI del pasajero:");
        dNILabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        dNILabel.setForeground(Color.DARK_GRAY);
        dNILabel.setHorizontalAlignment(SwingConstants.LEFT);

        JTextField dNIField = new JTextField(20);

        JButton eliminarButton = new JButton("Eliminar Pasajero");
        eliminarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.setBackground(Color.DARK_GRAY);
        eliminarButton.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(dNILabel, gbc);

        gbc.gridy = 1;
        panel.add(dNIField, gbc);

        gbc.gridy = 2;
        panel.add(eliminarButton, gbc);

        add(panel);

        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dNI = dNIField.getText().trim();

                if (dNI.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduce un DNI válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controlador.deletePasajero(dNI);
                JOptionPane.showMessageDialog(null, "Pasajero eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new PantallaEliminarPasajero();
    }
}

