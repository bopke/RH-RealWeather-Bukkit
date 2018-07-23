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
        for (String i : RealWeather.getAffectedWorlds()) {
            plugin.getServer().getWorld(i).setStorm(weatherID < 700);
            plugin.getServer().getWorld(i).setThundering(weatherID < 300);
            plugin.getServer().getWorld(i).setWeatherDuration(RealWeather.getInterval() * 2); // twice longer than typical interval between checks, just to be sure.
            plugin.getServer().getWorld(i).setThunderDuration(RealWeather.getInterval() * 2);
        }
    }
}


