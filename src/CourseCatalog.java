import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds every course offered this semester.
 * Now acts as a real Database Manager for courses.txt!
 */
public class CourseCatalog {

    // ── Singleton ────────────────────────────────────────────────────────────
    private static CourseCatalog instance;

    public static CourseCatalog getInstance() {
        if (instance == null)
            instance = new CourseCatalog();
        return instance;
    }

    // ── Data ─────────────────────────────────────────────────────────────────
    private final List<Course> courses = new ArrayList<>();
    private final String FILE_PATH = "courses.txt";

    private CourseCatalog() {
        loadCoursesFromFile(); // Load from the text file when the app starts!
    }

    /**
     * Reads the courses.txt file and turns every line into a Course Object.
     */
    private void loadCoursesFromFile() {
        courses.clear();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("Warning: courses.txt not found! Creating an empty catalog.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Split the line by the pipe symbol "|"
                // Note: in regex, "|" is a special character, so we use "\\|"
                String[] parts = line.split("\\|");

                if (parts.length == 9) {
                    Course course = new Course(
                            parts[0], // Code
                            parts[1], // Name
                            parts[2], // Description
                            Integer.parseInt(parts[3]), // Credits
                            parts[4], // Schedule
                            parts[5], // Instructor
                            Integer.parseInt(parts[6]), // Max Seats
                            Integer.parseInt(parts[7]), // Enrolled
                            parts[8]  // Icon
                    );
                    courses.add(course);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    /**
     * Call this method from the Admin Dashboard whenever you add, edit, or delete a course.
     * It rewrites the courses.txt file with the updated data.
     */
    public void saveCoursesToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Course c : courses) {
                // Re-build the pipe-separated string
                String line = c.getCode() + "|" +
                        c.getName() + "|" +
                        c.getDescription() + "|" +
                        c.getCreditHours() + "|" +
                        c.getSchedule() + "|" +
                        c.getInstructor() + "|" +
                        c.getMaxStudents() + "|" +
                        c.getEnrolledStudents() + "|" +
                        c.getIcon();
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Database Updated: courses.txt saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    // ── Public API (Used by your Dashboard) ──────────────────────────────────

    public List<Course> getAllCourses() {
        return Collections.unmodifiableList(courses);
    }

    public Course findByCode(String code) {
        if (code == null) return null;
        for (Course c : courses) {
            if (c.getCode().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return null;
    }

    // Admin Method: Add a new course to the catalog
    public void addCourse(Course newCourse) {
        courses.add(newCourse);
        saveCoursesToFile(); // Automatically save to text file!
    }
}