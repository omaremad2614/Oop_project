import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class register_login
{
    static final String FilePath = "user_login.txt";

    public static void register(String ID, String password) throws IOException {
        FileWriter fw = new FileWriter(FilePath, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(ID + ":" + password);
        bw.newLine();
        bw.close();
        System.out.println("Successfully registered " + ID);
    }


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
