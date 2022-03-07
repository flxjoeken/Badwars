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

    public Inventory menu = Bukkit.createInventory(null, 18, Component.text("Team Configurations"));
    public InventoryMenu invMenu;

    public TeamConfiguration(){

    }

    public void setupTeamConfigMenu() {
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
            if (TeamManager.teamCount < 8) {
                TeamManager.teamCount++;
                updateTeams();
            }
        };
        Action aMinus = p -> {
            if (TeamManager.teamCount > 2 && activeTeamCount() > 2) {
                TeamManager.teamCount--;
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

    private int activeTeamCount() {
        int c = 0;
        for (int i = 0; i < TeamManager.teamCount; i++) {
            c += TeamManager.activeTeams[i] ? 1 : 0;
        }
        return c;
    }

    private void doTeamAction(Player p, int slot) {
        if (menu.getContents()[slot] != null)
            p.sendMessage("You clicked " + slot + "!");
    }

    private void toggleTeamActive(Player p, int slot) {
        if (menu.getContents()[slot] == null) {
            if (activeTeamCount() > 2) {
                menu.setItem(slot, new ItemStack(Material.BARRIER));
                TeamManager.activeTeams[slot - 9] = false;
            }
        } else {
            menu.setItem(slot, null);
            TeamManager.activeTeams[slot - 9] = true;
        }
        updateTeams();
    }

    private void updateTeams() {
        for (TeamManager.BadwarsTeam bt : TeamManager.teams) {
            menu.setItem(bt.id, new ItemStack(bt.material));
        }
        for (int i = 0; i < 8; i++) {
            if (i >= TeamManager.teamCount || menu.getContents()[i + 9] != null) {
                menu.setItem(i, null);
            }

        }
    }

    private void bannerSetup(ItemStack banner, boolean isPlus) {
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

}
