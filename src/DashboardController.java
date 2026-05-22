import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DashboardController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label majorLabel;

    @FXML
    private ListView<Course> coursesListView;

    private Student currentStudent;

    public void setStudent(Student student) {

        currentStudent = student;

        nameLabel.setText(student.getName());

        majorLabel.setText(student.getMajor());

        coursesListView.getItems().addAll(
                student.getRegisteredCourses()
        );
    }

    @FXML
    private void openCatalog() {

        System.out.println("Open catalog clicked");
    }

    @FXML
    private void logout() {

        System.out.println("Logout clicked");
    }
}