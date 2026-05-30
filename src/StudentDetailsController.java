import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class StudentDetailsController {

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label termLabel;
    @FXML private Label gpaLabel;

    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> colCode;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, Integer> colCredits;
    @FXML private TableColumn<Course, Double> colTotal;

    @FXML
    public void initialize() {
        // 1. Grab the specific student we clicked on from memory!
        Student student = Session.getCurrentStudent();
        if (student == null) return;

        // 2. Populate the Top Cards
        idLabel.setText(String.valueOf(student.getId()));
        nameLabel.setText(student.getName());
        termLabel.setText("Term " + student.getTerm());
        gpaLabel.setText(String.valueOf(student.getGpa()));

        // 3. Setup the Table Columns
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("creditHours"));

        // Magically look inside the student's Grades object to get the Total Mark for each row!
        colTotal.setCellValueFactory(data -> {
            Course course = data.getValue();
            Grades grades = student.getGradesForCourse(course.getCode());
            return new SimpleObjectProperty<>(grades != null ? grades.getTotalMark() : 0.0);
        });

        // 4. Load their registered courses into the table
        ObservableList<Course> courses = FXCollections.observableArrayList(student.getRegisteredCourses());
        courseTable.setItems(courses);
    }

    @FXML
    private void handleClose() {
        // Grab the window this button is inside, and close it safely.
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }
}