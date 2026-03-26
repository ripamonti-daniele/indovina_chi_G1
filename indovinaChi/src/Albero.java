import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public class Albero implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Nodo root;

    public Albero(String domandaRoot) {
        root = new Nodo(domandaRoot);
    }

    public Albero(String domandaRoot, Map<String[], Boolean> nodi) {
        this(domandaRoot);
        inserisciNodi(nodi);
    }

    public void inserisciNodi(Map<String[], Boolean> nodi) {
        if (nodi == null) throw new IllegalArgumentException("I nodi non possono essere null");
        for (String[] domande : nodi.keySet()) {
            root.aggiungiNodo(domande[0], domande[1], nodi.get(domande));
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
