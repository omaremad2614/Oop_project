import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin() throws Exception{
        String id = idField.getText();
        String password = passwordField.getText();

        //validate the id
        ID_check check = ID_check.check(id);
        if(!check.valid){
            messageLabel.setText(check.text);
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        //password check
        PasswordValidation.Result passcheck = PasswordValidation.valid(password);
        if(!passcheck.valid){
            messageLabel.setText(passcheck.text);
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        //try to Log in
        Boolean success = register_login.login(id, password);
        if(success){
            messageLabel.setText("Login successful! Welcome, " + id);
            messageLabel.setStyle("-fx-text-fill: green;");
        }
        else {
            messageLabel.setText("Invalid ID or password.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }

    }

    @FXML
    private void handleRegister()throws Exception{
        String id = idField.getText();
        String password = passwordField.getText();

        //validate the id
        ID_check check = ID_check.check(id);
        if(!check.valid){
            messageLabel.setText(check.text);
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        //password check
        PasswordValidation.Result passcheck = PasswordValidation.valid(password);
        if(!passcheck.valid){
            messageLabel.setText(passcheck.text);
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        register_login.register(id, password);
        messageLabel.setText("Registered successfully!");
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void handleAdminLogin(){
        messageLabel.setText("coming soon!!");
    }

}
