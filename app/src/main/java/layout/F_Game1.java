package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import csim.csimemotions.Config;
import csim.csimemotions.DataBaseController;
import csim.csimemotions.Emotions;
import csim.csimemotions.Generic_Game;
import csim.csimemotions.MainActivity;
import csim.csimemotions.R;
import csim.csimemotions.SoundPlayer;
import csim.csimemotions.StateOfGame;
import csim.csimemotions.States;
import csim.csimemotions.log.LogManager;
import csim.csimemotions.stageResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link F_Game1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_Game1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Game1  extends Generic_Game {

    private DataBaseController dbc;



    private ImageView IV;

    private Emotions respuestaUsuario;
    private Emotions respuestaCorrecta;

    private OnFragmentInteractionListener mListener;
    private int numRows;
    private String[][] imagenes;
    private Map<Integer, Boolean> correctImages;
    private int indexOfCurrentImage;

    private Button bHappy, bSad, bAngry, btFear;
    private View.OnClickListener clickListener;
    private ImageView feedbackImage;



    private StateOfGame sg;

    private LogManager logMan;


    /**
     * Muestra el estado actual de este juego en la lista de estados.
     */
    private States currentGame = States.GAME1;


    /**
     * Maneja el contadorPost de la interfaz
     */
    private TextView marcador;
    private boolean wasSoundPlaying;

    public F_Game1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment F_Game1.
     */

    public static F_Game1 newInstance() {
        F_Game1 fragment = new F_Game1();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.currentGame = States.GAME1;
        super.respuestaUsuario = null;
        super.respuestaCorrecta = null;
        this.wasSoundPlaying = false;

        this.dbc = new DataBaseController("Imagenes", "Sonidos", getActivity());
        this.logMan = ((MainActivity)getActivity()).getLogMan();

        this.clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.btAngry:
                        F_Game1.super.respuestaUsuario = Emotions.ANGRY;
                        break;
                    case R.id.Game1_btFear:
                        F_Game1.super.respuestaUsuario = Emotions.FEAR;
                        break;
                    case R.id.btHappy:
                        F_Game1.super.respuestaUsuario = Emotions.HAPPY;
                        break;
                    case R.id.btSad:
                        F_Game1.super.respuestaUsuario = Emotions.SAD;
                        break;
                }


                stageResults respuesta = F_Game1.this.continueGame();
                F_Game1.this.desactivarInferfaz();
                F_Game1.this.procesarRespuesta(respuesta);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        F_Game1.this.activarInterfaz();
                    }
                }, Config.TIME_FEEDBACK + Config.TIME_IMAGES);


            }

        };


    }


    @Override
    protected void contadorPost() {

    }

    private void setButtonsEnabled(boolean enabled) {
        this.bAngry.setEnabled(enabled);
        this.bSad.setEnabled(enabled);
        this.bHappy.setEnabled(enabled);
        this.btFear.setEnabled(enabled);

        if(!enabled) {
            this.bAngry.setVisibility(View.INVISIBLE);
            this.bSad.setVisibility(View.INVISIBLE);
            this.bHappy.setVisibility(View.INVISIBLE);
            this.btFear.setVisibility(View.INVISIBLE);
        }else{
            this.bAngry.setVisibility(View.VISIBLE);
            this.bSad.setVisibility(View.VISIBLE);
            this.bHappy.setVisibility(View.VISIBLE);
            this.btFear.setVisibility(View.VISIBLE);
        }
    }

    private void desactivarInferfaz() {
        setButtonsEnabled(false);
        this.IV.setVisibility(View.INVISIBLE);
    }

    private void activarInterfaz() {
        setButtonsEnabled(true);
        this.IV.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.dialogoAlerta.setMessage(R.string.Game1_instructions);
        super.onActivityCreated(savedInstanceState);


        this.continueGame();

        this.bAngry = (Button) getActivity().findViewById(R.id.btAngry);
        this.btFear = (Button) getActivity().findViewById(R.id.Game1_btFear);
        this.bHappy = (Button) getActivity().findViewById(R.id.btHappy);
        this.bSad = (Button) getActivity().findViewById(R.id.btSad);
        this.feedbackImage = (ImageView) getActivity().findViewById(R.id.ivFeedback);

        this.IV = (ImageView) getActivity().findViewById(R.id.imVisor);

        // this.marcador = (TextView) getActivity().findViewById(R.id.Game1Marcador);

        this.bAngry.setOnClickListener(this.clickListener);
        this.btFear.setOnClickListener(this.clickListener);
        this.bHappy.setOnClickListener(this.clickListener);
        this.bSad.setOnClickListener(this.clickListener);


        TextView tvtitle = (TextView) getActivity().findViewById(R.id.game1TVTitle);
        Typeface tfTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Action_Man_Bold.ttf");
        tvtitle.setTypeface(tfTitle);

        Typeface tfFontsButtons = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AndikaNewBasic-B.ttf");
        this.bAngry.setTypeface(tfFontsButtons);
        this.btFear.setTypeface(tfFontsButtons);
        this.bHappy.setTypeface(tfFontsButtons);
        this.bSad.setTypeface(tfFontsButtons);


        // this.marcador.setText("0 / " + super.maxNumStages);
        // this.marcador.setTypeface(tfFontsButtons);

        super.feedBackSoundBien = new SoundPlayer(R.raw.feedback_bien_1);


        //super.sonido.play(getActivity());


        super.actividadPrincipal.stopSong();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f__game1, container, false);
    }


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


    /**
     * Continua el juego hasta su siguiente fase
     * <p/>
     * si el valor de retorno es 0, el juego continua
     * Si el valor de retorno es 1, se ha producido un acierto
     * Si el valor de retorno es -1, se ha producido una respuesta incorrecta
     * Si el valor de retorno es -2, el juego ha finalizado.
     *
     * @return int
     */
    public stageResults continueGame() {
        stageResults result = stageResults.GAME_STARTED;
        Random rnd = new Random();
        int newImage, newSound;
        String imageSelected[];
        boolean resMap;


        //Comprobar si ha empezado el juego


        if (super.respuestaCorrecta == null || super.respuestaUsuario == null) {

            this.imagenes = this.dbc.getUrlImagen(null, null, actividadPrincipal.getStateOfTheGame().getLevelActual());
            this.numRows = imagenes.length;
            this.correctImages = new HashMap<Integer, Boolean>();
        } else {
            super.saveStage();
            if (super.respuestaCorrecta == super.respuestaUsuario) {
                this.correctImages.put(this.indexOfCurrentImage, true);
                result = stageResults.PLAYER_WINS;
            } else {
                result = stageResults.PLAYER_ERROR;
            }
//            sonido.destroy();
        }


        //Se comprueba si el juego ha finalizado
        if (this.numRows > super.stageNumber && super.maxNumStages > super.stageNumber) {
            //Si no ha finalizado, se selecciona aleatoriamente una nueva imagen

            do {
                newImage = rnd.nextInt(this.numRows);
            } while (correctImages.containsKey(newImage) && correctImages.get(newImage) == true);

            imageSelected = this.imagenes[newImage];
            this.loadImageOnVisor(imageSelected[0]);

            //Establecer nuevos marcadores
            Emotions correctEmotion = this.getEmotionFromString(imageSelected[1]);
            super.respuestaCorrecta = correctEmotion;
            this.indexOfCurrentImage = newImage;


            //Ahora, se elige aleatoriamente una imagen cuya emocion coincida con la de la imagen
            //elegida

     //       int numSonidos = this.dbc.getNumRowsSonidos(correctEmotion);
     //       newSound = rnd.nextInt(numSonidos);

            /**
             * como la lista de sonidos a descargar siempre depende de la emocion escogida en la imagen
             * no podemos descargar toda la lista y almacenarla en un campo del objeto, como si que
             * ocurre con las imagenes.
             **/

     //       String[][] sonidos = this.dbc.getUrlSonido(null, correctEmotion);
     //       String[] sonidoEscogido = sonidos[newSound];
     //       int id = Integer.parseInt(sonidoEscogido[2]);

    //        sonido = new SoundPlayer(id);


        } else {
            result = stageResults.GAME_WON;
        }

        return result;
    }


    /**
     * Carga una imagen en el visor del Fragment. La imagen debe existir en el directorio de Assets
     * "imagesEmotion".
     * <p/>
     * Devuelve el resultado de la operacion.
     *
     * @param imageSelected nombre de la imagen a cargar en el visor del Fragment
     * @return boolean false si la imagen no ha podido ser cargada
     */
    private boolean loadImageOnVisor(String imageSelected) {
        boolean resultado = true;
        this.IV = (ImageView) getActivity().findViewById(R.id.imVisor);
        InputStream ims;
        Drawable d;

        if (IV != null) {
            try {
                ims = getActivity().getAssets().open("imagesEmotion/" + imageSelected);
                d = Drawable.createFromStream(ims, null);
                IV.setImageDrawable(d);

                //Redimensiona adecuadamente la imagen
                int size[] = this.setSizeOfImage((float) 0.75, (float) 0.32);
                IV.getLayoutParams().height = size[0];
                IV.getLayoutParams().width = size[1];
            } catch (IOException e) {
                e.printStackTrace();
                resultado = false;
            }
        }
        return resultado;

    }



    /**
     * Interpreta la respuesta del fin del juego.
     *
     * @param respuesta
     */
    public void procesarRespuesta(stageResults respuesta) {
        super.procesarRespuesta(respuesta);

        switch (respuesta) {
            case GAME_STARTED:  //Inicio del juego
                break;

            case PLAYER_ERROR: //Respuesta incorrecta

                //this.feedBack(false);
     //           this.reactivarAudio();
                break;
            case PLAYER_WINS:  //Respuesta correcta

                this.feedBack(true);
                //this.actualizarMarcador();
    //            this.reactivarAudio();
                break;

        }
    }





    /**
     * Actualiza los datos del marcador
     */
    private void actualizarMarcador() {
        this.marcador.setText(Integer.toString(this.correctImages.size()) + "/" + super.maxNumStages);
    }



    @Override
    public void onPause() {
        super.onPause();
     //   if (super.sonido.isPlaying()) {
     //       super.sonido.destroy();
      //      this.wasSoundPlaying = true;
      //  }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.wasSoundPlaying) {
        //    super.sonido.play(getActivity());
            this.wasSoundPlaying = false;
        }

    }
}
