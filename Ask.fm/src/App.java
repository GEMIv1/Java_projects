import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    
    public static void main(String[] args) throws Exception {
        String Userspath = "usersFile.txt";
        Scanner scanner = new Scanner(System.in);        
        int choice;

        do {
            System.out.println("1 - Login");
            System.out.println("2 - Sign up");
            System.out.println("3 - Exit");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    String name, password;
                    System.out.print("Enter the user name: ");
                    name = scanner.next();
                    System.out.print("Enter the password: ");
                    password = scanner.next();
                    LogIn(name, password, Userspath);
                    break;
                case 2:
                    String newName, newPassword, email;
                    System.out.print("Enter your name: ");
                    newName = scanner.next();
                    System.out.print("Enter your password: ");
                    newPassword = scanner.next();
                    System.out.print("Enter your e-mail: ");
                    email = scanner.next();
                    SignUp(newName, newPassword, email, Userspath);
                    break;
                case 3:
                    System.out.println("Exiting the application.");
                    break;
                default:
                    System.out.println("Invalid choice, try again!");
                    break;
            }
        } while (choice != 3);

        scanner.close();
    }

    static void LogIn(String name, String password, String path) {
        user u = null;
        try (BufferedReader buffer = new BufferedReader(new FileReader(path))) {
            String line, readName, readPass;
            boolean found = false;
            while ((line = buffer.readLine()) != null) {
                String[] userDetails = line.split(",");
                readName = userDetails[1].trim();
                readPass = userDetails[2].trim();
                
                if (readName.equals(name) && readPass.equals(password)) {
                    u = new user(Integer.parseInt(userDetails[0]),userDetails[1],userDetails[2],userDetails[3]);
                    found = true;
                    break;
                }
            }
            if (found) {
                System.out.println("Login successfully !");
                u.loadQuestions();
                LogInMenu(u,path);
            } else {
                System.out.println("Wrong user name or password !");
            }
        } 
        catch (Exception e) {
            System.out.println("Error reading user file. LogIn function: " + e.getMessage());
        }
    }

    static void LogInMenu(user u, String path){
        int choice;
        Scanner scanner = new Scanner(System.in);
        do { 
            System.out.println("1 - Send question");
            System.out.println("2 - Answer question");
            System.out.println("3 - Show feed");
            System.out.println("4 - Show my questions");
            System.out.println("5 - Show users");
            System.out.println("6 - Exit");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    String text;
                    int qID = generateId("questionsFile.txt");
                    int toID, anonymous;
                    System.out.println("To: ");
                    toID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter the question you need to ask: ");
                    text = scanner.nextLine();
                    System.out.print("Do you want to send the question in the anonymous mode? [1: yes, 2: no] ");
                    anonymous = scanner.nextInt();
                    question q = new question(qID, toID, (anonymous == 1), text );
                    u.sendQuestion(q);
                    break;
                case 2:
                    System.out.print("Enter the question ID to answer: ");
                    int qToAnswer = scanner.nextInt();
                    System.out.print("Your answer: ");
                    scanner.nextLine();
                    String ans = scanner.nextLine();
                    u.answerQuestion(qToAnswer,ans);
                    break;
                case 3:
                    try {
                        BufferedReader questionReader = new BufferedReader(new FileReader("questionsFile.txt"));
                        String questionLine;

                        BufferedReader answerReader = new BufferedReader(new FileReader("answersFile.txt"));
                        String answerLine;
                        ArrayList<String[]> answersList = new ArrayList<>();

                        while ((answerLine = answerReader.readLine()) != null) {
                            String[] answerDetails = answerLine.split(",");
                            answersList.add(answerDetails);
                        }

                        while ((questionLine = questionReader.readLine()) != null) {
                            String[] questionDetails = questionLine.split(",");
                            int qID1 = Integer.parseInt(questionDetails[0].trim());
                            String sender = questionDetails[1].trim();
                            int toID1 = Integer.parseInt(questionDetails[2].trim());
                            String questionText = questionDetails[3].trim();

                            System.out.println("Question ID: " + qID1);
                            System.out.println("Asked by: " + (sender.equals("anonymous") ? "Anonymous" : sender));
                            System.out.println("To User ID: " + toID1);
                            System.out.println("Question: " + questionText);
                            System.out.println("Answers:");

                            boolean hasAnswers = false;
                            for (String[] answerDetails : answersList) {
                                int answeredQID = Integer.parseInt(answerDetails[0].trim());
                                if (answeredQID == qID1) {
                                    System.out.println("- " + answerDetails[1] + ", Answerd by: " + answerDetails[2]);
                                    hasAnswers = true;
                                }
                            }

                            if (!hasAnswers) {
                                System.out.println("- No answers yet.");
                            }
                            System.out.println("----------------------------------");
                        }

                        questionReader.close();
                        answerReader.close();
                     } catch (Exception e) {
                        System.out.println("Error displaying feed: " + e.getMessage());
                    }
                    break;
                case 4:
                    u.showQuestions();
                    break;
                case 5:
                    String line;
                    try(BufferedReader buffer = new BufferedReader(new FileReader(path))){
                        while((line = buffer.readLine()) != null){
                            String [] userDetails = line.split(",");
                            System.out.print("ID: "+userDetails[0]+", Name: "+userDetails[1]+", Email: "+userDetails[3]+"\n"+"--------------------------------------\n2");
                        }
                    }catch (Exception e){
                        System.out.println("Error message"+e.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("Exitting ...");
                    break;
                default:
                    System.out.println("Invalid choice try again !");;
            }
        } while (choice!=6);
    }

    static void SignUp(String name, String password, String email, String path) {
        boolean found = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (name.equals(userDetails[1].trim())) {
                    found = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading user file. SignUp function: " + e.getMessage());
            return; 
        }
    
        if (!found) {
            try (FileWriter fileWriter = new FileWriter(path, true)) {  
                int id = generateId(path);
                fileWriter.write(id + "," + name + "," + password + "," + email + '\n');
                System.out.println("Sign up successful!");
            } catch (Exception e) {
                System.out.println("Error writing to user file. SignUp function: " + e.getMessage());
            }
        } else {
            System.out.println("User name already exists !!");
        }
    }
    

    static int generateId(String path) {
        int id = 1;
        try (BufferedReader buffer = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                id++;
            }
        } 
        catch (Exception e) {
            System.out.println("Error reading user file. ID generator function: " + e.getMessage());
        }

        return id;
    }    
}
