package semaforos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

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
}
