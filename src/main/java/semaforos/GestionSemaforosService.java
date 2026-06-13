package semaforos;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class GestionSemaforosService {
    private List<Semaforo> semaforos;
    private List<Denuncia> denuncias;
    private List<EquipoControl> equipos;
    private List<Persona> personas; // Para almacenar y buscar denunciantes

    public GestionSemaforosService() {
        this.semaforos = new ArrayList<>();
        this.denuncias = new ArrayList<>();
        this.equipos = new ArrayList<>();
        this.personas = new ArrayList<>();
    }

    public Denuncia registrarDenuncia(String codD, Date fechaDenuncia, String calleX, String calleY, 
                                      String problema, Prioridad prioridad, String nombrePersona, 
                                      String mailPersona, int nroSemaforo) {
        
        // Alternativamente, si ya existe una denuncia con el mismo código, se le informa la fecha de la denuncia.
        for (Denuncia d : denuncias) {
            if (d.getCodD().equals(codD)) {
                System.out.println("La denuncia con código " + codD + " ya existe. Fecha de creación: " + d.getFechaDenuncia());
                return d;
            }
        }

        // Se busca el denunciante por su mail
        Persona denunciante = null;
        for (Persona p : personas) {
            if (p.getMail().equalsIgnoreCase(mailPersona)) {
                denunciante = p;
                break;
            }
        }

        // Si no existe, se registra nombre y mail
        if (denunciante == null) {
            denunciante = new Persona(nombrePersona, mailPersona);
            personas.add(denunciante);
        }

        // Se verifica el semáforo por número
        Semaforo semaforoAfectado = null;
        for (Semaforo s : semaforos) {
            if (s.getNro() == nroSemaforo) {
                semaforoAfectado = s;
                break;
            }
        }

        // Si el semáforo no existe en el sistema, lo creamos para evitar errores
        if (semaforoAfectado == null) {
            semaforoAfectado = new Semaforo(nroSemaforo, "Averiado", calleX + " y " + calleY, TipoFaro.BASIC_LED);
            semaforos.add(semaforoAfectado);
        }

        // Genera la denuncia
        Denuncia nuevaDenuncia = new Denuncia(codD, fechaDenuncia, calleX, calleY, problema, prioridad, denunciante, semaforoAfectado);
        
        // Asociaciones bidireccionales
        denunciante.agregarDenuncia(nuevaDenuncia);
        semaforoAfectado.registrarDenuncia(nuevaDenuncia);

        // Se guarda en la lista del servicio
        denuncias.add(nuevaDenuncia);

        return nuevaDenuncia;
    }

    public void asignarOrden(Denuncia denuncia, OrdenComposicion orden) {
        // Asigna la orden a la denuncia. Lanzará OrdenYaAsignadaException si la denuncia ya tiene una orden.
        denuncia.asignarOrden(orden);
        
        // Si la orden tiene un equipo asignado, cambiamos el estado del equipo a OCUPADO
        if (orden.getEquipoAsignado() != null) {
            orden.getEquipoAsignado().setEstado(EstadoEquipo.OCUPADO);
        }
    }

    public void finalizarReparacion(OrdenComposicion orden) {
        if (orden != null) {
            // Carga la fecha efectiva de reparación en la orden y libera al equipo (y sus miembros)
            orden.completarReparacion(new Date());

            // Se imprime la orden de composición en consola
            System.out.println("========================================");
            System.out.println("ORDEN DE REPARACIÓN COMPLETADA");
            System.out.println("Código de Orden: " + orden.getNroOrden());
            System.out.println("Fecha de Reparación Programada: " + orden.getFechaReparacionProgramada());
            System.out.println("Fecha de Reparación Efectiva: " + orden.getFechaEfectivaReparacion());
            System.out.println("Detalle de Trabajo: " + orden.getDetalle());
            if (orden.getEquipoAsignado() != null) {
                System.out.println("Equipo de Control a cargo: " + orden.getEquipoAsignado().getCodigo());
            }
            System.out.println("========================================");
        }
    }

    public int getCantidadDenunciasPorSemaforo(int nroSemaforo) {
        for (Semaforo s : semaforos) {
            if (s.getNro() == nroSemaforo) {
                return s.getCantidadDenuncias();
            }
        }
        return 0;
    }

    public List<Denuncia> getDenunciasSinComponer() {
        List<Denuncia> sinComponer = new ArrayList<>();
        for (Denuncia d : denuncias) {
            if (d.getOrdenAsignada() == null) {
                sinComponer.add(d);
            }
        }
        return sinComponer;
    }

    public List<EquipoControl> getEquiposLibres() {
        List<EquipoControl> libres = new ArrayList<>();
        for (EquipoControl eq : equipos) {
            if (eq.getEstado() == EstadoEquipo.LIBRE) {
                libres.add(eq);
            }
        }
        return libres;
    }

    // Métodos auxiliares para configuración de los tests o del sistema
    public void agregarSemaforo(Semaforo s) {
        this.semaforos.add(s);
    }

    public void agregarEquipo(EquipoControl e) {
        this.equipos.add(e);
    }

    public List<Semaforo> getSemaforos() {
        return semaforos;
    }

    public List<Denuncia> getDenuncias() {
        return denuncias;
    }

    public List<EquipoControl> getEquipos() {
        return equipos;
    }

    public List<Persona> getPersonas() {
        return personas;
    }
}
