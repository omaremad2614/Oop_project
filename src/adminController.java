import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class adminController {

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
    private void handleAdminLogin(){

        try{
            String id = idField.getText();
            String password = passwordField.getText();

            //try to Log in
            Boolean success = admin_Login.login(id, password);
            if(success){
                messageLabel.setText("Login successful! Welcome, " + id);
                messageLabel.getStyleClass().removeAll("message-success", "message-error");
                messageLabel.getStyleClass().add("message-error");

                switchScene("");
            }
            else {
                messageLabel.setText("Invalid ID or password.");
                messageLabel.getStyleClass().removeAll("message-success", "message-error");
                messageLabel.getStyleClass().add("message-error");

            }
        }catch (Exception e){
            messageLabel.setText(e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }



    @FXML
    private void handleBack() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
