import javax.swing.SwingUtilities;

public class appLuncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new weatherAppGUI().setVisible(true);
            }
        });
        
    }
}
