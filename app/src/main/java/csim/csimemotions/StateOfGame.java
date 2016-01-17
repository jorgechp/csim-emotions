package csim.csimemotions;


import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * State of the Game
 * Created by jorge on 30/12/15.
 */
public class StateOfGame implements Serializable{
    private static StateOfGame INSTANCE = new StateOfGame();

    private byte level;
    private LinkedHashSet<States> estado;
    private States lastState;



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

    public void init() {
        this.level = 0;
        this.estado = new LinkedHashSet<States>();
    }

    public void init(byte level, States lastState, LinkedHashSet<States> listStates) {
        this.level = 0;
        this.estado = listStates;
        this.lastState = lastState;
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

}
