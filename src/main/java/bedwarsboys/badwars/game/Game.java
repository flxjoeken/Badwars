package bedwarsboys.badwars.game;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.team.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game {

    public static ArrayList<Game> games = new ArrayList<>();

    protected GameConfig gameConfig;
    protected ArrayList<Player> players;

    public Game(GameConfig gameConfig) {
        Game.games.add(this);
        this.gameConfig = gameConfig;
        this.players = new ArrayList<>();
    }

    public boolean startGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(gameConfig.getWorldName())) {
                players.add(p);
            }
        }
        for (Player p : players) {
            Team teamOfPlayer = gameConfig.getTeamOfPlayer(p);
            if (teamOfPlayer == null) {
                Bukkit.broadcast(Component.text(Badwars.PLUGIN_NAME + "Error in GameConfig"));
                return false;
            }
            p.teleport(teamOfPlayer.getSpawnPoint());
        }
        gameConfig.getSpawnerConfig().startSpawners();
        return true;
    }

    public void stopGame() {

    }
}
