package bedwarsboys.badwars.team;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class TeamManager {

    public static ArrayList<BadwarsTeam> teams = new ArrayList<>();
    public static int teamCount = 2;
    public static boolean[] activeTeams = {true, true, true, true, true, true, true, true};

    public TeamManager() {
        teams.add(new BadwarsTeam(0, "red", Material.RED_WOOL, NamedTextColor.RED));
        teams.add(new BadwarsTeam(1, "blue", Material.BLUE_WOOL, NamedTextColor.BLUE));
        teams.add(new BadwarsTeam(2, "green", Material.GREEN_WOOL, NamedTextColor.GREEN));
        teams.add(new BadwarsTeam(3, "yellow", Material.YELLOW_WOOL, NamedTextColor.YELLOW));
        teams.add(new BadwarsTeam(4, "pink", Material.PINK_WOOL, NamedTextColor.LIGHT_PURPLE));
        teams.add(new BadwarsTeam(5, "purple", Material.PURPLE_WOOL, NamedTextColor.DARK_PURPLE));
        teams.add(new BadwarsTeam(6, "black", Material.BLACK_WOOL, NamedTextColor.BLACK));
        teams.add(new BadwarsTeam(7, "white", Material.WHITE_WOOL, NamedTextColor.WHITE));

        for (BadwarsTeam bt : teams){
            Team t = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(bt.name.toUpperCase());
            t.color(bt.color);
            t.displayName(Component.text(bt.name.toUpperCase()));
            t.setAllowFriendlyFire(false);
            t.setCanSeeFriendlyInvisibles(true);
        }
    }

    public static class BadwarsTeam {
        public int id;
        public String name;
        public Material material;
        public NamedTextColor color;

        public BadwarsTeam(int id, String name, Material material, NamedTextColor color) {
            this.id = id;
            this.name = name;
            this.material = material;
            this.color = color;
        }
    }

}
