package bedwarsboys.badwars.commands;

import bedwarsboys.badwars.Badwars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class SetArenaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p) {
            if (args.length == 0) {
                return false;
            }
            int pos = Integer.parseInt(args[0]);
            String w = p.getLocation().getWorld().getName();
            double x, y, z;
            if (args.length == 1 || args.length == 4) {
                if (args.length == 1) {
                    if (!(args[0].equals("1") | args[0].equals("2"))){
                        return false;
                    }
                    //Save Player position as arena bounds 1 or 2
                    w = p.getLocation().getWorld().getName();
                    x = p.getLocation().getBlockX();
                    y = p.getLocation().getBlockY();
                    z = p.getLocation().getBlockZ();
                } else {
                    //Save given position as arena bounds 1 or 2
                    x = Double.parseDouble(args[1]);
                    y = Double.parseDouble(args[2]);
                    z = Double.parseDouble(args[3]);
                    Badwars.CONFIG.set("arena.pos" + args[0], w + "," + x + "," + y + "," + z);
                    Badwars.PLUGIN.saveConfig();
                    sender.sendMessage(Badwars.PLUGIN_NAME + "Position " + args[0] + " saved successfully.");
                }
                Badwars.CONFIG.set("arena.pos" + pos, w + "," + x + "," + y + "," + z);
                Badwars.PLUGIN.saveConfig();
                sender.sendMessage(Badwars.PLUGIN_NAME + "Position " + pos + " saved successfully.");
                return true;
            }
            if (args.length == 6) {
                //Save given positions as arena bounds 1 and 2
                //TODO
                sender.sendMessage(Badwars.PLUGIN_NAME + "Not implemented yet.");
                return true;
            } else {
                return false;
            }
        }
        sender.sendMessage(Badwars.PLUGIN_NAME + "This command is currently for Players only.");
        return true;
    }
}
