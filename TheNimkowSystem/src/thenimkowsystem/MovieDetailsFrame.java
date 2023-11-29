
package thenimkowsystem;

/**
 *
 * @author 
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailsFrame extends JFrame {

    private int movieId;

    public MovieDetailsFrame(int movieId) {
        this.movieId = movieId;
        initComponents();
        fetchMovieDetailsFromAPI(movieId);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel movieTitleLabel = new JLabel();
        JLabel moviePosterLabel = new JLabel();
        JTextArea movieInfoArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(movieInfoArea);

        movieInfoArea.setEditable(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(movieTitleLabel, BorderLayout.NORTH);
        mainPanel.add(moviePosterLabel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void fetchMovieDetailsFromAPI(int movieId) {
        try {
            String apiKey = "f846867b6184611eeff179631d3f9e26"; // Reemplazar con tu clave API
            URL url = new URL("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey + "&language=es-ES");
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

            String movieDetails = processMovieDetailsResponse(response.toString());
            updateUIWithMovieDetails(movieDetails);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processMovieDetailsResponse(String jsonResponse) {
        StringBuilder details = new StringBuilder();

        try {
            // Convertir la respuesta JSON a un objeto JSON para acceder a los datos
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // Obtener el título de la película
            String title = jsonObject.get("title").getAsString();
            details.append("Título: ").append(title).append("\n\n");

            // Obtener la descripción de la película (sinopsis)
            String overview = jsonObject.get("overview").getAsString();
            details.append("Sinopsis: ").append(overview).append("\n\n");

            // Obtener el nombre del director
            JsonArray crew = jsonObject.getAsJsonArray("crew");
            String directorName = "";
            for (JsonElement element : crew) {
                JsonObject crewMember = element.getAsJsonObject();
                if (crewMember.get("job").getAsString().equals("Director")) {
                    directorName = crewMember.get("name").getAsString();
                    break;
                }
            }
            details.append("Director: ").append(directorName).append("\n\n");

            // Obtener el género de la película
            JsonArray genres = jsonObject.getAsJsonArray("genres");
            StringBuilder genresList = new StringBuilder("Género: ");
            for (JsonElement element : genres) {
                JsonObject genre = element.getAsJsonObject();
                String genreName = genre.get("name").getAsString();
                genresList.append(genreName).append(", ");
            }
            if (genresList.length() > 9) {
                genresList.delete(genresList.length() - 2, genresList.length());
            }
            details.append(genresList);

            // Se puede continuar extrayendo más información según lo necesites

        } catch (Exception e) {
            e.printStackTrace();
        }

        return details.toString();
    }

    private void updateUIWithMovieDetails(String movieDetails) {
        JTextArea movieInfoArea = (JTextArea) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getView();
        movieInfoArea.setText(movieDetails);
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new MovieDetailsFrame(123).setVisible(true);
        });
    }
}


