package bedwarsboys.badwars.commands;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.game.Game;
import bedwarsboys.badwars.game.GameConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Badwars_DebugCommand implements CommandExecutor {
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
//        if (args[0].equalsIgnoreCase("startSpawners")) {
//            SpawnerConfig.startSpawners();
//            return true;
//        }
//        if (args[0].equalsIgnoreCase("stopSpawners")) {
//            SpawnerConfig.stopSpawners();
//            return true;
//        }
        if (args[0].equalsIgnoreCase("makeShop"))  {
            if (sender instanceof Player) {
                for (int i = 0; i < ((Player) sender).getInventory().getSize(); i++) {
                    ItemStack item = ((Player) sender).getInventory().getItem(i);
                    if (item != null)
                        sender.sendMessage("ItemStack item" + i + " = new ItemStack(Material." + item.getType() +");");
                }
            }
        }
        if (args[0].equalsIgnoreCase("stopGame")) {
            sender.sendMessage(Badwars.PLUGIN_NAME + "das erste Spiel wird nun gestoppt");
            Game.games.get(0).stopGame();
        }
        if (args[0].equalsIgnoreCase("startGame")) {
            sender.sendMessage(Badwars.PLUGIN_NAME + "starte nun das erste Spiel");
            if (GameConfig.getConfig(0) != null) {
                Game game = new Game(GameConfig.getConfig(0));
                game.startGame();
            }
        }
        if (args[0].equalsIgnoreCase("testConfig")) {
            GameConfig.createGameConfig((Player) sender);
            return true;
        }
        return false;
    }
}
