import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class dashboardController {

    // The empty container we just created in FXML
    @FXML private TilePane courseContainer;

    /**
     * Runs automatically when the student logs in.
     */
    @FXML
    public void initialize() {
        // 1. Clear out any old data
        courseContainer.getChildren().clear();

        // 2. Loop through every single course in our text file database!
        for (Course course : CourseCatalog.getInstance().getAllCourses()) {

            // 3. Build a visual card for it and add it to the screen
            VBox card = createCourseCard(course);
            courseContainer.getChildren().add(card);
        }
    }

    /**
     * This method dynamically builds the FXML Course Card using Java code!
     */
    private VBox createCourseCard(Course course) {
        // The main outer card
        VBox card = new VBox();
        card.getStyleClass().add("course-card");
        card.setPrefWidth(380.0);

        // -- 1. The Header (Blue Stripe) --
        HBox header = new HBox(16);
        header.getStyleClass().add("course-card-header");
        header.setPadding(new Insets(22, 24, 22, 24));
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane iconBg = new StackPane();
        iconBg.getStyleClass().add("course-icon-bg");
        iconBg.setPrefSize(52, 52);
        Label icon = new Label(course.getIcon());
        icon.getStyleClass().add("course-icon-label");
        iconBg.getChildren().add(icon);

        VBox titleBox = new VBox(4);
        Label codeLabel = new Label(course.getCode());
        codeLabel.getStyleClass().add("course-code");
        Label nameLabel = new Label(course.getName());
        nameLabel.getStyleClass().add("course-name");
        titleBox.getChildren().addAll(codeLabel, nameLabel);

        header.getChildren().addAll(iconBg, titleBox);

        // -- 2. The Body (White Area) --
        VBox body = new VBox(12);
        body.getStyleClass().add("course-card-body");
        body.setPadding(new Insets(20, 24, 28, 24));

        Label desc = new Label(course.getDescription());
        desc.getStyleClass().add("course-desc");
        desc.setWrapText(true);

        HBox meta = new HBox(8);
        meta.setAlignment(Pos.CENTER_LEFT);
        Label credits = new Label(course.getCreditHours() + " Credit Hours");
        credits.getStyleClass().add("course-meta");
        Label dot = new Label("·");
        dot.getStyleClass().add("course-meta-dot");
        Label schedule = new Label(course.getSchedule());
        schedule.getStyleClass().add("course-meta");
        meta.getChildren().addAll(credits, dot, schedule);

        body.getChildren().addAll(desc, meta);

        // -- 3. The Register Button --
        HBox btnContainer = new HBox();
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        btnContainer.setPadding(new Insets(10, 20, 16, 20));

        Button registerBtn = new Button("Register →");
        registerBtn.getStyleClass().add("course-btn");

        // Magically attach the click event to this specific course!
        registerBtn.setOnAction(event -> openDetail(course.getCode(), event));

        btnContainer.getChildren().add(registerBtn);

        // Snap all three pieces together
        card.getChildren().addAll(header, body, btnContainer);
        return card;
    }

    /**
     * Opens the course detail screen for the given course code.
     */
    private void openDetail(String code, ActionEvent event) {
        try {
            Course course = CourseCatalog.getInstance().findByCode(code);
            if (course == null) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("course_detail.fxml"));
            Parent root = loader.load();

            CourseDetailController ctrl = loader.getController();
            ctrl.setCourse(course);

            Node sourceButton = (Node) event.getSource();
            Parent currentDashboard = (Parent) sourceButton.getScene().getRoot();

            Session.setSavedDashboard(currentDashboard);
            sourceButton.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleOpenDashboard() { // Removed ActionEvent parameter
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("student_dashboard.fxml"));

            // Use your existing courseContainer to grab the screen!
            courseContainer.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignOut() { // Removed ActionEvent parameter
        try {
            Session.setCurrentStudent(null);

            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("login.fxml"));
            courseContainer.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}