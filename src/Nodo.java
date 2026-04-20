import java.io.Serial;
import java.io.Serializable;

/**
 * Classe astratta base per i nodi dell'albero decisionale.
 * <p>
 * Ogni nodo ha un id intero progressivo unico (generato da un contatore statico),
 * e due riferimenti opzionali ai figli: {@code si} (ramo "sì") e {@code no} (ramo "no").
 * I nodi foglia sono quelli che non hanno né {@code si} né {@code no}.
 * </p>
 * Implementa {@link Serializable}; il metodo {@link #readResolve()} gestisce
 * la risincronizzazione del contatore statico durante la deserializzazione.
 */
public abstract class Nodo implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    /** Contatore globale per la generazione degli id univoci dei nodi. */
    private static int contatore = 0;

    /** Identificatore univoco di questo nodo. */
    private final int id;

    /** Figlio raggiunto rispondendo "sì" alla domanda del nodo. */
    private Nodo si;

    /** Figlio raggiunto rispondendo "no" alla domanda del nodo. */
    private Nodo no;

    /**
     * Costruisce un nodo assegnandogli il prossimo id disponibile.
     */
    protected Nodo() {
        this.id = contatore++;
    }

    /**
     * Restituisce l'identificatore univoco del nodo.
     *
     * @return l'id del nodo
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce il figlio corrispondente alla risposta "sì".
     *
     * @return il nodo figlio "sì", o {@code null} se non impostato
     */
    public Nodo getSi() {
        return si;
    }

    /**
     * Restituisce il figlio corrispondente alla risposta "no".
     *
     * @return il nodo figlio "no", o {@code null} se non impostato
     */
    public Nodo getNo() {
        return no;
    }

    /**
     * Imposta il figlio "sì" del nodo.
     *
     * @param n il nodo da impostare come figlio "sì"
     * @throws IllegalStateException se il figlio "sì" è già stato impostato
     */
    public void setSi(Nodo n) {
        if (si != null) throw new IllegalStateException("Nodo 'si' già occupato");
        si = n;
    }

    /**
     * Imposta il figlio "no" del nodo.
     *
     * @param n il nodo da impostare come figlio "no"
     * @throws IllegalStateException se il figlio "no" è già stato impostato
     */
    public void setNo(Nodo n) {
        if (no != null) throw new IllegalStateException("Nodo 'no' già occupato");
        no = n;
    }

    /**
     * Indica se il nodo è una foglia, ovvero se non ha figli.
     *
     * @return {@code true} se entrambi i figli sono {@code null}
     */
    public boolean isFoglia() {
        return si == null && no == null;
    }

    /**
     * Restituisce una rappresentazione testuale base del nodo con il suo id.
     *
     * @return stringa nel formato {@code [id] }
     */
    @Override
    public String toString() {
        return "[" + id + "] ";
    }

    /**
     * Metodo di supporto alla deserializzazione. Aggiorna il contatore statico
     * per evitare collisioni di id dopo un ciclo di serializzazione/deserializzazione.
     *
     * @return questa stessa istanza
     */
    @Serial
    private Object readResolve() {
        if (this.id >= contatore) contatore = this.id + 1;
        return this;
    }
}