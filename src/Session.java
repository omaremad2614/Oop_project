import javafx.scene.Parent;

public class Session {

    private static Student currentStudent;

    private static String currentAdminCourseCode;

    // A place to save our dashboard screen
    private static Parent savedDashboard;

    public static void setSavedDashboard(Parent dashboard) {
        savedDashboard = dashboard;
    }

    public static Parent getSavedDashboard() {
        return savedDashboard;
    }

    public static void setCurrentStudent(
            Student student
    ){

        currentStudent = student;
    }

    public static Student getCurrentStudent(){

        return currentStudent;
    }

    public static void setCurrentAdminCourseCode(String code) {
        currentAdminCourseCode = code;
    }

    public static String getCurrentAdminCourseCode() {
        return currentAdminCourseCode;
    }

}