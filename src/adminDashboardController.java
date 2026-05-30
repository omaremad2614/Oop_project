import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;


public class adminDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label messageLabel;

    @FXML private ComboBox<String> weekSimulator;
    @FXML private CheckBox finalizeBox;

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, Double> colWk7, colWk12, colCW, colFinal, colTotal;

    @FXML private TextField wk7Field, wk12Field, cwField, finalField;

    private ObservableList<Student> myStudentsList = FXCollections.observableArrayList();
    private String myCourseCode;

    @FXML
    public void initialize() {
        myCourseCode = Session.getCurrentAdminCourseCode();
        if (myCourseCode == null) return;

        Course myCourse = CourseCatalog.getInstance().findByCode(myCourseCode);
        if (myCourse != null) welcomeLabel.setText("Gradebook: " + myCourse.getCode());

        // 1. Setup Standard Columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // 2. Setup Nested Grade Columns (Using a custom JavaFX lambda to look inside the Grades object!)
        colWk7.setCellValueFactory(data -> new SimpleObjectProperty<>(getGrades(data.getValue()).getWeek7()));
        colWk12.setCellValueFactory(data -> new SimpleObjectProperty<>(getGrades(data.getValue()).getWeek12()));
        colCW.setCellValueFactory(data -> new SimpleObjectProperty<>(getGrades(data.getValue()).getCourseWork()));
        colFinal.setCellValueFactory(data -> new SimpleObjectProperty<>(getGrades(data.getValue()).getFinalExam()));
        colTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(getGrades(data.getValue()).getTotalMark()));

        loadMyStudents();

        // 3. LISTEN FOR CLICKS: Auto-fill text fields when a student is selected
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                Grades g = getGrades(newSel);
                wk7Field.setText(String.valueOf(g.getWeek7()));
                wk12Field.setText(String.valueOf(g.getWeek12()));
                cwField.setText(String.valueOf(g.getCourseWork()));
                finalField.setText(String.valueOf(g.getFinalExam()));
                finalizeBox.setSelected(g.isFinalized());
            }
        });

        // 4. LISTEN FOR DOUBLE-CLICKS: Open the student profile
        studentTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && studentTable.getSelectionModel().getSelectedItem() != null) {
                openStudentDetails(studentTable.getSelectionModel().getSelectedItem());
            }
        });
        // Setup Week Simulator Dropdown
        weekSimulator.getItems().addAll("Week 1 (Classes Begin)", "Week 7 (Midterm 1)", "Week 12 (Midterm 2)", "Week 16 (Finals)");
        weekSimulator.setValue("Week 1 (Classes Begin)"); // Default to week 1

        // Whenever the professor changes the week, re-evaluate which text boxes should be locked!
        weekSimulator.valueProperty().addListener((obs, oldVal, newVal) -> updateTimeLocks(newVal));
        updateTimeLocks(weekSimulator.getValue()); // Lock them immediately on startup
    }

    private Grades getGrades(Student s) {
        return s.getGradesForCourse(myCourseCode);
    }

    private void loadMyStudents() {
        myStudentsList.clear();

        // Safety net: Destroy any hidden spaces from the admin's course code!
        String safeCourseCode = myCourseCode.trim();
        System.out.println("--- DEBUG: Professor is looking for students in: [" + safeCourseCode + "] ---");

        List<Student> allStudents = register_login.getAllStudents();
        System.out.println("DEBUG: Found " + allStudents.size() + " total students in user_login.txt");

        for (Student student : allStudents) {
            FileManager.loadStudentCourses(student); // Load their courses & grades

            System.out.print("DEBUG: Checking Student ID " + student.getId() + "... ");

            // Look at their grades to see if they take this class
            Grades grades = student.getGradesForCourse(safeCourseCode);

            if (grades != null) {
                System.out.println("MATCH! Added to table.");
                myStudentsList.add(student);
            } else {
                System.out.println("FAILED. They don't have a grades sheet for [" + safeCourseCode + "].");
            }
        }
        studentTable.setItems(myStudentsList);
    }

    @FXML
    private void handleSaveMarks() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Please select a student from the table first!");
            return;
        }

        try {
            // Read typed values
            double w7 = Double.parseDouble(wk7Field.getText());
            double w12 = Double.parseDouble(wk12Field.getText());
            double cw = Double.parseDouble(cwField.getText());
            double fin = Double.parseDouble(finalField.getText());


            // Validate against maximums
            if (w7 > 30 || w12 > 20 || cw > 10 || fin > 40) {
                messageLabel.setText("Error: A grade exceeds the maximum allowed points.");
                return;
            }

            // Update the object
            Grades g = selected.getGradesForCourse(myCourseCode);
            g.setWeek7(w7); g.setWeek12(w12); g.setCourseWork(cw); g.setFinalExam(fin);
            g.setWeek7(w7); g.setWeek12(w12); g.setCourseWork(cw); g.setFinalExam(fin);
            g.setFinalized(finalizeBox.isSelected()); // Save the finalized status!

            // Save to database file!
            FileManager.updateGradesInDatabase(selected.getId(), myCourseCode, g);

            messageLabel.setText("Marks saved successfully!");
            messageLabel.setStyle("-fx-text-fill: #00E676;"); // Green success color
            studentTable.refresh(); // Visually update the table

        } catch (NumberFormatException e) {
            messageLabel.setText("Error: Please enter valid numbers only.");
        }
    }

    private void openStudentDetails(Student student) {
        try {
            // Save the selected student to memory so the popup knows who to show
            Session.setCurrentStudent(student);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_details.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Forces them to close popup before continuing
            stage.setTitle("Student Profile");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleSignOut() {
        try {
            Session.setCurrentAdminCourseCode(null);
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            welcomeLabel.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void updateTimeLocks(String currentWeek) {
        int weekNum = 1; // Default to week 1

        // Safely check the string to find the correct week!
        if (currentWeek.contains("Week 7")) weekNum = 7;
        else if (currentWeek.contains("Week 12")) weekNum = 12;
        else if (currentWeek.contains("Week 16")) weekNum = 16;

        // Disable boxes based on the week! Coursework is always open.
        wk7Field.setDisable(weekNum < 7);
        wk12Field.setDisable(weekNum < 12);
        finalField.setDisable(weekNum < 16);
    }

}

