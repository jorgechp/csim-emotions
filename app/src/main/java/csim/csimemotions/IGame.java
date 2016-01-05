package csim.csimemotions;

/**
 * Created by jorge on 4/01/16.
 * <p/>
 * <p/>
 * Define los metodos basicos que estan presentes en cualquier juego.
 * En primer lugar, continueGame procesa el resultado de la interaccion
 * con el jugador, generando una respuesta que debe ser procesada. Final
 * mente el jugado recibe feedback sobre sus acciones.
 */
public interface IGame {
    /**
     * Recoge la respuesta del usuario y prepara la siguiente fase del juego, devolviendo
     * un valor con el resultado del usuario.
     *
     * @return stageResults enumeracion
     */
    stageResults continueGame();

    /**
     * Proporciona feedback al jugador
     *
     * @param is_correct
     */
    void feedBack(boolean is_correct);

    /**
     * Procesa el resultado de la fase actual del juego
     *
     * @param respuesta
     */
    void procesarRespuesta(stageResults respuesta);

}
