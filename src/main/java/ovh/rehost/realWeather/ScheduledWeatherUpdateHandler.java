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
        if (weatherID < 700) {
            RealWeather.setCurrentWeather(RealWeather.WeatherStates.rain);
            for (String i : RealWeather.getAffectedWorlds()) {
                plugin.getServer().getWorld(i).setStorm(true);
            }
        } else {
            RealWeather.setCurrentWeather(RealWeather.WeatherStates.clear);
            for (String i : RealWeather.getAffectedWorlds()) {
                plugin.getServer().getWorld(i).setStorm(false);
            }
        }
    }
}

