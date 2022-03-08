package bedwarsboys.badwars.team;

import bedwarsboys.badwars.invmenu.Action;
import bedwarsboys.badwars.invmenu.InventoryMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Every GameConfig will have one of these.
 * The TeamConfiguration saves which teams are enabled for the GameConfig, and how many of them.
 */
public class TeamConfig {

    //TODO: Simplify Team selection and connect each team with a Team instance.

    public int teamCount = 2;
    public boolean[] activeTeams = {true, true, true, true, true, true, true, true};
    ArrayList<Team> teams;

    public Inventory menu = Bukkit.createInventory(null, 18, Component.text("Team Configurations"));
    public InventoryMenu invMenu;

    public TeamConfig() {
        setupTeamConfigMenu();
        initScoreboardTeams();
    }

    void setupTeamConfigMenu() {
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
                actionList.add(i, p -> toggleTeamActive(slot));
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
        for (int i = 0; i < teamCount; i++) {
            c += activeTeams[i] ? 1 : 0;
        }
        return c;
    }

    private void doTeamAction(Player p, int slot) {
        if (menu.getContents()[slot] != null)
            p.sendMessage("You clicked " + slot + "!");
    }

    private void toggleTeamActive(int slot) {
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

    private void updateTeams() {
        for (Team.TEAMS bt : Team.TEAMS.values()) {
            menu.setItem(bt.id, new ItemStack(bt.material));
        }
        for (int i = 0; i < 8; i++) {
            if (i >= teamCount || menu.getContents()[i + 9] != null) {
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

    public void showMenuToPlayer(Player p) {
        p.openInventory(menu);
    }

    //TODO: Change name of registered team to GameConfig id + Team name
    //TODO: Maybe move to GameConfig
    void initScoreboardTeams(){
        for (Team.TEAMS bt : Team.TEAMS.values()){
            org.bukkit.scoreboard.Team t = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(bt.name.toUpperCase());
            t.color(bt.textColor);
            t.displayName(Component.text(bt.name.toUpperCase()));
            t.setAllowFriendlyFire(false);
            t.setCanSeeFriendlyInvisibles(true);
        }
    }

}
