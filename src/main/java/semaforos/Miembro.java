package semaforos;

public class Miembro {
    private String id;
    private String nombre;
    private String puesto;
    private boolean libre;
    private boolean esResponsable;

    public Miembro(String id, String nombre, String puesto, boolean libre, boolean esResponsable) {
        this.id = id;
        this.nombre = nombre;
        this.puesto = puesto;
        this.libre = libre;
        this.esResponsable = esResponsable;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setLibre(boolean estado) {
        this.libre = estado;
    }

    public boolean isLibre() {
        return libre;
    }

    public boolean isEsResponsable() {
        return esResponsable;
    }
}
