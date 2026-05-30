import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCourseController {

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField descField;
    @FXML private TextField creditsField;
    @FXML private TextField scheduleField;
    @FXML private TextField instructorField;
    @FXML private TextField capacityField;
    @FXML private Label messageLabel;

    @FXML
    private void handleSaveCourse() {
        try {
            // 1. Grab all the text
            String code = codeField.getText();
            String name = nameField.getText();
            String desc = descField.getText();
            String schedule = scheduleField.getText();
            String instructor = instructorField.getText();

            // 2. Validate empty fields
            if (code.isEmpty() || name.isEmpty() || capacityField.getText().isEmpty()) {
                messageLabel.setText("Please fill out all required fields.");
                messageLabel.setStyle("-fx-text-fill: #FF5252;");
                return;
            }

            // 3. Convert numbers
            int credits = Integer.parseInt(creditsField.getText());
            int capacity = Integer.parseInt(capacityField.getText());

            // 4. Create the new Course Object
            Course newCourse = new Course(code, name, desc, credits, schedule, instructor, capacity, 0, "📚");

            // 5. Save it to our Database!
            CourseCatalog.getInstance().addCourse(newCourse);

            // 6. Close the popup window
            handleCancel();

        } catch (NumberFormatException e) {
            messageLabel.setText("Credits and Capacity must be numbers!");
            messageLabel.setStyle("-fx-text-fill: #FF5252;");
        }
    }

    @FXML
    private void handleCancel() {
        // Find the window this button is inside, and close it.
        Stage stage = (Stage) codeField.getScene().getWindow();
        stage.close();
    }
}