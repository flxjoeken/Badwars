package bedwarsboys.badwars;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Badwars extends JavaPlugin {

    String PLUGIN_NAME = "[Badwars] ";
    CommandExecutor setArenaCommand = new SetArenaCommand();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("setarena").setExecutor(setArenaCommand);
        Bukkit.getLogger().info(PLUGIN_NAME+"Loaded Plugin.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
