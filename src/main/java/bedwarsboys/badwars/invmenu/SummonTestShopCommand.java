package bedwarsboys.badwars.invmenu;

import bedwarsboys.badwars.shop.ShopEntitiy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Lösst dich (hoffentlich bald) Inventar Menüs erstellen.
 */
public class SummonTestShopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {

            new ShopEntitiy(p.getLocation(), InventoryMenu.getMenuNumbers());

            return true;
        }
        return false;
    }
}
