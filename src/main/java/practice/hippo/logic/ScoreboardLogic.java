package practice.hippo.logic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import practice.hippo.HippoPractice;
import practice.hippo.playerdata.PlayerData;

public class ScoreboardLogic {

    private final HippoPractice parentPlugin;
    private final ScoreboardManager manager;

    private String IP = ChatColor.DARK_GREEN + "";
    private static final String MAP_LABEL = "" + ChatColor.GREEN + ChatColor.BOLD + "Map" + ChatColor.GRAY + ChatColor.BOLD + ":";
    private static final String PB_LABEL = "" + ChatColor.YELLOW + ChatColor.BOLD + "Personal Best" + ChatColor.GRAY + ChatColor.BOLD + ":";
    private static final String TIME_LABEL = "" + ChatColor.AQUA + ChatColor.BOLD + "Time" + ChatColor.GRAY + ChatColor.BOLD + ":";
    private static final String DEFAULT_TIME = "" + ChatColor.GRAY + "-.--- ";
    private static final String DEFAULT_PB = "" + ChatColor.GRAY + "-.---";
    private static final String DEFAULT_MAP_NAME = "" + ChatColor.GRAY + ChatColor.BOLD + "----";
    private static final String DARK_GRAY_LINE = "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "-------------------";

    public ScoreboardLogic(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
        this.manager = Bukkit.getScoreboardManager();
        IP = IP + parentPlugin.ip;
    }

    public void makeBoard(Player player) {
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("title", "dummy");
        objective.setDisplayName("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Hippo Practice");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        registerTeam("lineA", DARK_GRAY_LINE, "", "", 11, board, objective);

        registerTeam("mapLabel", MAP_LABEL, "", "", 10, board, objective);
        registerTeam("mapName", "", DEFAULT_MAP_NAME, "", 9, board, objective);
        registerTeam("pbLabel", PB_LABEL, "", "", 7, board, objective);
        registerTeam("pbName", "      ", DEFAULT_PB, "", 6, board, objective);
        registerTeam("timeLabel", TIME_LABEL, "", "", 4, board, objective);
        registerTeam("timeName", " ", DEFAULT_TIME, "", 3, board, objective);
        registerTeam("lineB", DARK_GRAY_LINE + " ", "", "", 1, board, objective);
        registerTeam("ip", IP, "", "", 0, board, objective);

        registerTeam("blank2", "  ", "", "", 2, board, objective);
        registerTeam("blank5", "   ", "", "", 5, board, objective);
        registerTeam("blank8", "    ", "", "", 8, board, objective);

        player.setScoreboard(board);
    }

    private void registerTeam(String teamID, String teamName, String prefix, String suffix, int score, Scoreboard board, Objective objective) {
        Team newTeam = board.registerNewTeam(teamID);
        newTeam.addEntry(teamName);
        newTeam.setPrefix(prefix);
        newTeam.setSuffix(suffix);
        objective.getScore(teamName).setScore(score);
    }

    public void updateMapName(Player player, String mapNameFormatted) {
        Scoreboard board = player.getScoreboard();
        board.getTeam("mapName").setPrefix(mapNameFormatted);
    }

    public void updatePB(PlayerData playerData, String mapName) {
        Player player = Bukkit.getPlayer(playerData.getPlayerName());
        if (player != null) {
            long pbInMS = playerData.getPB(mapName);
            String pbFormatted = "N/A";
            if (pbInMS != -1) {
                pbFormatted = Timer.computeTimeFormatted(pbInMS);
            }
            Scoreboard board = player.getScoreboard();
            board.getTeam("pbName").setPrefix(ChatColor.GRAY + pbFormatted);
        }
    }
}
