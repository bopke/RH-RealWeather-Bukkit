package pl.rehost.realWeather;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

public class ScheduledWeatherStateUpdateHandler implements Runnable {

    private final RealWeather plugin;


    ScheduledWeatherStateUpdateHandler(RealWeather plugin) {
        this.plugin = plugin;
    }

    private int getActualWeatherID() {
        StringBuilder result = new StringBuilder();
        URL url;
        try {
            url = new URL(String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s", this.plugin.getCity(), this.plugin.getCountry(), this.plugin.getApikey()));
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                return -1;
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            while ((str = rd.readLine()) != null) {
                result.append(str);
            }
            str = result.toString();
            JsonObject json = new JsonParser().parse(str).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject();
            return json.getAsJsonPrimitive("id").getAsInt();
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                return -2;
            }
        }
        return -1;
    }


    @Override
    public void run() {
        int weatherID = getActualWeatherID();
        Bukkit.getServer().getScheduler().runTask(plugin, new ScheduledWeatherUpdateHandler(plugin, weatherID));
    }

}
