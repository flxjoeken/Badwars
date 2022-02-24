package bedwarsboys.badwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Badwars extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Hallo ich habs geschaft");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
