import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.json.simple.JSONObject;

public class weatherAppGUI extends JFrame{
    private JSONObject weatherObject;
    public weatherAppGUI(){
        // setup add title to our gui
        super("Weather App");

        // configure gui to end the program process once its close 
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // size of gui
        setSize(450,650);

        // load the gui at the center of the screen
        setLocationRelativeTo(null);

        //
        setLayout(null);

        // prevent any resize
        setResizable(false);

        addGUIComponents();
    }
    
    private void addGUIComponents(){
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15,15,350,45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN,24));
        add(searchTextField);

        JLabel weatherConditionImage = new JLabel(loadImage("D:\\Projects\\Java\\Weather\\src\\cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        JLabel tmepratureText = new JLabel("10 °C");
        tmepratureText.setBounds(0,350,450,54);
        tmepratureText.setFont(new Font("Dialog",Font.BOLD,48));
        tmepratureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(tmepratureText);

        JLabel weatherConditiobDesc = new JLabel("Cloudy");
        weatherConditiobDesc.setBounds(0,405,450,36);
        weatherConditiobDesc.setFont(new Font("Dialog",Font.PLAIN,32));
        weatherConditiobDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditiobDesc);

        JLabel humidityImage = new JLabel(loadImage("D:\\Projects\\Java\\Weather\\src\\humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90,500,85,55);
        humidityText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(humidityText);

        JLabel windSpeedImage = new JLabel(loadImage("D:\\Projects\\Java\\Weather\\src\\windspeed.png"));
        windSpeedImage.setBounds(220,500,74,66);
        add(windSpeedImage);

        JLabel windSpeedText = new JLabel("<html><b>WindSpeed</b> 15km/h</b></html>");
        windSpeedText.setBounds(310,500,85,55);
        windSpeedText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windSpeedText);

        JButton searhButton = new JButton(loadImage("D:\\Projects\\Java\\Weather\\src\\search.png"));
        searhButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searhButton.setBounds(375,13,47,45);
        searhButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String userInput = searchTextField.getText();
                if(userInput.replaceAll("\\s", "").length()<=0){return;}
                
                weatherObject = weatherAPP.getWeatherData(userInput);

                if (weatherObject == null) {
                    JOptionPane.showMessageDialog(null, "Weather data not found for the input: " + userInput, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String weatherCondition = (String)weatherObject.get("weathearCondition");
                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("D:\\Projects\\Java\\Weather\\src\\clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("D:\\Projects\\Java\\Weather\\src\\cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("D:\\Projects\\Java\\Weather\\src\\rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("D:\\Projects\\Java\\Weather\\src\\snow.png"));
                        break;
                    default:
                        break;
                }

                double temperature = (double)weatherObject.get("temperature");
                tmepratureText.setText(temperature+" °C");
                
                weatherConditiobDesc.setText(weatherCondition);

                long humidityNumer = (long)weatherObject.get("humidity");
                humidityText.setText("<html><b>Humidity: </b>"+ humidityNumer +"%</html>");

                double windSpeed123 = (double)weatherObject.get("windSpeed");
                windSpeedText.setText("<html><b>WindSpeed: </b>" + windSpeed123 + "km/h</html>");
            }
        }); 
        add(searhButton);
    }

    private ImageIcon loadImage(String path){
        try{
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Could not find resourse");
        return null;
    }
}