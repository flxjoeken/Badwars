package bedwarsboys.badwars.team;

import bedwarsboys.badwars.game.GameConfig;
import bedwarsboys.badwars.invmenu.Action;
import bedwarsboys.badwars.invmenu.InventoryMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static bedwarsboys.badwars.game.GameConfig.*;

/**
 * Every GameConfig will have one of these.
 * The TeamConfiguration saves which teams are enabled for the GameConfig, and how many of them.
 */
public class TeamConfig implements Listener {

    //TODO: Simplify Team selection and connect each team with a Team instance.

    /**
     * number of teams that are active
     */
    public int teamCount = 2;
    /**
     * which teams are active
     */
    public boolean[] activeTeams = new boolean[Team.TEAMS.values().length];
    public ArrayList<Team> teams;

    Inventory menu = Bukkit.createInventory(null, 9 * 4, Component.text("Team Configurations"));
    public InventoryMenu invMenu;

    public TeamConfig() {
        activeTeams[0] = true;
        activeTeams[1] = true;
        setupTeamConfigMenu();
        initScoreboardTeams();
    }

    void setupTeamConfigMenu() {
        /*
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
        */

        menu.setItem(0, new ItemStack(Team.TEAMS.RED.material));
        menu.setItem(1, new ItemStack(Team.TEAMS.BLUE.material));
        /*
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
        */
        ArrayList<Action> actionList = new ArrayList<>();
        for (int i = 0; i < 9 * 4; i++) {
            int slot = i;
            // first row
            if (slot < 8) {
                actionList.add(i, p -> doTeamAction(p, slot, slot));
                continue;
            }
            //second row
            if (slot >= 9 && slot < 9 + 8) {
                actionList.add(i, p -> toggleTeamActive(slot, slot - 9));
                continue;
            }
            //third row
            if (slot >= 9 * 2 && slot < 9 * 2 + 8) {
                actionList.add(i, p -> doTeamAction(p, slot, slot - 18));
                continue;
            }
            if (slot >= 9 * 3 && slot < 9 * 3 + 8) {
                actionList.add(i, p -> toggleTeamActive(slot, slot - 27));
            }
            /*
            if (slot == 8) {
                actionList.add(slot, aPlus);
                continue;
            }
             only other case is slot==17
            actionList.add(slot, aMinus);
            */
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

    private void doTeamAction(Player p, int id, int slot) {
        if (menu.getContents()[slot] != null && id < Team.TEAMS.values().length)
            p.sendMessage("You clicked " + Team.TEAMS.values()[id].name + "!");
    }

    private void toggleTeamActive(int id, int slot) {
        if (menu.getContents()[slot] == null) {
            if (activeTeamCount() > 2) {
                menu.setItem(slot, new ItemStack(Material.BARRIER));
                activeTeams[id] = false;
            }
        } else {
            menu.setItem(slot, null);
            activeTeams[id] = true;
        }
        updateTeams();
    }

    private void updateTeams() {
        for (Team.TEAMS bt : Team.TEAMS.values()) {
            if (activeTeams[bt.id]) {
                if (bt.id < 8) {
                    menu.setItem(bt.id, new ItemStack(bt.material));
                } else {
                    menu.setItem(bt.id + 18, new ItemStack(bt.material));
                }
            }
        }
    }

    /**
     * Lets Player p select a team from the teams available
     *
     * @param p The player to show the menu to
     */
    //TODO
    public void showSelectTeamMenu(Player p) {
        Inventory iv = Bukkit.createInventory(null, 9 * 2, Component.text("Select Team: "));
        int currentSlot = 0;
        Action[] actions = new Action[activeTeams.length];
        for (int i = 0; i < activeTeams.length; i++) {
            Team.TEAMS t = Team.TEAMS.byID(i);
            if (t != null) {
                iv.setItem(currentSlot, new ItemStack(t.material));
                actions[currentSlot] = x -> {
                    joinTeam(x, t);
                };
                currentSlot++;
            }
        }
        InventoryMenu im = new InventoryMenu(iv, actions);
        im.showToPlayer(p);
    }

    /*
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
    */

    public boolean joinTeam(Player p, Team.TEAMS team) {
        //TODO
        return false;
    }

    public void showMenuToPlayer(Player p) {
        p.openInventory(menu);
    }

    //TODO: Change name of registered team to GameConfig id + Team name
    //TODO: Maybe move to GameConfig
    void initScoreboardTeams() {
        for (Team.TEAMS bt : Team.TEAMS.values()) {
            org.bukkit.scoreboard.Team t = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(bt.name.toUpperCase());
            t.color(bt.textColor);
            t.displayName(Component.text(bt.name.toUpperCase()));
            t.setAllowFriendlyFire(false);
            t.setCanSeeFriendlyInvisibles(true);
        }
    }

    @EventHandler
    private static void gameConfigClickEvent(PlayerInteractEvent e) {
        if (e.getPlayer().hasMetadata(CONFIGURES_GAME)) {
            Player p = e.getPlayer();
            GameConfig gameConfig = getRunningConfig(p);
            if (gameConfig == null) return;

            if (getRunningConfigMode(p) == 2) {
                //gameConfig.beds.add(e.getClickedBlock());
                Team.TEAMS t = getRunningConfigTeam(p);
                if (t != null) {
                    p.sendMessage(ADDED_BED_MESSAGE);
                }
            }
        }
    }

}
