package bedwarsboys.badwars;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SaveArenaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return false;
        }
        Location pos1 = Badwars.loadArenaPosition(1);
        Location pos2 = Badwars.loadArenaPosition(2);
        if (pos1 == null || pos2 == null) {
            sender.sendMessage(Badwars.PLUGIN_NAME + "One of the two positions has not been set, the area cannot be saved.");
            return true;
        }
        WorldBlockArea area = new WorldBlockArea(pos1, pos2);
        sender.sendMessage(Badwars.PLUGIN_NAME + "Saving area...");
        if (!area.saveBlockArea(args[0], true)) {
            sender.sendMessage(Badwars.PLUGIN_NAME + "An error occurred while saving.");
        } else {
            sender.sendMessage(Badwars.PLUGIN_NAME + "Successfully saved.");
        }
        return true;
    }
}
