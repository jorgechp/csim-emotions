package csim.csimemotions;


import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedHashSet;

/**
 * Los valores de las propiedades de éste objeto se serializan para que sean conservados.
 *
 *
 * State of the Game
 * Created by jorge on 30/12/15.
 */
public class StateOfGame implements Serializable{


    /***
     * Representa el máximo nivel alcanzado en el juego
     */
    private byte level;
    /**
     * Representa el nivel actual del usuario
     */
    private byte levelActual;
    private Hashtable<States, LinkedHashSet<Byte>> estado;
    private States lastState;
    /**
     * Inserta estados de juegos en los que el usuario ha renunciado a recibir la ayuda inicial
     */
    private LinkedHashSet<States> noMoreHelpPlease;


    public StateOfGame() {
        init();
        iniciarEstado();
    }


    private void iniciarEstado(){
        this.level = 1;
        this.levelActual = 1;
    }
    public void init() {
        iniciarEstado();
        this.estado = new Hashtable<States, LinkedHashSet<Byte>>();
        this.noMoreHelpPlease = new LinkedHashSet<States>();
    }

    public void init(byte level, States lastState, Hashtable<States, LinkedHashSet<Byte>> listStates) {
        iniciarEstado();
        this.estado = listStates;
        this.lastState = lastState;
        this.noMoreHelpPlease = new LinkedHashSet<States>();
    }


    public byte getLevel() {
        return level;
    }

    public States getEstado() {
        return lastState;
    }

    public void increaseLevel() {
        ++level;
    }

    /**
     * Añade un juego y un nivel en el estado del juego, si el juego ya existe, se añade el nivel
     *
     * @param st
     * @param difficulty
     */
    public void chargeReward(States st, byte difficulty) {
        if (this.estado.containsKey(st) == false) {
            LinkedHashSet<Byte> listaNiveles = new LinkedHashSet<>();
            listaNiveles.add(difficulty);
            this.estado.put(st, listaNiveles);
        } else {
            //Recupera el LinkedHashSet<Integer> con la lista de niveles, y se le añade el nuevo nivel
            this.estado.get(st).add(difficulty);
        }
        lastState = st;
    }

    /**
     * Comprueba si el usuario ha jugado a un juego en un nivel de dificultad determinado
     *
     * @param game
     * @param difficultyLevel
     * @return boolean
     */
    public boolean isGamePlayed(States game, byte difficultyLevel) {
        boolean resultado = false;

        if (this.estado.containsKey(game)) {
            resultado = this.estado.get(game).contains(difficultyLevel);
        }
        return resultado;

    }

    public byte getLevelActual() {
        return levelActual;
    }

    public void setLevelActual(byte levelActual) {
        this.levelActual = levelActual;
    }

    public void addNoHelpDialog(States game){
        this.noMoreHelpPlease.add(game);


    }

    public boolean isDialogNoHelp(States game){
        return this.noMoreHelpPlease.contains(game);
    }


}
