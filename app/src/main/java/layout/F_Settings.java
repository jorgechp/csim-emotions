package layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import csim.csimemotions.MainActivity;
import csim.csimemotions.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link F_Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Settings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View.OnClickListener ocl;

    private ImageView ivCustomize, ivExport;
    private MainActivity actividadPrincipal;


    private ImageView ivEEG;
    private TextView tvEEG;


    public F_Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment F_Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static F_Settings newInstance(String param1, String param2) {
        F_Settings fragment = new F_Settings();
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
        return inflater.inflate(R.layout.fragment_f__settings, container, false);
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
        this.ivCustomize = (ImageView) getActivity().findViewById(R.id.Settings_ivOptions);
        this.ivExport = (ImageView) getActivity().findViewById(R.id.Settings_ivExport);
        this.actividadPrincipal = (MainActivity) getActivity();
        this.tvEEG = (TextView) getActivity().findViewById(R.id.Settings_tvEEG);

        if(F_Settings.this.actividadPrincipal.getTemporalStateGame().isEnableEEG()) {
            this.tvEEG.setText(R.string.Settings_eeg_disable);
        }else{
            this.tvEEG.setText(R.string.Settings_eeg_enable);
        }
        this.ivEEG = (ImageView) getActivity().findViewById(R.id.Settings_ivEEG);



        this.ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Settings_ivOptions:
                        F_Settings.this.loadCustomSettings();
                        F_Settings.this.actividadPrincipal.getfUp().enableSettingsButton();
                        break;
                    case R.id.Settings_ivExport:
                        F_Settings.this.actividadPrincipal.startActivity(
                                Intent.createChooser(
                                        F_Settings.this.actividadPrincipal.getLogMan().export(),
                                        F_Settings.this.actividadPrincipal.getResources().getString(R.string.Settings_export_file_intent_title))
                        );
                        break;
                    case R.id.Settings_ivEEG:
                        boolean estadoEEG = F_Settings.this.actividadPrincipal.getTemporalStateGame().isEnableEEG();
                        F_Settings.this.actividadPrincipal.getTemporalStateGame().setEnableEEG(!estadoEEG);
                        if(estadoEEG){
                            F_Settings.this.tvEEG.setText(R.string.Settings_eeg_enable);
                        }else {
                            F_Settings.this.tvEEG.setText(R.string.Settings_eeg_disable);


                            if (F_Settings.this.actividadPrincipal.getUserConf().getUserName() == null || F_Settings.this.actividadPrincipal.getUserConf().getUserName().length() == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(F_Settings.this.actividadPrincipal);
                                builder.setTitle(R.string.Settings_eeg_title);

// Set up the input
                                final EditText input = new EditText(F_Settings.this.actividadPrincipal);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                builder.setView(input);

// Set up the buttons
                                builder.setPositiveButton(R.string.Settings_eeg_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String m_Text = input.getText().toString();
                                        F_Settings.this.actividadPrincipal.getUserConf().setUserName(m_Text);

                                    }
                                });
                                builder.setNegativeButton(R.string.Settings_eeg_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }
                        }
                        break;
                }
            }
        };
        this.ivCustomize.setOnClickListener(this.ocl);
        this.ivExport.setOnClickListener(this.ocl);

        this.ivEEG.setOnClickListener(this.ocl);

    }

    private void loadCustomSettings() {
        this.actividadPrincipal.getfCenter().loadNewFragment(R.id.Settings_ivOptions);
    }

    public void onAttach(Context context) {
        super.onAttach(getActivity());

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void retornar() {
        MainActivity ma = (MainActivity) getActivity();
        FCenterContent centerContent = ma.getfCenter();

        centerContent.checkUI();

        FragmentTransaction fManagerTransaction = getFragmentManager().beginTransaction();
        //fManagerTransaction.replace(this.getId(), fg);
        fManagerTransaction.remove(this);
        fManagerTransaction.show(centerContent);
        fManagerTransaction.commit();
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
