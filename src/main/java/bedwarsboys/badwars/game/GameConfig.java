package bedwarsboys.badwars.game;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.itemspawner.SpawnerConfig;
import bedwarsboys.badwars.team.Team;
import bedwarsboys.badwars.team.TeamConfig;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static bedwarsboys.badwars.BadwarsMessages.*;

/**
 * Represents a configuration of a Badwars Game. You can let Players configure the Game via Chat.
 *
 * @author felix, aaron
 */
@SerializableAs("GameConfig")
public class GameConfig implements ConfigurationSerializable {

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
    protected SpawnerConfig spawnerConfig;

    ArrayList<Location> shopLocations;

    public GameConfig() {
        //TODO: Make it work with the HashMap
        id = getFreeID();
        gameConfigs.put(id, this);

        teamConfig = new TeamConfig();
        spawnerConfig = new SpawnerConfig();
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

    public int getId() {
        return id;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("worldName", worldName);
        for (Team t : teamConfig.teams) {
            String col = t.getTeam().name;
            data.put("teamConfig." + col + ".active", t.isActive());
            data.put("teamConfig." + col + ".maxPlayers", t.getMaxPlayers());
            data.put("teamConfig." + col + ".spawnPoint", t.getSpawnPoint());
            data.put("teamConfig." + col + ".bedLocation", t.getBedlocation());
        }
        int counter = 0;
        for (Location loc : getSpawnerConfig().getCopperSpawners()) {
            data.put("spawnerConfig.copper." + counter, loc);
            counter++;
        }
        counter = 0;
        for (Location loc : getSpawnerConfig().getIronSpawners()) {
            data.put("spawnerConfig.iron." + counter, loc);
            counter++;
        }
        counter = 0;
        for (Location loc : getSpawnerConfig().getGoldSpawners()) {
            data.put("spawnerConfig.gold." + counter, loc);
            counter++;
        }
        counter = 0;
        for (Location loc : getSpawnerConfig().getSpecialSpawners()) {
            data.put("spawnerConfig.special." + counter, loc);
            counter++;
        }
        return data;
    }

    public static GameConfig deserialize(Map<String, Object> args) {
        GameConfig g = new GameConfig();
        g.id = (int) args.get("id");
        g.worldName = (String) args.get("worldName");
        for (Team t : g.getTeamConfig().teams) {
            String key;
            key = "teamConfig." + t.getTeam().name + ".active";
            if (args.containsKey(key)) {
                t.setActive((boolean) args.get(key));
            }
            key = "teamConfig." + t.getTeam().name + ".maxPlayers";
            if (args.containsKey(key)) {
                t.setMaxPlayers((int) args.get(key));
            }
            key = "teamConfig." + t.getTeam().name + ".spawnPoint";
            if (args.containsKey(key)) {
                t.setSpawnPoint((Location) args.get(key));
            }
            key = "teamConfig." + t.getTeam().name + ".bedLocation";
            if (args.containsKey(key)) {
                t.setBedlocation((Location) args.get(key));
            }
        }
        int counter = 0;
        while (args.containsKey("spawnerConfig.copper." + counter)) {
            Location l = (Location) args.get("spawnerConfig.copper." + counter);
            g.getSpawnerConfig().getCopperSpawners().add(l);
            counter++;
        }
        counter = 0;
        while (args.containsKey("spawnerConfig.iron." + counter)) {
            Location l = (Location) args.get("spawnerConfig.iron." + counter);
            g.getSpawnerConfig().getIronSpawners().add(l);
            counter++;
        }
        counter = 0;
        while (args.containsKey("spawnerConfig.gold." + counter)) {
            Location l = (Location) args.get("spawnerConfig.gold." + counter);
            g.getSpawnerConfig().getGoldSpawners().add(l);
            counter++;
        }
        counter = 0;
        while (args.containsKey("spawnerConfig.special." + counter)) {
            Location l = (Location) args.get("spawnerConfig.special." + counter);
            g.getSpawnerConfig().getSpecialSpawners().add(l);
            counter++;
        }
        return g;
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
