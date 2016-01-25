package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import csim.csimemotions.Config;
import csim.csimemotions.Emotions;
import csim.csimemotions.Generic_Game;
import csim.csimemotions.R;
import csim.csimemotions.SoundPlayer;
import csim.csimemotions.States;
import csim.csimemotions.stageResults;

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
    private ProgressBar progressBar;
    private int stagesCompleted;
    private byte speed;
    private ViewGroup.LayoutParams lp;
    private String[][] sonidos;
    private boolean isPlayerEnabled;

    private HashMap<ImageButton,Emotions> iButtonEmotionsRelations;
    private HashSet<Integer> listaSonidos;
    private ViewGroup layoutButton;
    private FrameLayout flScreen;
    private float screenSize[];

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        super.onActivityCreated(savedInstanceState);
        super.actividadPrincipal.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.currentGame = States.GAME33;
        this.senManager = (SensorManager) super.actividadPrincipal.getSystemService(Context.SENSOR_SERVICE);


        this.ocl = new View.OnClickListener() {
            boolean isActivo = false;
            @Override
            public void onClick(View v) {
                int id = v.getId();

                switch (id){
                    case R.id.Game5_player:
                        if(!isActivo) {
                            F_Game5.super.sonido.play(F_Game5.super.actividadPrincipal);
                            isActivo = true;
                            F_Game5.this.isPlayerEnabled = true;
                            F_Game5.this.ivPlayer.setImageResource(R.mipmap.ic_player_stop);
                        }else{
                            F_Game5.super.sonido.destroy();
                            isActivo = false;
                            F_Game5.this.isPlayerEnabled = false;
                            F_Game5.this.ivPlayer.setImageResource(R.mipmap.ic_play);
                        }
                        break;
                }
            }
        };



        this.iv1 = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_ib1);
        this.iv2 = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_ib2);
        this.iv3 = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_ib3);
        this.iv4 = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_ib4);

        this.ivPlayer = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game5_player);

        this.ttTitle = (TextView) super.actividadPrincipal.findViewById(R.id.Game5_tvTitle);

        this.progressBar = (ProgressBar) super.actividadPrincipal.findViewById(R.id.Game5_progressBar);

        this.flScreen = (FrameLayout) super.actividadPrincipal.findViewById(R.id.Game5_flScreen);
        this.stagesCompleted = 0;
        this.speed = (byte) (this.stagesCompleted + 2); //TODO cambiar esto por el nivel de dificultad del juego

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
                    x = (event.values[0]*F_Game5.this.speed);//%F_Game5.this.screenSize[0] ;
//                    if(x < 0){
//                        x = F_Game5.this.screenSize[0] -x;
//                    }

                }

                if(Math.abs(event.values[1]) > 1) {
                    y =  (event.values[1]*F_Game5.this.speed);//%F_Game5.this.screenSize[1] ;
//                    if(y < 0){
//                        y = F_Game5.this.screenSize[1] -y;
//                    }
                }


                F_Game5.this.ivPlayer.setX(F_Game5.this.ivPlayer.getX()-x);
                F_Game5.this.ivPlayer.setY(F_Game5.this.ivPlayer.getY() + y);

                checkCollisions(F_Game5.this.ivPlayer);

            }

            private void checkCollisions(ImageButton ib) {
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
        this.progressBar.setMax(Config.LEVEL_0_NUM_OF_STAGES);
        this.progressBar.setProgress(0);
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
                ++this.stagesCompleted;
                if(this.stagesCompleted < Config.LEVEL_0_NUM_OF_STAGES){
                    sr = stageResults.PLAYER_WINS;
                }else{
                    sr = stageResults.GAME_WON;
                }
            }else{
                sr = stageResults.PLAYER_ERROR;
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
        switch (respuesta){
            case GAME_WON:
                super.procesarRespuesta(respuesta);
                break;
            case PLAYER_WINS:
                super.sonido.destroy();
                super.feedBackSoundBien.play(super.actividadPrincipal, false);

                this.generateUI();
                this.generateSong();
                this.progressBar.setProgress(this.stagesCompleted);


                if(null!=this.layoutButton) { //for safety only  as you are doing onClick
                    this.layoutButton.removeView(this.ivPlayer);
                    this.ivPlayer = new ImageButton(super.actividadPrincipal);
                    this.ivPlayer.setBackgroundResource(R.mipmap.ic_play);
                    this.layoutButton.addView(this.ivPlayer);
                    this.ivPlayer.setLayoutParams(this.lp);
                    this.ivPlayer.setId(R.id.Game5_player);
                    this.ivPlayer.setOnClickListener(this.ocl);

                }
                this.isPlayerEnabled = false;
                break;
            case PLAYER_ERROR:

                break;
            case GAME_STARTED:
                this.generateUI();
                this.generateSong();
                break;
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
        this.iButtonEmotionsRelations.put(selectedButton, Emotions.SURPRISED);

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
