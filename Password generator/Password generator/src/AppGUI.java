//Front end
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.print.attribute.standard.JobKOctetsSupported;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class AppGUI extends JFrame{

    private passwordGenerator pass;

    public AppGUI(){
        super("Password Generator");
        setSize(540,530);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pass = new passwordGenerator();

        addGUIcommponent();
    }
    private void addGUIcommponent(){
        JLabel titleLabel = new JLabel("Password Generator");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 540, 39);
        add(titleLabel);

        JTextArea passwordOutput = new JTextArea();
        passwordOutput.setEditable(false);
        passwordOutput.setFont(new Font("Dialog", Font.BOLD, 32));

        JScrollPane passwordOutputPane = new JScrollPane(passwordOutput);
        passwordOutputPane.setBounds(17, 70, 490, 70);
        passwordOutputPane.setBorder(BorderFactory.createLineBorder(Color.black));
        add(passwordOutputPane);

        JLabel passwordLengthLabel = new JLabel("Password Length: ");
        passwordLengthLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        passwordLengthLabel.setBounds(25, 160, 200, 30);
        add(passwordLengthLabel);

        JTextArea passwordLengthInputArea = new JTextArea();
        passwordLengthInputArea.setFont(new Font("Dialog", Font.PLAIN, 24));
        passwordLengthInputArea.setBounds(230, 160, 150, 30);
        passwordLengthInputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(passwordLengthInputArea);

        JLabel nameLabel = new JLabel("Password for: ");
        nameLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        nameLabel.setBounds(25, 210, 150, 30);
        add(nameLabel);

        JTextArea nameInputArea = new JTextArea();
        nameInputArea.setFont(new Font("Dialog", Font.PLAIN, 24));
        nameInputArea.setBounds(180, 210, 200, 40);
        nameInputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(nameInputArea);

        JToggleButton uppercaseB = new JToggleButton("Uppercase");
        uppercaseB.setFont(new Font("Dialog", Font.PLAIN, 20));
        uppercaseB.setBounds(9, 260, 222, 41);
        add(uppercaseB);

        JToggleButton lowercaseB = new JToggleButton("Lowercase");
        lowercaseB.setFont(new Font("Dialog", Font.PLAIN, 20));
        lowercaseB.setBounds(295, 260, 222, 41);
        add(lowercaseB);

        JToggleButton numbersB = new JToggleButton("Numbers");
        numbersB.setFont(new Font("Dialog", Font.PLAIN, 20));
        numbersB.setBounds(9, 310, 222, 41);
        add(numbersB);

        JToggleButton symbolsB = new JToggleButton("Symbols");
        symbolsB.setFont(new Font("Dialog", Font.PLAIN, 20));
        symbolsB.setBounds(295, 310, 222, 41);
        add(symbolsB);

        JButton generateButton = new JButton("Generate");
        generateButton.setFont(new Font("Dialog", Font.BOLD, 26));
        generateButton.setBounds(150, 370, 222, 41);
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(passwordLengthInputArea.getText().length()<=0)return;
                
                boolean anyToogleSelected = lowercaseB.isSelected() || uppercaseB.isSelected() || numbersB.isSelected() || symbolsB.isSelected();
                int passLength = Integer.parseInt(passwordLengthInputArea.getText());
                if(anyToogleSelected){
                    String generatedPassword = pass.generatePassword(passLength,uppercaseB.isSelected(), lowercaseB.isSelected(), numbersB.isSelected(), symbolsB.isSelected());
                    passwordOutput.setText(generatedPassword);

                    int response = JOptionPane.showConfirmDialog(null, "Do you want to save the generated password (" + generatedPassword + ") for " + nameInputArea.getText(), "Alert", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        try(PrintWriter printWriter = new PrintWriter(new FileWriter("MyPasswords.txt",true))){
                            printWriter.print(nameInputArea.getText()+": ");
                            printWriter.println(generatedPassword);
                            JOptionPane.showMessageDialog(null, "The password saved successfully!!");
                        }catch(IOException E){
                            E.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error: can not save save in file!!");
                        }
                    } 
                    //else if (response == JOptionPane.NO_OPTION) {} 
                    else {
                        JOptionPane.showMessageDialog(null, "The password did not save!!");
                    }
                }
            }
        });
        add(generateButton);
    }
}
