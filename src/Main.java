
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/FXML/main.fxml"));
        Scene scene = new Scene(root);

        // ✅ Load CSS manually from classpath
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Set window icon and title
        Image icon = new Image("/Image/itc.png");
        stage.getIcons().add(icon);
        stage.setTitle("CPU SCHEDULING ALGORITHM SIMULATOR");

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
