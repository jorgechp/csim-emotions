package csim.csimemotions;


import java.io.Serializable;

/**
 * Created by jorge on 10/01/16.
 */
public class UserConfig implements Serializable {

    private static UserConfig ourInstance = new UserConfig();

    public static UserConfig getInstance() {
        return ourInstance;
    }


    private byte backgroundSoundNumber;
    private Emotions backgroundEmotionNumber;

    private String userName;

    private UserConfig() {
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


}
