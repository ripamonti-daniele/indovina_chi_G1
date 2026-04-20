import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Rappresenta l'albero decisionale binario del gioco "Indovina chi?".
 * <p>
 * Ogni nodo può essere un {@link NodoDomanda} (nodo interno con due figli:
 * risposta "sì" e risposta "no") oppure un {@link NodoPersona} (nodo foglia).
 * L'albero mantiene un indice {@link Map} che associa ogni id di nodo alla
 * sua istanza, per accesso in O(1).
 * </p>
 * Implementa {@link Serializable} per la persistenza.
 */
public class Albero implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Radice dell'albero (sempre un nodo domanda). */
    private final NodoDomanda root;

    /** Indice id → nodo per accesso rapido a qualsiasi nodo dell'albero. */
    private final Map<Integer, Nodo> indice = new HashMap<>();

    /**
     * Crea un nuovo albero con una singola radice di tipo {@link NodoDomanda}.
     *
     * @param domandaRoot testo della domanda radice (max 40 caratteri, non vuoto)
     * @throws IllegalArgumentException se la domanda non rispetta i vincoli di lunghezza
     */
    public Albero(String domandaRoot) {
        root = new NodoDomanda(domandaRoot);
        indice.put(root.getId(), root);
    }

    /**
     * Restituisce l'id del nodo radice.
     *
     * @return l'identificatore intero della radice
     */
    public int getRootId() {
        return root.getId();
    }

    /**
     * Restituisce il nodo radice dell'albero.
     *
     * @return la radice come {@link NodoDomanda}
     */
    public NodoDomanda getRoot() {
        return root;
    }

    /**
     * Inserisce un nuovo {@link NodoDomanda} come figlio di un nodo esistente.
     *
     * @param idPadre id del nodo padre (deve essere un {@link NodoDomanda})
     * @param domanda testo della nuova domanda
     * @param si      {@code true} per collegarlo come figlio "sì", {@code false} per "no"
     * @return l'id del nuovo nodo inserito
     * @throws IllegalArgumentException se non esiste nessun nodo con {@code idPadre}
     * @throws IllegalStateException    se il padre non è un {@link NodoDomanda} o se
     *                                  il ramo scelto è già occupato
     */
    public int inserisciDomanda(int idPadre, String domanda, boolean si) {
        NodoDomanda nd = new NodoDomanda(domanda);
        collegaNodo(idPadre, nd, si);
        return nd.getId();
    }

    /**
     * Inserisce un nuovo {@link NodoPersona} come figlio di un nodo esistente.
     *
     * @param idPadre id del nodo padre (deve essere un {@link NodoDomanda})
     * @param persona la persona da associare al nuovo nodo foglia
     * @param si      {@code true} per collegarlo come figlio "sì", {@code false} per "no"
     * @return l'id del nuovo nodo inserito
     * @throws IllegalArgumentException se non esiste nessun nodo con {@code idPadre}
     * @throws IllegalStateException    se il padre non è un {@link NodoDomanda} o se
     *                                  il ramo scelto è già occupato
     */
    public int inserisciPersona(int idPadre, Persona persona, boolean si) {
        NodoPersona np = new NodoPersona(persona);
        collegaNodo(idPadre, np, si);
        return np.getId();
    }

    /**
     * Collega un nodo già creato come figlio del nodo padre indicato.
     *
     * @param idPadre id del nodo padre
     * @param nuovo   il nodo da collegare
     * @param si      {@code true} per il ramo "sì", {@code false} per il ramo "no"
     * @throws IllegalArgumentException se non esiste nessun nodo con {@code idPadre}
     * @throws IllegalStateException    se il padre non è un {@link NodoDomanda} o se
     *                                  il ramo è già occupato
     */
    private void collegaNodo(int idPadre, Nodo nuovo, boolean si) {
        Nodo padre = indice.get(idPadre);
        if (padre == null) throw new IllegalArgumentException("Nessun nodo con id " + idPadre);
        if (!(padre instanceof NodoDomanda)) throw new IllegalStateException("Solo i nodi domanda possono avere figli");
        if (si) padre.setSi(nuovo);
        else padre.setNo(nuovo);
        indice.put(nuovo.getId(), nuovo);
    }

    /**
     * Restituisce una mappa con tutte le domande presenti nell'albero,
     * associando l'id del nodo al testo della domanda.
     *
     * @return {@link Map}&lt;id, testo domanda&gt; di tutti i {@link NodoDomanda}
     */
    public Map<Integer, String> getDomande() {
        Map<Integer, String> mappa = new HashMap<>();
        for (Nodo n : indice.values()) {
            if (n instanceof NodoDomanda nd) mappa.put(nd.getId(), nd.getDomanda());
        }
        return mappa;
    }

    /**
     * Restituisce una mappa con tutte le persone presenti nell'albero,
     * associando l'id del nodo alla {@link Persona} corrispondente.
     *
     * @return {@link Map}&lt;id, {@link Persona}&gt; di tutti i {@link NodoPersona}
     */
    public Map<Integer, Persona> getPersone() {
        Map<Integer, Persona> mappa = new HashMap<>();
        for (Nodo n : indice.values()) {
            if (n instanceof NodoPersona np) mappa.put(np.getId(), np.getPersona());
        }
        return mappa;
    }

    /**
     * Restituisce una rappresentazione testuale dell'albero, mostrando la radice.
     *
     * @return stringa descrittiva con la radice dell'albero
     */
    @Override
    public String toString() {
        return "root albero: " + root;
    }
}