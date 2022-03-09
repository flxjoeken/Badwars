package bedwarsboys.badwars.team;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

//should be implemented in own class?
public class Team {


    int maxPlayers;
    ArrayList<Player> players;
    /**
     * Every in-game team has a hardcoded team that decides it's name, id and color
     */
    TEAMS team;
    Location spawnPoint;
    Location bedlocation;

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
     * Create a new Team
     *
     * @param team the ID of the team
     * @param spawnPoint the spawn location of the team
     * @param maxPlayers the maximum amount of players in the team
     */
    public Team(TEAMS team, Location spawnPoint, int maxPlayers) {
        this.spawnPoint = spawnPoint;
        this.team = team;
        this.maxPlayers = maxPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public NamedTextColor getTeamColor() {
        return team.textColor;
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

    public Location getBedlocation() {
        return bedlocation;
    }

    public void setBedlocation(Location bedlocation) {
        this.bedlocation = bedlocation;
    }
    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
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

        public final int id;
        public final String name;
        public final Material material;
        public final NamedTextColor textColor;

        TEAMS(int id, String name, Material material, NamedTextColor textColor) {
            this.id = id;
            this.name = name;
            this.material = material;
            this.textColor = textColor;
        }

        public static TEAMS byID(int id){
            for (TEAMS t : TEAMS.values()){
                if (t.id == id){
                    return t;
                }
            }
            return null;
        }

    }

}
