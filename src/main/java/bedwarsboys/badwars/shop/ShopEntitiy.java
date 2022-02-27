package bedwarsboys.badwars.shop;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.invmenu.Action;
import bedwarsboys.badwars.invmenu.InventoryMenu;
import bedwarsboys.badwars.items.GameItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

public class ShopEntitiy{

    Villager villager;

    public ShopEntitiy(Location location, int menuNummer) {

        villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setMetadata(InventoryMenu.MENU_KEY, new FixedMetadataValue(Badwars.PLUGIN, menuNummer));
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 255, false, false, false));

        Inventory inventory = Bukkit.createInventory(this.getVillager(), InventoryType.CHEST);
        inventory.addItem(GameItems.getKnockbackStick());
        Action[] actions = {(Player player) -> player.getInventory().addItem(GameItems.getKnockbackStick())};

        InventoryMenu iv = new InventoryMenu(inventory, actions);
    }

    public Villager getVillager() {
        return villager;
    }
}
