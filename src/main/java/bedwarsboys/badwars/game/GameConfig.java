package bedwarsboys.badwars.game;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.itemspawner.Spawner;
import bedwarsboys.badwars.team.Team;
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
import java.util.Map;

public class GameConfig implements Listener, ConfigurationSerializable {

    protected static ArrayList<GameConfig> gameConfigs = new ArrayList<>();

    static final String CONFIGURES_GAME = "inGameConfig";
    static final String GAME_CONFIG_MODE =  "configState";

    //configure Teams procedure Chat Text.
    static final TextComponent BEGIN_MESSAGE = Component
            .text("Du möchtest ein Bedwars Spiel konfigurieren, du kannst jederzeit mit")
            .append(Component.text(" exit ")
            .color(NamedTextColor.RED)
            .append(Component.text("beenden. ")));
    static final TextComponent SELECT_TEAM_COLOR_MESSAGE = Component
            .text("Du möchtest ein neues Team hinzufügen, wähle dazu eine Farbe aus folgenden aus: ")
            .append(Component.text(NamedTextColor.RED.toString()).color(NamedTextColor.RED))
            .append(Component.text(NamedTextColor.BLUE.toString()).color(NamedTextColor.BLUE))
            .append(Component.text(NamedTextColor.GREEN.toString()).color(NamedTextColor.GREEN))
            .append(Component.text(NamedTextColor.YELLOW.toString()).color(NamedTextColor.YELLOW))
            .append(Component.text(NamedTextColor.LIGHT_PURPLE.toString()).color(NamedTextColor.LIGHT_PURPLE))
            .append(Component.text(NamedTextColor.DARK_PURPLE.toString()).color(NamedTextColor.DARK_PURPLE))
            .append(Component.text(NamedTextColor.BLACK.toString()).color(NamedTextColor.BLACK))
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
            .append(Component.text("fertig").color(NamedTextColor.RED))
            .append(Component.text(". "));

    //TODO configure Spawns chat text

    ArrayList<Team> teams;
    ArrayList<Spawner> spawners;
    ArrayList<Block> beds;
    ArrayList<Location> shopLocations;

    /**
     * Starts game configuration process for given Player
     * @param p Player
     */
    public static void createGameConfig(Player p) {
        gameConfigs.add(new GameConfig());

        p.setMetadata(CONFIGURES_GAME, new FixedMetadataValue(Badwars.PLUGIN, gameConfigs.size()-1));
        p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 0));

        p.sendMessage(BEGIN_MESSAGE);
        p.sendMessage(SELECT_TEAM_COLOR_MESSAGE);
    }

    @EventHandler
    private static void gameConfigChatEvent(AsyncChatEvent e) {
        if (e.getPlayer().hasMetadata(CONFIGURES_GAME)) {
            Player p = e.getPlayer();
            GameConfig gameConfig = GameConfig.gameConfigs.get(e.getPlayer().getMetadata(CONFIGURES_GAME).get(0).asInt()); //gets the gameConfig to change

            // there are several things to configure choose from them by changing the GAME_CONFIG_MODE metadata:
            // 0: add new team by color
            // 1: add Spawnpoint to team
            switch (e.getPlayer().getMetadata(GAME_CONFIG_MODE).get(0).asInt()) {
                case 0 -> {
                    gameConfig.teams.add(new Team(e.message().toString(), null));
                    p.sendMessage(ADD_SPAWN_MESSAGE);
                    p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 1)); // jump to next config Mode
                }
                case 1 -> {
                    Team lastTeam = gameConfig.teams.get(gameConfig.teams.size() - 1);
                    lastTeam.setSpawnPoint(p.getLocation());
                    p.sendMessage(ADDED_SPAWN_MESSAGE.append(Component.text(lastTeam.getTeamColor())).append(ADDED_SPAWN_MESSAGE1));
                    p.sendMessage(ADD_BED_MESSAGE);
                    p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 2));
                }
                case 3 -> {
                    if (e.message().contains(Component.text("fertig"))) {
                        e.getPlayer().removeMetadata(CONFIGURES_GAME, Badwars.PLUGIN);
                        e.getPlayer().removeMetadata(GAME_CONFIG_MODE, Badwars.PLUGIN);
                    } else if (e.message().contains(Component.text("weiter"))) {
                        p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 0));
                        p.sendMessage(SELECT_TEAM_COLOR_MESSAGE);
                    }
                }
            }
        }
    }

    @EventHandler
    private static void gameConfigClickEvent(PlayerInteractEvent e) {
        if (e.getPlayer().hasMetadata(CONFIGURES_GAME)) {
            Player p = e.getPlayer();
            GameConfig gameConfig = GameConfig.gameConfigs.get(e.getPlayer().getMetadata(CONFIGURES_GAME).get(0).asInt()); //gets the gameConfig to change

            if (e.getPlayer().getMetadata(GAME_CONFIG_MODE).get(0).asInt() == 2) {
                if (e.getMaterial().toString().contains("BED")) {
                    gameConfig.beds.add(e.getClickedBlock());
                    p.sendMessage(ADDED_BED_MESSAGE
                            .append(Component.text(gameConfig.teams.get(gameConfig.teams.size()-1).getTeamColor()))
                            .append(ADDED_BED_MESSAGE1));
                    p.sendMessage(ADD_TEAM_MESSAGE);
                }
            }
        }
    }

    public static boolean saveGameConfig() {
        //TODO save to Config File
        return true;
    }

    public static boolean loadGameConfig() {
        //TODO load from Config File
        return true;
    }

    /**
     * Soll in Zukunft eine funktionierende test GameConfig zurückgeben.
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

    @Override
    public @NotNull Map<String, Object> serialize() {
        //TODO serialize
        return null;
    }

    public static GameConfig deserialize(Map<String, Object> map) {
        //TODO deserialize
        return null;
    }
}
