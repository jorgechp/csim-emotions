package csim.csimemotions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import csim.csimemotions.log.Log;
import csim.csimemotions.log.LogManager;
import csim.csimemotions.log.LogStage;
import layout.FCenterContent;

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
    protected SoundPlayer feedBackSoundBien, finalVictory;
    protected String[][] imHappy, imSad, imAngry, imSurprised;
    protected LogManager logMan;
    protected Log logSession;
    private LogStage logStage;
    private int gamePoints;

    protected int stageNumber;
    private boolean wasSoundPlaying;
    protected AlertDialog.Builder dialogoAlerta;
    protected boolean isDialogAlert;
    protected CountDownTimer cdTimerPre, cdTimerPost;
    protected long maxTimePre;
    protected long maxTimePost;
    protected int maxNumStages;


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
            case "FEAR":
                correctEmotion = Emotions.FEAR;
                break;
            case "NONE":
                correctEmotion = Emotions.NONE;
                break;
        }

        return correctEmotion;

    }

    /**
     * Define el objeto contadorPost que se debe ejecutar cuando existe una cuenta atrás
     * (modo EEG, nivel de dificultad con tiempo...)
     */
    protected abstract void contadorPost();

    /**
     * Define el objeto contador que se debe ejecutar previamente a la cuenta atrás
     * en el modo EEG.
     */
    protected void contadorPre(){
        if(! this.actividadPrincipal.getTemporalStateGame().isEnableEEG()){
            return;
        }
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
        this.gamePoints = 0;
        this.actividadPrincipal = (MainActivity) getActivity();
        this.actividadPrincipal.stopSong();
        this.generarDificultad();
        this.isDialogAlert = true;
        this.dialogoAlerta = new AlertDialog.Builder(this.actividadPrincipal);
        this.dialogoAlerta.setNeutralButton(getString(R.string.GameDialog_OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(Generic_Game.this.actividadPrincipal, getString(R.string.GameDialog_letsPlay), Toast.LENGTH_SHORT).show();
            }
        });

        this.dialogoAlerta.setPositiveButton(getString(R.string.GameDialog_OKSave), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(Generic_Game.this.actividadPrincipal, getString(R.string.GameDialog_letsPlay), Toast.LENGTH_SHORT).show();
                actividadPrincipal.getStateOfTheGame().addNoHelpDialog(currentGame);
            }
        });

        this.dialogoAlerta.setIcon(R.mipmap.ic_gamealert_info);
        this.dialogoAlerta.setTitle(R.string.GameDialog_title);


    }

    /**
     * Establece el tiempo límite de cada juego
     */
    protected void generarDificultad(){
        StateOfGame sog = this.actividadPrincipal.getStateOfTheGame();
        int dificultad = sog.getLevelActual();
        long timePre = -1;
        long timePos = Config.LEVEL_0_TIME;
        int numStages = Config.LEVEL_0_NUM_OF_STAGES;

        switch (dificultad){
            case 2:
                timePos = Config.LEVEL_1_TIME;
                numStages = Config.LEVEL_1_NUM_OF_STAGES;
                break;
            case 3:
                timePos = Config.LEVEL_2_TIME;
                numStages = Config.LEVEL_2_NUM_OF_STAGES;
                break;

        }

        if(actividadPrincipal.getTemporalStateGame().isEnableEEG()){
            timePre = Config.EEG_MODE_PRE_TIME;
            timePos = Config.EEG_MODE_TIME;
            numStages = Config.LEVEL_EEG_NUM_OF_STAGES;
        }

        this.maxTimePre = timePre;
        this.maxTimePost = timePos;
        this.maxNumStages = numStages;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.actividadPrincipal.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.respuestaCorrecta = null;
        this.respuestaUsuario = null;
        this.dbc = this.actividadPrincipal.getDataBaseController();
        this.sg = this.actividadPrincipal.getStateOfTheGame();
        this.logMan = this.actividadPrincipal.getLogMan();
        this.feedbackImage = (ImageView) getActivity().findViewById(R.id.ivFeedback);
        this.feedBackSoundBien = new SoundPlayer(R.raw.feedback_bien_1);
        this.finalVictory = new SoundPlayer(R.raw.feedback_victory);


        this.imHappy = this.dbc.getUrlImagen(null, Emotions.HAPPY, actividadPrincipal.getStateOfTheGame().getLevelActual());
        this.imSad = this.dbc.getUrlImagen(null,Emotions.SAD,actividadPrincipal.getStateOfTheGame().getLevelActual());
        this.imAngry = this.dbc.getUrlImagen(null,Emotions.ANGRY,actividadPrincipal.getStateOfTheGame().getLevelActual());
        this.imSurprised = this.dbc.getUrlImagen(null,Emotions.FEAR,actividadPrincipal.getStateOfTheGame().getLevelActual());


        this.logSession = new Log(this.actividadPrincipal.getUserConf().getUserName(),System.currentTimeMillis(),this.actividadPrincipal.getTemporalStateGame().isEnableEEG());
        this.stageNumber = 1;


        //if(this.isDialogAlert && !this.actividadPrincipal.getStateOfTheGame().isDialogNoHelp(currentGame)) {
            //this.dialogoAlerta.show();
        //}




    }

    /**
     * Proporciona feedback al jugador
     *
     * @param is_correct
     */
    @Override
    public void feedBack(boolean is_correct) {
        Drawable resultIcon;
        SoundPlayer player = null;
        if (is_correct) {
            resultIcon = getResources().getDrawable(R.mipmap.ic_feedback_bien);
            player = this.feedBackSoundBien;
            player.play(getActivity(), false);
        } else {
            resultIcon = getResources().getDrawable(R.mipmap.ic_feedback_mal);

        }
        this.feedbackImage.setImageDrawable(resultIcon);


        this.feedbackImage.setVisibility(View.VISIBLE);


        /**
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

    protected void saveStage(){
        if(this.actividadPrincipal.getTemporalStateGame().isEnableLogging()) {
            this.logStage = new LogStage(this.respuestaCorrecta, actividadPrincipal.getStateOfTheGame().getLevelActual(), this.currentGame, System.currentTimeMillis(), this.respuestaUsuario, this.stageNumber);
            this.logSession.addStage(this.logStage);
        }
    }

    public void procesarRespuesta(stageResults respuesta) {


        if (this.sonido != null && this.sonido.isPlaying()) {
            this.sonido.destroy();
        }
        switch (respuesta) {
            case GAME_WON:
                this.finalVictory.play(this.actividadPrincipal,false);
                this.finalVictory.destroy();
                this.stageNumber++;
                this.sg.chargeReward(this.currentGame, actividadPrincipal.getStateOfTheGame().getLevelActual());
                this.actividadPrincipal.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                this.gamePoints += Config.GAME_POINTS;
                exitGame();
               // actividadPrincipal.getfCenter().processateArcadeMode();
                break;
            case PLAYER_WINS:
                this.stageNumber++;
                this.gamePoints += Config.STAGE_POINTS * (1 + actividadPrincipal.getStateOfTheGame().getLevelActual());
                break;
            case PLAYER_ERROR:
                this.stageNumber++;
                break;
            case USER_EXIT:
                this.gamePoints -= Config.GAME_POINTS;
                exitGame();
                break;

        }
    }

    private void exitGame(){
        if (this.sonido != null) {
            this.sonido.destroy();
        }
        if(this.actividadPrincipal.getTemporalStateGame().isEnableLogging()) {

            this.logMan.addLog(this.logSession);
            this.logMan.save();
        }
        this.respuestaUsuario = null;

        FCenterContent fg = this.actividadPrincipal.getfCenter();
        this.actividadPrincipal.getfUp().setGameMode(false);
        this.actividadPrincipal.playSong();
        this.actividadPrincipal.saveUserConfig();
        this.actividadPrincipal.setCurrentGame(null);
        this.actividadPrincipal.getUserConf().changePoints(this.gamePoints);

        fg.checkUI();

        FragmentTransaction fManagerTransaction = getFragmentManager().beginTransaction();
        //fManagerTransaction.replace(this.getId(), fg);
        fManagerTransaction.remove(this);
        fManagerTransaction.show(fg);
        actividadPrincipal.getfCenter().processateArcadeMode();
        fManagerTransaction.commit();

    }

    protected boolean loadImageOnVisor(String imageSelected, ImageButton ib) {

        return this.loadImageOnVisor(imageSelected,ib,0.55f,0.20f);
    }
    protected boolean loadImageOnVisor(String imageSelected, ImageButton ib, float sizeX, float sizeY) {

        boolean resultado = true;

        InputStream ims;
        Drawable d;

        if (ib != null) {
            try {
                ims = getActivity().getAssets().open("imagesEmotion/" + imageSelected);
                d = Drawable.createFromStream(ims, null);
                ib.setImageDrawable(d);

                //Redimensiona adecuadamente la imagen
                int size[] = this.setSizeOfImage(sizeX, sizeY);
                ib.getLayoutParams().height = size[0];
                ib.getLayoutParams().width = size[1];
            } catch (IOException e) {
                e.printStackTrace();
                resultado = false;
            }
        }
        return resultado;
    }

    /**
     * Determines if given points are inside view
     * @param x - x coordinate of point
     * @param y - y coordinate of point
     * @param view - view object to compare
     * @return true if the points are within view bounds, false otherwise
     *
     * http://stackoverflow.com/questions/4165765/how-to-get-a-view-from-an-event-coordinates-in-android
     */
    public static boolean isPointInsideView(float x, float y, View view){
        int location[] = new int[2];
        view.getLocationInWindow(location);
        float viewX = view.getX();
        float viewY = location[1];

        //point is inside view bounds

        float limiteSuperiorX = viewX + view.getMeasuredWidth();
        float limiteSuperiorY = viewY + view.getMeasuredHeight();
        if( (x > viewX) && (x < limiteSuperiorX)){
            if((y > viewY) && (y < limiteSuperiorY)){
                return true;
            }
        }



        return false;

    }

    public void backButtonPressed(){
        this.actividadPrincipal.getfCenter().emptyArcadeQueue();
        this.procesarRespuesta(stageResults.USER_EXIT);
    }
}
