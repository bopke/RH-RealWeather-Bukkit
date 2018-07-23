package ovh.rehost.realWeather;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;


public class RealWeather extends JavaPlugin {

    private static String country;
    private static String city;
    private static String apikey;
    private static int interval;

    private BukkitTask task;

    private static List<String> affectedWorlds;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadNewConfigValues();

        this.getLogger().info(String.format("Gathering information about current weather in %s,%s...", city, country));
        if (!ScheduledWeatherStateUpdateHandler.testAndSetup(this)) {
            this.getLogger().severe("Problem occured when trying to gather information about current weather. Shutting down...");
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
