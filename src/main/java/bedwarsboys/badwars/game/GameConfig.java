package bedwarsboys.badwars.game;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.itemspawner.Spawner;
import bedwarsboys.badwars.team.Team;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class GameConfig implements Listener {

    protected static ArrayList<GameConfig> gameConfigs = new ArrayList<>();

    static final String CONFIGURES_GAME = "inGameConig";
    static final String CONFIG_STATE =  "configState";

    static final TextComponent beginConfigMessage = Component
            .text("Du möchtest ein Bedwars Spiel konfigurieren, du kannst jederzeit mit")
            .append(Component.text(" exit ")
            .color(TextColor.color(Color.RED.asRGB())))
            .append(Component.text("beenden. "))
            .append(Component.newline())
            .append(Component.text("Wähle die Farbe des ersten Teams das du hinzufügen möchtest aus: "))
            .append(Component.text("RED"))
            .append(Component.newline())
            .append(Component.text("GREEN"))
            .append(Component.newline());


    ArrayList<Team> teams;
    ArrayList<Spawner> spawners;
    ArrayList<Block> beds;
    ArrayList<Location> shopLocations;

    public static boolean saveGameConfig() {
        //TODO
        return true;
    }

    public static boolean loadGameConfig() {
        //TODO
        return true;
    }

    public static void createGameConfig(Player p) {

        GameConfig gameConfig = new GameConfig();

        int configNum = gameConfigs.size();
        gameConfigs.add(gameConfig);

        p.setMetadata(CONFIGURES_GAME, new FixedMetadataValue(Badwars.PLUGIN, configNum));
        p.setMetadata(CONFIG_STATE, new FixedMetadataValue(Badwars.PLUGIN, 0));
        p.sendMessage(beginConfigMessage);
    }

    @EventHandler
    public void createGameConfigEvent(AsyncChatEvent e) {
        if (e.getPlayer().hasMetadata(CONFIGURES_GAME)) {
            Player p = e.getPlayer();
            GameConfig gameConfig = GameConfig.gameConfigs.get(e.getPlayer().getMetadata(CONFIGURES_GAME).get(0).asInt());

            switch (e.getPlayer().getMetadata(CONFIG_STATE).get(0).asInt()) {
                case 0:
                    gameConfig.teams.add(new Team(e.message().toString(), null));

                    p.setMetadata(CONFIG_STATE, new FixedMetadataValue(Badwars.PLUGIN, 1));

                    p.sendMessage(Component.text("Du hast ein neues Team mit der Farbe" + e.message() + "erstellt")
                            .append(Component.text("Stelle dich auf ihren Spawn und tippe"))
                            .append(Component.text("hier").color(TextColor.color(Color.RED.asRGB()))));
                    break;
                case 1:
                    gameConfig.teams.get(gameConfig.teams.size()-1).setSpawnPoint(p.getLocation());
                    p.sendMessage(Component.text("Der Spawnpoint wurde gesetzt"));

            }
        }
    }

    public static GameConfig getDemoConfig() {
        GameConfig gameConfig = new GameConfig();
        Team team1 = new Team("RED", Bukkit.getWorlds().get(0).getSpawnLocation());
        Team team2 = new Team("GREEN", Bukkit.getWorlds().get(0).getSpawnLocation());

        ArrayList<Team> teams = new ArrayList();

        teams.add(team1);
        teams.add(team2);
        gameConfig.teams = teams;



        return gameConfig;
    }
}
