package bedwarsboys.badwars.game;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Game {

    public static ArrayList<Game> games = new ArrayList<>();

    protected GameConfig gameConfig;
    protected ArrayList<Player> players;
    protected ArrayList<Team> teams;

    public Game(GameConfig gameConfig) {
        Game.games.add(this);
        this.gameConfig = gameConfig;
        this.players = new ArrayList<>();
    }

    public void startGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(gameConfig.getWorldName())) {
                players.add(p);
            }
        }
        for (Player p : players) {
            p.teleport(gameConfig.getTeamOfPlayer(p).getSpawnPoint());
        }
        gameConfig.getSpawnerConfig().startSpawners();
    }

    public void stopGame() {

    }
}
