package csim.csimemotions;


import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * Los valores de las propiedades de éste objeto se serializan para que sean conservados.
 *
 *
 * State of the Game
 * Created by jorge on 30/12/15.
 */
public class StateOfGame implements Serializable{
    private static StateOfGame INSTANCE = new StateOfGame();

    /***
     * Representa el máximo nivel alcanzado en el juego
     */
    private byte level;
    /**
     * Representa el nivel actual del usuario
     */
    private byte levelActual;
    private LinkedHashSet<States> estado;
    private States lastState;
    /**
     * Inserta estados de juegos en los que el usuario ha renunciado a recibir la ayuda inicial
     */
    private LinkedHashSet<States> noMoreHelpPlease;



    private StateOfGame() {
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StateOfGame();
        }
    }

    public static StateOfGame getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    private void iniciarEstado(){
        this.level = 0;
        this.levelActual = 0;
    }
    public void init() {
        iniciarEstado();
        this.estado = new LinkedHashSet<States>();
        this.noMoreHelpPlease = new LinkedHashSet<States>();
    }

    public void init(byte level, States lastState, LinkedHashSet<States> listStates) {
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

    public void chargeReward(States st) {
        if (this.estado.contains(st) == false) {
            this.estado.add(st);
            lastState = st;
        }
    }

    public boolean isGamePlayed(States game) {
        return this.estado.contains(game);
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
