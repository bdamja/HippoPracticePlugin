package practice.hippo.logic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardLogic {

    private final ScoreboardManager manager;

    public ScoreboardLogic() {
        manager = Bukkit.getScoreboardManager();
    }

    public void makeBoard(Player player) {
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("title", "dummy");
        objective.setDisplayName("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Hippo Practice");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team blank1 = board.registerNewTeam("");
        blank1.addEntry("");
        blank1.setSuffix("");
        blank1.setPrefix("");
        objective.getScore("").setScore(1);

        player.setScoreboard(board);
    }
}
