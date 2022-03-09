package bedwarsboys.badwars.commands;

import bedwarsboys.badwars.game.GameConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConfigureTeamsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //TODO: Show inventory for specific GameConfig
        if (!(sender instanceof Player p)) return false;

        GameConfig g = GameConfig.getRunningConfig(p);
        if (g == null){
            //TODO: Add command name
            p.sendMessage("You are not configuring a game. To use this command, please start configuring a game with [TODO]");
            return true;
        }
        if (GameConfig.getRunningConfigMode(p) != 1){
            p.sendMessage("You are not in the correct configuration mode to configure the teams.");
            return true;
        }
        g.getTeamConfig().invMenu.showToPlayer(p);
        return true;
    }
}
