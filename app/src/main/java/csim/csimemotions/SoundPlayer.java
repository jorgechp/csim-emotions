package csim.csimemotions;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by jorge on 23/12/15.
 * http://stackoverflow.com/questions/21043059/play-background-sound-in-android-applications
 */
public class SoundPlayer {
    MediaPlayer mp;
    int idSong;

    public SoundPlayer(int idSong) {
        this.idSong = idSong;
    }

    private void buildPlayer(Context context, boolean loop) {
        mp = MediaPlayer.create(context, this.idSong);
        mp.start();

        Log.d(Integer.toString(this.idSong), "On start");
        mp.setLooping(loop);
        mp.start();
    }

    public void play(Context context) {
        this.buildPlayer(context, true);
    }

    public void play(Context context, boolean loop) {
        this.buildPlayer(context, loop);
    }

    public void pause() {
        mp.pause();
    }
    public void destroy() {
        if (mp != null) {
            mp.stop();
        }
    }

    public boolean isPlaying() {
        boolean res = false;
        if (mp != null) {
            res = mp.isPlaying();
        }
        return res;
    }

    public void onDestroy() {
        this.destroy();
    }

}
