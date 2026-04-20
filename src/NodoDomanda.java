import java.io.Serial;

/**
 * Nodo interno dell'albero decisionale che contiene una domanda a risposta sì/no.
 * <p>
 * Estende {@link Nodo} e può avere due figli: quello raggiunto rispondendo "sì"
 * e quello raggiunto rispondendo "no". Il testo della domanda deve avere
 * lunghezza compresa tra 1 e 40 caratteri (dopo trim).
 * </p>
 */
public class NodoDomanda extends Nodo {
    @Serial
    private static final long serialVersionUID = 3L;

    /** Testo della domanda associata a questo nodo. */
    private String domanda;

    /**
     * Crea un nuovo nodo domanda con il testo specificato.
     *
     * @param domanda il testo della domanda (dopo trim, lunghezza 1–40)
     * @throws IllegalArgumentException se la domanda è vuota o supera i 40 caratteri
     */
    public NodoDomanda(String domanda) {
        super();
        setDomanda(domanda);
    }

    /**
     * Restituisce il testo della domanda.
     *
     * @return la domanda in minuscolo
     */
    public String getDomanda() {
        return domanda;
    }

    /**
     * Imposta il testo della domanda dopo averlo normalizzato e validato.
     *
     * @param domanda il testo da impostare
     * @throws IllegalArgumentException se la domanda è vuota o supera i 40 caratteri
     */
    private void setDomanda(String domanda) {
        domanda = domanda.trim().toLowerCase();
        if (domanda.isEmpty() || domanda.length() > 40) throw new IllegalArgumentException("Lunghezza domanda non valida");
        this.domanda = domanda;
    }

    /**
     * Restituisce una rappresentazione testuale del nodo con id e testo della domanda.
     *
     * @return stringa nel formato {@code [id] testo_domanda}
     */
    @Override
    public String toString() {
        return super.toString() + domanda;
    }
}