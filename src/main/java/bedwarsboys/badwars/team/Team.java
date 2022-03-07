package bedwarsboys.badwars.team;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.invmenu.Action;
import bedwarsboys.badwars.invmenu.InventoryMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

//should be implemented in own class?
public class Team {

    ArrayList<Player> players;
    /**
     * Every in-game team has a hardcoded team that decides it's name, id and color
     */
    TEAMS team;
    Location spawnPoint;
    Block bed;

    /**
     * Create a new Team
     * @param team the ID of the team
     * @param spawnPoint spawnLocation
     */
    public Team(TEAMS team, Location spawnPoint) {
        this.spawnPoint = spawnPoint;
        this.team = team;
    }

    /**
     * Lets Player p select a team from a Collection of Teams
     * @param p The player to show the menu to
     * @param teams The collection of teams to choose from
     */
    //TODO
    public static void selectTeamMenu(Player p, Collection<TEAMS> teams) {
        Inventory iv = Bukkit.createInventory(null, 9, Component.text("Select Team: "));
        InventoryMenu im;
        for (TEAMS t : teams) {
            iv.addItem(new ItemStack(t.material));
        }
        Action[] actions = {};
        im = new InventoryMenu(iv, actions);
        im.showToPlayer(p);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public TEAMS getTeamColor() {
        return team;
    }

    public void setTeamColor(TEAMS teamId) {
        this.team = teamId;
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Location spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public Block getBed() {
        return bed;
    }

    public void setBed(Block bed) {
        this.bed = bed;
    }

    public enum TEAMS {
        RED(0, "red", Material.RED_WOOL, NamedTextColor.RED),
        BLUE(1, "blue", Material.BLUE_WOOL, NamedTextColor.BLUE),
        GREEN(2, "green", Material.GREEN_WOOL, NamedTextColor.GREEN),
        YELLOW(3, "yellow", Material.YELLOW_WOOL, NamedTextColor.YELLOW),
        PINK(4, "pink", Material.PINK_WOOL, NamedTextColor.LIGHT_PURPLE),
        PURPLE(5, "purple", Material.PURPLE_WOOL, NamedTextColor.DARK_PURPLE),
        BLACK(6, "black", Material.BLACK_WOOL, NamedTextColor.BLACK),
        WHITE(7, "white", Material.WHITE_WOOL, NamedTextColor.WHITE);

        final int id;
        final String name;
        final Material material;
        final NamedTextColor textColor;

        TEAMS(int id, String name, Material material, NamedTextColor textColor) {
            this.id = id;
            this.name = name;
            this.material = material;
            this.textColor = textColor;
        }
    }

}
