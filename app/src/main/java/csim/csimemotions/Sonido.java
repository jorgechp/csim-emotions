package csim.csimemotions;

/**
 * Created by jorge on 26/12/15.
 */
public class Sonido {
    private String nombre;
    private Emotions categoria;
    private int identificador;


    public Sonido(String nombre, Emotions categoria, int identificador) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.identificador = identificador;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getIdentificador() {
        return this.identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
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
}
