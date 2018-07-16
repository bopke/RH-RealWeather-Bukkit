package ovh.rehost.realWeather;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;

public class ThunderChange implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onThunderChange(ThunderChangeEvent event) {
        if (!RealWeather.getAffectedWorlds().contains(event.getWorld().getName())) return;
        // event.toThunderState true = rain, false = otherwise
        if (event.toThunderState()) {
            if (RealWeather.getCurrentWeather() != RealWeather.WeatherStates.THUNDERSTORM) {
                event.setCancelled(true);
            } else {
                event.setCancelled(false);
            }
        } else {
            if (RealWeather.getCurrentWeather() != RealWeather.WeatherStates.THUNDERSTORM) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
