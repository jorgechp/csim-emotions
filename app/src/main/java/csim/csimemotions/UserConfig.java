package csim.csimemotions;


import java.io.Serializable;

/**
 * Created by jorge on 10/01/16.
 */
public class UserConfig implements Serializable {

    private static UserConfig ourInstance = new UserConfig();
    private int idSongSelected;
    private float userRating;
    private StateOfGame sog;

    public static UserConfig getInstance() {
        return ourInstance;
    }


    private byte backgroundSoundNumber;
    private Emotions backgroundEmotionNumber;

    private String userName;

    private UserConfig() {
        this.idSongSelected = -1;
        this.userRating = 0;
        this.backgroundEmotionNumber = Emotions.HAPPY;
    }

    public static UserConfig getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(UserConfig ourInstance) {
        UserConfig.ourInstance = ourInstance;
    }

    public byte getBackgroundSoundNumber() {
        return backgroundSoundNumber;
    }

    public void setBackgroundSoundNumber(byte backgroundSoundNumber) {
        this.backgroundSoundNumber = backgroundSoundNumber;
    }

    public Emotions getBackgroundEmotionNumber() {
        return backgroundEmotionNumber;
    }

    public void setBackgroundEmotion(Emotions backgroundEmotionNumber) {
        this.backgroundEmotionNumber = backgroundEmotionNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setIdSongSelected(int idSongSelected) {
        this.idSongSelected = idSongSelected;
    }

    public int getIdSongSelected() {
        return this.idSongSelected;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public float getUserRating() {
        return userRating;
    }

    public StateOfGame getSog() {
        return sog;
    }

    public void setSog(StateOfGame sog) {
        this.sog = sog;
    }
}
