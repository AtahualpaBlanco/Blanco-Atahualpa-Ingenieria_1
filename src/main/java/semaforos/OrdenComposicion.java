package semaforos;

import java.util.Date;

public class OrdenComposicion {
    private String nroOrden;
    private Date fechaReparacionProgramada;
    private Date fechaEfectivaReparacion;
    private String detalle;
    private Denuncia denuncia;
    private EquipoControl equipoAsignado;

    public OrdenComposicion(String nroOrden, Date fechaReparacionProgramada, String detalle, Denuncia denuncia) {
        this.nroOrden = nroOrden;
        this.fechaReparacionProgramada = fechaReparacionProgramada;
        this.detalle = detalle;
        this.denuncia = denuncia;
        this.fechaEfectivaReparacion = null;
        this.equipoAsignado = null;
    }

    public void completarReparacion(Date fechaEfectiva) {
        this.fechaEfectivaReparacion = fechaEfectiva;
        if (this.equipoAsignado != null) {
            this.equipoAsignado.liberarEquipo();
        }
        this.notificarDenunciante();
    }

    public boolean estaCompleta() {
        return this.fechaEfectivaReparacion != null;
    }

    public void notificarDenunciante() {
        if (denuncia != null && denuncia.getDenunciante() != null) {
            String mail = denuncia.getDenunciante().getMail();
            System.out.println("Enviando mail a: " + mail + " - La reparación del semáforo en la intersección " 
                    + denuncia.getCalleX() + " y " + denuncia.getCalleY() + " ha sido completada.");
        }
    }

    public String getNroOrden() {
        return nroOrden;
    }

    public Date getFechaReparacionProgramada() {
        return fechaReparacionProgramada;
    }

    public Date getFechaEfectivaReparacion() {
        return fechaEfectivaReparacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public EquipoControl getEquipoAsignado() {
        return equipoAsignado;
    }

    public void setEquipoAsignado(EquipoControl equipoAsignado) {
        this.equipoAsignado = equipoAsignado;
        if (equipoAsignado != null) {
            equipoAsignado.setEstado(EstadoEquipo.OCUPADO);
        }
    }
}
