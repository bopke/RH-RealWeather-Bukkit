package ovh.rehost.realWeather;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;


public class RealWeather extends JavaPlugin {

    public enum WeatherStates {
        thunderstorm, rain, clear
    }

    private static WeatherStates currentWeather;

    private static String country;
    private static String city;
    private static String apikey;

    private File configFile = new File(getDataFolder(), "config.yml");
    private static List<String> affectedWorlds;

    private void copyConfig(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckConfigs() {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            copyConfig(getResource("config.yml"), configFile);
        }
    }

    @Override
    public void onEnable() {
        configFile = new File(getDataFolder(), "config.yml");
        CheckConfigs();
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        affectedWorlds = config.getStringList("worlds");
        city = config.getString("city");
        country = config.getString("country");
        apikey = config.getString("API_Key");

        this.getLogger().info(String.format("Gathering information about current weather in %s,%s...", city, country));
        if (!ScheduledWeatherStateUpdateHandler.testAndSetup(this)) {
            this.getLogger().severe("Problem occured when trying to gather information about current weather. Shutting down...");
            this.getPluginLoader().disablePlugin(this);
        }

        getServer().getScheduler().runTaskTimerAsynchronously(this, new ScheduledWeatherStateUpdateHandler(this), 100L, 6000L);
    }

    static String getCity() {
        return city;
    }

    static String getCountry() {
        return country;
    }

    static String getApikey() {
        return apikey;
    }


    static List<String> getAffectedWorlds() {
        return affectedWorlds;
    }

    static WeatherStates getCurrentWeather() {
        return currentWeather;
    }

    static void setCurrentWeather(WeatherStates state) {
        currentWeather = state;
    }

    @Override
    public void onDisable() {
        // TODO: IT DONT WORK BUT I'LL LEAVE IT HERE
        getServer().getScheduler().cancelTasks(this);
    }
}
