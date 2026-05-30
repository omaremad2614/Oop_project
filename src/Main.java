import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("Student Portal");

        // Force the app to start at 1000x700 so the 2-column layout fits immediately
        stage.setScene(new Scene(root, 1000, 700));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}