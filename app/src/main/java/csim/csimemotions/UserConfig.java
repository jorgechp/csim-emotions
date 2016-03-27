package csim.csimemotions;


import java.io.Serializable;

/**
 * Created by jorge on 10/01/16.
 */
public class UserConfig implements Serializable {


    private int idSongSelected;
    /**
     * Último rating otorgado por el usuario a la aplicación
     */
    private float userRating;
    private StateOfGame sog;

    /**
     * Puntos del usuario
     */
    private long points;




    private byte backgroundSoundNumber;
    private Emotions backgroundEmotionNumber;

    private String userName;

    public UserConfig() {
        this.idSongSelected = -1;
        this.userRating = 0;
        points = 0;
        this.backgroundEmotionNumber = Emotions.HAPPY;
        userName = null;
        sog = new StateOfGame();


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

    public long getPoints() {
        return points;
    }

    /**
     * Aplica una variación de puntos al usuario.
     * <p/>
     * Si los puntos finales del usuarop fueran negativos,se establecerían a 0.
     *
     * @param increase
     */
    public void changePoints(byte increase) {
        this.points += increase;
        if (this.points < 0) {
            this.points = 0;
        }
    }
}
