package bedwarsboys.badwars.InventoryMenu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Soll in Zukunft Aktionen der Menüs ausführen
 */
public class InventoryMenuActionEvent implements Listener {
    @EventHandler
    void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof Player) {
            if (((Player) e.getInventory().getHolder()).getMetadata("inMenu").get(0).asBoolean()) {
                Bukkit.getLogger().info("führe Menü aktion aus");
            }
        }
    }
}
