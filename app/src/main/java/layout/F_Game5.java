package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import csim.csimemotions.Emotions;
import csim.csimemotions.Generic_Game;
import csim.csimemotions.R;
import csim.csimemotions.SoundPlayer;
import csim.csimemotions.States;
import csim.csimemotions.stageResults;

//import android.widget.ProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link F_Game5.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_Game5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Game5 extends Generic_Game {


    private SensorManager senManager;
    private SensorEventListener seListener;
    private OnFragmentInteractionListener mListener;
    private View.OnClickListener ocl;

    private TextView ttTitle;
    private Emotions em1, em2, em3,em4;
    private ImageButton iv1, iv2,iv3,iv4;
    private ImageButton ivPlayer;
    // private ProgressBar progressBar;

    private byte speed;
    private ViewGroup.LayoutParams lp;
    private String[][] sonidos;
    private boolean isPlayerEnabled;

    private HashMap<ImageButton,Emotions> iButtonEmotionsRelations;
    private HashSet<Integer> listaSonidos;
    private ViewGroup layoutButton;
    private FrameLayout flScreen;
    private float screenSize[];

    private boolean isEEGPreTime;
    private boolean isResponseRegistered;

    public F_Game5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment F_Game5.
     */
    // TODO: Rename and change types and number of parameters
    public static F_Game5 newInstance() {
        F_Game5 fragment = new F_Game5();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void contadorPre() {
        super.contadorPre();

        if(this.actividadPrincipal.getTemporalStateGame().isEnableEEG()) {
            isEEGPreTime = true;
            setEnableButtons(false);
        }

        super.cdTimerPre = new CountDownTimer(maxTimePre, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                F_Game5.this.setEnableButtons(true);
                isEEGPreTime = false;
                F_Game5.super.cdTimerPost.start();
            }
        };
    }

    private void setEnableButtons(boolean isEnabled) {
        int modo = View.INVISIBLE;
        if (isEnabled == true) {
            modo = View.VISIBLE;
        }
        this.iv1.setVisibility(modo);
        this.iv2.setVisibility(modo);
        this.iv3.setVisibility(modo);
        this.iv4.setVisibility(modo);
    }

    @Override
    protected void contadorPost() {
        super.cdTimerPost = new CountDownTimer(super.maxTimePost, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

            }

            public void onFinish() {
                F_Game5.super.sonido.destroy();
                F_Game5.super.respuestaUsuario = Emotions.NONE;

                F_Game5.this.procesarRespuesta(F_Game5.this.continueGame());

                F_Game5.this.resetPosition();
                F_Game5.this.isPlayerEnabled = false;
                F_Game5.this.generateSong();
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.currentGame = States.GAME33;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f__game5, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.dialogoAlerta.setMessage(R.string.Game5_instructions);
        super.onActivityCreated(savedInstanceState);

        isResponseRegistered = false;


        this.isEEGPreTime = false;

        this.senManager = (SensorManager) super.actividadPrincipal.getSystemService(Context.SENSOR_SERVICE);




        this.iv1 = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_ib1);
        this.iv2 = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_ib2);
        this.iv3 = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_ib3);
        this.iv4 = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_ib4);

        this.ivPlayer = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_player);

        this.ocl = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = v.getId();
                boolean isEEG = F_Game5.this.actividadPrincipal.getTemporalStateGame().isEnableEEG();

                switch (id){
                    case R.id.Game5_player:
                        if(!F_Game5.this.isPlayerEnabled) {
                            F_Game5.super.sonido.play(F_Game5.super.actividadPrincipal);
                            F_Game5.this.isPlayerEnabled = true;
                            F_Game5.this.ivPlayer.setBackgroundResource(R.mipmap.ic_player_stop);
                            if(isEEG && F_Game5.super.cdTimerPre != null) {
                                F_Game5.this.setEnableButtons(false);
                                isEEGPreTime = true;
                                F_Game5.super.cdTimerPre.start();
                            }
                            else if(maxTimePre != -1){
                                F_Game5.super.cdTimerPre.start();
                            }else if(maxTimePost != -1){
                                F_Game5.super.cdTimerPost.start();
                            }
                        }else{
                            if(!isEEG) {
                                F_Game5.super.sonido.destroy();
                                F_Game5.this.isPlayerEnabled = false;
                                F_Game5.this.ivPlayer.setBackgroundResource(R.mipmap.ic_play);
                                F_Game5.super.cdTimerPre.cancel();
                                F_Game5.super.cdTimerPost.cancel();
                            }
                        }
                        break;


                }

            }
        };





        this.ttTitle = (TextView) super.actividadPrincipal.findViewById(R.id.Game5_tvTitle);

        // this.progressBar = (ProgressBar) super.actividadPrincipal.findViewById(R.id.Game5_progressBar);

        this.flScreen = (FrameLayout) super.actividadPrincipal.findViewById(R.id.Game5_flScreen);

        this.speed = (byte) (super.stageNumber + 2); //TODO cambiar esto por el nivel de dificultad del juego

        this.lp = this.ivPlayer.getLayoutParams();

        this.sonidos = super.dbc.getUrlSonido(null, null);
        this.listaSonidos = new HashSet<>();
        this.iButtonEmotionsRelations = new HashMap<ImageButton,Emotions>();
        this.iButtonEmotionsRelations.put(this.iv1,null);
        this.iButtonEmotionsRelations.put(this.iv2,null);
        this.iButtonEmotionsRelations.put(this.iv3,null);
        this.iButtonEmotionsRelations.put(this.iv4, null);

        this.seListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(F_Game5.this.isPlayerEnabled == false){
                    return;
                }

                float x = 0;
                float y = 0;
                if(Math.abs(event.values[0]) > 1){
                    x = (event.values[0]*F_Game5.this.speed);


                }

                if(Math.abs(event.values[1]) > 1) {
                    y =  (event.values[1]*F_Game5.this.speed) ;

                }


                float newX, newY;
                newX = (F_Game5.this.ivPlayer.getX()-x)%F_Game5.this.screenSize[1] ;
                if(newX < 0){
                    newX = F_Game5.this.screenSize[1];
                }

                newY = (F_Game5.this.ivPlayer.getY() + y)%F_Game5.this.screenSize[0];
                if(newY < 0){
                    newY = F_Game5.this.screenSize[0];
                }

                F_Game5.this.ivPlayer.setX(newX);
                F_Game5.this.ivPlayer.setY(newY);

                checkCollisions(F_Game5.this.ivPlayer);

            }

            private void checkCollisions(ImageButton ib) {
                if(F_Game5.this.actividadPrincipal.getTemporalStateGame().isEnableEEG() && isEEGPreTime){
                    return;
                }
                if(isResponseRegistered){
                    return;
                }
                boolean isCollision = false;
                int location[] = new int[2];
                ib.getLocationInWindow(location);
                if(Generic_Game.isPointInsideView(location[0],location[1],F_Game5.this.iv1)){
                    F_Game5.super.respuestaUsuario = F_Game5.this.iButtonEmotionsRelations.get(F_Game5.this.iv1);
                    isCollision = true;
                }else if(Generic_Game.isPointInsideView(location[0],location[1],F_Game5.this.iv2)){
                    F_Game5.super.respuestaUsuario = F_Game5.this.iButtonEmotionsRelations.get(F_Game5.this.iv2);
                    isCollision = true;
                }else if(Generic_Game.isPointInsideView(location[0],location[1],F_Game5.this.iv3)){
                    F_Game5.super.respuestaUsuario = F_Game5.this.iButtonEmotionsRelations.get(F_Game5.this.iv3);
                    isCollision = true;
                }else if(Generic_Game.isPointInsideView(location[0],location[1],F_Game5.this.iv4)){
                    F_Game5.super.respuestaUsuario = F_Game5.this.iButtonEmotionsRelations.get(F_Game5.this.iv4);
                    isCollision = true;
                }

                if(isCollision){
                    isResponseRegistered = true;
                    F_Game5.this.procesarRespuesta(F_Game5.this.continueGame());

                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        this.senManager.registerListener(this.seListener, this.senManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        this.isPlayerEnabled = false;
        this.ivPlayer.setOnClickListener(this.ocl);

        this.sonidos = super.dbc.getUrlSonido(null,null);

        Typeface tfTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Action_Man_Bold.ttf");
        this.ttTitle.setTypeface(tfTitle);
        // this.progressBar.setMax(maxNumStages);
        // this.progressBar.setProgress(0);
        this.procesarRespuesta(this.continueGame());
        this.layoutButton = (ViewGroup) this.ivPlayer.getParent();
        this.screenSize = new float[2];
        this.layoutButton.post(new Runnable() {
            @Override
            public void run() {
                F_Game5.this.screenSize[0] = F_Game5.this.flScreen.getMeasuredHeight();
                F_Game5.this.screenSize[1] = F_Game5.this.flScreen.getMeasuredWidth();
                try {
                    super.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        this.contadorPre();
        this.contadorPost();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public stageResults continueGame() {
        stageResults sr = null;

        if(super.respuestaCorrecta == null){
            sr = stageResults.GAME_STARTED;
        }else{

            if(super.respuestaCorrecta == super.respuestaUsuario){
                sr = stageResults.PLAYER_WINS;



            }else{
                sr = stageResults.PLAYER_ERROR;
            }
            if(super.stageNumber >= maxNumStages){

                super.saveStage();

                sr = stageResults.GAME_WON;
            }
        }

        return sr;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void procesarRespuesta(stageResults respuesta) {

        boolean isCollision = false;
        boolean isCancelar = true;
        switch (respuesta){
            case GAME_WON:
                super.sonido.destroy();
                super.procesarRespuesta(respuesta);
                isCollision = true;
                break;
            case PLAYER_WINS:
                super.procesarRespuesta(respuesta);
                super.feedBackSoundBien.play(super.actividadPrincipal, false);
                processSelection();
                isCollision = true;
                break;
            case PLAYER_ERROR:
                isCollision = true;
                if(this.actividadPrincipal.getTemporalStateGame().isEnableEEG()) {
                    processSelection();
                    break;
                }
                isResponseRegistered = false;
                isCancelar = false;
                break;
            case GAME_STARTED:
                this.generateUI();
                this.generateSong();
                break;
            case USER_EXIT:

                super.procesarRespuesta(respuesta);
                if(feedBackSoundBien != null) {
                    super.feedBackSoundBien.destroy();
                }
                break;
        }

        if(this.actividadPrincipal.getTemporalStateGame().isEnableEEG() && super.cdTimerPost != null && isCancelar) {
            super.cdTimerPost.cancel();

        }
        if(super.cdTimerPost != null ){
            super.cdTimerPost.cancel();
        }

        if(super.cdTimerPre != null ){
            super.cdTimerPre.cancel();
        }



    }

    private void processSelection(){
        super.sonido.destroy();



        ++super.stageNumber;
        //  this.progressBar.setProgress(this.progressBar.getProgress() + 1);



        this.resetPosition();

        this.isPlayerEnabled = false;
        this.generateUI();
        this.generateSong();
        isResponseRegistered = false;
    }
    private void resetPosition(){

        if(null!=this.layoutButton) { //for safety only  as you are doing onClick
            this.layoutButton.removeView(this.ivPlayer);
            this.ivPlayer = new ImageButton(super.actividadPrincipal);
            this.ivPlayer.setBackgroundResource(R.mipmap.ic_play);
            this.layoutButton.addView(this.ivPlayer);
            this.ivPlayer.setLayoutParams(this.lp);
            this.ivPlayer.setId(R.id.Game5_player);
            this.ivPlayer.setOnClickListener(this.ocl);

        }
    }

    private void generateSong() {
        Random rnd = new Random();
        int size = this.sonidos.length;
        String sonidoSeleccionado[] = this.sonidos[rnd.nextInt(size)];

        super.sonido = new SoundPlayer(Integer.parseInt(sonidoSeleccionado[2]));
        super.respuestaCorrecta = super.getEmotionFromString(sonidoSeleccionado[1]);

    }

    private void generateUI() {
        Random rnd = new Random();
        ArrayList<ImageButton> listaIm = new ArrayList<>();
        listaIm.add(this.iv1);
        listaIm.add(this.iv2);
        listaIm.add(this.iv3);
        listaIm.add(this.iv4);


        int selectedView = rnd.nextInt(listaIm.size());
        ImageButton selectedButton = listaIm.get(selectedView);
        super.loadImageOnVisor(super.imHappy[rnd.nextInt(super.imHappy.length)][0],selectedButton , 0.15f, 0.08f);
        listaIm.remove(selectedView);
        this.iButtonEmotionsRelations.put(selectedButton, Emotions.HAPPY);

        selectedView = rnd.nextInt(listaIm.size());
        selectedButton = listaIm.get(selectedView);
        super.loadImageOnVisor(super.imSurprised[rnd.nextInt(super.imSurprised.length)][0], listaIm.get(selectedView), 0.15f, 0.08f);
        listaIm.remove(selectedView);
        this.iButtonEmotionsRelations.put(selectedButton, Emotions.FEAR);

        selectedView = rnd.nextInt(listaIm.size());
        selectedButton = listaIm.get(selectedView);
        super.loadImageOnVisor(super.imSad[rnd.nextInt(super.imSad.length)][0], listaIm.get(selectedView), 0.15f, 0.08f);
        listaIm.remove(selectedView);
        this.iButtonEmotionsRelations.put(selectedButton, Emotions.SAD);

        selectedView = rnd.nextInt(listaIm.size());
        selectedButton = listaIm.get(selectedView);
        super.loadImageOnVisor(super.imAngry[rnd.nextInt(super.imAngry.length)][0], listaIm.get(selectedView), 0.15f, 0.08f);
        listaIm.remove(selectedView);
        this.iButtonEmotionsRelations.put(selectedButton, Emotions.ANGRY);






    }


}
