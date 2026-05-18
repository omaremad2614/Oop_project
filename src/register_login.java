import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class register_login
{
    static final String FilePath = "user_login.txt";

    public static void register(String ID, String password) throws IOException {
        ID_check id = ID_check.check(ID);
        Scanner scanner = new Scanner(System.in);
        while (id.valid == false){
            System.out.println(id.text);
            System.out.println("re-enter your id");
            ID = scanner.nextLine();
            id =ID_check.check(ID);
        }
        System.out.println("enter password");
        password = scanner.nextLine();
        PasswordValidation.Result R = PasswordValidation.valid(password);
        while(R.valid == false) {
            System.out.println(R.text);
            System.out.println("re-enter your password!");
            password = scanner.nextLine();
            R = PasswordValidation.valid(password);
        }
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
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);

        System.out.println("1.Register");
        System.out.println("2.login");
        System.out.println("3.exit");
        System.out.println("Enter your choice");
        int choice = scan.nextInt();
        scan.nextLine();


        if(choice == 1){
            System.out.println("enter ID");
            String id = scan.next();
            register(id,"");
        }
        else if(choice == 2){
            System.out.println("enter ID");
            String id = scan.next();
            scan.nextLine();
            System.out.println("Enter your password");
            String password = scan.nextLine();
            login(id, password);
        }
        else if(choice == 3) {
            System.out.println("bey!!!");
            System.exit(0);
        }
        scan.close();
    }
}
