import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class user {
    private int uID;
    private String name, password, email;
    private ArrayList<question> questions;

    public user(int uID, String name, String password, String email) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.password = password;
        questions = new ArrayList<>();
    }

    public int getID() { return uID; }

    public void sendQuestion(question q) {
        try (FileWriter writer = new FileWriter("questionsFile.txt", true)) {
            if (q.getAnonymous()) {
                writer.write(q.getqID() + ",anonymous," + q.getToID() + "," + q.getText() + "\n");
            } else {
                writer.write(q.getqID() + "," + uID + "," + q.getToID() + "," + q.getText() + "\n");
            }
            System.out.println("Question sent successfully.");
        } catch (Exception e) {
            System.out.println("Error while saving the question: " + e.getMessage());
        }
        questions.add(q);
    }

    public void loadQuestions() {
    try (BufferedReader buffer = new BufferedReader(new FileReader("questionsFile.txt"))) {
        String line;
        while ((line = buffer.readLine()) != null) {
            String[] questionDetails = line.split(",");
            int senderId = questionDetails[1].equals("anonymous") ? -1 : Integer.parseInt(questionDetails[1].trim());
            int receiverId = Integer.parseInt(questionDetails[2].trim());

            if (senderId == this.uID || receiverId == this.uID) {
                question q = new question(
                    Integer.parseInt(questionDetails[0]), 
                    receiverId,                          
                    senderId == -1,                      
                    questionDetails[3]                  
                );

                try (BufferedReader answerBuffer = new BufferedReader(new FileReader("answersFile.txt"))) {
                    String answerLine;
                    while ((answerLine = answerBuffer.readLine()) != null) {
                        String[] answerDetails = answerLine.split(",");
                        int answeredQuestionId = Integer.parseInt(answerDetails[0].trim());
                        if (answeredQuestionId == q.getqID()) {
                            q.addAnswer(answerDetails[1].trim());
                        }
                    }
                }

                questions.add(q);
            }
        }
    } catch (Exception e) {
        System.out.println("Error while loading questions: " + e.getMessage());
    }
}


    public void answerQuestion(int questionId, String answer) {
        boolean questionFound = false;
        for (question q : questions) {
            if (q.getqID() == questionId) {
                q.addAnswer(answer);
                questionFound = true;
                System.out.println("Answer added successfully.");

                try (FileWriter writer = new FileWriter("answersFile.txt", true)) {
                    writer.write(questionId + "," + answer + ", from user "+ uID +"\n");
                } catch (Exception e) {
                    System.out.println("Error while saving the answer: " + e.getMessage());
                }
                break;
            }
        }
        if (!questionFound) {
            System.out.println("Question not found.");
        }
    }

    public void showQuestions() {
        if (questions.isEmpty()) {
            System.out.println("No questions to show.");
            return;
        }

        for (question q : questions) {
            System.out.println("Question ID: " + q.getqID() + ", Content: " + q.getText());
            q.showAnswers();
        }
    }
}
