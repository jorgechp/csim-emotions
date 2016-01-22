package layout;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashSet;
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
 * {@link F_Game4.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_Game4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Game4 extends Generic_Game {



    private View.OnTouchListener otl;


    private OnFragmentInteractionListener mListener;
    private float dx, dy;
    private ViewGroup.LayoutParams parms ;
    private LinearLayout.LayoutParams par;
    private ImageView ivPlayer;
    private ImageButton ivHappy, ivSad, ivAngry, ivSurprised;
    private LinearLayout llGame;
    private float heightDiscount;
    private ViewGroup layoutButton;


    private ProgressBar progressBar;
    private String[][] sonidos;
    private int numSonidos;
    private HashSet<Integer> listaSonidos;
    private Emotions soundCategory;
    private Emotions soundCategorySelectedByUser;
    private ViewGroup.LayoutParams layoutParams;

    private int numStagesCompleted;

    private TextView tvTitle;


    public F_Game4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment F_Game4.
     */
    // TODO: Rename and change types and number of parameters
    public static F_Game4 newInstance() {
        F_Game4 fragment = new F_Game4();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 50, 50, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        super.sonido = null;
        super.currentGame = States.GAME32;

        this.ivPlayer = (ImageView) super.actividadPrincipal.findViewById(R.id.Game4_ivPlayer);
        this.layoutParams = this.ivPlayer.getLayoutParams();

        this.layoutButton = (ViewGroup) this.ivPlayer.getParent();

        this.tvTitle = (TextView) super.actividadPrincipal.findViewById(R.id.Game4_ttTitle);


        this.ivHappy = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game4_ivHappy);
        this.ivSad = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game4_ivSad);
        this.ivSurprised = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game4_ivSurprised);
        this.ivAngry = (ImageButton) super.actividadPrincipal.findViewById(R.id.Game4_ivAngry);

        this.llGame = (LinearLayout) super.actividadPrincipal.findViewById(R.id.Game4_llGameLayout);
        this.progressBar = (ProgressBar) super.actividadPrincipal.findViewById(R.id.Game4_progressBar);
        this.progressBar.setProgress(0);
        this.progressBar.setMax(Config.LEVEL_0_NUM_OF_STAGES);

        this.soundCategory = null;

        /*
        * Es necesario descontar los layouts superiores para poder adecuar el movimiento del player de layouts inferiores
         */
        this.heightDiscount = llGame.getHeight() * 0.28f;

        this.sonidos = super.dbc.getUrlSonido(null,null);
        this.numSonidos = this.sonidos.length;
        this.listaSonidos = new HashSet<>();
        this.numStagesCompleted = 0;

        Typeface tfTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Action_Man_Bold.ttf");
        this.tvTitle.setTypeface(tfTitle);

        this.otl = new View.OnTouchListener() {

            int inferiorLimitHappy[] = new int[2];
            int inferiorLimitSad[] = new int[2];
            int inferiorLimitAngry[] = new int[2];
            int inferiorLimitSurprised[] = new int[2];



            int superiorLimitXHappy;
            int superiorLimitYHappy;
            int superiorLimitXSad;
            int superiorLimitYSad;
            int superiorLimitXAngry;
            int superiorLimitYAngry;
            int superiorLimitXSurprised;
            int superiorLimitYSurprised;

            boolean isConfigured = false;


//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int x = (int) event.getRawX();
//                final int y = (int) event.getRawY();
//                boolean res = false;
//
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN:
//                        res = true;
//                        dx = x-event.getRawX();
//                        dy = y-event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float newx = event.getRawX();
//                        float newy = event.getRawY();
//                        int pos[] = new int[2];
//                        ivHappy.getLocationInWindow(pos);
//                        ((ImageView)v).setX(newy);
//                        ((ImageView)v).setY(newx);
//                        Rect r = new Rect(); ;
//                        ivHappy.getHitRect(r);
//
//                        //ObjectAnimator animation2 = ObjectAnimator.ofFloat(v, "translationY", newy);
//                        //animation2.setDuration(3000);
//                       // animation2.setTarget(v);
//                        //animation2.start();
//                        if(r.contains((int)newx,(int)newy)){
//                            int a = 0;
//                            int b= 1;
//                        }
//                        break;
//                }
//                return res;
//            }
            public boolean onTouch(View v, MotionEvent event) {
                if(isConfigured == false){
                    configure();
                }
                int eid = event.getAction();
                final float yy = event.getRawY()-event.getY();





                switch (eid) {
                    case MotionEvent.ACTION_DOWN:
                        F_Game4.super.sonido.play(F_Game4.super.actividadPrincipal);
                        F_Game4.this.ivPlayer.setImageResource(R.mipmap.ic_player_blue);
                        break;
                    case MotionEvent.ACTION_UP:
                        F_Game4.super.sonido.destroy();
                        F_Game4.this.ivPlayer.setImageResource(R.mipmap.ic_play);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams mParams = (FrameLayout.LayoutParams) F_Game4.this.ivPlayer.getLayoutParams();
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        F_Game4.this.ivPlayer.setX(x);
                        F_Game4.this.ivPlayer.setY(y-50);
                        boolean isContinue = false;
                        //mParams.leftMargin = x;
                        //mParams.topMargin = y-350;
                        //F_Game4.this.ivPlayer.setLayoutParams(mParams);

                        if(x > inferiorLimitHappy[0] && x <  superiorLimitXHappy){
                            if(y > inferiorLimitHappy[1] && y < superiorLimitYHappy){
                                F_Game4.this.soundCategorySelectedByUser = Emotions.HAPPY;
                                isContinue = true;
                            }
                        }
                        if(x > inferiorLimitSad[0] && x <  superiorLimitXSad){
                            if(y > inferiorLimitSad[1] && y < superiorLimitYSad){
                                F_Game4.this.soundCategorySelectedByUser = Emotions.SAD;
                                isContinue = true;
                            }
                        }
                        if(x > inferiorLimitAngry[0] && x <  superiorLimitXAngry){
                            if(y > inferiorLimitAngry[1] && y < superiorLimitYAngry){
                                F_Game4.this.soundCategorySelectedByUser = Emotions.ANGRY;
                                isContinue = true;
                            }
                        }
                        if(x > inferiorLimitSurprised[0] && x <  superiorLimitXSurprised){
                            if(y > inferiorLimitSurprised[1] && y < superiorLimitYSurprised){
                                F_Game4.this.soundCategorySelectedByUser = Emotions.SURPRISED;
                                isContinue = true;
                            }
                        }

                        if(isContinue){
                            F_Game4.this.procesarRespuesta(F_Game4.this.continueGame());
                            F_Game4.super.sonido.destroy();
                        }

                        break;

                    default:
                        break;
                }
                return true;
            }

            private void configure() {
                F_Game4.this.ivHappy.getLocationInWindow(inferiorLimitHappy);
                F_Game4.this.ivSad.getLocationInWindow(inferiorLimitSad);
                F_Game4.this.ivAngry.getLocationInWindow(inferiorLimitAngry);
                F_Game4.this.ivSurprised.getLocationInWindow(inferiorLimitSurprised);

                superiorLimitXHappy = inferiorLimitHappy[0] + F_Game4.this.ivHappy.getMeasuredWidth();
                superiorLimitYHappy = inferiorLimitHappy[1] + F_Game4.this.ivHappy.getMeasuredHeight();
                superiorLimitXSad = inferiorLimitSad[0] + F_Game4.this.ivSad.getMeasuredWidth();
                superiorLimitYSad = inferiorLimitSad[1] + F_Game4.this.ivSad.getMeasuredHeight();
                superiorLimitXAngry= inferiorLimitAngry[0] + F_Game4.this.ivAngry.getMeasuredWidth();
                superiorLimitYAngry = inferiorLimitAngry[1] + F_Game4.this.ivAngry.getMeasuredHeight();
                superiorLimitXSurprised = inferiorLimitSurprised[0] + F_Game4.this.ivSurprised.getMeasuredWidth();
                superiorLimitYSurprised = inferiorLimitSurprised[1] + F_Game4.this.ivSurprised.getMeasuredHeight();

                isConfigured = true;
            }
        };

        this.ivPlayer.setOnTouchListener(this.otl);
        this.procesarRespuesta(this.continueGame());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game4, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void procesarRespuesta(stageResults respuesta) {
        boolean isGenerate = false;
        switch (respuesta){
            case GAME_WON:
                super.feedBackSoundBien.play(super.actividadPrincipal, false);
                super.procesarRespuesta(respuesta);
                break;
            case PLAYER_ERROR:
                break;
            case PLAYER_WINS:
                super.feedBackSoundBien.play(super.actividadPrincipal, false);
                this.progressBar.setProgress(this.numStagesCompleted);
                isGenerate = true;
                break;
            case GAME_STARTED:
                isGenerate = true;
                break;
        }
        if(isGenerate){
            Random rnd = new Random();
            int indexOfSelectedSound;

            do {
                indexOfSelectedSound = rnd.nextInt(this.numSonidos);
            }while(this.listaSonidos.contains(indexOfSelectedSound) == true);
            this.listaSonidos.add(indexOfSelectedSound);

            String sonidoSeleccionado[] = this.sonidos[indexOfSelectedSound];
            int id = Integer.parseInt(sonidoSeleccionado[2]);
            super.sonido = new SoundPlayer(id);
            this.soundCategory = super.getEmotionFromString(sonidoSeleccionado[1]);



            super.loadImageOnVisor(super.imHappy[rnd.nextInt(super.imHappy.length)][0], this.ivHappy,0.35f,0.10f);
            super.loadImageOnVisor(super.imAngry[rnd.nextInt(super.imAngry.length)][0], this.ivAngry,0.35f,0.10f);
            super.loadImageOnVisor(super.imSad[rnd.nextInt(super.imSad.length)][0], this.ivSad,0.35f,0.10f);
            super.loadImageOnVisor(super.imSurprised[rnd.nextInt(super.imSurprised.length)][0], this.ivSurprised,0.35f,0.10f);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public stageResults continueGame() {
        stageResults result = null;

        if(this.soundCategorySelectedByUser == null){
            result = stageResults.GAME_STARTED;
        }else{
            if(this.soundCategorySelectedByUser == this.soundCategory){
                ++this.numStagesCompleted;
                if(this.numStagesCompleted < Config.LEVEL_0_NUM_OF_STAGES){
                    result = stageResults.PLAYER_WINS;
                }else{
                    result = stageResults.GAME_WON;
                }

            }else{
                result = stageResults.PLAYER_ERROR;
            }

            if(null!=this.layoutButton) { //for safety only  as you are doing onClick
                this.layoutButton.removeView(this.ivPlayer);
                this.ivPlayer = new ImageButton(super.actividadPrincipal);
                this.ivPlayer.setBackgroundResource(R.mipmap.ic_play);
                this.layoutButton.addView(this.ivPlayer);
                this.ivPlayer.setLayoutParams(this.layoutParams);
                this.ivPlayer.setOnTouchListener(this.otl);
            }

            this.soundCategorySelectedByUser = null;

        }

        return result;
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
}
