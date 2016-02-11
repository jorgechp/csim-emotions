package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import csim.csimemotions.Emotions;
import csim.csimemotions.Generic_Game;
import csim.csimemotions.R;
import csim.csimemotions.SoundPlayer;
import csim.csimemotions.States;
import csim.csimemotions.stageResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link F_Game2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_Game2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Game2 extends Generic_Game  {



    private OnFragmentInteractionListener mListener;


    private int numSonidos;
    private String[][] sonidos;
    private boolean isSoundPlaying;
    private String[][] imagenesHappy, imagenesSad, imagenesAngry, imagenesSurprised;
    private int numRowsHappy, numRowsSad, numRowsAngry, numRowsSurprised;
    private ImageButton ibHappy, ibSad, ibAngry, ibSuprised;
    private int stageNum;
    private ProgressBar progessb;
    private ImageButton ibPlayer;
    private TextView tvTitle;
    private TextView tvEEGTimer;


    public F_Game2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment F_Game2.
     */
    // TODO: Rename and change types and number of parameters
    public static F_Game2 newInstance() {
        F_Game2 fragment = new F_Game2();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isSoundPlaying = false;
        this.currentGame = States.GAME2;
        this.stageNum = 1;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        /**
         * DialogAlert se crea en Generic_game
         */
        super.dialogoAlerta.setMessage(getString(R.string.Game2_instructions));


        super.onActivityCreated(savedInstanceState);


        this.ibPlayer = (ImageButton) getActivity().findViewById(R.id.Game2_ibPlayer);

        this.ibHappy = (ImageButton) getActivity().findViewById(R.id.Game2_ibHappy);
        this.ibSad = (ImageButton) getActivity().findViewById(R.id.Game2_ibSad);
        this.ibSuprised = (ImageButton) getActivity().findViewById(R.id.Game2_ibSurprised);
        this.ibAngry = (ImageButton) getActivity().findViewById(R.id.Game2_ibAngry);
        F_Game2.this.setEnableButtons(false);

        this.progessb = (ProgressBar) getActivity().findViewById(R.id.Game2_pbProgress);
        this.progessb.setMax(super.maxNumStages);
        this.actualizarMarcador();

        this.procesarRespuesta(this.continueGame());
        this.ibPlayer.setBackgroundResource(R.mipmap.ic_play);
        this.ibPlayer.setScaleType(ImageView.ScaleType.FIT_XY);

        this.tvTitle = (TextView) getActivity().findViewById(R.id.game2TVTitle);
        this.tvEEGTimer = (TextView) getActivity().findViewById(R.id.Game2_Timer);


        clickListener = new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                int id = v.getId();
                boolean isUserEmotion = false;
                switch (id) {
                    case R.id.Game2_ibPlayer:
                        boolean isPreTime = maxTimePre != -1;
                        if (F_Game2.this.isSoundPlaying == false) {
                            F_Game2.super.sonido.play(getActivity());
                            F_Game2.this.isSoundPlaying = true;
                            F_Game2.this.setEnableButtons(true);
                            F_Game2.this.ibPlayer.setBackgroundResource(R.drawable.ic_pause_vector);
                            if(maxTimePost != -1 && !isPreTime){

                                F_Game2.super.cdTimerPost.start();
                            }else if(isPreTime){
                                F_Game2.this.setEnableButtons(false);
                                F_Game2.super.cdTimerPre.start();
                            }
                        } else {
                            if(!isPreTime) {
                                F_Game2.this.ibPlayer.setBackgroundResource(R.mipmap.ic_play);
                                F_Game2.this.isSoundPlaying = false;
                                F_Game2.super.sonido.pause();
                                F_Game2.super.cdTimerPost.cancel();
                            }
                        }


                        break;
                    case R.id.Game2_ibAngry:
                        F_Game2.this.respuestaUsuario = Emotions.ANGRY;
                        isUserEmotion = true;
                        break;
                    case R.id.Game2_ibHappy:
                        F_Game2.this.respuestaUsuario = Emotions.HAPPY;
                        isUserEmotion = true;
                        break;
                    case R.id.Game2_ibSad:
                        F_Game2.this.respuestaUsuario = Emotions.SAD;
                        isUserEmotion = true;
                        break;
                    case R.id.Game2_ibSurprised:
                        F_Game2.this.respuestaUsuario = Emotions.FEAR;
                        isUserEmotion = true;
                        break;
                }

                if (isUserEmotion) {
                    F_Game2.this.newStage();

                }
            }
        };


        ImageButton ib = (ImageButton) getActivity().findViewById(R.id.Game2_ibPlayer);
        ib.setOnClickListener(clickListener);
        this.ibHappy.setOnClickListener(clickListener);
        this.ibSuprised.setOnClickListener(clickListener);
        this.ibSad.setOnClickListener(clickListener);
        this.ibAngry.setOnClickListener(clickListener);

        Typeface tfTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Action_Man_Bold.ttf");
        this.tvTitle.setTypeface(tfTitle);

        this.contadorPre();
        this.contadorPost();

    }

    private void newStage() {
        this.procesarRespuesta(F_Game2.this.continueGame());
        this.isSoundPlaying = false;
        this.ibPlayer.setBackgroundResource(R.mipmap.ic_play);
        this.setEnableButtons(false);
    }

    private void setEnableButtons(boolean b) {
        int modo = View.INVISIBLE;
        if (b == true) {
            modo = View.VISIBLE;
        }
        this.ibAngry.setVisibility(modo);
        this.ibSuprised.setVisibility(modo);
        this.ibHappy.setVisibility(modo);
        this.ibSad.setVisibility(modo);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f__game2, container, false);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    protected void contadorPre() {
        super.contadorPre();
        this.setEnableButtons(false);


        super.cdTimerPre = new CountDownTimer(super.maxTimePre, 1000) {




            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                F_Game2.this.setEnableButtons(true);

                F_Game2.super.cdTimerPost.start();
            }
        };
    }



    @Override
    protected void contadorPost() {
        super.cdTimerPost = new CountDownTimer(super.maxTimePost, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                F_Game2.this.tvEEGTimer.setText(Long.toString(millisUntilFinished/1000));
            }

            public void onFinish() {
                F_Game2.super.respuestaUsuario = Emotions.NONE;
                F_Game2.this.stageNum++;
                F_Game2.this.actualizarMarcador();
                F_Game2.this.newStage();
                F_Game2.this.tvEEGTimer.setText(null);
            }
        };
    }



    public void onAttach(Context context) {
        super.onAttach(getActivity());

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Recoge la respuesta del usuario y prepara la siguiente fase del juego, devolviendo
     * un valor con el resultado del usuario.
     *
     * @return stageResults enumeracion
     */
    @Override
    public stageResults continueGame() {

        stageResults result = stageResults.GAME_STARTED;
        Random rnd = new Random();
        int newImage, newSound;
        String soundSelected[];
        String[] imageSelected;

        if (super.respuestaCorrecta == null || super.respuestaUsuario == null) {

            this.imagenesHappy = super.dbc.getUrlImagen(null, Emotions.HAPPY, 1); // 1 es la dificultad
            this.imagenesSad = super.dbc.getUrlImagen(null, Emotions.SAD, 1); // 1 es la dificultad
            this.imagenesAngry = super.dbc.getUrlImagen(null, Emotions.ANGRY, 1); // 1 es la dificultad
            this.imagenesSurprised = super.dbc.getUrlImagen(null, Emotions.FEAR, 1); // 1 es la dificultad

            this.numRowsHappy = super.dbc.getNumRowsImagenes(Emotions.HAPPY);
            this.numRowsSad = super.dbc.getNumRowsImagenes(Emotions.SAD);
            this.numRowsAngry = super.dbc.getNumRowsImagenes(Emotions.ANGRY);
            this.numRowsSurprised = super.dbc.getNumRowsImagenes(Emotions.FEAR);

            this.numSonidos = super.dbc.getNumRowsSonidos();
            this.sonidos = super.dbc.getUrlSonido(null, null);
            result = stageResults.GAME_STARTED;
        } else {
            super.saveStage();
            if (super.respuestaUsuario == super.respuestaCorrecta) {
                result = stageResults.PLAYER_WINS;
                ++this.stageNum;
            } else {
                result = stageResults.PLAYER_ERROR;
            }
            if (super.maxNumStages < this.stageNum) {
                result = stageResults.GAME_WON;
            }

        }

        newSound = rnd.nextInt(this.numSonidos);
        String[] sonidoEscogido = this.sonidos[newSound];
        int id = Integer.parseInt(sonidoEscogido[2]);
        sonido = new SoundPlayer(id);


        newImage = rnd.nextInt(this.numRowsHappy);
        imageSelected = this.imagenesHappy[newImage];
        super.loadImageOnVisor(imageSelected[0], this.ibHappy);

        newImage = rnd.nextInt(this.numRowsAngry);
        imageSelected = this.imagenesAngry[newImage];
        super.loadImageOnVisor(imageSelected[0], this.ibAngry);

        newImage = rnd.nextInt(this.numRowsSad);
        imageSelected = this.imagenesSad[newImage];
        super.loadImageOnVisor(imageSelected[0], this.ibSad);

        newImage = rnd.nextInt(this.numRowsSurprised);
        imageSelected = this.imagenesSurprised[newImage];
        super.loadImageOnVisor(imageSelected[0], this.ibSuprised);


        Emotions correctEmotion = this.getEmotionFromString(sonidoEscogido[1]);
        this.respuestaCorrecta = correctEmotion;


        return result;
    }




    /**
     * Procesa el resultado de la fase actual del juego
     *
     * @param respuesta
     */
    @Override
    public void procesarRespuesta(stageResults respuesta) {
        if(super.maxTimePost != -1 && super.cdTimerPost != null) {
            super.cdTimerPost.cancel();
        }
        if(super.maxTimePre != -1 && super.cdTimerPre != null) {
            super.cdTimerPre.cancel();
        }
        super.procesarRespuesta(respuesta);
        switch (respuesta) {
            case GAME_STARTED:  //Inicio del juego
                break;
            case GAME_WON: //Fin del juego


                break;
            case PLAYER_ERROR: //Respuesta incorrecta

                this.feedBack(false);
                break;
            case PLAYER_WINS:  //Respuesta correcta
                this.tvEEGTimer.setText(null);
                this.feedBack(true);
                this.actualizarMarcador();
                break;
        }
    }

    private void actualizarMarcador() {
        this.progessb.setProgress(this.stageNum);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }


}
