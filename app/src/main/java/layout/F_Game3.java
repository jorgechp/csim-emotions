package layout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

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
 * {@link F_Game3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_Game3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Game3 extends Generic_Game {
    private boolean isGameExecuting;
    private ImageButton selectedButton;
    private HashMap<ImageButton,SoundPlayer> soundTable;
    private String[][] soundList;
    private SoundPlayer newPositionSound;
    private Emotions emotionSelected, userEmotionSelected;

    private ImageButton ibHappy, ibSurprised, ibAngry, ibSad;
    private int numButtons;
    private int heightLimitsMax, widthLimitsMax;
    private int widthtLimitsMin;
    private int heightLimitsMin;
    private ProgressBar rb;
    private Thread threadSound;
    private TextView ttTitle, ttDescription;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    protected class Triple{
        ImageButton ib;
        ObjectAnimator oa1,oa2;


    }

    private OnFragmentInteractionListener mListener;
    private Triple soundButtons[];
    private FrameLayout soundLayout;
    private View.OnClickListener oclSound,oclEmotions;

    public F_Game3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment F_Game3.
     */
    // TODO: Rename and change types and number of parameters
    public static F_Game3 newInstance() {
        F_Game3 fragment = new F_Game3();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.numButtons = Config.LEVEL_0_NUM_OF_STAGES / 2 + 1;
        this.soundButtons = new Triple[this.numButtons];

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.currentGame = States.GAME31;
        this.soundTable = new HashMap<>();
        this.isGameExecuting = true;
        this.soundLayout =  (FrameLayout) super.actividadPrincipal.findViewById(R.id.Game3_flSongTab);
        this.soundList = super.dbc.getUrlSonido(null,null);

        this.ibAngry = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game3_ib_angry);
        this.ibHappy = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game3_ib_happy);
        this.ibSad = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game3_ib_sad);
        this.ibSurprised = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game3_ib_surprised);

        this.newPositionSound = new SoundPlayer(R.raw.water_drop);

        this.ttTitle = (TextView) super.actividadPrincipal.findViewById(R.id.Game31_title);
        this.ttDescription = (TextView) super.actividadPrincipal.findViewById(R.id.Game31_ttGameDescription);

        this.rb = (ProgressBar) super.actividadPrincipal.findViewById(R.id.Game31_rbProgressBar);
        this.rb.setMax(this.numButtons);
        this.rb.setProgress(0);


        int id = new Random().nextInt();
        android.view.ViewGroup.LayoutParams  params;

        this.oclSound = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton b = ((ImageButton)v);
                if(b == F_Game3.this.selectedButton){
                    F_Game3.this.selectedButton.setBackgroundResource(R.mipmap.ic_play);
                    F_Game3.this.selectedButton = null;
                    F_Game3.super.sonido.destroy();
                    F_Game3.this.enableButtons(false);
                }else {
                    F_Game3.this.setNewSelectedButton(b);
                    if (F_Game3.super.sonido != null) {
                        F_Game3.super.sonido.destroy();
                    }
                    F_Game3.super.sonido = F_Game3.this.getSpForSelectedButton();
                    F_Game3.super.sonido.play(F_Game3.super.actividadPrincipal);
                    F_Game3.this.enableButtons(true);
                }

            }
        };

        this.oclEmotions = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(F_Game3.this.selectedButton != null) {
                    switch (v.getId()) {

                        case R.id.Game3_ib_angry:
                            F_Game3.this.userEmotionSelected = Emotions.ANGRY;
                            break;
                        case R.id.Game3_ib_happy:
                            F_Game3.this.userEmotionSelected = Emotions.HAPPY;
                            break;
                        case R.id.Game3_ib_surprised:
                            F_Game3.this.userEmotionSelected = Emotions.SURPRISED;
                            break;
                        case R.id.Game3_ib_sad:
                            F_Game3.this.userEmotionSelected = Emotions.SAD;
                            break;
                    }
                    F_Game3.this.procesarRespuesta(F_Game3.this.continueGame());
                }
            }
        };

        this.ibSad.setOnClickListener(this.oclEmotions);
        this.ibSurprised.setOnClickListener(this.oclEmotions);
        this.ibHappy.setOnClickListener(this.oclEmotions);
        this.ibAngry.setOnClickListener(this.oclEmotions);

        for(byte contador = 0; contador < this.soundButtons.length; ++contador){
            ImageButton ib = new ImageButton(getActivity());
            ib.setBackgroundResource(R.mipmap.ic_play);
            params = new android.view.ViewGroup.LayoutParams (FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
            ib.setLayoutParams(params);

            this.soundLayout.addView(ib);

            this.soundButtons[contador] = new Triple();
            this.soundButtons[contador].ib = ib;
            this.soundButtons[contador].ib.setTag(id);
            this.soundButtons[contador].ib.setOnClickListener(this.oclSound);
            ++id;
        }

        trasladarObjeto();

        Typeface tfTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Action_Man_Bold.ttf");
        Typeface tfFontsButtons = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AndikaNewBasic-B.ttf");

        this.ttTitle.setTypeface(tfTitle);
        this.ttDescription.setTypeface(tfFontsButtons);
    }

    /**
     * Habilita o deshabilita los botones de emociones de la interfaz
     * @param b boolean
     */
    private void enableButtons(boolean b) {
        this.ibAngry.setEnabled(b);
        this.ibHappy.setEnabled(b);
        this.ibSad.setEnabled(b);
        this.ibSurprised.setEnabled(b);
    }

    /**
     * Genera y devuelve un audio asociado al botón seleccionado.
     * @return SoundPlayer
     */
    private SoundPlayer getSpForSelectedButton() {
        SoundPlayer sp = null;
        if(this.selectedButton != null){
            if (this.soundTable.containsKey(this.selectedButton)){
                sp = this.soundTable.get(this.selectedButton);
            }else{
                Random rnd = new Random();
                String[] selectedSound = this.soundList[rnd.nextInt(this.soundList.length)];
                int id = Integer.valueOf(selectedSound[2]);
                sp = new SoundPlayer(id);
                this.soundTable.put(this.selectedButton,sp);
                this.emotionSelected = super.getEmotionFromString(selectedSound[1]);
            }
        }
        return sp;
    }

    private  void setNewSelectedButton(ImageButton b) {
        if(this.selectedButton != null){
            this.selectedButton.setBackgroundResource(R.mipmap.ic_play);
        }
        b.setBackgroundResource(R.mipmap.ic_player_blue);
        this.selectedButton = b;
    }

    private void trasladarObjeto() {
        final Random r = new Random();

        threadSound = new Thread() {

            @Override
            public void run() {

                try {
                    while (!isInterrupted()) {
                        Thread.sleep(3000);
                        getActivity().runOnUiThread(new Runnable() {
                            public boolean isLimitCalculated = false;

                            @Override
                            public void run() {
                                if(!isLimitCalculated) {
                                    float hLimit = (F_Game3.this.soundLayout.getMeasuredHeight() * 0.05f);
                                    float wLimit = (F_Game3.this.soundLayout.getMeasuredWidth() * 0.05f);
                                    F_Game3.this.heightLimitsMax = (int) (F_Game3.this.soundLayout.getHeight() - hLimit);
                                    F_Game3.this.widthLimitsMax = (int) (F_Game3.this.soundLayout.getWidth() - wLimit);
                                    F_Game3.this.heightLimitsMin = (int) hLimit;
                                    F_Game3.this.widthtLimitsMin = (int) wLimit;
                                    isLimitCalculated = true;
                                }
                                for(Triple ibt : F_Game3.this.soundButtons) {

                                    int coordenadas[] = new int[2];
                                    ibt.ib.getLocationInWindow(coordenadas);
                                    ibt.ib.setTranslationY((r.nextInt(F_Game3.this.heightLimitsMax) + coordenadas[0] + F_Game3.this.heightLimitsMin) % F_Game3.this.heightLimitsMax);
                                    ibt.ib.setTranslationX((r.nextInt(F_Game3.this.widthLimitsMax) + coordenadas[1] + F_Game3.this.widthtLimitsMin) % F_Game3.this.widthLimitsMax);

                                    if(F_Game3.this.selectedButton == null) {
                                        F_Game3.this.newPositionSound.play(F_Game3.super.actividadPrincipal,false);
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        threadSound.start();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_3, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public stageResults continueGame() {
        stageResults sr = stageResults.PLAYER_ERROR;
        if(this.emotionSelected == this.userEmotionSelected){
            --this.numButtons;
            if(this.numButtons == 0){
                sr = stageResults.GAME_WON;
            }else{
                sr = stageResults.PLAYER_WINS;
            }
        }
        return sr;
    }

    @Override
    public void procesarRespuesta(stageResults respuesta) {
        switch (respuesta){
            case GAME_WON:
                this.threadSound.interrupt();
                super.feedBackSoundBien.play(super.actividadPrincipal, false);
                super.procesarRespuesta(respuesta);
                break;
            case PLAYER_ERROR:
                super.feedBackSoundMal.play(super.actividadPrincipal,false);
                this.userEmotionSelected = null;
                this.selectedButton.setBackgroundResource(R.mipmap.ic_play);
                this.selectedButton = null;
                super.sonido.destroy();
                break;
            case PLAYER_WINS:
                super.feedBackSoundBien.play(super.actividadPrincipal,false);
                this.userEmotionSelected = null;
                this.soundLayout.removeView(this.selectedButton);
                this.selectedButton = null;
                super.sonido.destroy();
                this.rb.setProgress(this.rb.getProgress() + 1);
                break;
        }

    }
}
