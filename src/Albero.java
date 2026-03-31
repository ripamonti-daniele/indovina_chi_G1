import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Albero implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Nodo root;

    public Albero(String domandaRoot) {
        root = new Nodo(domandaRoot);
    }

    public Albero(String domandaRoot, List<InfoNodo> nodi) {
        this(domandaRoot);
        inserisciNodi(nodi);
    }

    public void inserisciNodi(List<InfoNodo> nodi) {
        if (nodi == null) throw new IllegalArgumentException("I nodi non possono essere null");
        for (InfoNodo n : nodi) {
            root.aggiungiNodo(n.domandaRoot(), n.domanda(), n.si());
        }
    }
    public void inserisciNodo(InfoNodo n) {
        root.aggiungiNodo(n.domandaRoot(), n.domanda(), n.si());
    }

    public void inserisciNodo(String domandaRoot, String domanda, boolean si) {
        root.aggiungiNodo(domandaRoot, domanda, si);
    }

    public void inserisciPersone(List<InfoPersona> nodi) {
        if (nodi == null) throw new IllegalArgumentException("I nodi non possono essere null");
        for (InfoPersona n : nodi) {
            root.aggiungiPersona(n.domandaRoot(), n.persona(), n.si());
        }
    }
    public void inserisciPersona(InfoPersona n) {
        root.aggiungiPersona(n.domandaRoot(), n.persona(), n.si());
    }

    public void inserisciPersona(String domandaRoot, Persona persona, boolean si) {
        root.aggiungiPersona(domandaRoot, persona, si);
    }

    public Nodo getRoot(){
        return root;
    }


    @Override
    public String toString() {
        return "root albero: " + root.toString();
    }
}
