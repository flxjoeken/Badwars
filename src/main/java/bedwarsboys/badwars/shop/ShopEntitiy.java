package bedwarsboys.badwars.shop;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.invmenu.Action;
import bedwarsboys.badwars.invmenu.InventoryMenu;
import bedwarsboys.badwars.items.GameItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShopEntitiy{

    Villager villager;

    public ShopEntitiy(Location location, int menuNummer) {

        villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setMetadata(InventoryMenu.MENU_KEY, new FixedMetadataValue(Badwars.PLUGIN, menuNummer));
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 255, false, false, false));

        Inventory inventory = Bukkit.createInventory(this.getVillager(), InventoryType.CHEST, Component.text("SHOP"));
        ItemStack is = new ItemStack(Material.ACACIA_LEAVES);
    }

    public Villager getVillager() {
        return villager;
    }

}
