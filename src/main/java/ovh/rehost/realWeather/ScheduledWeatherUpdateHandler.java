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
            for (String i : RealWeather.getAffectedWorlds()) {
                plugin.getServer().getWorld(i).setStorm(true);
                plugin.getServer().getWorld(i).setThundering(true);
                plugin.getServer().getWorld(i).setWeatherDuration(12000); // twice longer than typical interval between checks
                plugin.getServer().getWorld(i).setThunderDuration(12000);
            }
        } else if (weatherID < 700) {
            for (String i : RealWeather.getAffectedWorlds()) {
                plugin.getServer().getWorld(i).setStorm(true);
                plugin.getServer().getWorld(i).setThundering(false);
                plugin.getServer().getWorld(i).setWeatherDuration(12000);
                plugin.getServer().getWorld(i).setThunderDuration(12000);
            }
        } else {
            for (String i : RealWeather.getAffectedWorlds()) {
                plugin.getServer().getWorld(i).setStorm(false);
                plugin.getServer().getWorld(i).setThundering(false);
                plugin.getServer().getWorld(i).setWeatherDuration(12000);
                plugin.getServer().getWorld(i).setThunderDuration(12000);
            }
        }
    }
}

