package bedwarsboys.badwars.commands;

import bedwarsboys.badwars.Badwars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetLobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player p) {
                Badwars.CONFIG.set("lobby.x", p.getLocation().getBlockX());
                Badwars.CONFIG.set("lobby.y", p.getLocation().getBlockY());
                Badwars.CONFIG.set("lobby.z", p.getLocation().getBlockZ());
                Badwars.CONFIG.set("lobby.w", p.getWorld().getName());
                Badwars.PLUGIN.saveConfig();
                sender.sendMessage(Badwars.PLUGIN_NAME + "Successfully saved the lobby spawn.");
            } else {
                sender.sendMessage(Badwars.PLUGIN_NAME + "To set the lobby from console, you need to specify the coordinates.");
            }
            return true;
        }
        if (args.length == 4) {
            try {
                String world = args[0];
                if (Bukkit.getWorld(world) == null){
                    sender.sendMessage(Badwars.PLUGIN_NAME + "The world does not exist. Please try again.");
                    return true;
                }
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);
                Badwars.CONFIG.set("lobby.w", world);
                Badwars.CONFIG.set("lobby.x", x);
                Badwars.CONFIG.set("lobby.y", y);
                Badwars.CONFIG.set("lobby.z", z);
                Badwars.PLUGIN.saveConfig();
            } catch (NumberFormatException e) {
                sender.sendMessage(Badwars.PLUGIN_NAME + "The coordinates don't seem to be numbers. Please try again.");
            }
        }
        return true;
    }
}
