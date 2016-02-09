package csim.csimemotions;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import csim.csimemotions.communication.HttpCom;
import layout.FCenterContent;


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
    private Button ib_ibPlayerSaveSong, b_saveNickName, b_SaveUserRating;
    private RatingBar  rt_UserRating;

    private EditText et_nickName;

    private View.OnClickListener playerClickListener, soundSelectionClickListener, nicknameListener, ratingUser;


    private MainActivity actividadPrincipal;
    private DataBaseController dbc;

    private OnFragmentInteractionListener mListener;

    private byte userSelectedSong;
    private Emotions userSelectedEmotion;

    private String[][] musicDatabase;
    private SoundPlayer sp;

    private boolean isPlaying;


    private Toast tSavedSong;
    private int idSongSelected;
    private UserConfig userConfig;

    private TextView tvt1, tvt2,tvt3, tvt4,tvt5;
    private TextView tvd1,tvd2,tvd3;



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
        this.isPlaying = false;
        this.idSongSelected = 0;
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

        this.tSavedSong = Toast.makeText(getActivity(),
                R.string.OptionsSettings_player_savedSongToast, Toast.LENGTH_SHORT);

        this.ib_playerAngry = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerAngry);
        this.ib_playerHappy = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerHappy);
        this.ib_playerSad = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerSad);
        this.ib_playerSurprised = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerSurprised);

        this.ib_playerBack = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerBack);
        this.ib_playerPlay = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerPlay);
        this.ib_playerNext = (ImageButton) getActivity().findViewById(R.id.OptionsSettings_ib_playerNext);

        this.ib_ibPlayerSaveSong = (Button) getActivity().findViewById(R.id.OptionsSettings_button_player_Save_song);
        this.b_saveNickName = (Button) getActivity().findViewById(R.id.OptionsSettings_btSaveNickName);
        this.b_SaveUserRating = (Button) getActivity().findViewById(R.id.OptionsSettings_btSaveUserRating);

        this.et_nickName = (EditText) getActivity().findViewById(R.id.OptionsSettings_et_Nickname);

        this.rt_UserRating = (RatingBar) getActivity().findViewById(R.id.OptionsSettings_rtBar);

        this.tvt1 = (TextView) getActivity().findViewById(R.id.OptionsSettings_tv_title1);
        this.tvt2 = (TextView) getActivity().findViewById(R.id.OptionsSettings_tv_title2);
        this.tvt3 = (TextView) getActivity().findViewById(R.id.OptionsSettings_tv_title3);
        this.tvt4 = (TextView) getActivity().findViewById(R.id.OptionsSettings_tv_title4);
        this.tvt5 = (TextView) getActivity().findViewById(R.id.OptionsSettings_tv_title5);

        this.tvd1 = (TextView) getActivity().findViewById(R.id.OptionsSettings_tv_description1);
        this.tvd2 = (TextView) getActivity().findViewById(R.id.OptionsSettings_tv_description2);
        this.tvd3 = (TextView) getActivity().findViewById(R.id.OptionsSettings_tv_description3);

        this.userSelectedSong = 0;
        this.userSelectedEmotion = Emotions.HAPPY;

        this.actividadPrincipal = (MainActivity) getActivity();
        this.actividadPrincipal.stopSong();
        this.dbc = this.actividadPrincipal.getDataBaseController();

        this.loadMusicDatabase();
        userConfig = this.actividadPrincipal.getUserConf();
        this.et_nickName.setText(userConfig.getUserName());
        this.rt_UserRating.setRating(userConfig.getUserRating());

        this.playerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numOfSongs = F_settings_options_menu.this.musicDatabase.length;
                int numberOfselectedSong = F_settings_options_menu.this.userSelectedSong;

                if (F_settings_options_menu.this.sp != null) {
                    F_settings_options_menu.this.sp.destroy();
                }
                switch (v.getId()) {
                    case R.id.OptionsSettings_ib_playerBack:
                        F_settings_options_menu.this.ib_playerPlay.setBackgroundResource(R.mipmap.ic_play);
                        --numberOfselectedSong;
                        F_settings_options_menu.this.userSelectedSong = (byte) ((numberOfselectedSong < 0) ? (numOfSongs - (Math.abs(numberOfselectedSong) % numOfSongs)) % numOfSongs : (numberOfselectedSong % numOfSongs));
                        break;
                    case R.id.OptionsSettings_ib_playerPlay:
                        if (F_settings_options_menu.this.isPlaying) {
                            F_settings_options_menu.this.ib_playerPlay.setBackgroundResource(R.mipmap.ic_play);

                        } else {
                            String[] selectedSong = F_settings_options_menu.this.musicDatabase[F_settings_options_menu.this.userSelectedSong];
                            idSongSelected = Integer.parseInt(selectedSong[2]);
                            F_settings_options_menu.this.sp = new SoundPlayer(idSongSelected);
                            F_settings_options_menu.this.sp.play(getActivity());
                            F_settings_options_menu.this.ib_playerPlay.setBackgroundResource(R.mipmap.ic_player_stop);
                        }
                        F_settings_options_menu.this.isPlaying = !F_settings_options_menu.this.isPlaying;
                        break;
                    case R.id.OptionsSettings_ib_playerNext:
                        F_settings_options_menu.this.ib_playerPlay.setBackgroundResource(R.mipmap.ic_play);
                        F_settings_options_menu.this.userSelectedSong = (byte) (F_settings_options_menu.this.userSelectedSong + 1);
                        F_settings_options_menu.this.userSelectedSong %= numOfSongs;
                        break;
                    case R.id.OptionsSettings_button_player_Save_song:

                        F_settings_options_menu.this.ib_playerPlay.setBackgroundResource(R.mipmap.ic_play);

                        userConfig.setBackgroundEmotion(F_settings_options_menu.this.userSelectedEmotion);
                        userConfig.setBackgroundSoundNumber(F_settings_options_menu.this.userSelectedSong);
                        userConfig.setIdSongSelected(idSongSelected);
                        F_settings_options_menu.this.actividadPrincipal.saveUserConfig();
                        F_settings_options_menu.this.tSavedSong.show();
                        break;
                }
            }
        };

        this.soundSelectionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F_settings_options_menu.this.enableButtons();
                switch (v.getId()) {
                    case R.id.OptionsSettings_ib_playerAngry:
                        F_settings_options_menu.this.ib_playerAngry.setImageResource(R.color.color_disabled_element);
                        F_settings_options_menu.this.userSelectedEmotion = Emotions.ANGRY;
                        break;
                    case R.id.OptionsSettings_ib_playerHappy:
                        F_settings_options_menu.this.ib_playerHappy.setImageResource(R.color.color_disabled_element);
                        F_settings_options_menu.this.userSelectedEmotion = Emotions.HAPPY;
                        break;
                    case R.id.OptionsSettings_ib_playerSad:
                        F_settings_options_menu.this.ib_playerSad.setImageResource(R.color.color_disabled_element);
                        F_settings_options_menu.this.userSelectedEmotion = Emotions.SAD;
                        break;
                    case R.id.OptionsSettings_ib_playerSurprised:
                        F_settings_options_menu.this.ib_playerSurprised.setImageResource(R.color.color_disabled_element);
                        F_settings_options_menu.this.userSelectedEmotion = Emotions.SURPRISED;
                        break;
                }
                F_settings_options_menu.this.loadMusicDatabase();
            }
        };

        this.nicknameListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.OptionsSettings_btSaveNickName:
                        String text = F_settings_options_menu.this.et_nickName.getText().toString();
                        if(text.length() > 0 ) {
                            userConfig.setUserName(text);
                            F_settings_options_menu.this.actividadPrincipal.saveUserConfig();


                        }
                        break;
                }
            }
        };

        this.ratingUser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.OptionsSettings_btSaveUserRating:
                        float rating = F_settings_options_menu.this.rt_UserRating.getRating();
                        userConfig.setUserRating(rating);
                        F_settings_options_menu.this.actividadPrincipal.saveUserConfig();
                        break;
                }
            }
        };


        this.ib_playerAngry.setOnClickListener(this.soundSelectionClickListener);
        this.ib_playerHappy.setOnClickListener(this.soundSelectionClickListener);
        this.ib_playerSad.setOnClickListener(this.soundSelectionClickListener);
        this.ib_playerSurprised.setOnClickListener(this.soundSelectionClickListener);

        this.ib_playerBack.setOnClickListener(this.playerClickListener);
        this.ib_playerPlay.setOnClickListener(this.playerClickListener);
        this.ib_playerNext.setOnClickListener(this.playerClickListener);
        this.ib_ibPlayerSaveSong.setOnClickListener(this.playerClickListener);

        this.b_saveNickName.setOnClickListener(this.nicknameListener);
        this.b_SaveUserRating.setOnClickListener(this.ratingUser);
        Typeface tfTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Action_Man_Bold.ttf");
        Typeface tfFontsButtons = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AndikaNewBasic-B.ttf");

        this.tvt1.setTypeface(tfTitle);
        this.tvt2.setTypeface(tfTitle);
        this.tvt3.setTypeface(tfTitle);
        this.tvt4.setTypeface(tfTitle);
        this.tvt5.setTypeface(tfTitle);

        this.tvd1.setTypeface(tfFontsButtons);
        this.tvd2.setTypeface(tfFontsButtons);
        this.tvd3.setTypeface(tfFontsButtons);

    }

    private void loadMusicDatabase() {
        this.musicDatabase = this.dbc.getUrlSonido(null, this.userSelectedEmotion);
        this.userSelectedSong = 0;
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

        FCenterContent centerContent = this.actividadPrincipal.getfCenter();


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


    private void enableButtons() {
        this.ib_playerAngry.setImageResource(android.R.color.transparent);
        this.ib_playerHappy.setImageResource(android.R.color.transparent);
        this.ib_playerSurprised.setImageResource(android.R.color.transparent);
        this.ib_playerSad.setImageResource(android.R.color.transparent);
    }
}
