package bedwarsboys.badwars.team;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scoreboard.Team;

import javax.naming.Name;
import java.util.ArrayList;

public class TeamManager {

    public int teamCount = 2;
    public boolean[] activeTeams = {true, true, true, true, true, true, true, true};

    public TeamManager() {
        //TODO: Add more teams

        for (TEAMS bt : TEAMS.values()){
            Team t = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(bt.name.toUpperCase());
            t.color(bt.textColor);
            t.displayName(Component.text(bt.name.toUpperCase()));
            t.setAllowFriendlyFire(false);
            t.setCanSeeFriendlyInvisibles(true);
        }
    }



}
