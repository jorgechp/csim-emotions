package csim.csimemotions;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Hashtable;

import csim.csimemotions.log.LogManager;
import layout.FBarUp;
import layout.FCenterContent;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends ActionBarActivity {

    private Hashtable<String, UserConfig> users;

    private StateOfGame stateGame;
    private TemporalStateOfGame tStateGame;
    private UserConfig userConf;


    private FCenterContent fCenter;
    private FBarUp fUp;
    private SoundPlayer backgroundSound;

    private Generic_Game currentGame;

    private LogManager logMan;




    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private DataBaseController dbc;

    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            // mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
            //        | View.SYSTEM_UI_FLAG_FULLSCREEN
            //         | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            //        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            //        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            //      | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            // mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };



    public MainActivity() throws NotSerializableException {
        /**
         * Se instancia el estado del juego
         */
        this.stateGame = new StateOfGame();




    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mVisible = true;


        this.dbc = new DataBaseController("Imagenes", "Sonidos", this);

          // deleteDatabase("Imagenes");
          // deleteDatabase("Sonido");

        int rowsImagenes, rowsSonidos;
        try {
            rowsImagenes = this.dbc.getNumRowsImagenes();
            rowsSonidos = this.dbc.getNumRowsSonidos();
        } catch (RuntimeException r) {
            rowsImagenes = rowsSonidos = 0;
        }



        //Si no hay imagenes en la Base de Datos, esta se ha de crear.
        if (rowsImagenes == 0 || rowsSonidos == 0) {
            try {
                this.generateDataBase();
            } catch (IOException e) {
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            loadUserConfig();
            loadUserState();


        } catch (NotSerializableException e) {
            e.printStackTrace();
        }

        if(this.userConf.getIdSongSelected() == -1){
            this.loadDefaultSong();
        }
        this.backgroundSound = new SoundPlayer(this.userConf.getIdSongSelected());
        this.backgroundSound.play(this);

        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        //mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        //mContentView.setOnClickListener(new View.OnClickListener() {
        //   @Override
        //   public void onClick(View view) {
        //       toggle();
        //   }
        //});

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        this.tStateGame = TemporalStateOfGame.getInstance();
        this.tStateGame.setEnableLogging(true);
        this.tStateGame.setEnableEEG(false);
        this.logMan = LogManager.getInstance();
        this.logMan.setActividadPrincipal(this);



    }

    private void loadUserState() {
        if (this.userConf.getSog() != null) {
            this.stateGame = this.userConf.getSog();
        } else {
            this.stateGame = new StateOfGame();
            this.stateGame.init();
            this.userConf.setIdSongSelected(R.raw.eh1);

        }
    }

    /**
     * Carga desde un fichero el objeto serializado con las preferencias del usuario
     */
    private void loadUserConfig() throws java.io.NotSerializableException{

        // save the object to file
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = this.openFileInput(Config.USER_CONFIG_FILENAME);
            in = new ObjectInputStream(fis);
            this.userConf = (UserConfig) in.readObject();
            in.close();
        } catch (FileNotFoundException | InvalidClassException ex) {
            this.userConf = new UserConfig();
            this.loadDefaultSong();
            saveUserConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (this.userConf == null) {
            this.userConf = new UserConfig();

        }

        try {
            fis = this.openFileInput(Config.USERS_CONFIG_FILENAME);
            in = new ObjectInputStream(fis);
            users = (Hashtable<String, UserConfig>) in.readObject();
            in.close();
        } catch (FileNotFoundException|InvalidClassException ex) {
            users = new Hashtable<String, UserConfig>();
            saveUserConfig();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (this.users == null) {
            this.users = new Hashtable<String, UserConfig>();

        }


    }

    private void loadDefaultSong(){
        String[][] sonido = this.dbc.getUrlSonido(null, this.userConf.getBackgroundEmotionNumber());
        this.userConf.setIdSongSelected(Integer.parseInt(sonido[0][2]));
    }

    public void stopSong(){
        this.backgroundSound.destroy();
    }
    public void playSong() {
        this.backgroundSound.play(this);
    }

    /**
     * Serializa en un fichero el objeto con las preferencias de un usuario
     */
    public void saveUserConfig() {
        this.userConf.setSog(this.stateGame);
        // save the object to file
        FileOutputStream fos = null;
        ObjectOutputStream out = null;

        try {
            fos = this.openFileOutput(Config.USERS_CONFIG_FILENAME, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(this.users);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            fos = this.openFileOutput(Config.USER_CONFIG_FILENAME, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(this.userConf);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Carga la configuración de un usuario
     *
     * @param text
     */
    public void appendUser(String text) {
        if (this.users.containsKey(text) == false) {
            UserConfig newUserConfig = new UserConfig();


            this.userConf = newUserConfig;
            this.userConf.setUserName(text);
            this.stateGame = this.userConf.getSog();

            this.loadDefaultSong();
            this.users.put(text, this.userConf);
            saveUserConfig();
        } else {
            this.userConf = users.get(text);

        }
        loadUserState();
        fCenter.rebootUI();
        fCenter.checkUI();

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.fCenter.rebootUI();
        this.fCenter.checkUI();
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    /*****/


    /*****/


    @Override
    protected void onPause() {
        super.onPause();
        this.backgroundSound.destroy();
        if (this.isFinishing()) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.backgroundSound.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.backgroundSound.destroy();
    }

    public DataBaseController getDataBaseController() {
        return dbc;
    }

    public FCenterContent getfCenter() {
        return fCenter;
    }

    public void setfCenter(FCenterContent fCenter) {
        this.fCenter = fCenter;
    }

    public StateOfGame getStateOfTheGame() {
        return this.stateGame;
    }

    /**
     * Genera la base de datos insertando todas las imagenes que existan en el directorio drawable
     * <p/>
     * Las imagenes se identifican con el siguiente codigo
     * <p/>
     * EMOTIONCODE_DIFICULTAD_CATEGORIA_NUMERO.png
     */
    private void generateDataBase() throws IOException, IllegalAccessException {

        String[] images;
        Field[] fields;
        images = getAssets().list("imagesEmotion");
        Emotions category;


        for (String imgName : images) {
            String splittedName[] = imgName.split("_");

            String categoryStr = splittedName[0];
            int level = Integer.parseInt(splittedName[1]);
            category = null;

            switch (categoryStr) {
                case "ha":
                    category = Emotions.HAPPY;
                    break;
                case "a":
                    category = Emotions.ANGRY;
                    break;
                case "af":
                    category = Emotions.FEAR;
                    break;
                case "sa":
                    category = Emotions.SAD;
                    break;
            }


            this.dbc.insertarImagen(new Imagen(imgName, category, level, null));


        }

        fields = R.raw.class.getFields();

        int rid;
        String filename;
        for (int count = 0; count < fields.length; count++) {
            rid = fields[count].getInt(fields[count]);
            filename = fields[count].getName();

            if (filename.charAt(0) == 'e') { //e es la nomenclatura de los sonidos asociados a emociones
                char tipo = filename.charAt(1);

                switch (tipo) {
                    case 'a':
                        category = Emotions.ANGRY;
                        break;
                    case 'h':
                        category = Emotions.HAPPY;
                        break;
                    case 's':
                        category = Emotions.SAD;
                        break;
                    case 'f':
                        category = Emotions.FEAR;
                        break;
                    default:
                        category = Emotions.NONE;
                        break;
                }
                this.dbc.insertarSonido(new Sonido(filename, category, rid));
            }
        }
    }

    public void setfUp(FBarUp fUp) {
        this.fUp = fUp;
    }

    public FBarUp getfUp() {
        return fUp;
    }

    public UserConfig getUserConf() {
        return userConf;
    }

    public LogManager getLogMan() {
        return logMan;
    }

    public void setUserConf(UserConfig userConf) {
        this.userConf = userConf;
    }

    public TemporalStateOfGame getTemporalStateGame() {
        return tStateGame;
    }

    public Generic_Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Generic_Game currentGame) {
        this.currentGame = currentGame;
    }



    @Override
    public void onBackPressed() {
        if(this.currentGame != null){
            this.currentGame.backButtonPressed();
        }else{
            this.finish();
        }
    }
}
