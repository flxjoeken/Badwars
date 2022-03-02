package bedwarsboys.badwars;

import bedwarsboys.badwars.commands.PasteArenaCommand;
import bedwarsboys.badwars.commands.SaveArenaCommand;
import bedwarsboys.badwars.commands.SetArenaCommand;
import bedwarsboys.badwars.invmenu.SummonTestShopCommand;
import bedwarsboys.badwars.invmenu.InventoryMenuActionEvent;
import bedwarsboys.badwars.team.TeamConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Badwars extends JavaPlugin {

    public static Badwars PLUGIN;
    public static final String PLUGIN_NAME = "[Badwars] ";
    public static FileConfiguration CONFIG;

    CommandExecutor setArenaCommand = new SetArenaCommand();
    CommandExecutor saveArenaCommand = new SaveArenaCommand();
    CommandExecutor pasteArenaCommand = new PasteArenaCommand();
    CommandExecutor summonTestShopCommand = new SummonTestShopCommand();
    CommandExecutor configureTeamsCommand = new TeamConfiguration.ConfigureTeamsCommand();

    @Override
    public void onEnable() {
        // Plugin startup logic
        PLUGIN = this;
        CONFIG = this.getConfig();

        TeamConfiguration.setupTeamConfigMenu();

        Objects.requireNonNull(this.getCommand("setarena")).setExecutor(setArenaCommand);
        Objects.requireNonNull(this.getCommand("savearena")).setExecutor(saveArenaCommand);
        Objects.requireNonNull(this.getCommand("pastearena")).setExecutor(pasteArenaCommand);
        Objects.requireNonNull(this.getCommand("summontestshop")).setExecutor(summonTestShopCommand);
        Objects.requireNonNull(this.getCommand("configureteams")).setExecutor(configureTeamsCommand);
        Bukkit.getLogger().info(PLUGIN_NAME + "Loaded Plugin.");
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryMenuActionEvent(), PLUGIN);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Location loadArenaPosition(int pos) {
        if (pos < 1 || pos > 2) {
            return null;
        }
        if (!Badwars.CONFIG.isSet("arena.pos" + pos)) {
            Bukkit.getLogger().info(Badwars.PLUGIN_NAME+"The positions for the arena are not set yet, so the area cannot be saved to a file.");
            return null;
        }
        String[] values = Objects.requireNonNull(Badwars.CONFIG.getString("arena.pos" + pos)).split(",");
        String name = values[0];
        double x = Double.parseDouble(values[1]);
        double y = Double.parseDouble(values[2]);
        double z = Double.parseDouble(values[3]);
        assert name != null;
        World w = Bukkit.getWorld(name);
        return new Location(w, x, y, z);
    }

}
