package csim.csimemotions.log;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import com.opencsv.CSVWriter;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import csim.csimemotions.MainActivity;



/**
 * Created by jorge on 26/01/16.
 */
public class LogManager {
    private LinkedList<Log> sessionLog;
    private MainActivity actividadPrincipal;
    //private HttpCom comunicadorWeb;
    private static LogManager ourInstance = new LogManager();

    public static LogManager getInstance() {
        return ourInstance;
    }

    private LogManager() {
        sessionLog = new LinkedList();
       // comunicadorWeb = new HttpCom(Config.DATABASE_URL);
    }

    public void addLog(Log l){
        this.sessionLog.add(l);
    }

    public void setActividadPrincipal(MainActivity actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }


    private File saveAsCSV(){
        List<String[]> list = null;
        Log l = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            Timestamp tm = new Timestamp(System.currentTimeMillis());
            String dir = new SimpleDateFormat("MMdd").format(tm);

            File myDir = new File(root + "/csim_emotions_csv/"+dir);
            myDir.mkdirs();
            String fname = "log_"+tm.toString()+".csv";
            File fileOutPut = new File (myDir, fname);
            if (!fileOutPut.exists()) {
                fileOutPut.createNewFile();
            }
            CSVWriter writer = new CSVWriter(new FileWriter(fileOutPut.getAbsoluteFile(),false));
            l = null;
            list = new ArrayList<String[]>();

            list.add(new String[]{"idUser", "timestamp", "isEEG", "game", "stage", "difficulty", "correctAnswer", "userAnswer", "timestampStage"});

            while(this.sessionLog.size() > 0){
                l = this.sessionLog.poll();
                list.addAll(l.getList());
            }
            writer.writeAll(list);
            writer.flush();
            return fileOutPut;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void saveDataBase() {
        Map<String,String> params = new HashMap<String, String>();
        ArrayList<String[]> listaStages = null;
        int stageNumber = 0;

        params.put("uploadLog","0");

        while(this.sessionLog.size() > 0){
            listaStages = this.sessionLog.poll().getList();
            for(String[] stage : listaStages){
                params.put("idUser_"+stageNumber,stage[0]);
                params.put("timestamp_"+stageNumber,stage[1]);
                params.put("isEEG_"+stageNumber,stage[2]);
                params.put("game_"+stageNumber,stage[3]);
                params.put("stage_"+stageNumber,stage[4]);
                params.put("difficulty_"+stageNumber,stage[5]);
                params.put("correctAnswer_"+stageNumber,stage[6]);
                params.put("userAnswer_" + stageNumber, stage[7]);
                params.put("timestampStage_"+stageNumber,stage[8]);
                ++stageNumber;
            }

        }

        params.put("numberOfStages",Integer.toString(stageNumber));

        //this.comunicadorWeb.communicate(params);

    }
    /*
     * http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.actividadPrincipal.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Intent export(){
        this.save();
        File f = this.saveAsCSV();
        this.sessionLog.clear();
        //serialize();
        Intent i = null;

        i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        i.setType("*/*");


        return i;
    }

    public void save(){
        this.saveAsCSV();
    }


}
