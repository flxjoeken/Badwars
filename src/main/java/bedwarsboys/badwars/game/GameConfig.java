package bedwarsboys.badwars.game;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.itemspawner.SpawnerConfig;
import bedwarsboys.badwars.team.Team;
import bedwarsboys.badwars.team.TeamConfig;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

import static bedwarsboys.badwars.BadwarsMessages.*;

/**
 * Represents a configuration of a Badwars Game. You can let Players configure the Game via Chat.
 *
 * @author felix, aaron
 */
public class GameConfig {

    //FixedMetadata Keys
    protected static HashMap<Integer, GameConfig> gameConfigs = new HashMap<>();

    public static final String CONFIGURES_GAME = "inGameConfig";
    /**
     * 1 = setting up teams
     * 2 = setting up team spawns
     */
    public static final String GAME_CONFIG_MODE = "configState";
    public static final String CONFIGURING_TEAM = "configuringTeam";


    //TODO configure Spawners chat text + logic

    private int id;
    private String worldName;
    protected TeamConfig teamConfig;
    private SpawnerConfig spawnerConfig;

    ArrayList<Location> shopLocations;

    public GameConfig() {
        //TODO: Make it work with the HashMap
        id = getFreeID();
        gameConfigs.put(id, this);

        teamConfig = new TeamConfig();

        shopLocations = new ArrayList<>();
    }

    /**
     * This method returns the lowest free id from gameConfigs
     *
     * @return returns the lowest free id
     */
    static int getFreeID() {
        int i = 0;
        for (int id : gameConfigs.keySet().stream().sorted().toList()) {
            if (i == id) i++;
        }
        return i;
    }

