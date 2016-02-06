package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import csim.csimemotions.F_settings_options_menu;
import csim.csimemotions.MainActivity;
import csim.csimemotions.R;
import csim.csimemotions.stageResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FBarUp.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FBarUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FBarUp extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;


    private ImageButton ibSettingsGames, ibSettings;
    private View.OnClickListener onclick;
    private boolean isSettings, isSettingsOptions;
    private MainActivity actividadPrincipal;
    private F_Settings fgSettings;
    private F_settings_options_menu fgSettingsOptions;
    private FCenterContent fCenterContent;
    private boolean isGameMode;

    public FBarUp() {
        // Required empty public constructor
    }

    public void setFgSettingsOptions(F_settings_options_menu fgSettingsOptions) {
        this.fgSettingsOptions = fgSettingsOptions;
    }

    public void setIsSettingsOptions(boolean isSettingsOptions) {
        this.isSettingsOptions = isSettingsOptions;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FBarUp.
     */
    // TODO: Rename and change types and number of parameters
    public static FBarUp newInstance(String param1, String param2) {
        FBarUp fragment = new FBarUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isSettings = false;
        this.isSettingsOptions = false;



    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void loadSettingsFragment() {
        this.fgSettings = (F_Settings) this.fCenterContent.loadNewFragment(R.id.ibSettings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f_bar_up, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.ibSettingsGames = (ImageButton) getActivity().findViewById(R.id.ibPreferenciasGames);
        this.ibSettings = (ImageButton) getActivity().findViewById(R.id.ibPreferencias);
        this.onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.ibPreferencias:
                        FBarUp.this.loadSettings();
                        FBarUp.this.ibSettings.setVisibility(View.INVISIBLE);
                        isSettingsOptions= false;

                        break;
                    case R.id.ibPreferenciasGames:
                        if(FBarUp.this.isGameMode){
                            FBarUp.this.actividadPrincipal.getCurrentGame().procesarRespuesta(stageResults.USER_EXIT);
                        }else {
                            if (FBarUp.this.isSettings == false) {
                                FBarUp.this.loadSettings();

                            } else {
                                FBarUp.this.loadGames();
                                FBarUp.this.ibSettingsGames.setBackgroundResource(R.mipmap.ic_settings);
                                FBarUp.this.isSettings = false;
                            }
                        }
                        break;
                }

            }
        };
        this.ibSettingsGames.setOnClickListener(this.onclick);
        this.ibSettings.setOnClickListener(this.onclick);
        this.actividadPrincipal = (MainActivity) getActivity();
        this.actividadPrincipal.setfUp(this);
        this.fCenterContent = this.actividadPrincipal.getfCenter();
        this.ibSettings.setVisibility(View.INVISIBLE);


    }


    private void loadSettings(){
        this.loadSettingsFragment();
        this.ibSettingsGames.setBackgroundResource(R.mipmap.ic_barup_play);
        this.isSettings = true;
    }
    private void loadGames() {
        if(isSettingsOptions){
            this.fgSettingsOptions.retornar();
            isSettingsOptions = false;
            this.ibSettings.setVisibility(View.INVISIBLE);

        }else {
            this.fgSettings.retornar();
        }
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

    public void enableSettingsButton() {
        FBarUp.this.ibSettings.setVisibility(View.VISIBLE);
        this.isSettingsOptions = true;
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

    public void setGameMode(boolean isGame) {
        if (isGame) {
            //this.ibSettingsGames.setVisibility(View.INVISIBLE);
            FBarUp.this.ibSettingsGames.setBackgroundResource(R.mipmap.ic_barup_play);

        } else {
            //this.ibSettingsGames.setVisibility(View.VISIBLE);
            FBarUp.this.ibSettingsGames.setBackgroundResource(R.mipmap.ic_settings);

        }
        FBarUp.this.isGameMode = isGame;
    }


}
