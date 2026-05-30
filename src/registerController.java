import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class registerController {

    @FXML private TextField nameField; // NEW
    @FXML private TextField idField;

    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField;
    @FXML private Button togglePasswordBtn;

    // NEW: Confirm Password Bindings
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField confirmPasswordTextField;
    @FXML private Button toggleConfirmBtn;

    @FXML private Label messageLabel;
    @FXML private Label ruleLength, ruleNumber, ruleUpper, ruleLower, ruleSpace;

    private boolean isPasswordVisible = false;
    private boolean isConfirmVisible = false;

    @FXML
    public void initialize() {
        // Main Password Listeners
        passwordTextField.textProperty().addListener((obs, old, newVal) -> {
            if (isPasswordVisible) passwordField.setText(newVal);
            validatePasswordRealTime(newVal);
        });
        passwordField.textProperty().addListener((obs, old, newVal) -> {
            if (!isPasswordVisible) passwordTextField.setText(newVal);
            validatePasswordRealTime(newVal);
        });

        // Confirm Password Listeners
        confirmPasswordTextField.textProperty().addListener((obs, old, newVal) -> {
            if (isConfirmVisible) confirmPasswordField.setText(newVal);
        });
        confirmPasswordField.textProperty().addListener((obs, old, newVal) -> {
            if (!isConfirmVisible) confirmPasswordTextField.setText(newVal);
        });
    }

    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        passwordTextField.setVisible(isPasswordVisible);
        passwordTextField.setManaged(isPasswordVisible);
        passwordField.setVisible(!isPasswordVisible);
        passwordField.setManaged(!isPasswordVisible);
        togglePasswordBtn.setText(isPasswordVisible ? "🙈" : "👁");
    }

    @FXML
    private void toggleConfirmVisibility() {
        isConfirmVisible = !isConfirmVisible;
        confirmPasswordTextField.setVisible(isConfirmVisible);
        confirmPasswordTextField.setManaged(isConfirmVisible);
        confirmPasswordField.setVisible(!isConfirmVisible);
        confirmPasswordField.setManaged(!isConfirmVisible);
        toggleConfirmBtn.setText(isConfirmVisible ? "🙈" : "👁");
    }

    private void validatePasswordRealTime(String password) {
        updateRuleLabel(ruleLength, password.length() >= 8);
        updateRuleLabel(ruleNumber, password.matches(".*[0-9].*"));
        updateRuleLabel(ruleUpper, password.matches(".*[A-Z].*"));
        updateRuleLabel(ruleLower, password.matches(".*[a-z].*"));
        updateRuleLabel(ruleSpace, !password.contains(" ") && !password.isEmpty());
    }

    private void updateRuleLabel(Label label, boolean isValid) {
        String baseText = label.getText().substring(2);
        if (isValid) {
            label.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            label.setText("✓ " + baseText);
        } else {
            label.setStyle("-fx-text-fill: gray; -fx-font-weight: normal;");
            label.setText("• " + baseText);
        }
    }

    @FXML
    private void handleConfirm() {
        try {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String password = isPasswordVisible ? passwordTextField.getText() : passwordField.getText();
            String confirm = isConfirmVisible ? confirmPasswordTextField.getText() : confirmPasswordField.getText();

            // 1. Validate Name (Must be at least 3 words)
            if (name.split("\\s+").length < 3) {
                showMessage("Please enter your First, Middle, and Last name.", false);
                return;
            }

            // 2. Validate Password Match
            if (!password.equals(confirm)) {
                showMessage("Passwords do not match!", false);
                return;
            }

            // 3. Validate ID
            ID_check check = ID_check.check(id);
            if (!check.valid) {
                showMessage(check.text, false);
                return;
            }

            // 4. Validate Password Rules
            PasswordValidation.Result passcheck = PasswordValidation.valid(password);
            if (!passcheck.valid) {
                showMessage(passcheck.text, false);
                return;
            }

            // 5. Save to Database (We now pass 3 arguments instead of 2!)
            register_login.register(id, password, name);
            showMessage("Account Created Successfully! Please Sign In.", true);

        } catch (Exception e) {
            showMessage("Error creating account: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            messageLabel.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showMessage(String text, boolean isSuccess) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().removeAll("message-success", "message-error");
        messageLabel.getStyleClass().add(isSuccess ? "message-success" : "message-error");
    }
}