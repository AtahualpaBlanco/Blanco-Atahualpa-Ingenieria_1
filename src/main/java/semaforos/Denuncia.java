package semaforos;

import java.util.Date;

public class Denuncia {
    private String codD;
    private Date fechaDenuncia;
    private String calleX;
    private String calleY;
    private String problema;
    private Prioridad prioridad;
    private Persona denunciante;
    private Semaforo semaforoAfectado;
    private OrdenComposicion ordenAsignada;

    public Denuncia(String codD, Date fechaDenuncia, String calleX, String calleY, String problema, Prioridad prioridad, Persona denunciante, Semaforo semaforoAfectado) {
        this.codD = codD;
        this.fechaDenuncia = fechaDenuncia;
        this.calleX = calleX;
        this.calleY = calleY;
        this.problema = problema;
        this.prioridad = prioridad;
        this.denunciante = denunciante;
        this.semaforoAfectado = semaforoAfectado;
        this.ordenAsignada = null; // Inicialmente no tiene orden asignada
    }

    public boolean esPrioridadValida() {
        // Al ser Prioridad un enum, si no es nula, es una prioridad válida definida en el sistema.
        return this.prioridad != null;
    }

    public void asignarOrden(OrdenComposicion orden) {
        if (this.ordenAsignada != null) {
            throw new OrdenYaAsignadaException("La denuncia ya tiene una orden asignada.");
        }
        this.ordenAsignada = orden;
    }

    public String getCodD() {
        return codD;
    }

    public Date getFechaDenuncia() {
        return fechaDenuncia;
    }

    public String getCalleX() {
        return calleX;
    }

    public String getCalleY() {
        return calleY;
    }

    public String getProblema() {
        return problema;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public Persona getDenunciante() {
        return denunciante;
    }

    public Semaforo getSemaforoAfectado() {
        return semaforoAfectado;
    }

    public OrdenComposicion getOrdenAsignada() {
        return ordenAsignada;
    }
}
