package ovh.rehost.realWeather;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RealWeather extends JavaPlugin {

    private String country;
    private String city;
    private String apikey;
    private int interval;

    private BukkitTask task;

    private List<String> affectedWorlds;
    private Map<String, String> messages = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadNewConfigValues();

        this.getLogger().info(messages.get("localisation-information").replaceAll("%city%", city).replaceAll("%country%", country));

        this.getCommand("rh-realweather").setExecutor(new RealweatherCommand(this));

        task = getServer().getScheduler().runTaskTimerAsynchronously(this, new ScheduledWeatherStateUpdateHandler(this), 0L, (long) interval);
    }

    String getCity() {
        return this.city;
    }

    String getCountry() {
        return this.country;
    }

    String getApikey() {
        return this.apikey;
    }

    int getInterval() {
        return this.interval;
    }

    Map<String, String> getMessages() {
        return this.messages;
    }

    List<String> getAffectedWorlds() {
        return this.affectedWorlds;
    }

    private void loadNewConfigValues() {
        FileConfiguration config = getConfig();
        affectedWorlds = config.getStringList("worlds");
        city = config.getString("city");
        country = config.getString("country");
        apikey = config.getString("API_Key");
        interval = config.getInt("interval");
        ConfigurationSection mess = config.getConfigurationSection("messages");
        messages.put("localisation-information", ChatColor.translateAlternateColorCodes('&', mess.getString("localisation-information")));
        messages.put("invalid-response-code", ChatColor.translateAlternateColorCodes('&', mess.getString("invalid-response-code")));
        messages.put("reloading-config", ChatColor.translateAlternateColorCodes('&', mess.getString("reloading-config")));
        messages.put("reloaded-config", ChatColor.translateAlternateColorCodes('&', mess.getString("reloaded-config")));
        messages.put("API-offline", ChatColor.translateAlternateColorCodes('&', mess.getString("API-offline")));
    }

    private void reloadPluginConfig() {
        reloadConfig();
        loadNewConfigValues();
    }

    void reloadPlugin() {
        reloadPluginConfig();
        getServer().getScheduler().cancelTask(task.getTaskId());
        task = getServer().getScheduler().runTaskTimerAsynchronously(this, new ScheduledWeatherStateUpdateHandler(this), 0L, (long) interval);
    }

    @Override
    public void onDisable() {
    }
}
