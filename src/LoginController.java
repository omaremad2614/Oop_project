import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.List;

public class LoginController {

    // FXML Bindings: These link directly to the fx:id elements in your login.fxml file
    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel; // We will use THIS single label for all messages

    /**
     * Instantly switches the screen to a new FXML file.
     * We swap the "root" of the current scene instead of making a new scene.
     * This prevents the window from resizing or breaking fullscreen mode!
     */
    private void switchScene(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Grab the current scene from our messageLabel and just replace its content
        messageLabel.getScene().setRoot(root);
    }

    /**
     * Switches the screen after a 2-second delay.
     * Great for showing a "Logging in..." message before actually moving.
     */
    private void switchSceneWithDelay(String fxmlFile) throws Exception {
        messageLabel.setText("Logging in...");
        messageLabel.getStyleClass().removeAll("message-error");
        messageLabel.getStyleClass().add("message-success");

        // Create a 2-second timer
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            try {
                switchScene(fxmlFile); // Reuse our clean switchScene method!
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pause.play(); // Start the timer
    }

    /**
     * Triggered when the user clicks the "SIGN IN" button.
     */
    @FXML
    private void handleLogin() {
        try {
            String id = idField.getText();
            String password = passwordField.getText();

            // 1. Validate that the ID format is correct
            ID_check check = ID_check.check(id);
            if (!check.valid) {
                showMessage(check.text, false);
                return; // Stop running this method
            }

            // 2. Validate that the password meets our security rules
            PasswordValidation.Result passcheck = PasswordValidation.valid(password);
            if (!passcheck.valid) {
                showMessage(passcheck.text, false);
                return;
            }

            // 3. Check the text file to see if the user exists and credentials match
            Student loggedInStudent = register_login.login(id, password);

            if (loggedInStudent != null) { // If it's not null, login was a success!

                // NEW WAY: Just hand the whole student to FileManager and let it do all the heavy lifting!
                FileManager.loadStudentCourses(loggedInStudent);

                // Save to Session and go to Dashboard
                Session.setCurrentStudent(loggedInStudent);
                switchSceneWithDelay("student_dashboard.fxml");
            } else {
                showMessage("Invalid ID or password.", false);
            }
        } catch (Exception e) {
            showMessage(e.getMessage(), false);
        }
    }

    /**
     * Triggered when the user clicks "Create an account".
     */
    @FXML
    private void handleRegister() {
        try {
            switchScene("register_page.fxml");
        } catch (Exception e) {
            showMessage(e.getMessage(), false);
        }
    }

    /**
     * Triggered when the user clicks "Admin Login".
     */
    @FXML
    private void handleAdminLogin() {
        try {
            switchSceneWithDelay("AdminLoginPage.fxml");
        } catch (Exception e) {
            showMessage(e.getMessage(), false);
        }
    }

    /**
     * Helper method to easily display colored text on the screen.
     */
    private void showMessage(String text, boolean isSuccess) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().removeAll("message-success", "message-error");
        messageLabel.getStyleClass().add(isSuccess ? "message-success" : "message-error");
    }
}