package ovh.rehost.realWeather;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ScheduledWeatherStateUpdateHandler implements Runnable {

    private static Plugin plugin;


    ScheduledWeatherStateUpdateHandler(Plugin plug) {
        plugin = plug;
    }


    static boolean testAndSetup(Plugin plug) {
        try {
            doEverythingDesiredForThisClass(plug);
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            doEverythingDesiredForThisClass(plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doEverythingDesiredForThisClass(Plugin plug) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s", RealWeather.getCity(), RealWeather.getCountry(), RealWeather.getApikey()));
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String str;
        while ((str = rd.readLine()) != null) {
            result.append(str);
        }
        str = result.toString();
        JsonObject json = new JsonParser().parse(str).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject();
        int weatherID = json.getAsJsonPrimitive("id").getAsInt();
        Bukkit.getServer().getScheduler().runTask(plug, new ScheduledWeatherUpdateHandler(plug, weatherID));
    }
}
