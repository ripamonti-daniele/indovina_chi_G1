import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Albero implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final NodoDomanda root;
    private final Map<Integer, Nodo> indice = new HashMap<>();

    public Albero(String domandaRoot) {
        root = new NodoDomanda(domandaRoot);
        indice.put(root.getId(), root);
    }

    public int getRootId() {
        return root.getId();
    }

    public NodoDomanda getRoot() {
        return root;
    }

    public int inserisciDomanda(int idPadre, String domanda, boolean si) {
        NodoDomanda nd = new NodoDomanda(domanda);
        collegaNodo(idPadre, nd, si);
        return nd.getId();
    }

    public int inserisciPersona(int idPadre, Persona persona, boolean si) {
        NodoPersona np = new NodoPersona(persona);
        collegaNodo(idPadre, np, si);
        return np.getId();
    }

    private void collegaNodo(int idPadre, Nodo nuovo, boolean si) {
        Nodo padre = indice.get(idPadre);
        if (padre == null) throw new IllegalArgumentException("Nessun nodo con id " + idPadre);
        if (!(padre instanceof NodoDomanda)) throw new IllegalStateException("Solo i nodi domanda possono avere figli");
        if (si) padre.setSi(nuovo);
        else padre.setNo(nuovo);
        indice.put(nuovo.getId(), nuovo);
    }

    public Map<Integer, String> getDomande() {
        Map<Integer, String> mappa = new HashMap<>();
        for (Nodo n : indice.values()) {
            if (n instanceof NodoDomanda nd) mappa.put(nd.getId(), nd.getDomanda());
        }
        return mappa;
    }

    public Map<Integer, Persona> getPersone() {
        Map<Integer, Persona> mappa = new HashMap<>();
        for (Nodo n : indice.values()) {
            if (n instanceof NodoPersona np) mappa.put(np.getId(), np.getPersona());
        }
        return mappa;
    }

    @Override
    public String toString() {
        return "root albero: " + root;
    }
}