package layout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import csim.csimemotions.Config;
import csim.csimemotions.Emotions;
import csim.csimemotions.Generic_Game;
import csim.csimemotions.IGame;
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
public class F_Game2 extends Generic_Game implements IGame {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters


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
        super.onActivityCreated(savedInstanceState);

        this.ibPlayer = (ImageButton) getActivity().findViewById(R.id.Game2_ibPlayer);

        this.ibHappy = (ImageButton) getActivity().findViewById(R.id.Game2_ibHappy);
        this.ibSad = (ImageButton) getActivity().findViewById(R.id.Game2_ibSad);
        this.ibSuprised = (ImageButton) getActivity().findViewById(R.id.Game2_ibSurprised);
        this.ibAngry = (ImageButton) getActivity().findViewById(R.id.Game2_ibAngry);
        F_Game2.this.setEnableButtons(false);

        this.progessb = (ProgressBar) getActivity().findViewById(R.id.Game2_pbProgress);
        this.progessb.setMax(Config.LEVEL_0_NUM_OF_STAGES);
        this.actualizarMarcador();

        this.procesarRespuesta(this.continueGame());
        this.ibPlayer.setImageResource(R.mipmap.ic_play);
        this.ibPlayer.setScaleType(ImageView.ScaleType.FIT_XY);

        clickListener = new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                int id = v.getId();
                boolean isUserEmotion = false;
                switch (id) {
                    case R.id.Game2_ibPlayer:
                        if (F_Game2.this.isSoundPlaying == false) {
                            F_Game2.super.sonido.play(getActivity());
                            F_Game2.this.isSoundPlaying = true;
                            F_Game2.this.setEnableButtons(true);
                            F_Game2.this.ibPlayer.setImageResource(R.drawable.ic_pause_vector);
                        } else {
                            F_Game2.this.ibPlayer.setImageResource(R.mipmap.ic_play);
                            F_Game2.this.isSoundPlaying = false;
                            F_Game2.super.sonido.pause();
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
                        F_Game2.this.respuestaUsuario = Emotions.SURPRISED;
                        isUserEmotion = true;
                        break;
                }

                if (isUserEmotion) {
                    F_Game2.this.procesarRespuesta(F_Game2.this.continueGame());
                    F_Game2.this.isSoundPlaying = false;
                    F_Game2.this.ibPlayer.setImageResource(R.mipmap.ic_play);
                    F_Game2.this.setEnableButtons(false);
                }
            }
        };


        ImageButton ib = (ImageButton) getActivity().findViewById(R.id.Game2_ibPlayer);
        ib.setOnClickListener(clickListener);
        this.ibHappy.setOnClickListener(clickListener);
        this.ibSuprised.setOnClickListener(clickListener);
        this.ibSad.setOnClickListener(clickListener);
        this.ibAngry.setOnClickListener(clickListener);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
            this.imagenesSurprised = super.dbc.getUrlImagen(null, Emotions.SURPRISED, 1); // 1 es la dificultad

            this.numRowsHappy = super.dbc.getNumRowsImagenes(Emotions.HAPPY);
            this.numRowsSad = super.dbc.getNumRowsImagenes(Emotions.SAD);
            this.numRowsAngry = super.dbc.getNumRowsImagenes(Emotions.ANGRY);
            this.numRowsSurprised = super.dbc.getNumRowsImagenes(Emotions.SURPRISED);

            this.numSonidos = super.dbc.getNumRowsSonidos();
            this.sonidos = super.dbc.getUrlSonido(null, null);
            result = stageResults.GAME_STARTED;
        } else {
            if (super.respuestaUsuario == super.respuestaCorrecta) {
                result = stageResults.PLAYER_WINS;
                ++this.stageNum;
                if (Config.LEVEL_0_NUM_OF_STAGES < this.stageNum) {
                    result = stageResults.GAME_WON;
                }
            } else {
                result = stageResults.PLAYER_ERROR;
            }
            super.respuestaUsuario = null;
        }

        newSound = rnd.nextInt(this.numSonidos);
        String[] sonidoEscogido = this.sonidos[newSound];
        int id = Integer.parseInt(sonidoEscogido[2]);
        sonido = new SoundPlayer(id);


        newImage = rnd.nextInt(this.numRowsHappy);
        imageSelected = this.imagenesHappy[newImage];
        this.loadImageOnVisor(imageSelected[0], this.ibHappy);

        newImage = rnd.nextInt(this.numRowsAngry);
        imageSelected = this.imagenesAngry[newImage];
        this.loadImageOnVisor(imageSelected[0], this.ibAngry);

        newImage = rnd.nextInt(this.numRowsSad);
        imageSelected = this.imagenesSad[newImage];
        this.loadImageOnVisor(imageSelected[0], this.ibSad);

        newImage = rnd.nextInt(this.numRowsSurprised);
        imageSelected = this.imagenesSurprised[newImage];
        this.loadImageOnVisor(imageSelected[0], this.ibSuprised);


        Emotions correctEmotion = this.getEmotionFromString(sonidoEscogido[1]);
        this.respuestaCorrecta = correctEmotion;


        return result;
    }

    private boolean loadImageOnVisor(String imageSelected, ImageButton ib) {

        boolean resultado = true;

        InputStream ims;
        Drawable d;

        if (ib != null) {
            try {
                ims = getActivity().getAssets().open("imagesEmotion/" + imageSelected);
                d = Drawable.createFromStream(ims, null);
                ib.setImageDrawable(d);

                //Redimensiona adecuadamente la imagen
                int size[] = this.setSizeOfImage((float) 0.55, (float) 0.20);
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
     * Procesa el resultado de la fase actual del juego
     *
     * @param respuesta
     */
    @Override
    public void procesarRespuesta(stageResults respuesta) {
        switch (respuesta) {
            case GAME_STARTED:  //Inicio del juego
                break;
            case GAME_WON: //Fin del juego
                if (super.sonido != null) {
                    super.sonido.destroy();
                }
                F_Game2.this.sg.chargeReward(this.currentGame);
                FCenterContent fg = this.actividadPrincipal.getfCenter();
                this.actividadPrincipal.getfUp().setGameMode(false);
                fg.checkUI();

                FragmentTransaction fManagerTransaction = getFragmentManager().beginTransaction();
                //fManagerTransaction.replace(this.getId(), fg);
                fManagerTransaction.remove(this);
                fManagerTransaction.show(fg);
                fManagerTransaction.commit();


                break;
            case PLAYER_ERROR: //Respuesta incorrecta
                this.feedBack(false);
                break;
            case PLAYER_WINS:  //Respuesta correcta
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}