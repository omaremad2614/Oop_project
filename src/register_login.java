import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class register_login {
    static final String FilePath = "user_login.txt";

    // NEW FORMAT: ID:Password:Name:GPA:Term

    public static void register(String ID, String password, String name) throws IOException {
        FileWriter fw = new FileWriter(FilePath, true);
        BufferedWriter bw = new BufferedWriter(fw);

        // FIXED: Added the Name in the 3rd slot, surrounded by colons!
        bw.write(ID + ":" + password + ":" + name + ":0.0:1");

        bw.newLine();
        bw.close();
        System.out.println("Successfully registered " + ID + " (" + name + ")");
    }

    public static Student login(String ID, String password) throws Exception {
        File file = new File(FilePath);
        if(!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.contains(":") || line.trim().isEmpty()) continue;

                String[] parts = line.split(":");
                String user = parts[0];
                String pass = parts[1];

                // FIXED: Shifted all the array numbers to account for the new Name!
                String realName = (parts.length >= 3) ? parts[2] : "Student";
                double gpa = (parts.length >= 4) ? Double.parseDouble(parts[3]) : 0.0;
                int term = (parts.length >= 5) ? Integer.parseInt(parts[4]) : 1;

                if(user.equals(ID) && pass.equals(password)){
                    System.out.println("Login successful! Welcome, " + realName);
                    // FIXED: Pass the realName into the Student object instead of hardcoding "Student"
                    return new Student(Integer.parseInt(ID), realName, "student@email.com", gpa, term);
                }
            }
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(FilePath);
        if (!file.exists()) return students;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains(":") || line.trim().isEmpty()) continue;
                String[] parts = line.split(":");

                // FIXED: Same shifts here!
                String realName = (parts.length >= 3) ? parts[2] : "Student";
                double gpa = (parts.length >= 4) ? Double.parseDouble(parts[3]) : 0.0;
                int term = (parts.length >= 5) ? Integer.parseInt(parts[4]) : 1;

                // FIXED: Use realName here too
                students.add(new Student(Integer.parseInt(parts[0]), realName, "student@email.com", gpa, term));
            }
        } catch (Exception e) {
            System.out.println("Error reading students: " + e.getMessage());
        }
        return students;
    }

    public static void updateStudentGpa(int targetId, double newGpa) {
        List<String> fileLines = new ArrayList<>();
        File file = new File(FilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2 && parts[0].equals(String.valueOf(targetId))) {

                    // FIXED: We must grab the Name and Term so we don't accidentally delete them!
                    String realName = (parts.length >= 3) ? parts[2] : "Student";
                    String currentTerm = (parts.length >= 5) ? parts[4] : "1";

                    // Rebuild the line safely: ID:Pass:Name:NewGPA:Term
                    fileLines.add(parts[0] + ":" + parts[1] + ":" + realName + ":" + newGpa + ":" + currentTerm);
                } else {
                    fileLines.add(line);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String l : fileLines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}