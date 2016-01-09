package csim.csimemotions;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link F_settings_options_menu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_settings_options_menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_settings_options_menu extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ImageButton ib_playerPlay, ib_playerBack, ib_playerNext;
    private ImageButton ib_playerHappy, ib_playerSad, ib_playerAngry, ib_playerSurprised;

    private View.OnClickListener playerClickListener;

    private MainActivity actividadPrincipal;
    private DataBaseController dbc;

    private OnFragmentInteractionListener mListener;

    public F_settings_options_menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment F_settings_options_menu.
     */
    // TODO: Rename and change types and number of parameters
    public static F_settings_options_menu newInstance(String param1, String param2) {
        F_settings_options_menu fragment = new F_settings_options_menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f_settings_options_menu, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        this.ib_playerAngry = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerAngry);
        this.ib_playerHappy = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerHappy);
        this.ib_playerSad = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerSad);
        this.ib_playerSurprised = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerSurprised);

        this.ib_playerBack = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerBack);
        this.ib_playerPlay = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerPlay);
        this.ib_playerNext = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerNext);

        this.playerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F_settings_options_menu.this.enableButtons();
                switch (v.getId()) {
                    case R.id.OptionsSettings_ib_playerAngry:
                        F_settings_options_menu.this.ib_playerAngry.setImageResource(R.color.color_disabled_element);
                        break;
                    case R.id.OptionsSettings_ib_playerHappy:
                        F_settings_options_menu.this.ib_playerAngry.setImageResource(R.color.color_disabled_element);
                        break;
                    case R.id.OptionsSettings_ib_playerSad:
                        F_settings_options_menu.this.ib_playerAngry.setImageResource(R.color.color_disabled_element);
                        break;
                    case R.id.OptionsSettings_ib_playerSurprised:
                        F_settings_options_menu.this.ib_playerAngry.setImageResource(R.color.color_disabled_element);
                        break;
                    case R.id.OptionsSettings_ib_playerBack:
                        F_settings_options_menu.this.ib_playerAngry.setImageResource(R.color.color_disabled_element);
                        break;
                    case R.id.OptionsSettings_ib_playerPlay:
                        F_settings_options_menu.this.ib_playerAngry.setImageResource(R.color.color_disabled_element);
                        break;
                    case R.id.OptionsSettings_ib_playerNext:
                        F_settings_options_menu.this.ib_playerAngry.setImageResource(R.color.color_disabled_element);
                        break;
                }

            }
        };

        this.ib_playerAngry.setOnClickListener(this.playerClickListener);
        this.ib_playerHappy.setOnClickListener(this.playerClickListener);
        this.ib_playerSad.setOnClickListener(this.playerClickListener);
        this.ib_playerSurprised.setOnClickListener(this.playerClickListener);

        this.ib_playerBack.setOnClickListener(this.playerClickListener);
        this.ib_playerPlay.setOnClickListener(this.playerClickListener);
        this.ib_playerNext.setOnClickListener(this.playerClickListener);


        this.actividadPrincipal = (MainActivity) getActivity();
        this.dbc = this.actividadPrincipal.getDataBaseController();

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void enableButtons() {
        this.ib_playerAngry.setImageResource(android.R.color.transparent);
        this.ib_playerHappy.setImageResource(android.R.color.transparent);
        this.ib_playerSurprised.setImageResource(android.R.color.transparent);
        this.ib_playerSad.setImageResource(android.R.color.transparent);
    }
}
