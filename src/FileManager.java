import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading student course registrations using a text file.
 * Database Format: StudentID|CourseCode
 */
public class FileManager {

    private static final String FILE_PATH = "student_courses.txt";

    /**
     * Checks if a specific student is already registered for a specific course.
     * Used by CourseDetailController to lock the register button.
     */
    /**
     * Checks if a specific student is already registered for a specific course.
     * Used by CourseDetailController to lock the register button.
     */
    public static boolean isAlreadyRegistered(String studentId, String courseCode) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");

                // THE FIX: Change == 2 to >= 2 so it successfully reads the new 6-part grade lines!
                if (parts.length >= 2) {
                    // Added our .trim() and .toUpperCase() safety nets here too!
                    String savedId = parts[0].trim();
                    String savedCode = parts[1].trim().toUpperCase();

                    // If both the ID and Course Code match, they are already registered
                    if (savedId.equals(studentId.trim()) && savedCode.equals(courseCode.trim().toUpperCase())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading student courses: " + e.getMessage());
        }
        return false;
    }

    /**
     * Officially enrolls a student by writing their ID, Course, and 4 empty grades to the text file.
     */
    public static void saveStudentCourse(int studentId, String courseCode) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            // THE CRITICAL FIX: We must add the four |0.0 slots for the grades!
            bw.write(studentId + "|" + courseCode + "|0.0|0.0|0.0|0.0|false");

            bw.newLine();
            System.out.println("Saved LMS registration: " + studentId + " -> " + courseCode);
        } catch (IOException e) {
            System.out.println("Error saving registration: " + e.getMessage());
        }
    }

    /**
     * Reads the database and returns a list of all courses a specific student is taking.
     * We will use this when the student logs in to load their dashboard!
     */
    public static List<Course> loadStudentCourses(Student student) {
        List<Course> registeredCourses = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return registeredCourses;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");

                if (parts.length >= 2 && parts[0].trim().equals(String.valueOf(student.getId()))) {
                    String courseCode = parts[1].trim().toUpperCase();

                    Course course = CourseCatalog.getInstance().findByCode(courseCode);

                    if (course != null) {
                        boolean added = student.addCourse(course);

                        if (parts.length == 6) {
                            Grades grades = student.getGradesForCourse(courseCode);
                            if (grades != null) {
                                grades.setWeek7(Double.parseDouble(parts[2].trim()));
                                grades.setWeek12(Double.parseDouble(parts[3].trim()));
                                grades.setCourseWork(Double.parseDouble(parts[4].trim()));
                                grades.setFinalExam(Double.parseDouble(parts[5].trim()));
                                if (parts.length == 7) {
                                    grades.setFinalized(Boolean.parseBoolean(parts[6].trim()));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading student courses: " + e.getMessage());
        }
        return registeredCourses;
    }

    /**
     * Finds a specific student's registration in the database and updates their 4 grades!
     */
    public static void updateGradesInDatabase(int studentId, String courseCode, Grades newGrades) {
        List<String> fileLines = new ArrayList<>();
        File file = new File(FILE_PATH);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                // If it's the exact student and the exact course...
                if (parts.length >= 2 && parts[0].equals(String.valueOf(studentId)) && parts[1].equals(courseCode)) {
                    // Rebuild the line with the newly typed grades!
                    String updatedLine = studentId + "|" + courseCode + "|" +
                            newGrades.getWeek7() + "|" +
                            newGrades.getWeek12() + "|" +
                            newGrades.getCourseWork() + "|" +
                            newGrades.getFinalExam() + "|" +
                            newGrades.isFinalized();
                    fileLines.add(updatedLine);
                } else {
                    fileLines.add(line); // Keep everyone else's data unchanged
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Write the updated lines back to the text file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String l : fileLines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

}