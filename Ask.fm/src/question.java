import java.util.ArrayList;

public class question {
    private String text;
    private int qID,toID;
    boolean anonymous;
    private ArrayList<String> answers;

    public question(int qID, int toID, boolean anonymous, String text){
        this.qID = qID;
        this.text = text;
        this.toID = toID;
        this.anonymous = anonymous;
        this.answers = new ArrayList<>();
    }

    public void addAnswer(String answer) {
        answers.add(answer);
    }

    public ArrayList<String> getAnswer() {
        return answers;
    }

    public int getqID() {
        return qID;
    }

    public String getText() {
        return text;
    }

    public int getToID() {
        return toID;
    }

    public  boolean getAnonymous(){
        return anonymous;
    }

    public void showAnswers() {
        if (answers.isEmpty()) {
            System.out.println("No answers yet.");
        } else {
            for (int i = 0; i < answers.size(); i++) {
                System.out.println("Answer " + (i + 1) + ": " + answers.get(i));
            }
        }
    }

    
}
