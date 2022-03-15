package bedwarsboys.badwars;

import bedwarsboys.badwars.commands.*;
import bedwarsboys.badwars.game.GameConfig;
import bedwarsboys.badwars.invmenu.InventoryMenuActionEvent;
import bedwarsboys.badwars.invmenu.SummonTestShopCommand;
import bedwarsboys.badwars.lobby.LobbyManager;
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

    public static LobbyManager lobbyManager;

    CommandExecutor setArenaCommand = new SetArenaCommand();
    CommandExecutor saveArenaCommand = new SaveArenaCommand();
    CommandExecutor pasteArenaCommand = new PasteArenaCommand();
    CommandExecutor summonTestShopCommand = new SummonTestShopCommand();
    CommandExecutor configureTeamsCommand = new ConfigureTeamsCommand();
    CommandExecutor setTeamSpawnCommand = new SetTeamSpawnCommand();
    CommandExecutor setLobbyCommand = new SetLobbyCommand();
    CommandExecutor bedwars_DebugCommand = new Bedwars_DebugCommand();

    @Override
    public void onEnable() {
        // Plugin startup logic
        PLUGIN = this;
        CONFIG = this.getConfig();

        //TODO: Maybe! We need to somewhere setup all the team menus for the GameConfig instances
        //TeamConfiguration.setupTeamConfigMenu();
        lobbyManager = new LobbyManager();

        Objects.requireNonNull(this.getCommand("setarena")).setExecutor(setArenaCommand);
        Objects.requireNonNull(this.getCommand("savearena")).setExecutor(saveArenaCommand);
        Objects.requireNonNull(this.getCommand("pastearena")).setExecutor(pasteArenaCommand);
        Objects.requireNonNull(this.getCommand("summontestshop")).setExecutor(summonTestShopCommand);
        Objects.requireNonNull(this.getCommand("configureteams")).setExecutor(configureTeamsCommand);
        Objects.requireNonNull(this.getCommand("setteamspawn")).setExecutor(setTeamSpawnCommand);
        Objects.requireNonNull(this.getCommand("setlobby")).setExecutor(setLobbyCommand);
        Objects.requireNonNull(this.getCommand("badwars_debug")).setExecutor(bedwars_DebugCommand);
        Bukkit.getLogger().info(PLUGIN_NAME + "Loaded Plugin.");
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryMenuActionEvent(), PLUGIN);
        Bukkit.getServer().getPluginManager().registerEvents(new GameConfig.GameConfigEvents(), PLUGIN);
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
