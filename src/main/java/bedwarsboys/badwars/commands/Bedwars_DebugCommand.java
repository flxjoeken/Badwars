package bedwarsboys.badwars.commands;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.game.GameConfig;
import bedwarsboys.badwars.itemspawner.Spawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Bedwars_DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equalsIgnoreCase("pasteLobby")) {
            Badwars.lobbyManager.createLobbyContainer();
            return true;
        }
        if (args[0].equalsIgnoreCase("resetLobby")) {
            Badwars.lobbyManager.removeLobbyContainer();
            return true;
        }
        if (args[0].equalsIgnoreCase("startSpawners")) {
            Spawner.startSpawners();
            return true;
        }
        if (args[0].equalsIgnoreCase("stopSpawners")) {
            Spawner.stopSpawners();
            return true;
        }
        if (args[0].equalsIgnoreCase("testConfig")) {
            GameConfig.letPlayerCreateNewConfig((Player) sender);
            return true;
        }
        return false;
    }
}
