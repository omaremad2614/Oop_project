import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class adminController {

    // ── FXML Bindings (These match the new dark-mode FXML) ──────────────
    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    /**
     * Triggered when the user clicks the "SIGN IN" button.
     */
    @FXML
    private void handleAdminLogin() {
        try {
            String id = idField.getText();
            String password = passwordField.getText();

            // Check if fields are empty
            if (id.isEmpty() || password.isEmpty()) {
                showMessage("Please enter both ID and Password.", false);
                return;
            }

            // Call your backend code (admin_Login.java) to check the text file!
            String assignedCourseCode = admin_Login.login(id, password);

            if (assignedCourseCode != null) {
                Session.setCurrentAdminCourseCode(assignedCourseCode);

                showMessage("Logging in... Authenticating Professor.", true);

                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
                pause.setOnFinished(event -> {
                    try {
                        switchScene("admin_dashboard.fxml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                pause.play();

            } else {
                showMessage("Invalid Admin ID or Password.", false);
            }

        } catch (Exception e) {
            showMessage("Database error: " + e.getMessage(), false);
        }
    }

    /**
     * Triggered when the user clicks "← Go back to student login".
     */
    @FXML
    private void handleBack() {
        try {
            switchScene("login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── Helper Methods ───────────────────────────────────────────────────

    /**
     * Instantly switches the screen to a new FXML file without resizing the window.
     */
    private void switchScene(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Swap the "root" to keep our fullscreen intact
        messageLabel.getScene().setRoot(root);
    }

    /**
     * Helper method to easily display colored text on the screen.
     */
    private void showMessage(String text, boolean isSuccess) {
        messageLabel.setText(text);

        // Use inline styles here to guarantee it looks good against the dark background
        if (isSuccess) {
            messageLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;"); // Bright Green
        } else {
            messageLabel.setStyle("-fx-text-fill: #FF5252; -fx-font-weight: bold;"); // Bright Red
        }
    }
}