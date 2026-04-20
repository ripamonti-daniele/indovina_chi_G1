import java.io.Serial;

public class NodoPersona extends Nodo {
    @Serial
    private static final long serialVersionUID = 4L;

    private final Persona persona;

    public NodoPersona(Persona p) {
        super();
        if (p == null) throw new IllegalArgumentException("La persona non può essere null");
        //non serve il costruttore di copia perché la classe Persona è immutabile
        this.persona = p;
    }

    public Persona getPersona() {
        //non serve il costruttore di copia perché la classe Persona è immutabile
        return persona;
    }

    // I nodi foglia non possono avere figli
    @Override
    public void setSi(Nodo n) {
        throw new UnsupportedOperationException("Un nodo persona non può avere figli");
    }

    @Override
    public void setNo(Nodo n) {
        throw new UnsupportedOperationException("Un nodo persona non può avere figli");
    }

    @Override
    public String toString() {
        return super.toString() + persona.getNome() + " (persona)";
    }
}