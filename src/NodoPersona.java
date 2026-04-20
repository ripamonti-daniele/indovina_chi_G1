import java.io.Serial;

/**
 * Nodo foglia dell'albero decisionale che rappresenta una {@link Persona}.
 * <p>
 * Non può avere figli: i metodi {@link #setSi(Nodo)} e {@link #setNo(Nodo)}
 * lanciano {@link UnsupportedOperationException}. Poiché {@link Persona} è
 * immutabile, non è necessario il costruttore di copia.
 * </p>
 */
public class NodoPersona extends Nodo {
    @Serial
    private static final long serialVersionUID = 4L;

    /** La persona associata a questo nodo foglia. */
    private final Persona persona;

    /**
     * Crea un nodo foglia associato alla persona specificata.
     *
     * @param p la persona da associare; non deve essere {@code null}
     * @throws IllegalArgumentException se {@code p} è {@code null}
     */
    public NodoPersona(Persona p) {
        super();
        if (p == null) throw new IllegalArgumentException("La persona non può essere null");
        //non serve il costruttore di copia perché la classe Persona è immutabile
        this.persona = p;
    }

    /**
     * Restituisce la persona associata a questo nodo.
     *
     * @return la {@link Persona} del nodo
     */
    public Persona getPersona() {
        //non serve il costruttore di copia perché la classe Persona è immutabile
        return persona;
    }

    /**
     * Operazione non supportata: un nodo persona è sempre una foglia.
     *
     * @param n ignorato
     * @throws UnsupportedOperationException sempre
     */
    @Override
    public void setSi(Nodo n) {
        throw new UnsupportedOperationException("Un nodo persona non può avere figli");
    }

    /**
     * Operazione non supportata: un nodo persona è sempre una foglia.
     *
     * @param n ignorato
     * @throws UnsupportedOperationException sempre
     */
    @Override
    public void setNo(Nodo n) {
        throw new UnsupportedOperationException("Un nodo persona non può avere figli");
    }

    /**
     * Restituisce una rappresentazione testuale con id e nome della persona.
     *
     * @return stringa nel formato {@code [id] nome (persona)}
     */
    @Override
    public String toString() {
        return super.toString() + persona.getNome() + " (persona)";
    }
}