import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CourseDetailController {

    // ── FXML Bindings ────────────────────────────────────────────────────────
    @FXML private Label  iconLabel;
    @FXML private Label  codeLabel;
    @FXML private Label  nameLabel;
    @FXML private Label  descLabel;
    @FXML private Label  hoursLabel;
    @FXML private Label  scheduleLabel;
    @FXML private Label  instructorLabel;
    @FXML private Label  capacityLabel;
    @FXML private Label  seatsLabel;
    @FXML private Label  messageLabel;
    @FXML private Button registerBtn;
    @FXML private Button backBtn;

    // The specific course the user clicked on
    private Course course;

    /**
     * Populate all labels with the chosen course's details.
     * This is called by the Dashboard right before showing this screen.
     */
    public void setCourse(Course course) {
        this.course = course;
        refreshUI();
    }

    /**
     * Fills the UI text fields and checks if the student is already registered.
     */
    private void refreshUI() {
        if (course == null) return;

        // Set basic text details
        iconLabel.setText(course.getIcon());
        codeLabel.setText(course.getCode());
        nameLabel.setText(course.getName());
        descLabel.setText(course.getDescription());
        hoursLabel.setText(course.getCreditHours() + " credit hour(s)");
        scheduleLabel.setText(course.getSchedule());
        instructorLabel.setText(course.getInstructor());
        capacityLabel.setText(course.getEnrolledStudents() + " / " + course.getMaxStudents());
        updateSeatsLabel();

        // 1. Reset the button to its default state first
        registerBtn.setDisable(false);
        registerBtn.setText("Register for this Course");

        Student currentStudent = Session.getCurrentStudent();
        if (currentStudent != null) {
            String studentId = String.valueOf(currentStudent.getId());

            try {
                // 2. The Ultimate Check: Ask the FileManager if they are in the database!
                boolean inDatabase = FileManager.isAlreadyRegistered(studentId, course.getCode());

                // 3. Check the student's current session list as a backup
                boolean inSessionList = false;
                for (Course c : currentStudent.getRegisteredCourses()) {
                    if (c.getCode().equals(course.getCode())) {
                        inSessionList = true;
                        break;
                    }
                }

                // If they are in the database OR the session list, disable the button!
                if (inDatabase || inSessionList) {
                    registerBtn.setDisable(true);
                    registerBtn.setText("Already Registered");
                    return; // Stop checking, we are done here
                }
            } catch (Exception e) {
                System.out.println("Error checking registration status: " + e.getMessage());
            }
        }

        // 4. Finally, disable the button if the course is completely full
        if (!course.hasAvailableSeats()) {
            registerBtn.setDisable(true);
            registerBtn.setText("Course Full");
        }
    }

    /**
     * Calculates seats left and updates the label text.
     */
    private void updateSeatsLabel() {
        int seats = course.getAvailableSeats();
        seatsLabel.setText(seats == 0 ? "No seats available" : seats + " seat(s) open");
    }

    /**
     * Helper to show colored success/error messages on the screen.
     */
    private void showMessage(String text, boolean success) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().removeAll("message-success", "message-error");
        messageLabel.getStyleClass().add(success ? "message-success" : "message-error");
    }

    // ── Button Click Handlers ────────────────────────────────────────────────

    /**
     * Triggered when the user clicks "Register for this Course".
     */
    @FXML
    private void handleRegister() {
        try {
            Student currentStudent = Session.getCurrentStudent();

            // Failsafe: Ensure a student is actually logged in
            if(currentStudent == null){
                showMessage("No student logged in.", false);
                return;
            }

            // Loop through student's courses to prevent duplicate registration
            for(Course c : currentStudent.getRegisteredCourses()){
                if(c.getCode().equals(course.getCode())){
                    showMessage("You already registered this course.", false);
                    registerBtn.setDisable(true);
                    registerBtn.setText("Already Registered");
                    return;
                }
            }

            // Check if the class is completely full
            if(!course.hasAvailableSeats()){
                showMessage("This course is full.", false);
                registerBtn.setDisable(true);
                registerBtn.setText("Course Full");
                return;
            }

            // Double-check the text file database
            if(FileManager.isAlreadyRegistered(String.valueOf(currentStudent.getId()), course.getCode())){
                showMessage("You already registered for this course.", false);
                registerBtn.setDisable(true);
                return;
            }

            // If all checks pass, officially enroll the student
            course.enroll(); // Adds +1 to enrolled students
            CourseCatalog.getInstance().saveCoursesToFile();
            currentStudent.addCourse(course); // Adds course to the student object
            FileManager.saveStudentCourse(currentStudent.getId(), course.getCode()); // Saves to text file

            // Update UI to reflect the new numbers
            capacityLabel.setText(course.getEnrolledStudents() + " / " + course.getMaxStudents());
            updateSeatsLabel();

            showMessage("Successfully registered for " + course.getName(), true);
            registerBtn.setDisable(true);
            registerBtn.setText("Registered");

        } catch (Exception e) {
            showMessage(e.getMessage(), false);
        }
    }

    /**
     * Returns the user to the Dashboard WITHOUT resizing the window.
     */
    @FXML
    private void handleBack() {
        try {
            // 1. Ask the Session for the exact dashboard we left behind
            Parent savedDashboard = Session.getSavedDashboard();

            if (savedDashboard != null) {
                // 2. We have it! Swap it back in. All scroll positions remain perfectly intact.
                messageLabel.getScene().setRoot(savedDashboard);
            } else {
                // 3. Fallback: If for some reason it's missing, load a fresh one.
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("dashboard.fxml"));
                messageLabel.getScene().setRoot(loader.load());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}