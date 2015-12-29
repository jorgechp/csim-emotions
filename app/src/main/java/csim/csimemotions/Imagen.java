package csim.csimemotions;

import android.graphics.Bitmap;

/**
 * Created by jorge on 24/12/15.
 */
public class Imagen {
    private String nombre;
    private Emotions categoria;
    private int dificultad;
    private Bitmap image;

    public Imagen(String nombre, Emotions categoria, int dificultad, Bitmap image) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.dificultad = dificultad;
        this.image = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Emotions getCategoria() {
        return categoria;
    }

    public void setCategoria(Emotions categoria) {
        this.categoria = categoria;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
