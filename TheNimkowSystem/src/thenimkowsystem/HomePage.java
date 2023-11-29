package thenimkowsystem;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import thenimkowsystem.MovieDetailsFrame;

public class HomePage extends JFrame {

    private JPanel resultsPanel;

    public HomePage() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMovies(searchField.getText().trim()));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        resultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(new JScrollPane(resultsPanel), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
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

                for (JsonElement element : results) {
                    JsonObject movie = element.getAsJsonObject();
                    String title = movie.get("title").getAsString();

                    String posterPath = movie.get("poster_path").getAsString();
                    String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

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
                }

                revalidate();
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomePage().setVisible(true);
        });
    }
}
