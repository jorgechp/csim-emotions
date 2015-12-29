package csim.csimemotions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Special thanks to http://www.sgoliver.net/blog/bases-de-datos-en-android-i-primeros-pasos/
 */
public class DataBase extends SQLiteOpenHelper {

    private String sqlCreate1 = "CREATE TABLE Imagenes (nombre TEXT, categoriaEmocion TEXT, dificultad INTEGER)";
    private String sqlCreate2 = "CREATE TABLE Sonidos (nombre TEXT, categoriaEmocion TEXT, identificador INTEGER)";

    public DataBase(Context contexto, String nombre,
                    CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate1);
        db.execSQL(sqlCreate2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {


        //Se elimina la versión anterior de la tabla
        this.removeDB(db);


        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate1);
        db.execSQL(sqlCreate2);
    }

    private void removeDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS Imagenes");
        db.execSQL("DROP TABLE IF EXISTS Sonidos");
    }


}