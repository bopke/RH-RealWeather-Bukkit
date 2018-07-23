package ovh.rehost.realWeather;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RealWeather extends JavaPlugin {

    private static String country;
    private static String city;
    private static String apikey;
    private static int interval;

    private BukkitTask task;

    private static List<String> affectedWorlds;
    private static Map<String, String> messages = new HashMap<String, String>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadNewConfigValues();

        this.getLogger().info(messages.get("localisation-information").replaceAll("%city%", city).replaceAll("%country%", country));
        if (!ScheduledWeatherStateUpdateHandler.testAndSetup(this)) {
            this.getLogger().severe(messages.get("setup-error"));
            this.getPluginLoader().disablePlugin(this);
        }

        this.getCommand("rh-realweather").setExecutor(new RealweatherCommand(this));

        task = getServer().getScheduler().runTaskTimerAsynchronously(this, new ScheduledWeatherStateUpdateHandler(), 100L, (long) interval);
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

    static int getInterval() {
        return interval;
    }

    static Map<String, String> getMessages() {
        return messages;
    }

    static List<String> getAffectedWorlds() {
        return affectedWorlds;
    }

    private void loadNewConfigValues() {
        FileConfiguration config = getConfig();
        affectedWorlds = config.getStringList("worlds");
        city = config.getString("city");
        country = config.getString("country");
        apikey = config.getString("API_Key");
        interval = config.getInt("interval");
        ConfigurationSection mess = config.getConfigurationSection("messages");
        messages.put("localisation-information", mess.getString("localisation-information"));
        messages.put("setup-error", mess.getString("setup-error"));
        messages.put("reloading-config", mess.getString("reloading-config"));
        messages.put("reloaded-config", mess.getString("reloaded-config"));
    }

    private void reloadPluginConfig() {
        reloadConfig();
        loadNewConfigValues();
    }

    void reloadPlugin() {
        reloadPluginConfig();
        getServer().getScheduler().cancelTask(task.getTaskId());
        task = getServer().getScheduler().runTaskTimerAsynchronously(this, new ScheduledWeatherStateUpdateHandler(), 0L, (long) interval);
    }

    @Override
    public void onDisable() {
    }
}
