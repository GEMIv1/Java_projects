// retrive the weather data from the api -- backend
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class weatherAPP {
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);
        JSONObject location = (JSONObject)locationData.get(0);
        double latitude = (double)location.get("latitude");
        double longitude =(double)location.get("longitude");
        String urlString = "https://api.open-meteo.com/v1/forecast?"+"latitude="+latitude+"&longitude="+longitude+"&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";
        try{
            HttpURLConnection conn = fetchApiResponse(urlString);
            
            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to the API.");
                return null;
            }
            
            StringBuilder resultJSON = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while(scanner.hasNext()){
                resultJSON.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resulObject = (JSONObject)parser.parse(String.valueOf(resultJSON));
            JSONObject hourly = (JSONObject)resulObject.get("hourly");
            JSONArray time = (JSONArray)hourly.get("time");
            int idx = findIdxOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray)hourly.get("temperature_2m");
            double temperature = (double)temperatureData.get(idx);

            JSONArray weatherCodeData = (JSONArray)hourly.get("weather_code");
            String weathearCondition = convertWeatherCode((long)weatherCodeData.get(idx));

            JSONArray humidityData = (JSONArray)hourly.get("relative_humidity_2m");
            long humidity = (long)humidityData.get(idx);

            JSONArray windSpeedData = (JSONArray)hourly.get("wind_speed_10m");
            double windSpeed = (double)windSpeedData.get(idx);

            JSONObject WeatherDataFrontEnd = new JSONObject();
            WeatherDataFrontEnd.put("temperature", temperature);
            WeatherDataFrontEnd.put("weathearCondition", weathearCondition);
            WeatherDataFrontEnd.put("humidity", humidity);
            WeatherDataFrontEnd.put("windSpeed", windSpeed);
            return WeatherDataFrontEnd;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static String convertWeatherCode(long code){
        String weatherCondition = "";
        if(code==0L){
            weatherCondition = "Clear";

        }
        else if(code<=3L && code >0L){
            weatherCondition = "Cloudy";
        }
        else if((code>=51L && code<=67L) || (code>=80L && code<=99L)){
            weatherCondition = "Rain";
        }
        else if((code>=71L && code<=77L)){
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }

    private static int findIdxOfCurrentTime(JSONArray time){
        String currentTime = getCurrentTime();
        for(int i=0;i<time.size();i++){
            String str = (String)time.get(i);
            if(str.equalsIgnoreCase(currentTime))return i;
        }

        return 0;
    }

    public static String getCurrentTime(){
        LocalDateTime currDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formattedDataTime = currDateTime.format(formatter);
        return formattedDataTime;
    }

    public static JSONArray getLocationData(String locaitonName){
        locaitonName = locaitonName.replaceAll(" ", "+");//new york == new+york
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locaitonName + "&count=10&language=en&format=json";
        try{
            HttpURLConnection conn =  fetchApiResponse(urlString);
            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API.");
                return null;
            }
            else{
                StringBuilder resultJSON = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    resultJSON.append(scanner.nextLine());
                }
                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resuJsonObject = (JSONObject)parser.parse(String.valueOf(resultJSON));
                JSONArray locationData = (JSONArray)resuJsonObject.get("results");
                return locationData;
            }


        }catch(Exception e){
            e.getStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {    
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch(IOException e) {
            e.printStackTrace(); // Corrected here
        }
        return null; 
    }
    
}