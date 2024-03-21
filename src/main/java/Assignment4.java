import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Assignment4 {
    
    // My key: 3e53e4bc8b9215314789abc753636bf5
    
    // return a GeoInfo object for the given city
    public static GeoInfo getGeoInfo(String city) {
        GeoInfo gi = new GeoInfo();
         try {
            URL url = new URL("https://api.openweathermap.org/geo/1.0/direct?q="+ city+ "&appid=3e53e4bc8b9215314789abc753636bf5");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                Gson gson = new Gson();
                GeoInfo[] geoInfoArray = gson.fromJson(br, GeoInfo[].class);
                
                String cityName = "";
                double cityLat = 0;
                double cityLon = 0;
                
                for (GeoInfo geoInfo : geoInfoArray) {
                    cityName = geoInfo.name;
                    cityLat = geoInfo.lat;
                    cityLon = geoInfo.lon;
                }
             
                gi.name = cityName;
                gi.lat = cityLat;
                gi.lon = cityLon;

                br.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gi;
    }

    // return temperature for the given GeoInfo object
    public static double getTemperature(GeoInfo gi) {
        double lat = gi.lat;
        double lon = gi.lon;
        double temp = 0;
         try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+ lat+ "&lon="+ lon+ "&appid=3e53e4bc8b9215314789abc753636bf5");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                Gson gson = new Gson();
                GeoInfoTemp geoInfoTemp = gson.fromJson(br, GeoInfoTemp.class);
             
                temp = KelvinToFahrenheit(geoInfoTemp.main.temp);

                br.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }
    
    public static double KelvinToFahrenheit(double kelvin) {
        double fahrenheit = (kelvin - 273.15) * 9/5 + 32;
        return fahrenheit;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a city: ");
        String city = scan.nextLine();

        GeoInfo gi = getGeoInfo(city);
        System.out.println("lat = " + gi.lat + " lon = " + gi.lon);
        double temp = getTemperature(gi);
        System.out.printf("Temperature: %3.1f (Fahrenheit)\n", temp);
    }
}

class GeoInfo {
    String name;
    double lat;
    double lon;
}

class GeoInfoTemp {
    Main main;
}

class Main {
    double temp;
}
