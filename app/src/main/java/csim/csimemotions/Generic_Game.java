package csim.csimemotions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by jorge on 5/01/16.
 */
public abstract class Generic_Game extends android.support.v4.app.Fragment implements IGame {
    protected MainActivity actividadPrincipal;
    protected DataBaseController dbc;
    protected StateOfGame sg;
    protected View.OnClickListener clickListener;
    protected States currentGame;
    protected SoundPlayer sonido;
    protected Emotions respuestaCorrecta, respuestaUsuario;
    protected ImageView feedbackImage;
    protected SoundPlayer feedBackSoundBien, feedBackSoundMal;
    private boolean wasSoundPlaying;


    protected Emotions getEmotionFromString(String emotion) {
        Emotions correctEmotion = null;

        switch (emotion) {
            case "HAPPY":
                correctEmotion = Emotions.HAPPY;
                break;
            case "SAD":
                correctEmotion = Emotions.SAD;
                break;
            case "ANGRY":
                correctEmotion = Emotions.ANGRY;
                break;
            case "SURPRISED":
                correctEmotion = Emotions.SURPRISED;
                break;
            case "NONE":
                correctEmotion = Emotions.NONE;
                break;
        }

        return correctEmotion;

    }

    /**
     * Calcula el tamano en pixeles de una imagen, a partir de una escala relativa
     *
     * @param heigthScale
     * @param widthScale
     * @return int[] primer elemento, tamano de la altura, y segundo elemento, tamano de la anchura. (en pixeles)
     */
    protected int[] setSizeOfImage(float heigthScale, float widthScale) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int screenHeight = Math.round(displaymetrics.heightPixels * heigthScale);
        int screenWidth = Math.round(displaymetrics.widthPixels * widthScale);

        return new int[]{screenHeight, screenWidth};
    }


    public void onAttach(Context context) {
        super.onAttach(getActivity());

    }

    /**
     * reactiva el audio tras obtener feedback
     */
    protected void reactivarAudio() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sonido.play(getActivity());
            }
        }, Config.TIME_FEEDBACK);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.actividadPrincipal = (MainActivity) getActivity();
        this.actividadPrincipal.stopSong();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.respuestaCorrecta = null;
        this.respuestaUsuario = null;
        this.dbc = this.actividadPrincipal.getDataBaseController();
        this.sg = this.actividadPrincipal.getStateOfTheGame();
        this.feedbackImage = (ImageView) getActivity().findViewById(R.id.ivFeedback);
        this.feedBackSoundBien = new SoundPlayer(R.raw.feedback_bien_1);
        this.feedBackSoundMal = new SoundPlayer(R.raw.feedback_mal_1);


    }

    /**
     * Proporciona feedback al jugador
     *
     * @param is_correct
     */
    @Override
    public void feedBack(boolean is_correct) {
        Drawable resultIcon;
        SoundPlayer player;
        if (is_correct) {
            resultIcon = getResources().getDrawable(R.mipmap.ic_feedback_bien);
            player = this.feedBackSoundBien;
        } else {
            resultIcon = getResources().getDrawable(R.mipmap.ic_feedback_mal);
            player = this.feedBackSoundMal;
        }
        this.feedbackImage.setImageDrawable(resultIcon);

        player.play(getActivity(), false);
        this.feedbackImage.setVisibility(View.VISIBLE);


        /**
         * TODO: Aunque no es deseable realizar un Thread.sleep(10000);
         * el resultado utilizando handler es poco intuitivo para el usuario
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Generic_Game.this.feedbackImage.setVisibility(View.INVISIBLE);
            }
        }, Config.TIME_FEEDBACK);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (this.sonido != null && this.sonido.isPlaying()) {
            this.sonido.destroy();
            this.wasSoundPlaying = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.wasSoundPlaying) {
            this.sonido.play(getActivity());
            this.wasSoundPlaying = false;
        }

    }


}
