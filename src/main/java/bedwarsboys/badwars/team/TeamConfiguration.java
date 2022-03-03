package bedwarsboys.badwars.team;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.invmenu.Action;
import bedwarsboys.badwars.invmenu.InventoryMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TeamConfiguration {

    public static Inventory menu = Bukkit.createInventory(null, 18, Component.text("Team Configurations"));
    public static InventoryMenu invMenu;
    public static int teamCount = 2;
    public static boolean[] activeTeams = {true, true, true, true, true, true, true, true};

    public static void setupTeamConfigMenu() {
        //Plus Banner
        ItemStack plus = new ItemStack(Material.GREEN_BANNER, 1);
        ItemMeta plusMeta = plus.getItemMeta();
        plusMeta.displayName(Component.text("add Team"));
        plus.setItemMeta(plusMeta);
        bannerSetup(plus, true);

        //Minus Banner
        ItemStack minus = new ItemStack(Material.RED_BANNER, 1);
        ItemMeta minusMeta = minus.getItemMeta();
        minusMeta.displayName(Component.text("remove Team"));
        minus.setItemMeta(minusMeta);
        bannerSetup(minus, false);

        menu.setItem(8, plus);
        menu.setItem(8 + 9, minus);

        menu.setItem(0, new ItemStack(Material.RED_WOOL));
        menu.setItem(1, new ItemStack(Material.BLUE_WOOL));
        Action aPlus = p -> {
            if (teamCount < 8) {
                teamCount++;
                updateTeams();
            }
        };
        Action aMinus = p -> {
            if (teamCount > 2 && activeTeamCount() > 2) {
                teamCount--;
                updateTeams();
            }
        };
        ArrayList<Action> actionList = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            int slot = i;
            if (slot <= 7) {
                actionList.add(i, p -> doTeamAction(p, slot));
                continue;
            }
            if (slot > 8 && slot <= 16) {
                actionList.add(i, p -> toggleTeamActive(p, slot));
                continue;
            }
            if (slot == 8) {
                actionList.add(slot, aPlus);
                continue;
            }
            // only other case is slot==17
            actionList.add(slot, aMinus);
        }
        invMenu = new InventoryMenu(menu, actionList.toArray(new Action[0]));
    }

    private static int activeTeamCount() {
        int c = 0;
        for (int i = 0; i < teamCount; i++) {
            c += activeTeams[i] ? 1 : 0;
        }
        return c;
    }

    private static void doTeamAction(Player p, int slot) {
        if (menu.getContents()[slot] != null)
            p.sendMessage("You clicked " + slot + "!");
    }

    private static void toggleTeamActive(Player p, int slot) {
        if (menu.getContents()[slot] == null) {
            if (activeTeamCount() > 2) {
                menu.setItem(slot, new ItemStack(Material.BARRIER));
                activeTeams[slot - 9] = false;
            }
        } else {
            menu.setItem(slot, null);
            activeTeams[slot - 9] = true;
        }
        updateTeams();
    }

    private static void updateTeams() {
        menu.setItem(0, new ItemStack(Material.RED_WOOL));
        menu.setItem(1, new ItemStack(Material.BLUE_WOOL));
        menu.setItem(2, new ItemStack(Material.GREEN_WOOL));
        menu.setItem(3, new ItemStack(Material.YELLOW_WOOL));
        menu.setItem(4, new ItemStack(Material.PINK_WOOL));
        menu.setItem(5, new ItemStack(Material.PURPLE_WOOL));
        menu.setItem(6, new ItemStack(Material.BLACK_WOOL));
        menu.setItem(7, new ItemStack(Material.WHITE_WOOL));
        for (int i = 0; i < 8; i++) {
            if (i >= teamCount || menu.getContents()[i + 9] != null) {
                menu.setItem(i, null);
            }

        }
    }

    private static void bannerSetup(ItemStack banner, boolean isPlus) {
        ArrayList<Pattern> patterns = new ArrayList<>();
        if (isPlus) {
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER));
        }
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        BannerMeta meta = ((BannerMeta) banner.getItemMeta());
        meta.displayName(banner.getItemMeta().displayName());
        meta.setPatterns(patterns);
        banner.setItemMeta(meta);
    }

    public static class ConfigureTeamsCommand implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (!(sender instanceof Player)) return false;
            invMenu.showToPlayer((Player) sender);
            return true;
        }
    }

    public static class SetTeamSpawnCommand implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (sender instanceof Player p) {
                if (args.length == 1) {
                    // see if input is a number
                    boolean isNum = false;
                    int num = -1;
                    try {
                        // if input is a number, assign it to num
                        num = Integer.parseInt(args[0]);
                        // if (num <= 7) isNum = true;
                    } catch (NumberFormatException ignored) {
                    }
                    // check if input is a colors name
                    int textNum = switch (args[0]) {
                        case "red" -> 0;
                        case "blue" -> 1;
                        case "green" -> 2;
                        case "yellow" -> 3;
                        case "pink" -> 4;
                        case "purple" -> 5;
                        case "black" -> 6;
                        case "white" -> 7;
                        default -> -1;
                    };
                    if (textNum != -1) {
                        num = textNum;
                    }
                    // assign the spawnpoint for given team if input was valid
                    if (num != -1) {
                        Badwars.CONFIG.set("teams." + num + ".x", p.getLocation().getBlockX());
                        Badwars.CONFIG.set("teams." + num + ".y", p.getLocation().getBlockY());
                        Badwars.CONFIG.set("teams." + num + ".z", p.getLocation().getBlockZ());
                        Badwars.PLUGIN.saveConfig();
                        sender.sendMessage(Badwars.PLUGIN_NAME+"Successfully saved spawnpoint.");
                    } else {
                        sender.sendMessage(Badwars.PLUGIN_NAME+"Input was not valid.");
                    }
                    return true;


                }
                return false;
            }
            sender.sendMessage(Badwars.PLUGIN_NAME+"This command is currently only for players.");
            return true;
        }
    }

}
