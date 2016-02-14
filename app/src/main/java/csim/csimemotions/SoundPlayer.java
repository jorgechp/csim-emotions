package csim.csimemotions;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

/**
 * Created by jorge on 23/12/15.
 * http://stackoverflow.com/questions/21043059/play-background-sound-in-android-applications
 */
public class SoundPlayer {
    private MediaPlayer mp;
    private int idSong;
    private MediaPlayer.OnCompletionListener completionListener;

    public SoundPlayer(int idSong) {
        this.idSong = idSong;
    }
    public SoundPlayer(AssetFileDescriptor descriptor) throws IOException {

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
        descriptor.close();
        mp.prepare();
        mp.setVolume(1f, 1f);

        idSong = -1; // Necesario para indicar que no es una identificacion sino un descriptor

    }

    private void buildPlayer(Context context, boolean loop) {
        if(idSong != -1) {
            mp = MediaPlayer.create(context, this.idSong);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        mp.start();

        Log.d(Integer.toString(this.idSong), "On start");
        mp.setLooping(loop);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();

            }

            ;
        });
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

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener ocl){
        this.mp.setOnCompletionListener(ocl);
    }

    public void release(){
        this.mp.release();
    }



}
