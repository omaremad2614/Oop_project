import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class admin_Login
{
    static final String FilePath = "admins.txt";

    public static String login(String ID, String password) throws Exception {
        File file = new File(FilePath);

        if(!file.exists()){
            System.out.println("File does not exist");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.contains(":") || line.trim().isEmpty()) continue;

                String[] parts = line.split(":");
                // Now we expect 3 parts: ID, Password, CourseCode
                if(parts.length < 3) continue;

                String user = parts[0];
                String pass = parts[1];
                String courseCode = parts[2]; // Grab the specific course!

                if(user.equals(ID) && pass.equals(password)){
                    System.out.println("Login successful! Welcome Professor, teaching " + courseCode);
                    return courseCode; // Return the course code!
                }
            }
        }
        System.out.println("Invalid username or password.");
        return null;
    }
}

