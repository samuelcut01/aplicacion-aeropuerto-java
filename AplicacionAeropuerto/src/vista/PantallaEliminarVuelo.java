package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controlador.ControladorVuelo;

public class PantallaEliminarVuelo extends JFrame {

    private static final long serialVersionUID = 1L;
    private ControladorVuelo controlador;

    public PantallaEliminarVuelo() {
        controlador = new ControladorVuelo();

        setTitle("Eliminar Vuelo");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel codigoLabel = new JLabel("Introduce el código del vuelo:");
        codigoLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        codigoLabel.setForeground(Color.DARK_GRAY);
        codigoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JTextField codigoField = new JTextField(20);

        JButton eliminarButton = new JButton("Eliminar Vuelo");
        eliminarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.setBackground(Color.DARK_GRAY);
        eliminarButton.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(codigoLabel, gbc);

        gbc.gridy = 1;
        panel.add(codigoField, gbc);

        gbc.gridy = 2;
        panel.add(eliminarButton, gbc);

        add(panel);

        // ACTION LISTENERS
        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoField.getText().trim();

                if (codigo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduce un código válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controlador.deleteVueloPorCodigo(codigo);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new PantallaEliminarVuelo();
    }
}
