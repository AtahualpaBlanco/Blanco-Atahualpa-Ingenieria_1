package semaforos;

import java.util.List;
import java.util.ArrayList;

public class Semaforo {
    private int nro;
    private String estado;
    private String ubicacion;
    private TipoFaro tipoFaro;
    private List<Luz> luces;
    private List<Denuncia> historicoDenuncias;

    public Semaforo(int nro, String estado, String ubicacion, TipoFaro tipoFaro) {
        this.nro = nro;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.tipoFaro = tipoFaro;
        this.luces = new ArrayList<>();
        this.historicoDenuncias = new ArrayList<>();

        // Se crean en conjunto con el semáforo (3 luces)
        this.agregarLuz(new Luz("SERIE-" + nro + "-1", "FabricanteGenérico", "LED", "Rojo"));
        this.agregarLuz(new Luz("SERIE-" + nro + "-2", "FabricanteGenérico", "LED", "Amarillo"));
        this.agregarLuz(new Luz("SERIE-" + nro + "-3", "FabricanteGenérico", "LED", "Verde"));
    }

    public int getNro() {
        return nro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public TipoFaro getTipoFaro() {
        return tipoFaro;
    }

    public void agregarLuz(Luz luz) {
        this.luces.add(luz);
    }

    public List<Luz> getLuces() {
        return luces;
    }

    public void registrarDenuncia(Denuncia denuncia) {
        this.historicoDenuncias.add(denuncia);
    }

    public List<Denuncia> getHistoricoDenuncias() {
        return historicoDenuncias;
    }

    public int getCantidadDenuncias() {
        return historicoDenuncias.size();
    }
}
