package bedwarsboys.badwars.team;

import bedwarsboys.badwars.invmenu.Action;
import bedwarsboys.badwars.invmenu.InventoryMenu;
import net.kyori.adventure.text.Component;
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
    String teamColor;
    Location spawnPoint;
    Block bed;

    /**
     * teamColor must be a color of the Minecraft wools
     * @param teamColor the Color of the team
     * @param spawnPoint spawnLocation
     */
    public Team(String teamColor, Location spawnPoint) {
        this.spawnPoint = spawnPoint;
        this.teamColor = teamColor;
    }

    /**
     * lets a Player p select a team from a Collection of Teams
     * @param p Player who'll choose
     * @param teams A collection of teams to choose from
     */
    public static void selectTeamMenu(Player p, Collection<Team> teams) {
        Inventory iv = Bukkit.createInventory(null, 9, Component.text("select Team: "));
        InventoryMenu im;
        for (Team t : teams) {
            iv.addItem(new ItemStack(t.getColoredWoolMaterial()));
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

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
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

    public Material getColoredWoolMaterial() {
        return Material.getMaterial(teamColor+"_WOOL");
    }
}
