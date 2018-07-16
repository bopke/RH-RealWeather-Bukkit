package ovh.rehost.realWeather;

import org.bukkit.plugin.Plugin;

public class ScheduledWeatherUpdateHandler implements Runnable {

    private int weatherID;
    private Plugin plugin;

    ScheduledWeatherUpdateHandler(Plugin plugin, int weatherID) {
        this.weatherID = weatherID;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (weatherID < 300) {
            RealWeather.setCurrentWeather(RealWeather.WeatherStates.THUNDERSTORM);
            for (String i : RealWeather.getAffectedWorlds()) {
                plugin.getServer().getWorld(i).setStorm(true);
                plugin.getServer().getWorld(i).setThundering(true);
            }
        } else if (weatherID < 700) {
            RealWeather.setCurrentWeather(RealWeather.WeatherStates.RAIN);
            for (String i : RealWeather.getAffectedWorlds()) {
                plugin.getServer().getWorld(i).setStorm(true);
                plugin.getServer().getWorld(i).setThundering(false);
            }
        } else {
            RealWeather.setCurrentWeather(RealWeather.WeatherStates.CLEAR);
            for (String i : RealWeather.getAffectedWorlds()) {
                plugin.getServer().getWorld(i).setStorm(false);
                plugin.getServer().getWorld(i).setThundering(false);
            }
        }
    }
}

