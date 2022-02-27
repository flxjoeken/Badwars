package bedwarsboys.badwars.InventoryMenu;

import bedwarsboys.badwars.Badwars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;


/**
 * @author felix
 */
public class InventoryMenu implements Listener {

    protected static int menuNumbers = 0;
    protected static HashMap<Integer[], Action> actions = new HashMap<>();

    protected int menuNummer;
    protected Inventory inventory;

    public InventoryMenu(Inventory inventory, Action[] actions) {
        menuNummer = menuNumbers++;
        for (int i = 0; i < actions.length; i++) {
            Integer[] identifier = {menuNummer, i};
            InventoryMenu.actions.put(identifier, actions[i]);
        }
        this.inventory = inventory;
    }

    /**
     * Zeigt dem Spieler das Menü
     * @param p Spieler
     */
    public void showToPlayer(Player p) {
        p.openInventory(inventory);
        p.setMetadata("inMenu", new FixedMetadataValue(Badwars.PLUGIN, menuNummer));
    }

    /**
     * Versteckt das Menü vor dem Spieler
     * @param p Spieler
     */
    public void hideFromPlayer(Player p) {
        p.setMetadata("inMenu", new FixedMetadataValue(Badwars.PLUGIN, -1));
        p.closeInventory();
    }

    public static void configMenu(Player p) {

    }

    public static Action getAction(int menuNummer, int slot) {
        Integer[] identifier = {menuNummer, slot};
        return actions.get(identifier);
    }
}
