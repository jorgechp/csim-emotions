package layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import csim.csimemotions.Config;
import csim.csimemotions.IRetornable;
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

    private OnFragmentInteractionListener mListener;


    private ImageButton ibSettingsGames, ibSettings, ibSteps, ibChangeUser;
    private View.OnClickListener onclick;
    private boolean isSettings, isSettingsOptions;
    private MainActivity actividadPrincipal;
    private F_Settings fgSettings;
    private IRetornable fgSettingsOptions;
    private FCenterContent fCenterContent;
    private boolean isGameMode;
    private TextView tvGamePoints;


    public FBarUp() {
        // Required empty public constructor
    }

    public void setFgSettingsOptions(IRetornable fgSettingsOptions) {
        this.fgSettingsOptions = fgSettingsOptions;
    }

    public void setIsSettingsOptions(boolean isSettingsOptions) {
        this.isSettingsOptions = isSettingsOptions;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment FBarUp.
     */

    public static FBarUp newInstance() {
        FBarUp fragment = new FBarUp();
        Bundle args = new Bundle();

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
        this.fgSettings = (F_Settings) this.fCenterContent.loadNewFragment(R.id.ibGames);
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

        tvGamePoints = (TextView) getView().findViewById(R.id.tvPoints);
        this.ibSettingsGames = (ImageButton) getActivity().findViewById(R.id.ibGames);
        this.ibSettings = (ImageButton) getActivity().findViewById(R.id.ibPreferencias);
        this.ibSteps = (ImageButton) getActivity().findViewById(R.id.ibArcadeMode);
        this.ibChangeUser = (ImageButton) getActivity().findViewById(R.id.ib_changeUser);
        this.onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.ib_changeUser:
                        AlertDialog.Builder builderPassword = new AlertDialog.Builder(FBarUp.this.actividadPrincipal);
                        builderPassword.setTitle(R.string.FOptions_settings_saveNickModalTitle);

                        final AlertDialog.Builder builderNick = new AlertDialog.Builder(FBarUp.this.actividadPrincipal);
                        builderNick.setTitle(R.string.FOptions_settings_newNickTitle);


                        // Set up the input
                        final EditText inputPassword = new EditText(FBarUp.this.actividadPrincipal);
                        final EditText inputNick = new EditText(FBarUp.this.actividadPrincipal);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        inputNick.setInputType(InputType.TYPE_CLASS_TEXT);
                        builderPassword.setView(inputPassword);
                        builderNick.setView(inputNick);

                        // Set up the buttons
                        builderPassword.setPositiveButton(R.string.FOptions_settings_saveNickModalAccept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String message = null;
                                if (inputPassword.getText().toString().equals(Config.OPTIONS_PASSWORD)) {
                                    builderNick.show();
                                } else {
                                    message = getString(R.string.FOptions_settings_saveNickToastCanceled);
                                }

                                Toast toast = Toast.makeText(
                                        FBarUp.this.actividadPrincipal,
                                        message,
                                        Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                        builderPassword.setNegativeButton(R.string.FOptions_settings_saveNickModalCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        // Set up the buttons
                        builderNick.setPositiveButton(R.string.FOptions_settings_saveNickModalAccept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String message = null;
                                String text = inputNick.getText().toString();
                                if (text.length() > 0) {
                                    FBarUp.this.actividadPrincipal.appendUser(text);
                                    FBarUp.this.actividadPrincipal.saveUserConfig();
                                    fCenterContent.checkUI();

                                    message = getString(R.string.FOptions_settings_saveNickToastAccepted);
                                } else {
                                    message = getString(R.string.FOptions_settings_saveNickToastCanceledNoInput);
                                }


                            }
                        });
                        builderNick.setNegativeButton(R.string.FOptions_settings_saveNickModalCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builderPassword.show();

                        break;
                    case R.id.ibArcadeMode:
                        actividadPrincipal.getfCenter().emptyArcadeQueue();
                        actividadPrincipal.getfCenter().startArcadeMode();
                        actividadPrincipal.getfCenter().processateArcadeMode();


                        break;
                    case R.id.ibPreferencias:
                        FBarUp.this.loadSettings();
                        FBarUp.this.ibSettings.setVisibility(View.INVISIBLE);
                        isSettingsOptions= false;

                        break;
                    case R.id.ibGames:
                        actividadPrincipal.getfCenter().emptyArcadeQueue();
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
        this.ibSteps.setOnClickListener(this.onclick);
        this.ibChangeUser.setOnClickListener(this.onclick);

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
            this.ibSteps.setVisibility(View.VISIBLE);

        }else {
            this.fgSettings.retornar();
        }
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

    public void updateBarUp() {
        if (this.tvGamePoints != null) {
            this.tvGamePoints.setText(Long.toString(actividadPrincipal.getUserConf().getPoints()));
        }
    }


}
