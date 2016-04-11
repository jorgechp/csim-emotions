package layout;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
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
 * {@link F_Game6.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_Game6#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Game6 extends Generic_Game {


    private OnFragmentInteractionListener mListener;
    private String[] tales;


    private TextView[] ttTales;
    private ImageButton ibHappy, ibSad, ibAngry, ibSurprised;
    private ImageButton ibContinue;

    // private ProgressBar pbProgress;

    private View.OnClickListener ocl;
    private MediaPlayer.OnCompletionListener oCompletionListener;
    private int numParrafos;

    /**
     * Establece la fuente de los párrafos
     */
    private Typeface fontParagraphs;




    /**
     * Almacena una estructura
     * parrafo -> emocion
     * pudiendo ser emocion == null si no existe una emoción asociada al párrafo
     */
    private Queue<Pair<String,Emotions>> contentTale;
    private Queue<AssetFileDescriptor>  completeSounds, soundsStage;
    private int contadorSonido;

    /**
     * sonidiIdle es una variable auxiliar que almacena el identificador de una canción generada a
     * partir de una emoción predeterminada.
     *
     * Cuando la lista de narraciones se acaba, se comprueba el valor de sonidoIdle y, si no vale
     * -1, se reproduce.
     */
    private int sonidoIdle;
    private TextView tvTitle;
    private boolean isTramaFinalizada;


    public F_Game6() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment F_Game6.
     */

    public static F_Game6 newInstance() {
        F_Game6 fragment = new F_Game6();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.currentGame = States.GAME4;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.isDialogAlert = false;

        super.onActivityCreated(savedInstanceState);




        this.contentTale = new LinkedList<Pair<String,Emotions>>();
        this.soundsStage = new LinkedList<AssetFileDescriptor>();
        this.completeSounds = new LinkedList<AssetFileDescriptor>();
        this.contadorSonido = 1;
        this.sonidoIdle = -1;
        this.numParrafos = 0;
        this.isTramaFinalizada = false;


        this.ttTales = new TextView[2];

        this.ttTales[0] = (TextView) super.actividadPrincipal.findViewById(R.id.Game6_Text1);
        this.ttTales[1] = (TextView) super.actividadPrincipal.findViewById(R.id.Game6_Text2);

        this.tvTitle = (TextView) super.actividadPrincipal.findViewById(R.id.Game6_tvTitle);
        Typeface tfTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Action_Man_Bold.ttf");
        this.fontParagraphs = Typeface.createFromAsset(getActivity().getAssets(), "fonts/opensans_light.ttf");
        this.tvTitle.setTypeface(tfTitle);

        this.ttTales[0].setTypeface(this.fontParagraphs);
        this.ttTales[1].setTypeface(this.fontParagraphs);

        this.ibAngry = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game6_ibAngry);
        this.ibSad = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game6_ibSad);
        this.ibHappy = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game6_ibHappy);
        this.ibSurprised = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game6_ibSurprised);

        this.ibContinue = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game6_ibContinue);

        //  this.pbProgress = (ProgressBar) super.actividadPrincipal.findViewById(R.id.Game6_progressBar);

        //Se genera la lista de cuentos.
        try {
            tales = super.actividadPrincipal.getAssets().list("tales");
            generateGame();


        } catch (IOException e) {
            e.printStackTrace();
        }

        this.ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                boolean isAnswer = true;
                switch (id){
                    case R.id.Game6_ibAngry:
                        F_Game6.this.respuestaUsuario = Emotions.ANGRY;
                        break;
                    case R.id.Game6_ibSad:
                        F_Game6.this.respuestaUsuario = Emotions.SAD;
                        break;
                    case R.id.Game6_ibHappy:
                        F_Game6.this.respuestaUsuario = Emotions.HAPPY;
                        break;
                    case R.id.Game6_ibSurprised:
                        F_Game6.this.respuestaUsuario = Emotions.FEAR;
                        break;
                    case R.id.Game6_ibContinue:
                        if(F_Game6.this.isTramaFinalizada == false) {
                            F_Game6.this.continuarHistoria();
                            F_Game6.this.ibContinue.setVisibility(View.INVISIBLE);
                            isAnswer = false;
                        }

                        break;
                }
                if(isAnswer) {
                    F_Game6.this.procesarRespuesta(F_Game6.this.continueGame());

                }
            }
        };

        this.oCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //
                F_Game6.super.sonido.destroy();
                if(F_Game6.this.soundsStage.size() > 0) {
                    try {
                        F_Game6.super.sonido = new SoundPlayer(F_Game6.this.soundsStage.poll());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    F_Game6.super.sonido.play(F_Game6.super.actividadPrincipal, false);
                    F_Game6.super.sonido.setOnCompletionListener(this);

                }else{

                    if(F_Game6.this.sonidoIdle != -1){
                        F_Game6.super.sonido = new SoundPlayer(F_Game6.this.sonidoIdle);
                        F_Game6.super.sonido.play(F_Game6.super.actividadPrincipal,false);
                        F_Game6.this.sonidoIdle = -1;
                        F_Game6.this.enableEmotionButtons(true);
                    }else{
                        F_Game6.this.ibContinue.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        this.ibAngry.setOnClickListener(this.ocl);
        this.ibSad.setOnClickListener(this.ocl);
        this.ibHappy.setOnClickListener(this.ocl);
        this.ibSurprised.setOnClickListener(this.ocl);
        this.ibContinue.setOnClickListener(this.ocl);

        this.enableEmotionButtons(false);
        this.ibContinue.setVisibility(View.INVISIBLE);

        //this.pbProgress.setProgress(0);
        //this.pbProgress.setMax(this.numParrafos);




        procesarRespuesta(continueGame());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f__game6, container, false);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    protected void contadorPost() {

    }

    /**
     * Extrae parrafos del array de parrafos de la historia, los analiza y los presenta
     * por pantalla.
     * @return boolean true si la historia ha terminado
     */
    private boolean generarSiguienteTrama(){
        this.ttTales[0].setText(null);
        this.ttTales[1].setText(null);
        Pair<String, Emotions> element;
        boolean isFinished = false;
        boolean isHistoryFinished = false;


        byte contador = 0;
        while(!isFinished){
            element = this.contentTale.poll();
            if(element != null) {
                this.ttTales[contador].setText(element.first);
                this.soundsStage.add(this.completeSounds.poll());
                //this.pbProgress.setProgress(this.pbProgress.getProgress() + 1);

                //element.second contiene una emocion, siempre y cuando su valor no sea nulo
                if (element.second != null) {

                    isFinished = true;
                    super.respuestaCorrecta = element.second;
                }
            }else{
                isFinished = true;

                //No queda más audio por insertar
                if(this.soundsStage.size() == 0) {
                    isHistoryFinished = true;
                }
            }



            if(contador < 1){
                ++contador;
            }else{
                isFinished = true;
            }
        }
        return isHistoryFinished;

    }


    @Override
    public void procesarRespuesta(stageResults respuesta) {
        super.procesarRespuesta(respuesta);

        this.ibContinue.setVisibility(View.INVISIBLE);




        switch (respuesta){
            case PLAYER_WINS:
                //super.sonido.destroy();

                this.enableEmotionButtons(false);
                super.saveStage();
                this.respuestaCorrecta = null;
            case GAME_STARTED:
                this.continuarHistoria();
                break;
            case PLAYER_ERROR:
                super.saveStage();
                super.sonido.play(super.actividadPrincipal);
                break;
            case GAME_WON:
            case USER_EXIT:
                this.completeSounds = null;


        }

    }

    /**
     * Continua el desarrollo de la historia
     */
    private void continuarHistoria(){

        if(generarSiguienteTrama() == false) {
            generateImages();

                try {
                    AssetFileDescriptor soundPoll = this.soundsStage.poll();
                    if (soundPoll != null) {
                        super.sonido = new SoundPlayer(soundPoll);
                        super.sonido.play(super.actividadPrincipal, false);
                        super.sonido.setOnCompletionListener(this.oCompletionListener);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (this.respuestaCorrecta != null) {
                    generateSound();


                }

        }else{
            isTramaFinalizada = true;
            this.procesarRespuesta(this.continueGame());
        }



    }
    /**
     * Selecciona imagenes de emociones de forma aleatoria
     */
    private void generateImages() {
        Random rnd = new Random();
        super.loadImageOnVisor(super.imHappy[rnd.nextInt(super.imHappy.length)][0], this.ibHappy,0.22f, 0.15f);
        super.loadImageOnVisor(super.imAngry[rnd.nextInt(super.imAngry.length)][0], this.ibAngry,0.22f, 0.15f);
        super.loadImageOnVisor(super.imSad[rnd.nextInt(super.imSad.length)][0], this.ibSad,0.22f, 0.15f);
        super.loadImageOnVisor(super.imSurprised[rnd.nextInt(super.imSurprised.length)][0], this.ibSurprised,0.22f, 0.15f);

    }

    /**
     * Genera aleatoriamente un sonido relacionado con la emoción actual
     */
    private void generateSound(){
        Random rnd = new Random();
        String sonidos[][] = super.dbc.getUrlSonido(null,super.respuestaCorrecta);
        int indexOfSelectedSound = rnd.nextInt(sonidos.length);
        String sonidoSeleccionado[] = sonidos[indexOfSelectedSound];
        int id = Integer.parseInt(sonidoSeleccionado[2]);
        this.sonidoIdle = id;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public stageResults continueGame() {
        stageResults srespuesta = null;
        if(super.respuestaUsuario == null && super.respuestaCorrecta == null){
            srespuesta = stageResults.GAME_STARTED;
        }else if(super.respuestaUsuario == super.respuestaCorrecta){
            srespuesta = stageResults.PLAYER_WINS;
        }else{
            srespuesta = stageResults.PLAYER_ERROR;
        }

        // Si se han agotado los sonidos, se ha completado el juego
        if(this.isTramaFinalizada){
            srespuesta = stageResults.GAME_WON;
        }

        return srespuesta;
    }

    /**
     * Habilita los botones de emociones de la pantalla.
     * @param isEnabled
     */
    private void enableEmotionButtons(boolean isEnabled) {
        int visibilityEmotionButtons = View.INVISIBLE;

        if(isEnabled){
            visibilityEmotionButtons= View.VISIBLE;

        }
        this.ibAngry.setVisibility(visibilityEmotionButtons);
        this.ibHappy.setVisibility(visibilityEmotionButtons);
        this.ibSad.setVisibility(visibilityEmotionButtons);
        this.ibSurprised.setVisibility(visibilityEmotionButtons);


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

        void onFragmentInteraction(Uri uri);
    }


    /**
     * Lee un cuento al azar desde un fichero de texto y lo carga en memoria.
     */
    private void generateGame(){
        byte numTales = (byte) this.tales.length;
        Random rnd = new Random();
        InputStream inputStreamText = null;
        AssetFileDescriptor inputSound = null;

        byte selectedTale = 4;//(byte) rnd.nextInt(numTales);
        AssetManager assetManager = super.actividadPrincipal.getAssets();
        BufferedReader reader = null;

        try {
            inputStreamText = assetManager.open("tales/" + this.tales[selectedTale]);


            reader = new BufferedReader(new InputStreamReader(inputStreamText));



            String line;
            String parrafo = null;
            Emotions emotion = null;

            while ((line = reader.readLine()) != null) {
                char primero = line.charAt(0);
                boolean isAdd = false;

                if(primero == '<'){

                    isAdd = true;
                }
                else if(primero == '>'){
                    switch (line.charAt(1)){
                        case 'A':
                            emotion = Emotions.ANGRY;
                            break;
                        case 'S':
                            emotion = Emotions.SAD;
                            break;
                        case 'H':
                            emotion = Emotions.HAPPY;
                            break;
                        case 'U': // Fear
                            emotion = Emotions.FEAR;
                            break;

                    }
                    isAdd = true;
                }
                else{
                    parrafo = line;
                    String soundName = "talesSongs/t" + (1 + selectedTale) + "/" + "t_" + contadorSonido + ".ogg";
                    inputSound = super.actividadPrincipal.getAssets().openFd(soundName);
                    if(inputSound != null) {
                        F_Game6.this.completeSounds.add(inputSound);
                    }
                    ++contadorSonido;
                }

                if(isAdd){
                    this.contentTale.add(new Pair<String, Emotions>(parrafo,emotion));
                    parrafo = null;
                    emotion = null;
                    isAdd = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.numParrafos = this.contentTale.size();
    }
}
