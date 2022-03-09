package bedwarsboys.badwars.game;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.itemspawner.Spawner;
import bedwarsboys.badwars.team.Team;
import bedwarsboys.badwars.team.TeamConfig;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

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

    //configure Teams procedure Chat Text.
    static final TextComponent BEGIN_MESSAGE_1 = Component
            .text("Herzlich Willkommen im Spiel-Konfigurations-Generator. Möchtest du die Konfiguration " +
                    "abbrechen, kannst du das jederzeit mit")
            .append(Component.text(" exit ")
                    .color(NamedTextColor.RED)
                    .append(Component.text("im Chat tun.")));
    static final TextComponent BEGIN_MESSAGE_2 = Component
            .text("Als erstes kannst du auswählen, wie viele Teams und welche Teamfarben du in dieser " +
                    "Konfiguration verwenden möchtest. Dazu wird dir gleich ein Menü eingeblendet");
    static final TextComponent ADD_SPAWN_MESSAGE = Component.text("Stelle dich nun auf den " +
                    "Wiedereinstiegspunkt des Teams und tippe ")
            .append(Component.text("spawn").color(NamedTextColor.GREEN));
    static final TextComponent ADDED_SPAWN_MESSAGE = Component.text("Der Wiedereinstiegspunkt von " +
            "Team %0 wurde erfolgreich erfasst");
    static final TextComponent ADD_BED_MESSAGE = Component.text("Klicke nun auf das Bett des Teams");
    static final TextComponent ADD_TEAM_MESSAGE = Component.text("Möchtest du ein weiteres Team hinzufügen dann tippe ")
            .append(Component.text("weiter ").color(NamedTextColor.RED))
            .append(Component.text("sonst tippe "))
            .append(Component.text("fertig ").color(NamedTextColor.RED))
            .append(Component.text("."));
    static final TextComponent ADD_SPAWNER_MESSAGE = Component.text("Nun kannst du die Spawner des Spiels konfigurieren. Stelle dich dazu auf den Ort an dem die Ressourcen erscheinen sollen und tippe")
            .append(Component.text("gold").color(NamedTextColor.RED))
            .append(Component.text(", "))
            .append(Component.text("eisen").color(NamedTextColor.RED))
            .append(Component.text(" oder "))
            .append(Component.text("bronze").color(NamedTextColor.RED))
            .append(Component.text(" um den Spawner zu erstellen. "));
    static final TextComponent ADDED_SPAWNER_MESSAGE = Component.text("Ein ");
    static final TextComponent ADDED_SPAWNER_MESSAGE1 = Component.text(" wurde erfolgreich erstellt. ")
            .append(Component.text("Möchtest du einen weiteren Spawner erstellen, dann tippe "))
            .append(Component.text("weiter").color(NamedTextColor.RED))
            .append(Component.text(", sonst tippe "))
            .append(Component.text("fertig").color(NamedTextColor.RED))
            .append(Component.text(". "));
    static final TextComponent FINISHED_CONFIG = Component.text("Du hast das Spiel erfolgreich konfiguriert")
            .append(Component.newline())
            .append(Component.text("goodbye!"));

    public static final String ADDED_BED_MESSAGE = "Das Bett von Team %0 wurde erfolgreich erfasst.";
    static final TextComponent TEAM_NOT_EXIST = Component.text("Dieses Team existiert nicht.");
    //TODO Change to German
    static final String NOT_CONFIGURING_TEAM = "You are not configuring a team.";

    //TODO configure Spawners chat text + logic
    String worldName;
    private TeamConfig teamConfig;
    ArrayList<Spawner> spawners;
    // The GameConfig does not have beds, every team has beds.
    //ArrayList<Block> beds;
    ArrayList<Location> shopLocations;

    public GameConfig() {
        //TODO: Make it work with the HashMap
        gameConfigs.put(this);

        shopLocations = new ArrayList<>();
        spawners = new ArrayList<>();
    }

    /**
     * Starts game configuration process for given Player
     *
     * @param p Player
     */
    public static void createGameConfig(Player p) {
        GameConfig gc = new GameConfig();
        int i = 0;
        for (int id : gameConfigs.keySet().stream().sorted().toList()) {
            if (i == id) i++;
        }
        gameConfigs.put(i, gc);
        gc.worldName = p.getWorld().getName();

        p.setMetadata(CONFIGURES_GAME, new FixedMetadataValue(Badwars.PLUGIN, i));
        p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 0));

        p.sendMessage(BEGIN_MESSAGE_1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Badwars.PLUGIN, () -> p.sendMessage(BEGIN_MESSAGE_2), 40);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Badwars.PLUGIN, () -> {
            //get the config the Player is configuring
            GameConfig g = getRunningConfig(p);
            if (g != null) {
                //TODO: Open Inventory again if the Player closes it.
                g.teamConfig.showMenuToPlayer(p);
            }
        }, 80);
    }

    public static GameConfig getConfig(int id) {
        if (gameConfigs.size() > id) {
            return gameConfigs.get(id);
        }
        return null;
    }

    public TeamConfig getTeamConfig() {
        return teamConfig;
    }

    @EventHandler
    private static void gameConfigChatEvent(AsyncChatEvent e) {
        if (e.getPlayer().hasMetadata(CONFIGURES_GAME)) {
            Player p = e.getPlayer();
            if (e.message().toString().equals("exit")) {
                p.removeMetadata(CONFIGURES_GAME, Badwars.PLUGIN);
            }
        }
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
        String name = p.getMetadata(CONFIGURING_TEAM).get(0).asString();
        try {
            return Team.TEAMS.valueOf(name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
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

    /**
     * Soll in Zukunft eine funktionierende test GameConfig zurückgeben.
     *
     * @return GameConfig for test purposes
     */
    public static GameConfig getDemoConfig() {
        //TODO complete Demo Config
        GameConfig gameConfig = new GameConfig();
        Team team1 = new Team("RED", Bukkit.getWorlds().get(0).getSpawnLocation());
        Team team2 = new Team("GREEN", Bukkit.getWorlds().get(0).getSpawnLocation());

        ArrayList<Team> teams = new ArrayList<>();

        teams.add(team1);
        teams.add(team2);
        gameConfig.teams = teams;

        return gameConfig;
    }

    public static class GameConfigEvents implements Listener {
        //reacts to chat inputs from Player while configuration
        @EventHandler
        public void gameConfigChatEvent(AsyncChatEvent e) {
            if (e.getPlayer().hasMetadata(CONFIGURES_GAME)) {
                Player p = e.getPlayer();
                GameConfig gameConfig = GameConfig.gameConfigs.get(e.getPlayer().getMetadata(CONFIGURES_GAME).get(0).asInt()); //gets the gameConfig to change

                // there are several things to configure. Choose from them by changing the GAME_CONFIG_MODE metadata:
                //  0: Start configuration message
                //  1: add new team by color
                //  2: add Spawnpoint to team
                //  3: choose Bed Location
                //  4: ask for additional Team
                //  5:
                switch (e.getPlayer().getMetadata(GAME_CONFIG_MODE).get(0).asInt()) {
                    case 0 -> {
                        if (e.message() instanceof TextComponent && ((TextComponent) e.message()).content().contains("weiter")) {
                            p.sendMessage(SELECT_TEAM_COLOR_MESSAGE);
                            p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 1));
                        }
                    }
                    case 1 -> {
                        if (e.message() instanceof TextComponent) {
                            String messagestr = ((TextComponent) e.message()).content();
                            //TODO test if message is a BedColor
                            gameConfig.teams.add(new Team(messagestr, null));
                            p.sendMessage(ADD_SPAWN_MESSAGE);
                            p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 2)); // jump to next config Mode
                        }
                    }
                    case 2 -> {
                        if (e.message() instanceof TextComponent && ((TextComponent) e.message()).content().contains("spawn")) {
                            Team lastTeam = gameConfig.teams.get(gameConfig.teams.size() - 1);
                            lastTeam.setSpawnPoint(p.getLocation());
                            p.sendMessage(ADDED_SPAWN_MESSAGE.append(Component.text(lastTeam.getTeamColor())).append(ADDED_SPAWN_MESSAGE1));
                            p.sendMessage(ADD_BED_MESSAGE);
                            p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 3));
                        }
                    }
                    case 4 -> {
                        if (e.message() instanceof TextComponent) {
                            if (((TextComponent) e.message()).content().contains("fertig")) {
                                //TODO
                            } else if (((TextComponent) e.message()).content().contains("weiter")) {
                                p.sendMessage(SELECT_TEAM_COLOR_MESSAGE);
                                p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 1));
                            }
                        }
                    }
                    case 5 -> {

                    }
                    case 10 -> {
                        e.getPlayer().removeMetadata(CONFIGURES_GAME, Badwars.PLUGIN);
                        e.getPlayer().removeMetadata(GAME_CONFIG_MODE, Badwars.PLUGIN);
                        p.sendMessage(FINISHED_CONFIG);
                    }
                }
            }
        }

        //reacts to Players clicks while they configure the Game
        @EventHandler
        public void gameConfigClickEvent(PlayerInteractEvent e) {
            if (e.getPlayer().hasMetadata(CONFIGURES_GAME) && e.getClickedBlock() != null) {

                Player player = e.getPlayer();
                GameConfig gameConfig = GameConfig.gameConfigs.get(player.getMetadata(CONFIGURES_GAME).get(0).asInt()); //gets the gameConfig to change

                if (e.getPlayer().getMetadata(GAME_CONFIG_MODE).get(0).asInt() == 3) {
                    if (e.getClickedBlock().getType().toString().contains("BED")) {
                        gameConfig.teams.get(gameConfig.teams.size() - 1).setBedlocation(e.getClickedBlock().getLocation());
                        player.sendMessage(ADDED_BED_MESSAGE
                                .append(Component.text(gameConfig.teams.get(gameConfig.teams.size() - 1).getTeamColor() + " "))
                                .append(ADDED_BED_MESSAGE1));
                        player.sendMessage(ADD_TEAM_MESSAGE);
                        player.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 4));
                    }
                }
            }
        }
    }
}
