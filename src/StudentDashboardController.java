import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

public class StudentDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label idLabel;
    @FXML private Label termLabel;
    @FXML private Label gpaLabel;

    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> colCode;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, String> colWk7, colWk12, colCW, colFinal, colTotal;

    @FXML
    public void initialize() {
        Student me = Session.getCurrentStudent();
        if (me == null) return;

        // 1. Setup Top Stats
        welcomeLabel.setText("Welcome back, " + me.getId());
        idLabel.setText(String.valueOf(me.getId()));
        termLabel.setText("Term " + me.getTerm());
        gpaLabel.setText(String.valueOf(me.getGpa()));

        // 2. Setup Course Info Columns
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // 3. Setup Grade Columns (Using lambdas to grab the grades for each specific course)
        colWk7.setCellValueFactory(data -> new SimpleStringProperty(formatGrade(getGrades(me, data.getValue()).getWeek7())));
        colWk12.setCellValueFactory(data -> new SimpleStringProperty(formatGrade(getGrades(me, data.getValue()).getWeek12())));
        colCW.setCellValueFactory(data -> new SimpleStringProperty(formatGrade(getGrades(me, data.getValue()).getCourseWork())));
        colFinal.setCellValueFactory(data -> new SimpleStringProperty(formatGrade(getGrades(me, data.getValue()).getFinalExam())));
        colTotal.setCellValueFactory(data -> new SimpleStringProperty(formatGrade(getGrades(me, data.getValue()).getTotalMark())));
        // 4. Fill the table with the courses the student is registered for!
        ObservableList<Course> myCourses = FXCollections.observableArrayList(me.getRegisteredCourses());
        courseTable.setItems(myCourses);
    }

    // Helper method to safely grab grades
    private Grades getGrades(Student student, Course course) {
        Grades g = student.getGradesForCourse(course.getCode());
        return (g != null) ? g : new Grades(); // Return empty grades if something goes wrong
    }

    @FXML
    private void handleOpenCatalog() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml")); // Assuming your catalog is still dashboard.fxml
            welcomeLabel.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleSignOut() {
        try {
            Session.setCurrentStudent(null); // Clear memory
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            welcomeLabel.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String formatGrade(double grade) {
        if (grade == 0) return "0";
        if (grade % 1 == 0) return String.format("%.0f", grade);
        return String.valueOf(grade);
    }
}