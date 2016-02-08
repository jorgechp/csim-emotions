package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import csim.csimemotions.Config;
import csim.csimemotions.F_settings_options_menu;
import csim.csimemotions.Generic_Game;
import csim.csimemotions.MainActivity;
import csim.csimemotions.R;
import csim.csimemotions.States;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FCenterContent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FCenterContent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCenterContent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageButton btGame1;
    private ImageButton btGame2;
    private ImageButton btGame31,btGame32,btGame33;
    private ImageButton btGame4;
    private Button btNextLevel;

    private View.OnClickListener onClickListener;

    private MainActivity mainActivity;
    private FrameLayout stage1, stage2, stage3, stage4, finalStage;
    private FrameLayout space1, space2, space3, spaceFill;

    private TextView tvTitle;



    public FCenterContent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment FCenterContent.
     */
    // TODO: Rename and change types and number of parameters
    public static FCenterContent newInstance() {
        FCenterContent fragment = new FCenterContent();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = ((MainActivity) (getActivity()));
        mainActivity.setfCenter(this);
        mainActivity.getStateOfTheGame();




        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                loadNewFragment(v.getId());
            }
        };



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f_center_content, container, false);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvTitle = (TextView) getView().findViewById(R.id.tvFCenterTitle);


        //Buttons

        btGame1 = (ImageButton) getView().findViewById(R.id.ibGame1);
        btGame2 = (ImageButton) getView().findViewById(R.id.ibGame2);
        btGame31 = (ImageButton) getView().findViewById(R.id.ibGame31);
        btGame32 = (ImageButton) getView().findViewById(R.id.ibGame32);
        btGame33 = (ImageButton) getView().findViewById(R.id.ibGame33);
        btGame4 = (ImageButton) getView().findViewById(R.id.ibGame4);
        btNextLevel = (Button) getView().findViewById(R.id.ibSettings);

        this.space1 = (FrameLayout) getActivity().findViewById(R.id.flMargin1);
        this.space2 = (FrameLayout) getActivity().findViewById(R.id.flMargin2);
        this.space3 = (FrameLayout) getActivity().findViewById(R.id.flMargin3);
        this.spaceFill = (FrameLayout) getActivity().findViewById(R.id.flFillLayout);

        this.stage1 = (FrameLayout) getActivity().findViewById(R.id.flStage1);
        this.stage2 = (FrameLayout) getActivity().findViewById(R.id.flStage2);
        this.stage3 = (FrameLayout) getActivity().findViewById(R.id.flStage3);
        this.stage4 = (FrameLayout) getActivity().findViewById(R.id.flStage4);
        this.finalStage = (FrameLayout) getActivity().findViewById(R.id.flFinalStage);

        btGame1.setOnClickListener(this.onClickListener);
        btGame2.setOnClickListener(this.onClickListener);
        btGame31.setOnClickListener(this.onClickListener);
        btGame32.setOnClickListener(this.onClickListener);
        btGame33.setOnClickListener(this.onClickListener);
        btGame4.setOnClickListener(this.onClickListener);
        btNextLevel.setOnClickListener(this.onClickListener);



        Typeface tfTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Action_Man_Bold.ttf");
        tvTitle.setTypeface(tfTitle);

        this.resize();
        this.checkUI();

    }


    private void resize() {
        /**
         * La separacion entre elementos de la interfaz se produce en tiempo de ejecucion
         * ajustando el tamano en funcion de la resolucion de pantalla.
         */
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        int dpHeight = (int) (outMetrics.heightPixels * Config.STAGES_WIDTH);
        int dpWidth = (int) (outMetrics.widthPixels * Config.STAGES_WIDTH);



//  int stages[] = {R.id.flStage1, R.id.flStage2, R.id.flStage3, R.id.flStage4};
//        ViewGroup.MarginLayoutParams lp;
//        FrameLayout fl;
//        for (int stage : stages) {
//            fl = (FrameLayout) getActivity().findViewById(stage);
//            lp = (ViewGroup.MarginLayoutParams) fl.getLayoutParams();
//            lp.setMargins(0, dpHeight, 0, dpWidth);
//
//        }
        int stages[] = {R.id.flMargin1, R.id.flMargin2, R.id.flMargin3, R.id.flMargin3};
        ViewGroup.MarginLayoutParams lp;
        FrameLayout fl;
        for (int stage : stages) {
            fl = (FrameLayout) getActivity().findViewById(stage);

            lp = (ViewGroup.MarginLayoutParams) fl.getLayoutParams();
            lp.height = dpHeight;

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public Fragment loadNewFragment(int stat) {

        FragmentTransaction fManagerTransaction = getFragmentManager().beginTransaction();
        Fragment fg = null;
        boolean isGame = true;

        switch (stat) {
            case R.id.ibGame1:
                fg = new F_Game1();
                break;
            case R.id.ibGame2:
                fg = new F_Game2();
                break;
            case R.id.ibGame31:
                fg = new F_Game3();
                break;
            case R.id.ibGame32:
                fg = new F_Game4();
                break;
            case R.id.ibGame33:
                fg = new F_Game5();
                break;
            case R.id.ibGame4:
                fg = new F_Game6();
                break;
            case R.id.ibNextLevel:
                fg = null;
                mainActivity.getStateOfTheGame().increaseLevel();
                isGame = false;
                break;
            case R.id.Settings_ivOptions:
                fg = new F_settings_options_menu();
                this.mainActivity.getfUp().setFgSettingsOptions((F_settings_options_menu) fg);
                isGame = false;
                break;
        }

        if (isGame) {
            this.mainActivity.getfUp().setGameMode(true);
            this.mainActivity.setCurrentGame((Generic_Game) fg);


        }

        if (fg != null) {
            fManagerTransaction.replace(R.id.fCenter, fg);
            fManagerTransaction.hide(this);
            fManagerTransaction.addToBackStack(this.getTag());
            //fManagerTransaction.remove(this);
            fManagerTransaction.commit();
        }
        return fg;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to  Activity.onResume of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        this.resize();
    }

    /**
     * Actualiza la información de la interfaz de usuario en base a los logros obtenidos por el
     * usuario.
     */
    public void checkUI() {
        States estado = mainActivity.getStateOfTheGame().getEstado();

        if (mainActivity.getStateOfTheGame().isGamePlayed(States.GAME1)) { // Se ha superado el GAME1
            Drawable drawable = new ColorDrawable(Config.COLOR_CLOSED_STAGES);
            this.space1.setForeground(drawable);
            this.stage2.setForeground(drawable);
            this.btGame2.setEnabled(true);

            if(mainActivity.getStateOfTheGame().isGamePlayed(States.GAME2)){
                this.space2.setForeground(drawable);
                this.stage3.setForeground(drawable);
                this.btGame31.setEnabled(true);
                this.btGame32.setEnabled(true);
                this.btGame33.setEnabled(true);
            }

            boolean isGame31Played = mainActivity.getStateOfTheGame().isGamePlayed(States.GAME31);
            boolean isGame32Played = mainActivity.getStateOfTheGame().isGamePlayed(States.GAME32);
            boolean isGame33Played = mainActivity.getStateOfTheGame().isGamePlayed(States.GAME33);

            if(isGame31Played && isGame32Played && isGame33Played){
                this.space3.setForeground(drawable);
                this.stage4.setForeground(drawable);
                this.btGame4.setEnabled(true);

            }

            boolean isGame4Played = mainActivity.getStateOfTheGame().isGamePlayed(States.GAME4);

            if(isGame4Played){
                this.spaceFill.setForeground(drawable);
                this.finalStage.setForeground(drawable);
                this.btNextLevel.setEnabled(true);
            }
        } else {
            this.anularBotones();
        }
    }

    private void anularBotones() {
        this.btGame2.setEnabled(false);
        this.btNextLevel.setEnabled(false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();

    }
}
