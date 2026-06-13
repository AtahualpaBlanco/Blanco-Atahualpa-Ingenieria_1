package semaforos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class GestionSemaforosTest {

    private GestionSemaforosService service;
    private Semaforo semaforoDefecto;

    @BeforeEach
    public void setup() {
        // Inicializar el servicio y un semáforo por defecto para las pruebas
        service = new GestionSemaforosService();
        semaforoDefecto = new Semaforo(101, "Averiado", "Calle Falsa 123", TipoFaro.BASIC_LED);
        service.agregarSemaforo(semaforoDefecto);
    }

    @AfterEach
    public void teardown() {
        System.out.println("Prueba unitaria completada.");
    }

    @Test
    public void testComposicionEstrictaSemaforoYLuces() {
        // Verificar que el semáforo tenga exactamente 3 luces creadas
        List<Luz> luces = semaforoDefecto.getLuces();
        assertEquals(3, luces.size(), "El semáforo debe estar compuesto por exactamente 3 luces.");

        // Obtener la luz en la posición 0 y verificar que las referencias coincidan (assertSame)
        Luz primeraLuz = luces.get(0);
        Luz mismaLuz = semaforoDefecto.getLuces().get(0);
        assertSame(primeraLuz, mismaLuz, "La referencia de la luz obtenida debe ser exactamente la misma que la guardada internamente.");
    }

    @Test
    @Timeout(value = 400, unit = TimeUnit.MILLISECONDS)
    public void testRobustezAnteDuplicadosConTimeout() {
        // Configurar una denuncia
        Persona denunciante = new Persona("Juan Gomez", "juan@gmail.com");
        Denuncia denuncia = new Denuncia("D-001", new Date(), "Calle A", "Calle B", "Faro roto", Prioridad.ALTA, denunciante, semaforoDefecto);
        
        // Crear una primera orden
        OrdenComposicion orden1 = new OrdenComposicion("O-001", new Date(), "Reparar faro roto", denuncia);
        
        // Asignarle la orden
        service.asignarOrden(denuncia, orden1);

        // Crear una segunda orden
        OrdenComposicion orden2 = new OrdenComposicion("O-002", new Date(), "Reparar faro de nuevo", denuncia);

        // Verificar que intentar asociar la segunda orden lanza OrdenYaAsignadaException
        assertThrows(OrdenYaAsignadaException.class, () -> {
            service.asignarOrden(denuncia, orden2);
        }, "Debería haber lanzado OrdenYaAsignadaException porque la denuncia ya tiene una orden.");
    }

    @Test
    public void testFlujoReparacionExitoso() {
        // Crear miembros
        List<Miembro> miembros = new ArrayList<>();
        miembros.add(new Miembro("M1", "Carlos", "Técnico", false, true)); // responsable
        miembros.add(new Miembro("M2", "Ana", "Técnico", false, false));
        miembros.add(new Miembro("M3", "Pedro", "Ayudante", false, false));
        miembros.add(new Miembro("M4", "Luis", "Ayudante", false, false));

        // Crear equipo
        EquipoControl equipo = new EquipoControl("EQ-01", "Electricidad", miembros);
        // El equipo inicialmente está ocupado para simular la asignación
        equipo.setEstado(EstadoEquipo.OCUPADO);
        service.agregarEquipo(equipo);

        // Crear denuncia y orden
        Persona denunciante = new Persona("Maria Lopez", "maria@gmail.com");
        Denuncia denuncia = new Denuncia("D-002", new Date(), "Calle X", "Calle Y", "Faro quemado", Prioridad.MEDIA, denunciante, semaforoDefecto);
        OrdenComposicion orden = new OrdenComposicion("O-002", new Date(), "Cambiar foco", denuncia);
        orden.setEquipoAsignado(equipo);

        service.asignarOrden(denuncia, orden);

        // Simular la finalización de la reparación
        service.finalizarReparacion(orden);

        // Verificar estado de la orden y del equipo
        assertTrue(orden.estaCompleta(), "La orden debería estar completa.");
        assertEquals(EstadoEquipo.LIBRE, equipo.getEstado(), "El estado del equipo debería ser LIBRE.");

        // Recorrer los 4 miembros y verificar que todos estén libres
        assertEquals(4, equipo.getMiembros().size(), "El equipo debe tener 4 miembros.");
        for (Miembro m : equipo.getMiembros()) {
            assertTrue(m.isLibre(), "El miembro " + m.getNombre() + " debe estar libre.");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Alta", "Media", "Baja"})
    public void testPrioridadesValidasParametrizado(String prioridadStr) {
        Prioridad p = null;
        if (prioridadStr.equalsIgnoreCase("Alta")) {
            p = Prioridad.ALTA;
        } else if (prioridadStr.equalsIgnoreCase("Media")) {
            p = Prioridad.MEDIA;
        } else if (prioridadStr.equalsIgnoreCase("Baja")) {
            p = Prioridad.BAJA;
        }

        Persona denunciante = new Persona("Laura", "laura@gmail.com");
        Denuncia denuncia = new Denuncia("D-PARAM", new Date(), "Calle X", "Calle Y", "Problema faro", p, denunciante, semaforoDefecto);
        
        assertTrue(denuncia.esPrioridadValida(), "La prioridad " + prioridadStr + " debería ser válida.");
    }

    @Test
    public void testMetricasEstadisticasEHistorial() {
        int nroSemaforo = 999;
        Semaforo semaforo = new Semaforo(nroSemaforo, "Averiado", "Calle 1 y Calle 2", TipoFaro.SMART_LED);
        service.agregarSemaforo(semaforo);

        // Registrar 3 denuncias diferentes al mismo semáforo
        service.registrarDenuncia("D-999-1", new Date(), "Calle 1", "Calle 2", "Luz rota", Prioridad.ALTA, "Denunciante 1", "d1@mail.com", nroSemaforo);
        service.registrarDenuncia("D-999-2", new Date(), "Calle 1", "Calle 2", "Cortocircuito", Prioridad.MEDIA, "Denunciante 2", "d2@mail.com", nroSemaforo);
        service.registrarDenuncia("D-999-3", new Date(), "Calle 1", "Calle 2", "Palo caído", Prioridad.BAJA, "Denunciante 3", "d3@mail.com", nroSemaforo);

        // Invocar método del servicio y verificar que el resultado sea exactamente 3
        int cantidadDenuncias = service.getCantidadDenunciasPorSemaforo(nroSemaforo);
        assertEquals(3, cantidadDenuncias, "El semáforo debe tener exactamente 3 denuncias registradas.");
    }
}

