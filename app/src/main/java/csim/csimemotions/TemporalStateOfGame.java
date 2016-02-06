package csim.csimemotions;

/**
 * Created by jorge on 27/01/16.
 */
public class TemporalStateOfGame {
    private boolean enableLogging;
    private boolean enableEEG;

    private static TemporalStateOfGame ourInstance = new TemporalStateOfGame();

    public static TemporalStateOfGame getInstance() {
        return ourInstance;
    }

    private TemporalStateOfGame() {
        this.enableLogging = false;
        this.enableEEG = false;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public boolean isEnableEEG() {
        return enableEEG;
    }

    public void setEnableEEG(boolean enableEEG) {
        this.enableEEG = enableEEG;
    }
}
