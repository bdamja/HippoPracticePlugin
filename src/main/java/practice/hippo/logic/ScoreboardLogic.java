package practice.hippo.logic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardLogic {

    private final HippoPractice parentPlugin;
    private final ScoreboardManager manager;

    private ChatColor[] scoreboardColors;
    private String title;
    private String ip;
    private String mapLabel;
    private String pbLabel;
    private String timeLabel;
    private String defaultTime;
    private String defaultPB;
    private String defaultMapName;
    private String darkGrayLine;

    public ScoreboardLogic(HippoPractice parentPlugin, ChatColor[] scoreboardColors) {
        this.parentPlugin = parentPlugin;
        this.manager = Bukkit.getScoreboardManager();
        this.scoreboardColors = scoreboardColors;
        setTeams();
    }

    public void makeBoard(Player player) {
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("title", "dummy");
        objective.setDisplayName(title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        registerTeam("lineA", darkGrayLine, "", "", 11, board, objective);

        registerTeam("mapLabel", mapLabel, "", "", 10, board, objective);
        registerTeam("mapName", "", defaultMapName, "", 9, board, objective);
        registerTeam("pbLabel", pbLabel, "", "", 7, board, objective);
        registerTeam("pbName", defaultPB, "", "", 6, board, objective);
        registerTeam("timeLabel", timeLabel, "", "", 4, board, objective);
        registerTeam("timeName", " ", defaultTime, "", 3, board, objective);
        registerTeam("lineB", darkGrayLine + " ", "", "", 1, board, objective);
        registerTeam("ip", ip, "", "", 0, board, objective);

        registerTeam("blank2", "  ", "", "", 2, board, objective);
        registerTeam("blank5", "   ", "", "", 5, board, objective);
        registerTeam("blank8", "    ", "", "", 8, board, objective);

        player.setScoreboard(board);
    }

//    private String ip = ChatColor.DARK_GREEN + "someserver.net";
//    private String mapLabel = "" + ChatColor.GREEN + ChatColor.BOLD + "Map" + ChatColor.GRAY + ChatColor.BOLD + ":";
//    private String pbLabel = "" + ChatColor.YELLOW + ChatColor.BOLD + "Personal Best" + ChatColor.GRAY + ChatColor.BOLD + ":";
//    private String timeLabel = "" + ChatColor.AQUA + ChatColor.BOLD + "Time" + ChatColor.GRAY + ChatColor.BOLD + ":";
//    private String defaultTime = "" + ChatColor.GRAY + "-.--- ";
//    private String defaultPB = "" + ChatColor.GRAY + "-.---";
//    private String defaultMapName = "" + ChatColor.GRAY + ChatColor.BOLD + "----";
//    private String darkGrayLine = "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "-------------------";

    private void registerTeam(String teamID, String teamName, String prefix, String suffix, int score, Scoreboard board, Objective objective) {
        Team newTeam = board.registerNewTeam(teamID);
        newTeam.addEntry(teamName);
        newTeam.setPrefix(prefix);
        newTeam.setSuffix(suffix);
        objective.getScore(teamName).setScore(score);
    }

    public void update(Player player, String mapNameFormatted) {
        setTeams();
        makeBoard(player);
        updateMapName(player, mapNameFormatted);
    }

    public void updateMapName(Player player, String mapNameFormatted) {
        Scoreboard board = player.getScoreboard();
        board.getTeam("mapName").setPrefix(mapNameFormatted);
    }

    public ChatColor[] getScoreboardColors() {
        return scoreboardColors;
    }

    private void setTeams() {
        this.title = scoreboardColors[0] + "Hippo Practice";
        this.darkGrayLine = scoreboardColors[1] + "" + ChatColor.STRIKETHROUGH + "-------------------";
        this.mapLabel = scoreboardColors[2] + "Map" + ChatColor.GRAY + ChatColor.BOLD + ":";
        this.defaultMapName = scoreboardColors[3] + "----";
        this.pbLabel = scoreboardColors[4] + "Personal Best" + ChatColor.GRAY + ChatColor.BOLD + ":";
        this.defaultPB = scoreboardColors[5] + "-.---";
        this.timeLabel = scoreboardColors[6] + "Time" + ChatColor.GRAY + ChatColor.BOLD + ":";
        this.defaultTime = scoreboardColors[7] + "-.---";
        this.ip = scoreboardColors[0] + "someserver.net";
    }
}
