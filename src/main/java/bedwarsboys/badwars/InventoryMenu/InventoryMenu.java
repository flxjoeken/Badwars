package bedwarsboys.badwars.InventoryMenu;

import bedwarsboys.badwars.Badwars;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;


/**
 * @author felix
 */
public class InventoryMenu implements Listener {

    protected Inventory inventory;

    public InventoryMenu(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Zeigt dem Spieler das Menü
     * @param p Spieler
     */
    public void showToPlayer(Player p) {
        p.openInventory(inventory);
        p.setMetadata("inMenu", new FixedMetadataValue(Badwars.PLUGIN, true));
    }

    /**
     * Versteckt das Menü vor dem Spieler
     * @param p Spieler
     */
    public void hideFromPlayer(Player p) {
        p.setMetadata("inMenu", new FixedMetadataValue(Badwars.PLUGIN, false));
        p.closeInventory();
    }

    public static void configMenu(Player p) {
        //Inventory creative = Bukkit.createInventory(null, InventoryType.CREATIVE);

        Inventory toConfig = Bukkit.createInventory(p, InventoryType.CHEST);
        toConfig.setItem(0, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
        toConfig.setItem(1, new ItemStack(Material.CHEST));

        InventoryMenu toConfigMenu = new InventoryMenu(toConfig);

        toConfigMenu.showToPlayer(p);
    }
}
