package semaforos;

import java.util.List;
import java.util.ArrayList;

public class Persona {
    private String nombre;
    private String mail;
    private List<Denuncia> denunciasRealizadas;

    public Persona(String nombre, String mail) {
        this.nombre = nombre;
        this.mail = mail;
        this.denunciasRealizadas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getMail() {
        return mail;
    }

    public void agregarDenuncia(Denuncia denuncia) {
        this.denunciasRealizadas.add(denuncia);
    }

    public List<Denuncia> getDenunciasRealizadas() {
        return denunciasRealizadas;
    }

    public int getCantidadDenuncias() {
        return denunciasRealizadas.size();
    }
}
