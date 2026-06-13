package semaforos;

import java.util.List;
import java.util.ArrayList;

public class EquipoControl {
    private String codigo;
    private String especialidad;
    private EstadoEquipo estado;
    private List<Miembro> miembros;

    public EquipoControl(String codigo, String especialidad, List<Miembro> miembros) {
        this.codigo = codigo;
        this.especialidad = especialidad;
        this.miembros = miembros != null ? miembros : new ArrayList<>();
        this.estado = EstadoEquipo.LIBRE; // El estado inicial es Libre
    }

    public void liberarEquipo() {
        this.estado = EstadoEquipo.LIBRE;
        for (Miembro miembro : miembros) {
            miembro.setLibre(true);
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public List<Miembro> getMiembros() {
        return miembros;
    }

    public int getCantidadMiembros() {
        return miembros.size();
    }
}
