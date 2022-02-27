package bedwarsboys.badwars.invmenu;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.items.GameItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;


/**
 * @author felix
 */
public class InventoryMenu{

    //key for Metadata Value
    public static final String MENU_KEY = "Menu";

    protected static ArrayList<InventoryMenu> inventoryMenus = new ArrayList<>();
    protected static int menuNumbers = 0;
    protected static ArrayList<Action[]> actionLists = new ArrayList<>();

    protected int menuNummer;
    protected Inventory inventory;

    public InventoryMenu(Inventory inventory, Action[] actions) {
        inventoryMenus.add(this);
        menuNummer = menuNumbers++;
        actionLists.add(actions);
        this.inventory = inventory;
    }

    /**
     * Zeigt dem Spieler das Menü
     * @param p Spieler
     */
    public void showToPlayer(Player p) {
        p.setMetadata(InventoryMenu.MENU_KEY, new FixedMetadataValue(Badwars.PLUGIN, menuNummer));
        p.openInventory(inventory);
    }

    public static void configMenu(Player p) {
//        for (int i = 0; i < p.getInventory().getSize(); i++) {
//            Bukkit.getLogger().info(i + ", " + p.getInventory().getItem(i));
//        }

        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST);
        inventory.addItem(GameItems.getKnockbackStick());
        Action[] actions = {(Player player) -> player.getInventory().addItem(GameItems.getKnockbackStick())};

        InventoryMenu iv = new InventoryMenu(inventory, actions);
        iv.showToPlayer(p);
    }

    public static Action getAction(int menuNummer, int slot) {
        if (actionLists.get(menuNummer).length > slot && actionLists.get(menuNummer)[slot] != null)
            return actionLists.get(menuNummer)[slot];
        return (Player p) -> {};
    }

    public int getMenuNummer() {
        return menuNummer;
    }

    public static InventoryMenu getInventoryMenu(int number) {
        return inventoryMenus.get(number);
    }

    public static int getMenuNumbers() {
        return menuNumbers;
    }
}
