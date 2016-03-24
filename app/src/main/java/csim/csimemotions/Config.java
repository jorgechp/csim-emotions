package csim.csimemotions;

/**
 * Configuracion de diversos parametros del juego
 * Created by jorge on 29/12/15.
 */
public class Config {
    /**
     * Numero de pruebas en el nivel 0
     */
    public static final byte LEVEL_0_NUM_OF_STAGES = 7;
    /**
     * Numero de pruebas en el nivel 1
     */
    public static final byte LEVEL_1_NUM_OF_STAGES = 7;
    /**
     * Numero de pruebas en el nivel 2
     */
    public static final byte LEVEL_2_NUM_OF_STAGES = 12;
    /**
     * Numero de pruebas en el nivel 3
     */
    public static final byte LEVEL_3_NUM_OF_STAGES = 14;
    /**
     * Numero de pruebas en el modo EEG
     */
    public static final byte LEVEL_EEG_NUM_OF_STAGES = 5;

    /**
     * Tiempo maximo de la prueba completa, nivel 0
     */
    public static final int LEVEL_0_TIME = -1;

    /**
     * Tiempo maximo de la prueba completa, nivel 1
     */
    public static final int LEVEL_1_TIME = 60000;

    /**
     * Tiempo maximo de la prueba completa, nivel 2
     */
    public static final int LEVEL_2_TIME = 40000;

    /**
     * Tiempo maximo de la prueba completa, nivel 3
     */
    public static final int LEVEL_3_TIME = 35000;

    /**
     * Establece el tiempo de cada etapa en el modo EEG
     */
    public static final int EEG_MODE_TIME = 7000;

    /**
     * Establece el tiempo, en el modo EEG, previo al inicio de la evaluación
     */
    public static final int EEG_MODE_PRE_TIME = 7000;

    /**
     * Tiempo de muestra del feedback. en milisegundos
     */
    public static final short TIME_FEEDBACK = 4000;

    /**
     * Tiempo de aparicion de las imagenes en la UI tras el feedback
     */
    public static final short TIME_IMAGES = 4000;

    /**
     * En el menu de juegos, margen entre diferentes layers de niveles de juegos
     */
    public static final float STAGES_WIDTH = (float) 0.1475;

    /**
     * Color de fondo de los layers de juegos bloqueados.
     */
    public static final int COLOR_CLOSED_STAGES = 0x00706377;

    /**
     * Nombre del fichero con la configuración del usuario
     */
    public static final String USER_CONFIG_FILENAME = "userConfig.data";

    /**
     * Nombre del fichero de registro de eventos de usuarios
     */
    public static final String USER_LOG_FILENAME = "logHistory.log";

    /**
     * Nombre del fichero de registro de eventos de usuarios, en formato CSV
     */
    public static final String USER_LOG_FOLDER_CSV = "CSIM";



    /**
     * Dirección web de la interfaz de comunicación con la web
     */
    public static final String DATABASE_URL = "http://csimemotions.webcindario.com/";


}
