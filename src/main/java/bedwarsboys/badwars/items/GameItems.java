package bedwarsboys.badwars.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * creates ItemStack's of useful Items
 */
public class GameItems {
    /**
     *
     * @param level Knockback level
     */
    public static ItemStack getKnockbackStick(int level) {
        ItemStack stack = new ItemStack(Material.STICK);
        ItemMeta meta = stack.getItemMeta();

        meta.displayName(Component.text("Knockback Stick"));
        meta.addEnchant(Enchantment.KNOCKBACK, level, true);

        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getKnockbackStick() {
        return getKnockbackStick(1);
    }
}