    /**
     * Starts game configuration process for given Player
     *
     * @param p Player
     */
    public static void createGameConfig(Player p) {
        GameConfig gc = new GameConfig();
        gc.worldName = p.getWorld().getName();

        p.setMetadata(CONFIGURES_GAME, new FixedMetadataValue(Badwars.PLUGIN, gc.id));
        p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 0));

        p.sendMessage(BEGIN_MESSAGE_1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Badwars.PLUGIN, () -> p.sendMessage(BEGIN_MESSAGE_2), 80);
        /*
        Bukkit.getScheduler().scheduleSyncDelayedTask(Badwars.PLUGIN, () -> {
            //get the config the Player is configuring
            GameConfig g = getRunningConfig(p);
            if (g != null) {
                //TODO: Open Inventory again if the Player closes it.
                g.teamConfig.showTeamConfigMenu(p);
            }
        }, 80);
        */
    }

    public static GameConfig getConfig(int id) {
        if (gameConfigs.containsKey(id)) {
            return gameConfigs.get(id);
        }
        return null;
    }

    @Nullable
    public Team getTeamOfPlayer(Player p) {
        for (Team t : getTeamConfig().teams) {
            if (t.getPlayers().contains(p)) {
                return t;
            }
        }
        return null;
    }

    @Nonnull
    public TeamConfig getTeamConfig() {
        return teamConfig;
    }

    /**
     * To get the GameConfig instance a Player is configuring
     *
     * @param p The Player to check for
     * @return Returns the GameConfig, or null if the Player is not configuring one.
     */
    public static GameConfig getRunningConfig(Player p) {
        if (!p.hasMetadata(CONFIGURES_GAME)) {
            return null;
        }
        int id = p.getMetadata(CONFIGURES_GAME).get(0).asInt();
        if (GameConfig.gameConfigs.size() < id) {
            return null;
        }
        return GameConfig.gameConfigs.get(id);
    }

    public static int getRunningConfigMode(Player p) {
        if (!p.hasMetadata(CONFIGURES_GAME) || !p.hasMetadata(GAME_CONFIG_MODE)) {
            return -1;
        }
        return p.getMetadata(GAME_CONFIG_MODE).get(0).asInt();
    }

    public static Team.TEAMS getRunningConfigTeam(Player p) {
        if (!p.hasMetadata(CONFIGURES_GAME) || !p.hasMetadata(CONFIGURING_TEAM)) {
            return null;
        }
        int id = p.getMetadata(CONFIGURING_TEAM).get(0).asInt();
        try {
            return Team.TEAMS.byID(id);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static void setRunningConfigTeam(Player p, Team.TEAMS team) {
        p.setMetadata(CONFIGURING_TEAM, new FixedMetadataValue(Badwars.PLUGIN, team.id));
    }

    public String getWorldName() {
        return worldName;
    }

    public SpawnerConfig getSpawnerConfig() {
        return spawnerConfig;
    }

    public static boolean saveGameConfig() {
        //TODO save to Config File
        return true;
    }

    /**
     * loads GameConfig from the plugin's configuration file
     *
     * @return the configs number, -1 if an error occurred
     */
    public static int loadGameConfig() {
        //TODO load from Config File
        return 0;
    }

    public static class GameConfigEvents implements Listener {
        //reacts to chat inputs from Player while configuration
        @SuppressWarnings("ConstantConditions")
        @EventHandler
        public void gameConfigChatEvent(AsyncChatEvent e) {
            if (e.getPlayer().hasMetadata(CONFIGURES_GAME)) {
                Player p = e.getPlayer();
                GameConfig gameConfig = getRunningConfig(p); //gets the gameConfig to change

                // there are several things to configure. Choose from them by changing the GAME_CONFIG_MODE metadata:
                //  0: Start configuration message
                //  1: add new team by color
                //  2: add Spawnpoint to team
                //  3: choose Bed Location
                //  4: ask for additional Team
                //  5:
                switch (getRunningConfigMode(e.getPlayer())) {
                    case 0 -> {
                        if (componentToString(e.message()).equals("weiter")) {
                            e.setCancelled(true);
                            p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 1));
                            p.sendMessage(SELECT_TEAM_COLOR_MESSAGE);
                            for (Team.TEAMS t : Team.TEAMS.values()) {
                                p.sendMessage(Component.text(t.name).color(t.textColor));
                            }
                        }
                    }
                    case 1 -> {
                        String msg = componentToString(e.message());
                        if (msg != null) {
                            Team.TEAMS team = Team.TEAMS.byName(msg);
                            if (team != null) {
                                p.sendMessage(ADD_SPAWN_MESSAGE);
                                // jump to next config Mode
                                p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 2));
                                setRunningConfigTeam(p, team);
                            } else {
                                p.sendMessage(TEAM_NOT_EXIST_MESSAGE);
                            }
                            e.setCancelled(true);
                        }
                    }
                    case 2 -> {
                        if (componentToString(e.message()).equals("spawn")) {
                            e.setCancelled(true);
                            Team.TEAMS t = getRunningConfigTeam(e.getPlayer());
                            if (t != null) {
                                Team team = gameConfig.getTeamConfig().teams.get(t.id);
                                team.setSpawnPoint(p.getLocation());
                                team.setActive(true);
                                Component c = ADDED_SPAWN_MESSAGE.replaceText(TextReplacementConfig.builder().match("%0").replacement(t.name).build());
                                p.sendMessage(c);
                                p.sendMessage(ADD_BED_MESSAGE);
                                p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 3));
                            }
                        }
                    }
                    case 4 -> {
                            if (componentToString(e.message()).equals("fertig")) {
                                e.setCancelled(true);
                                //TODO
                            } else if (((TextComponent) e.message()).content().contains("weiter")) {
                                e.setCancelled(true);
                                p.sendMessage(SELECT_TEAM_COLOR_MESSAGE);
                                for (Team.TEAMS t : Team.TEAMS.values()) {
                                    p.sendMessage(Component.text(t.name).color(t.textColor));
                                }
                                p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 1));
                            }
                    }
                    case 5 -> {

                    }
                    case 10 -> {
                        e.getPlayer().removeMetadata(CONFIGURES_GAME, Badwars.PLUGIN);
                        e.getPlayer().removeMetadata(GAME_CONFIG_MODE, Badwars.PLUGIN);
                        p.sendMessage(FINISHED_CONFIG_MESSAGE);
                    }
                }
                if (componentToString(e.message()).equals("exit")) {
                    e.setCancelled(true);
                    p.removeMetadata(CONFIGURES_GAME, Badwars.PLUGIN);
                    p.removeMetadata(GAME_CONFIG_MODE, Badwars.PLUGIN);
                    p.removeMetadata(CONFIGURING_TEAM, Badwars.PLUGIN);
                }
            }
        }

        String componentToString(Component c) {
            if (c instanceof TextComponent) {
                return ((TextComponent) c).content();
            }
            return "";
        }

        //reacts to Players clicks while they configure the Game
        @EventHandler
        public void gameConfigClickEvent(PlayerInteractEvent e) {
            Player p = e.getPlayer();
            GameConfig gameConfig = getRunningConfig(p); //gets the gameConfig to change
            if (gameConfig != null && e.getClickedBlock() != null) {
                Team.TEAMS t = getRunningConfigTeam(p);
                if (getRunningConfigMode(p) == 3 && t != null) {
                    p.sendMessage(ADD_TEAM_MESSAGE);
                    gameConfig.getTeamConfig().teams.get(t.id).setBedlocation(e.getClickedBlock().getLocation());
                    p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 4));
                }
            }
        }
    }
}
