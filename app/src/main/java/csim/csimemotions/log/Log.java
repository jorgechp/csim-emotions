package csim.csimemotions.log;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;


import csim.csimemotions.StateOfGame;

/**
 * Created by jorge on 26/01/16.
 */
public class Log implements Serializable {
    private String user;
    private long timestamp;
    private boolean isEEG;
    private ArrayList<LogStage> logStage;

    public Log(String user, long timestamp, boolean isEEG) {
        this.user = user;
        this.timestamp = timestamp;
        this.logStage = new ArrayList<LogStage>();
        this.isEEG = isEEG;
    }

    public ArrayList<LogStage> getLogStage() {
        return logStage;
    }

    public void setLogStage(ArrayList<LogStage> logStage) {
        this.logStage = logStage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void addStage(LogStage ls){
        this.logStage.add(ls);
    }

    public boolean isEEG() {
        return isEEG;
    }

    public void setIsEEG(boolean isEEG) {
        this.isEEG = isEEG;
    }

    public ArrayList<String[]> getList() {
        ArrayList<String[]> lista = new ArrayList<String[]>();

        String string[];

        for (LogStage ls: this.logStage
             ) {
                string = new String[9];
                string[0] = this.user;
                string[1] = Long.toString(this.timestamp);
                string[2] = Boolean.toString(this.isEEG);
                String stage[] = ls.getStrings();

                for(int i = 0; i < stage.length;++i){
                    string[i+3] = stage[i];
                }
            lista.add(string);

        }
        return lista;
    }
}
