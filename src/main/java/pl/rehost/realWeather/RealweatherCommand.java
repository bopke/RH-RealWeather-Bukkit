package pl.rehost.realWeather;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RealweatherCommand implements CommandExecutor {

    private final RealWeather plugin;

    RealweatherCommand(RealWeather plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("rh.realweather.reload")) {
            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("reload")) {
                    this.plugin.getLogger().info(plugin.getMessages().get("reloading-config"));
                    this.plugin.reloadPlugin();
                    commandSender.sendMessage(plugin.getMessages().get("reloaded-config"));
                    return true;
                }
            }
        }
        return false;
    }
}
