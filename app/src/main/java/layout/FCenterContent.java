package layout;

import android.content.Context;
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

import csim.csimemotions.Config;
import csim.csimemotions.MainActivity;
import csim.csimemotions.R;

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
    private Button btSettings;

    private View.OnClickListener onClickListener;

    public FCenterContent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FCenterContent.
     */
    // TODO: Rename and change types and number of parameters
    public static FCenterContent newInstance(String param1, String param2) {
        FCenterContent fragment = new FCenterContent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) (getActivity())).setfCenter(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.ibGame1:
                        loadNewFragment(R.id.ibGame1);
                        break;
                    case R.id.ibGame2:
                        loadNewFragment(R.id.ibGame2);
                        break;
                    case R.id.ibSettings:
                        loadNewFragment(R.id.ibSettings);
                        break;
                }

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

        //Buttons

        btGame1 = (ImageButton) getView().findViewById(R.id.ibGame1);
        btGame2 = (ImageButton) getView().findViewById(R.id.ibGame2);
        btSettings = (Button) getView().findViewById(R.id.ibSettings);


        btGame1.setOnClickListener(this.onClickListener);
        btGame2.setOnClickListener(this.onClickListener);
        btSettings.setOnClickListener(this.onClickListener);

        this.resize();


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


        int stages[] = {R.id.flStage1, R.id.flStage2, R.id.flStage3, R.id.flStage4};
        ViewGroup.MarginLayoutParams lp;
        FrameLayout fl;
        for (int stage : stages) {
            fl = (FrameLayout) getActivity().findViewById(stage);
            lp = (ViewGroup.MarginLayoutParams) fl.getLayoutParams();
            lp.setMargins(0, dpHeight, 0, dpWidth);
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

    public void loadNewFragment(int stat) {

        FragmentTransaction fManagerTransaction = getFragmentManager().beginTransaction();
        Fragment fg = null;

        switch (stat) {
            case R.id.ibSettings:
                fg = new F_Settings();
                break;
            case R.id.ibGame1:
                fg = new F_Game1();
                break;
            case R.id.ibGame2:
                fg = new F_Game2();
                break;
        }

        if (fg != null) {
            fManagerTransaction.replace(R.id.fCenter, fg);
            fManagerTransaction.hide(this);
            fManagerTransaction.addToBackStack(this.getTag());
            //fManagerTransaction.remove(this);
            fManagerTransaction.commit();
        }
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        this.resize();
    }
}
