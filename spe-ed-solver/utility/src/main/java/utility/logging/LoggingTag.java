package utility.logging;

public enum LoggingTag {
    GAME_INFO("[GAME_INFO]"), INFO("[INFO]"), WARNING("[WARNING]"), ERROR("[ERROR]"), FATAL_ERROR("[FATAL_ERROR]");

    private String tag;

    private LoggingTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }
}
