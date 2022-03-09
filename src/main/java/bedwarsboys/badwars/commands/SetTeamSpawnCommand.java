package bedwarsboys.badwars.commands;

import bedwarsboys.badwars.Badwars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetTeamSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 1) {
                // see if input is a number
                boolean isNum = false;
                int num = -1;
                try {
                    // if input is a number, assign it to num
                    num = Integer.parseInt(args[0]);
                    // if (num <= 7) isNum = true;
                } catch (NumberFormatException ignored) {
                }
                // check if input is a colors name
                //TODO: Change to new system from TeamManager.teams
                int textNum = switch (args[0]) {
                    case "red" -> 0;
                    case "blue" -> 1;
                    case "green" -> 2;
                    case "yellow" -> 3;
                    case "pink" -> 4;
                    case "purple" -> 5;
                    case "black" -> 6;
                    case "white" -> 7;
                    default -> -1;
                };
                if (textNum != -1) {
                    num = textNum;
                }
                // assign the spawnpoint for given team if input was valid
                if (num != -1) {
                    Badwars.CONFIG.set("teams." + num + ".x", p.getLocation().getBlockX());
                    Badwars.CONFIG.set("teams." + num + ".y", p.getLocation().getBlockY());
                    Badwars.CONFIG.set("teams." + num + ".z", p.getLocation().getBlockZ());
                    Badwars.PLUGIN.saveConfig();
                    sender.sendMessage(Badwars.PLUGIN_NAME + "Successfully saved spawnpoint.");
                } else {
                    sender.sendMessage(Badwars.PLUGIN_NAME + "Input was not valid.");
                }
                return true;


            }
            return false;
        }
        sender.sendMessage(Badwars.PLUGIN_NAME + "This command is currently only for players.");
        return true;
    }
}
