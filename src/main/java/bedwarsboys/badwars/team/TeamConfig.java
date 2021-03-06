package bedwarsboys.badwars.team;

import bedwarsboys.badwars.game.GameConfig;
import bedwarsboys.badwars.invmenu.Action;
import bedwarsboys.badwars.invmenu.InventoryMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;

import static bedwarsboys.badwars.BadwarsMessages.*;
import static bedwarsboys.badwars.game.GameConfig.*;

/**
 * Every GameConfig will have one of these.
 * The TeamConfiguration saves which teams are enabled for the GameConfig, and how many of them.
 */
public class TeamConfig implements Listener {

    //TODO: Simplify Team selection and connect each team with a Team instance.

    public ArrayList<Team> teams = new ArrayList<>();

    Inventory menu = Bukkit.createInventory(null, 9 * 4, Component.text("Team Configurations"));
    public InventoryMenu invMenu;

    public TeamConfig() {

        for (Team.TEAMS t : Team.TEAMS.values()) {
            teams.add(new Team(t, null));
        }

        setupTeamConfigMenu();
        // initScoreboardTeams();
        updateTeams();
    }

    void setupTeamConfigMenu() {

        menu.setItem(8, createHelpBanner());

        for (int i = 9 + 2; i < 9 + 8; i++) {
            if (Team.TEAMS.byID(i - 9) == null) continue;
            menu.setItem(i, new ItemStack(Material.BARRIER));
        }
        for (int i = 3 * 9; i < 3 * 9 + 8; i++) {
            if (Team.TEAMS.byID(i - 19) == null) continue;
            menu.setItem(i, new ItemStack(Material.BARRIER));
        }
        ArrayList<Action> actionList = new ArrayList<>();
        for (int i = 0; i < 9 * 4; i++) {
            int slot = i;
            if (slot == 8 || slot == 8 + 9 || slot == 8 + 2 * 9 || slot == 8 + 3 * 9) {
                actionList.add(i, null);
            }
            // first row
            if (slot < 8) {
                actionList.add(i, p -> doTeamAction(p, slot, slot));
                continue;
            }
            //second row
            if (slot >= 9 && slot < 9 + 8) {
                actionList.add(i, p -> toggleTeamActive(slot - 9, slot));
                continue;
            }
            //third row
            if (slot >= 9 * 2 && slot < 9 * 2 + 8) {
                actionList.add(i, p -> doTeamAction(p, slot - 10, slot));
                continue;
            }
            //fourth row
            if (slot >= 9 * 3 && slot < 9 * 3 + 8) {
                actionList.add(i, p -> toggleTeamActive(slot - 19, slot));
            }
        }
        invMenu = new InventoryMenu(menu, actionList.toArray(new Action[0]));
    }

    private int activeTeamCount() {
        int c = 0;
        for (Team t : teams) {
            c += t.isActive() ? 1 : 0;
        }
        return c;
    }

    private void doTeamAction(Player p, int id, int slot) {
        if (menu.getContents()[slot] != null && id < Team.TEAMS.values().length)
            p.sendMessage("You clicked " + Team.TEAMS.values()[id].name + "!");
    }

    private void toggleTeamActive(int id, int slot) {
        if (Team.TEAMS.byID(id) == null) return;
        if (menu.getContents()[slot] == null) {
            if (activeTeamCount() > 2) {
                menu.setItem(slot, new ItemStack(Material.BARRIER));
                teams.get(id).setActive(false);
            } else {
                Bukkit.getServer().broadcast(Component.text("Not enough teams active"));
            }
        } else {
            menu.setItem(slot, null);
            teams.get(id).setActive(true);
        }
        updateTeams();
    }

    private void updateTeams() {
        for (Team.TEAMS bt : Team.TEAMS.values()) {
            int offset = 0;
            if (bt.id >= 8) {
                offset = 18;
            }
            if (teams.get(bt.id).isActive()) {
                menu.setItem(bt.id + offset, new ItemStack(bt.material));
            } else {
                menu.setItem(bt.id + offset, new ItemStack(Material.valueOf(bt.material.name().replace("WOOL", "CARPET"))));
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
        Action[] actions = new Action[teams.size()];
        for (int i = 0; i < teams.size(); i++) {
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

    private ItemStack createHelpBanner() {
        ItemStack s = new ItemStack(Material.WHITE_BANNER);
        ArrayList<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        BannerMeta meta = ((BannerMeta) s.getItemMeta());
        meta.setPatterns(patterns);
        meta.displayName(Component.text("Hilfe").color(NamedTextColor.GOLD));
        int lineSize = 50;
        ArrayList<Component> lore = stringToLore("Um ein Team zu aktivieren, klicke auf das rote Kreuz " +
                "unter der Farbe.", lineSize);
        lore.addAll(stringToLore("Zum deaktivieren klicke den leeren Platz unter der Teamfarbe.", lineSize));
        lore.addAll(stringToLore("Es m??ssen immer mindestens zwei Teams aktiv sein.", lineSize));
        lore.addAll(stringToLore("Um den Spawnpunkt eines Teams und die Bett-Position einzustellen, " +
                "klicke auf die entsprechende Farbe.", lineSize));
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        s.setItemMeta(meta);
        return s;
    }

    ArrayList<Component> stringToLore(String message, int charPerLine) {
        ArrayList<Component> lore = new ArrayList<>();
        String[] words = message.split(" ");
        StringBuilder nextLine = new StringBuilder();
        for (String word : words) {
            if ((nextLine + word).length() > charPerLine) {
                lore.add(Component.text(nextLine.toString()));
                nextLine = new StringBuilder();
            }
            nextLine.append(word).append(" ");
        }
        lore.add(Component.text(nextLine.toString()));
        return lore;
    }

    public boolean joinTeam(Player p, Team.TEAMS team) {
        //TODO
        return false;
    }

    public void showTeamConfigMenu(Player p) {
        invMenu.showToPlayer(p);
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
