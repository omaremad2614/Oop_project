import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class registerController {

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Button backButton;
    @FXML private Button confirm;

    @FXML
    private void handleBack() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void handleConfirm(){
            try{
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
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e){
                messageLabel.setText(e.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
    }
}
