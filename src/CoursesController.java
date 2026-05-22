import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;

public class CoursesController {

    private CourseCatalog catalog = new CourseCatalog();

    @FXML

    private void handleCourseClick(javafx.event.ActionEvent event) {

        Button clickedButton = (Button) event.getSource();

        String courseName = clickedButton.getText();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Selected");
        alert.setHeaderText(null);
        alert.setContentText("You selected:\n" + courseName);

        alert.showAndWait();
    }
}