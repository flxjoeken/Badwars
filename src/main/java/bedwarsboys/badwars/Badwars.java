package bedwarsboys.badwars;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Badwars extends JavaPlugin {

    public static Badwars PLUGIN;
    public static final String PLUGIN_NAME = "[Badwars] ";
    public static FileConfiguration CONFIG;
    CommandExecutor setArenaCommand = new SetArenaCommand();

    @Override
    public void onEnable() {
        // Plugin startup logic
        PLUGIN = this;
        CONFIG = this.getConfig();
        Objects.requireNonNull(this.getCommand("setarena")).setExecutor(setArenaCommand);
        Bukkit.getLogger().info(PLUGIN_NAME+"Loaded Plugin.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
