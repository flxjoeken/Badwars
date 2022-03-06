package bedwarsboys.badwars.game;

import bedwarsboys.badwars.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Game {

    protected static ArrayList<Game> games = new ArrayList<>();
    protected GameConfig gameConfig;

    public Game(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public void startGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Team.selectTeamMenu(p, gameConfig.teams);
        }
    }

    public static class startGameCommand implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            GameConfig.letPlayerCreateNewConfig((Player) sender);
            return true;
        }
    }
}
