package csim.csimemotions;

/**
 * Configuracion de diversos parametros del juego
 * Created by jorge on 29/12/15.
 */
public class Config {
    /**
     * Numero de pruebas en el nivel 0
     */
    public static final byte LEVEL_0_NUM_OF_STAGES = 3;
    /**
     * Numero de pruebas en el nivel 1
     */
    public static final byte LEVEL_1_NUM_OF_STAGES = 20;
    /**
     * Numero de pruebas en el nivel 2
     */
    public static final byte LEVEL_2_NUM_OF_STAGES = 25;
    /**
     * Numero de pruebas en el nivel 3
     */
    public static final byte LEVEL_3_NUM_OF_STAGES = 30;


    /**
     * Tiempo maximo de la prueba completa, nivel 0
     */
    public static final byte LEVEL_0_TIME = -1;

    /**
     * Tiempo maximo de la prueba completa, nivel 1
     */
    public static final byte LEVEL_1_TIME = 60;

    /**
     * Tiempo maximo de la prueba completa, nivel 2
     */
    public static final byte LEVEL_2_TIME = 40;

    /**
     * Tiempo maximo de la prueba completa, nivel 3
     */
    public static final byte LEVEL_3_TIME = 35;

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
    public static final float STAGES_WIDTH = (float) 0.0475;



}
