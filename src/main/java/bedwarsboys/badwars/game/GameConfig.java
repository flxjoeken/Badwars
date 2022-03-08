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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class GameConfig implements Listener {

    protected static ArrayList<GameConfig> gameConfigs = new ArrayList<>();

    static final String CONFIGURES_GAME = "inGameConfig";
    static final String GAME_CONFIG_MODE = "configState";
    static final String CONFIGURING_TEAM = "configuringTeam";

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
    static final String ADDED_BED_MESSAGE = "Das Bett von Team %0 wurde erfolgreich erfasst.";
    static final TextComponent TEAM_NOT_EXIST = Component.text("Dieses Team existiert nicht.");
    static final String NOT_CONFIGURING_TEAM = "You are not configuring a team.";

    //TODO configure Spawners chat text + logic

    TeamConfig teamConfig;
    ArrayList<Spawner> spawners;
    // The GameConfig does not have beds, every team has beds.
    //ArrayList<Block> beds;
    ArrayList<Location> shopLocations;

    /**
     * Starts game configuration process for given Player
     *
     * @param p Player
     */
    public static void createGameConfig(Player p) {
        gameConfigs.add(new GameConfig());

        p.setMetadata(CONFIGURES_GAME, new FixedMetadataValue(Badwars.PLUGIN, gameConfigs.size()));
        p.setMetadata(GAME_CONFIG_MODE, new FixedMetadataValue(Badwars.PLUGIN, 0));

        p.sendMessage(BEGIN_MESSAGE_1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Badwars.PLUGIN, new Runnable() {
            @Override
            public void run() {
                p.sendMessage(BEGIN_MESSAGE_2);
            }
        }, 40);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Badwars.PLUGIN, new Runnable() {
            @Override
            public void run() {
                //get the config the Player is configuring
                GameConfig g = getRunningConfig(p);
                if (g != null) {
                    //TODO: Open Inventory again if the Player closes it.
                    g.teamConfig.showMenuToPlayer(p);
                }
            }
        }, 80);
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

    @EventHandler
    private static void gameConfigClickEvent(PlayerInteractEvent e) {
        if (e.getPlayer().hasMetadata(CONFIGURES_GAME)) {
            Player p = e.getPlayer();
            GameConfig gameConfig = getRunningConfig(p);
            if (gameConfig == null) return;

            if (getRunningConfigMode(p) == 2) {
                //gameConfig.beds.add(e.getClickedBlock());
                Team.TEAMS t = getRunningConfigTeam(p);
                if (t != null) {
                    p.sendMessage(ADDED_BED_MESSAGE);
                }
            }
        }
    }

    /**
     * To get the GameConfig instance a Player is configuring
     *
     * @param p The Player to check for
     * @return Returns the GameConfig, or null if the Player is not configuring one.
     */
    static GameConfig getRunningConfig(Player p) {
        if (!p.hasMetadata(CONFIGURES_GAME)) {
            return null;
        }
        int id = p.getMetadata(CONFIGURES_GAME).get(0).asInt();
        if (GameConfig.gameConfigs.size() < id) {
            return null;
        }
        return GameConfig.gameConfigs.get(id);
    }

    static int getRunningConfigMode(Player p) {
        if (!p.hasMetadata(CONFIGURES_GAME) || !p.hasMetadata(GAME_CONFIG_MODE)) {
            return -1;
        }
        return p.getMetadata(GAME_CONFIG_MODE).get(0).asInt();
    }

    static Team.TEAMS getRunningConfigTeam(Player p) {
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

    public static boolean loadGameConfig() {
        //TODO load from Config File
        return true;
    }
}
