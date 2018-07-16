package ovh.rehost.realWeather;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


public class RealWeather extends JavaPlugin {

    public enum WeatherStates {
        THUNDERSTORM, RAIN, CLEAR
    }

    private static WeatherStates currentWeather;

    private static String country;
    private static String city;
    private static String apikey;

    private static List<String> affectedWorlds;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        affectedWorlds = getConfig().getStringList("worlds");
        city = getConfig().getString("city");
        country = getConfig().getString("country");
        apikey = getConfig().getString("API_Key");

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
