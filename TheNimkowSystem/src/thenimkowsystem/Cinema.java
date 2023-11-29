
package thenimkowsystem;

/**
 *
 * @author Diego
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import thenimkowsystem.MovieDetailsFrame;

public class Cinema {

    private JPanel resultsPanel;

    public Cinema() {
        initUI();
    }

    private void initUI() {
        JFrame frame = new JFrame("Movie Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMovies(searchField.getText()));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        frame.add(searchPanel, BorderLayout.NORTH);

        resultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void searchMovies(String searchTerm) {
        if (!searchTerm.isEmpty()) {
            String apiKey = "f846867b6184611eeff179631d3f9e26";

            try {
                String actorIds = null;
                String genreId = null;
                URL url = new URL("https://api.themoviedb.org/3/discover/movie?api_key=" + apiKey + "&with_genres=" + genreId + "&with_cast=" + actorIds);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                StringBuilder response;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");

                resultsPanel.removeAll();

                for (JsonElement element : results) {
                    JsonObject movie = element.getAsJsonObject();

                    // Obtener el título de la película
                    JsonElement titleElement = movie.get("title");
                    String title = (titleElement != null && !titleElement.isJsonNull()) ? titleElement.getAsString() : "No Title Available";

                    // Obtener la URL del póster de la película
                    String posterPath = movie.get("poster_path").getAsString();
                    String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

                    // Cargar la imagen del póster o manejar excepciones
                    try {
                        ImageIcon posterImage = new ImageIcon(new URL(imageUrl));
                        Image scaledImage = posterImage.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
                        ImageIcon scaledPosterImage = new ImageIcon(scaledImage);

                        JLabel movieLabel = new JLabel(title);
                        movieLabel.setIcon(scaledPosterImage);
                        int movieId = movie.get("id").getAsInt();
                        movieLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                new MovieDetailsFrame(movieId).setVisible(true);
                            }
                        });

                        resultsPanel.add(movieLabel);
                    } catch (Exception e) {
                        // Manejo de excepciones al cargar la imagen
                        ImageIcon errorImage = new ImageIcon("ruta/a/imagen/error.png"); // Ruta a una imagen de error
                        JLabel errorLabel = new JLabel(title, errorImage, JLabel.CENTER);
                        resultsPanel.add(errorLabel);
                    }
                }

                resultsPanel.revalidate();
                resultsPanel.repaint();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Cinema());
    }
}








