import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        Student s1 = new Student(241005357,"Hamza", "hmzahra2006@gmail.com", "Engineerng");
        Course c1 = new Course("ECE2201", "Digital Systems", 3);



        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("Login");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(new Scene(root));
        stage.show();

    }


    public static void main(String[] args) {launch(args);}
}