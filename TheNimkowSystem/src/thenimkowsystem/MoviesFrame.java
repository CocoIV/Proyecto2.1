
package thenimkowsystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoviesFrame extends JFrame {

    private JPanel moviesPanel;

    public MoviesFrame(List<String> imagePaths) {
        initComponents(imagePaths);
    }

    private void initComponents(List<String> imagePaths) {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        moviesPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        JScrollPane scrollPane = new JScrollPane(moviesPanel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        for (String imagePath : imagePaths) {
            ImageIcon movieImage = new ImageIcon(imagePath);
            JLabel movieLabel = new JLabel(movieImage);
            moviesPanel.add(movieLabel);
        }

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                List<String> imagePaths = new ArrayList<>(); // Agrega aquí las rutas de las imágenes obtenidas
                new MoviesFrame(imagePaths).setVisible(true);
            }
        });
    }
}


