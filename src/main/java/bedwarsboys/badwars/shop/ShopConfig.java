package bedwarsboys.badwars.shop;

import bedwarsboys.badwars.invmenu.Action;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ShopConfig {

    Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("SHOP"));
    Action[] actions;

    public void copyPlayerInventory(Player p) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, p.getInventory().getItem(i));
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }
}
