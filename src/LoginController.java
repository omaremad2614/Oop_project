import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private void switchScene(String fxmlFile) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleLogin() throws Exception{
        try{
            String id = idField.getText();
            String password = passwordField.getText();

            //validate the id
            ID_check check = ID_check.check(id);
            if(!check.valid){
                messageLabel.setText("Invalid ID or password.");
                messageLabel.getStyleClass().removeAll("message-success", "message-error");
                messageLabel.getStyleClass().add("message-error");

                return;
            }

            //password check
            PasswordValidation.Result passcheck = PasswordValidation.valid(password);
            if(!passcheck.valid){
                messageLabel.setText("Invalid ID or password.");
                messageLabel.getStyleClass().removeAll("message-success", "message-error");
                messageLabel.getStyleClass().add("message-error");

                return;
            }

            //try to Log in
            Boolean success = register_login.login(id, password);
            if(success){
                messageLabel.setText("Login successful! Welcome, " + id);
                messageLabel.getStyleClass().removeAll("message-success", "message-error");
                messageLabel.getStyleClass().add("message-error");

                switchScene("dashboard.fxml");
            }
            else {
                messageLabel.setText("Invalid ID or password.");
                messageLabel.getStyleClass().removeAll("message-success", "message-error");
                messageLabel.getStyleClass().add("message-error");

            }
        } catch (Exception e){
            messageLabel.setText(e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }


    }

    @FXML
    private void handleRegister()throws Exception{
        try{
            switchScene("register_page.fxml");
        } catch (Exception e){
            messageLabel.setText(e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleAdminLogin(){
        try{
            switchScene("AdminLoginPage.fxml");
        } catch (Exception e){
            messageLabel.setText(e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

}
