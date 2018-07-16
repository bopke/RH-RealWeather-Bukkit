package ovh.rehost.realWeather;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChange implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!RealWeather.getAffectedWorlds().contains(event.getWorld().getName())) return;
        // event.toWeatherState true = rain, false = otherwise
        if (event.toWeatherState()) {
            if (RealWeather.getCurrentWeather() == RealWeather.WeatherStates.CLEAR) {
                event.setCancelled(true);
            } else {
                event.setCancelled(false);
            }
        } else {
            if (RealWeather.getCurrentWeather() == RealWeather.WeatherStates.CLEAR) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
