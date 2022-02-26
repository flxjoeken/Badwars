package bedwarsboys.badwars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PasteArenaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length != 1) {
            return false;
        }
        WorldBlockArea area = new WorldBlockArea();
        if (area.loadBlockArea(args[0])){
            area.pasteArea();
            sender.sendMessage("Arena "+ args[0]+" loaded.");
        } else {
            sender.sendMessage("Error loading arena "+ args[0]+".");
        }
        return true;
    }
}
