package bedwarsboys.badwars.InventoryMenu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Lösst dich (hoffentlich bald) Inventar Menüs erstellen.
 */
public class ConfigInventoryMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            InventoryMenu.configMenu(p);
            return true;
        }
        return false;
    }
}
