package csim.csimemotions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jorge on 24/12/15.
 */
public class DataBaseController implements IPersistencia {
    private SQLiteDatabase db;
    private String nombreImagenes, nombreSonidos;
    private String[] camposImagenes = new String[]{"nombre", "categoriaEmocion", "dificultad"};
    private String[] camposSonidos = new String[]{"nombre", "categoriaEmocion", "identificador"};

    public DataBaseController(String nombreImagenes, String nombreSonidos, Context context) {
        this.db = new DataBase(context, nombreImagenes, null, 1).getWritableDatabase();
        this.nombreImagenes = nombreImagenes;
        this.nombreSonidos = nombreSonidos;
    }


    @Override
    public void insertarImagen(Imagen im) {
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("nombre", im.getNombre());
        nuevoRegistro.put("categoriaEmocion", im.getCategoria().name());
        nuevoRegistro.put("dificultad", im.getDificultad());

        this.db.insert(this.nombreImagenes, null, nuevoRegistro);
    }

    @Override
    public void actualizarImagen(Imagen im, String oldName) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", im.getNombre());
        valores.put("categoriaEmocion", im.getCategoria().name());
        valores.put("dificultad", im.getDificultad());

        this.db.update(this.nombreImagenes, valores, "nombre=" + oldName, null);
    }

    @Override
    public void eliminarImagen(Imagen im) {
        this.db.delete(this.nombreImagenes, "nombre=" + im.getNombre(), null);
    }

    @Override
    public void insertarSonido(Sonido s) {
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("nombre", s.getNombre());
        nuevoRegistro.put("categoriaEmocion", s.getCategoria().name());
        nuevoRegistro.put("identificador", s.getIdentificador());

        this.db.insert(this.nombreSonidos, null, nuevoRegistro);
    }

    @Override
    public void actualizarSonido(Sonido s, String oldName) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", s.getNombre());
        valores.put("categoriaEmocion", s.getCategoria().name());
        valores.put("identificador", s.getIdentificador());

        this.db.update(this.nombreSonidos, valores, "nombre=" + oldName, null);
    }

    @Override
    public void eliminarSonido(Sonido s) {
        this.db.delete(this.nombreSonidos, "nombre=" + s.getNombre(), null);
    }

    @Override
    public String[][] getUrlImagen(String nombre, Emotions categoria, int dificultad) {

        ArrayList<String[]> listaImagenes = new ArrayList<String[]>();
        String consultaStart = "SELECT * FROM imagenes";
        String consultaWhere = "";

        if (nombre != null) {
            consultaWhere += "nombre LIKE '" + nombre + "'";
        }
        if (categoria != null) {
            if (consultaWhere.length() > 0) {
                consultaWhere += " AND ";
            }
            consultaWhere += "categoriaEmocion LIKE '" + categoria + "'";
        }
        if (dificultad != -1) {
            if (consultaWhere.length() > 0) {
                consultaWhere += " AND ";
            }
            consultaWhere += "dificultad = '" + dificultad + "'";
        }

        String selectQuery = (consultaWhere.length() == 0) ? (consultaStart) : (consultaStart + " WHERE " + consultaWhere);
        Cursor c = db.rawQuery(selectQuery, null);

        //Cursor c = db.query(this.nombreImagenes, this.camposImagenes, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                String imNombre = c.getString(0);
                String imCategoria = c.getString(1);
                String imDificultad = Integer.toString(c.getInt(2));

                listaImagenes.add(new String[]{imNombre, imCategoria, imDificultad});


            } while (c.moveToNext());
        }

        return Arrays.copyOf(listaImagenes.toArray(), listaImagenes.size(), String[][].class);


    }

    @Override
    public String[][] getUrlSonido(String nombre, Emotions categoria) {
        ArrayList<String[]> listaSonidos = new ArrayList<String[]>();
        String consultaStart = "SELECT * FROM sonidos";
        String consultaWhere = "";

        if (nombre != null) {
            consultaWhere += "nombre LIKE'" + nombre + "'";
        }
        if (categoria != null) {
            consultaWhere += "categoriaEmocion LIKE'" + categoria + "'";
        }

        String selectQuery = (consultaWhere.length() == 0) ? (consultaStart) : (consultaStart + " WHERE " + consultaWhere);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                String imNombre = c.getString(0);
                String imCategoria = c.getString(1);
                String id = Integer.toString(c.getInt(2));

                listaSonidos.add(new String[]{imNombre, imCategoria, id});


            } while (c.moveToNext());
        }

        return Arrays.copyOf(listaSonidos.toArray(), listaSonidos.size(), String[][].class);

    }

    public int getNumRowsImagenes() {
        Cursor c = db.query(this.nombreImagenes, this.camposImagenes, null, null, null, null, null);
        return c.getCount();
    }

    public int getNumRowsImagenes(Emotions category) {
        Cursor c = db.query(this.nombreImagenes, this.camposImagenes, "categoriaEmocion LIKE '" + category.name() + "'", null, null, null, null);
        return c.getCount();
    }

    /**
     * Obtiene el numero de sonidos en la base de datos
     *
     * @return
     */
    public int getNumRowsSonidos() {
        Cursor c = db.query(this.nombreSonidos, this.camposSonidos, null, null, null, null, null);
        return c.getCount();
    }

    /**
     * Obtiene el numero de sonidos existentes en la base de datos que se ajustan a una determinada categoria
     * @param category
     * @return
     */
    public int getNumRowsSonidos(Emotions category) {
        Cursor c = db.query(this.nombreSonidos, this.camposSonidos, "categoriaEmocion LIKE '" + category.name() + "'", null, null, null, null);
        return c.getCount();
    }

    /**
     * Devuelve la version de la base de datos
     * @return
     */
    public int getVersion() {
        return this.db.getVersion();
    }


}
