package ovh.rehost.realWeather;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ScheduledWeatherStateUpdateHandler implements Runnable {

    private static RealWeather plugin;


    ScheduledWeatherStateUpdateHandler() {
    }

    private static int getActualWeatherID() throws IOException {
        StringBuilder result = new StringBuilder();
        URL url;
        try {
            url = new URL(String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s", plugin.getCity(), plugin.getCountry(), plugin.getApikey()));
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            while ((str = rd.readLine()) != null) {
                result.append(str);
            }
            str = result.toString();
            JsonObject json = new JsonParser().parse(str).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject();
            return json.getAsJsonPrimitive("id").getAsInt();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static boolean testAndSetup(RealWeather plug) {
        try {
            plugin = plug;
            int weatherID = getActualWeatherID();
            Bukkit.getServer().getScheduler().runTask(plugin, new ScheduledWeatherUpdateHandler(plugin, weatherID));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            int weatherID = getActualWeatherID();
            Bukkit.getServer().getScheduler().runTask(plugin, new ScheduledWeatherUpdateHandler(plugin, weatherID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
