package bedwarsboys.badwars.invmenu;

import bedwarsboys.badwars.Badwars;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Objects;

/**
 * Soll in Zukunft Aktionen der Menüs ausführen
 */
public class InventoryMenuActionEvent implements Listener {
    @EventHandler
    void onClick(InventoryClickEvent e) {

        //only treat clicks outside players inventories
        if (Objects.requireNonNull(e.getClickedInventory()).getType().equals(InventoryType.PLAYER))
            return;

        if (e.getWhoClicked() instanceof Player player) {

            if (!player.getMetadata(InventoryMenu.MENU_KEY).isEmpty() &&
                    player.getMetadata(InventoryMenu.MENU_KEY).get(0).asInt() != -1) {

                int menuNum = player.getMetadata(InventoryMenu.MENU_KEY).get(0).asInt();
                int slotNum = e.getSlot();

//                Bukkit.broadcastMessage("Player " + player.getDisplayName() + " is in Menu Number: " + ChatColor.RED + menuNum + ChatColor.RESET +
//                    ". Clicked on slot " + ChatColor.RED + slotNum + ChatColor.RESET + " in a " + e.getClickedInventory().getType().toString());

                Action action = InventoryMenu.getAction(menuNum, slotNum);

                action.doIt(player);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    void closeInventory(InventoryCloseEvent e) {
        if (e.getPlayer().hasMetadata(InventoryMenu.MENU_KEY))
            e.getPlayer().removeMetadata(InventoryMenu.MENU_KEY, Badwars.PLUGIN);
    }

    @EventHandler
    void openShop(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Villager))
            return;

        if (e.getRightClicked().hasMetadata(InventoryMenu.MENU_KEY)) {
            e.setCancelled(true);

            Villager villager = (Villager) e.getRightClicked();
            InventoryMenu im = InventoryMenu.getInventoryMenu(villager.getMetadata(InventoryMenu.MENU_KEY).get(0).asInt());

            im.showToPlayer(e.getPlayer());
        }
    }
}
