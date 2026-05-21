import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class admin_Login
{
    static final String FilePath = "admins.txt";

    public static Boolean login(String ID, String password) throws Exception {
        File file = new File(FilePath);

        if(!file.exists()){
            System.out.println("File does not exist");
            return false;
        }

        FileReader fl = new FileReader(file);
        BufferedReader br = new BufferedReader(fl);

        String line;
        while ((line = br.readLine()) != null) {
            if(!line.contains(":") || line.trim().isEmpty()) continue;
            String[] parts = line.split(":");
            if(parts.length > 2) continue;
            String user = parts[0];
            String pass = parts[1];

            if(user.equals(ID) && pass.equals(password)){
                br.close();
                System.out.println("Login successful! Welcome, " + ID);
                return true;
            }
        }
        br.close();
        System.out.println("Invalid username or password.");
        return false;
    }
}
