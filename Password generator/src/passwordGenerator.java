//Back end
import java.util.Random;

public class passwordGenerator {
    public static String LOWERCASE_chars = "abcdefghijklmnopqrstuvwxyz";
    public static String UPPERCASE_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String NUMBERS = "0123456789";
    public static String SYMBOLS = "!@#$%^&*()-_+=[]{};:.,<>/?";
    private final Random random;

    public passwordGenerator(){random = new Random();}//constructor

    public String generatePassword(int Length,boolean upper, boolean lower, boolean number, boolean symbols){
            StringBuilder passBuilder = new StringBuilder();

            String validChars = "";
            if(upper) validChars+=UPPERCASE_chars;
            if(lower) validChars+=LOWERCASE_chars;
            if(number) validChars+=NUMBERS;
            if(symbols) validChars+=SYMBOLS;

            for(int i=0;i<Length;i++){
                int randomIdx = random.nextInt(validChars.length());
                char randomChar = validChars.charAt(randomIdx);
                passBuilder.append(randomChar);
            }
        return passBuilder.toString();
    }
}
