package csim.csimemotions;

/**
 * Created by jorge on 24/12/15.
 */
public interface IPersistencia {

    public void insertarImagen(Imagen im);

    public void actualizarImagen(Imagen im, String oldName);

    public void eliminarImagen(Imagen im);


    public void insertarSonido(Sonido s);

    public void actualizarSonido(Sonido s, String oldName);

    public void eliminarSonido(Sonido s);


    public String[][] getUrlImagen(String nombre, Emotions categoria, int dificultad);

    public String[][] getUrlSonido(String nombre, Emotions categoria);

    public int getNumRowsImagenes();

    public int getNumRowsImagenes(Emotions category, int dificultad);

    public int getNumRowsSonidos();

    public int getNumRowsSonidos(Emotions category);

    public int getVersion();
}
