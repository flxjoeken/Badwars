package bedwarsboys.badwars.game;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.itemspawner.Spawner;
import bedwarsboys.badwars.team.Team;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import javax.naming.Name;
import java.util.ArrayList;

/**
 * Represents a configuration of a Badwars Game. You can let Players configure the Game via Chat.
 *
 * @author felix, aaron
 */
public class GameConfig {

    //FixedMetadata Keys
    static final String CONFIGURES_GAME = "inGameConfig";
    static final String GAME_CONFIG_MODE = "configState";

    static final TextComponent BEGIN_MESSAGE = Component
            .text("Du möchtest ein Bedwars Spiel konfigurieren. Das du kannst jederzeit mit")
            .append(Component.text(" exit ")
                    .color(NamedTextColor.RED))
            .append(Component.text("beenden. Tippe "))
            .append(Component.text("weiter ").color(NamedTextColor.RED))
            .append(Component.text("um fortzufahren. "));
    static final TextComponent SELECT_TEAM_COLOR_MESSAGE = Component
            .text("Du möchtest ein neues Team hinzufügen, wähle dazu eine Farbe aus folgenden aus: ")
            .append(Component.text(NamedTextColor.RED.toString()).color(NamedTextColor.RED))
            .append(Component.text(", "))
            .append(Component.text(NamedTextColor.BLUE.toString()).color(NamedTextColor.BLUE))
            .append(Component.text(", "))
            .append(Component.text(NamedTextColor.GREEN.toString()).color(NamedTextColor.GREEN))
            .append(Component.text(", "))
            .append(Component.text(NamedTextColor.YELLOW.toString()).color(NamedTextColor.YELLOW))
            .append(Component.text(", "))
            .append(Component.text(NamedTextColor.LIGHT_PURPLE.toString()).color(NamedTextColor.LIGHT_PURPLE))
            .append(Component.text(", "))
            .append(Component.text(NamedTextColor.DARK_PURPLE.toString()).color(NamedTextColor.DARK_PURPLE))
            .append(Component.text(", "))
            .append(Component.text(NamedTextColor.BLACK.toString()).color(NamedTextColor.BLACK))
            .append(Component.text(", "))
            .append(Component.text(NamedTextColor.WHITE.toString()).color(NamedTextColor.WHITE));
    static final TextComponent ADD_SPAWN_MESSAGE = Component.text("Stelle dich nun auf den Wiedereinstiegspunkt des Teams und tippe ")
            .append(Component.text("spawn").color(NamedTextColor.RED));
    static final TextComponent ADDED_SPAWN_MESSAGE = Component.text("Der Wiedereinstiegspunkt von Team ");
    static final TextComponent ADDED_SPAWN_MESSAGE1 = Component.text(" wurde erfolgreich erfasst");
    static final TextComponent ADD_BED_MESSAGE = Component.text("Klicke nun auf das Bett des Teams");
    static final TextComponent ADDED_BED_MESSAGE = Component.text("Das Bett von Team ");
    static final TextComponent ADDED_BED_MESSAGE1 = Component.text("wurde erfolgreich erfasst. ");
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

    protected static ArrayList<GameConfig> gameConfigs = new ArrayList<>();

    //TODO configure Spawners chat text + logic
    ArrayList<Team> teams;
    ArrayList<Spawner> spawners;
    ArrayList<Location> shopLocations;

    public GameConfig() {
        gameConfigs.add(this);

        teams = new ArrayList<>();
        shopLocations = new ArrayList<>();
        spawners = new ArrayList<>();
    }

    /**
     * Creates a new GameConfig and lets a Player configure it with help of the Chat.
     *
     * @param p Player
     */
    public static void letPlayerCreateNewConfig(Player p) {
        gameConfigs.add(new GameConfig());

        p.setMetadata(CONFIGURES_GAME, new FixedMetadataValue(Badwars.PLUGIN, gameConfigs.size() - 1));
        p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 0));

        p.sendMessage(BEGIN_MESSAGE);
    }

    /**
     * saves the GameConfig into the plugins Config file
     *
     * @return if save was successful
     */
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
