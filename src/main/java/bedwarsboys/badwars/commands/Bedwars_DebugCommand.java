package bedwarsboys.badwars.commands;

import bedwarsboys.badwars.Badwars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Bedwars_DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equalsIgnoreCase("pasteLobby")){
            Badwars.lobbyManager.createLobbyContainer();
            return true;
        }
        if (args[0].equalsIgnoreCase("resetLobby")){
            Badwars.lobbyManager.removeLobbyContainer();
            return true;
        }
        if (args[0].equalsIgnoreCase("startSpawners")){
            if (!Badwars.lobbyManager.loadSpawners()){
                sender.sendMessage("Couldn't load arena area.");
            }
            Badwars.lobbyManager.startSpawners();
            return true;
        }
        if (args[0].equalsIgnoreCase("stopSpawners")){
            Badwars.lobbyManager.stopSpawners();
            return true;
        }
        return false;
    }
}
