import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Albero implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Nodo root;

    public Albero(String domandaRoot) {
        root = new Nodo(domandaRoot);
    }

    public int getRootId() {
        return root.getId();
    }

    public int inserisciDomanda(int idRoot, String domanda, boolean si) {
        return root.aggiungiNodo(idRoot, domanda, si).getId();
    }

    public int inserisciPersona(int idRoot, Persona persona, boolean si) {
        return root.aggiungiPersona(idRoot, persona, si).getId();
    }

    public Nodo getRoot() {
        return root;
    }

    public Map<Integer, String> getDomande() {
        Map<Integer, String> mappa = new HashMap<>();
        trovaDomande(root, mappa);
        return mappa;
    }

    private void trovaDomande(Nodo n, Map<Integer, String> mappa) {
        if (n == null) return;
        if (n.getDomanda() != null) {
            mappa.put(n.getId(), n.getDomanda());
            trovaDomande(n.getSi(), mappa);
            trovaDomande(n.getNo(), mappa);
        }
    }

    public Map<Integer, Persona> getPersone() {
        Map<Integer, Persona> mappa = new HashMap<>();
        trovaPersone(root, mappa);
        return mappa;
    }

    private void trovaPersone(Nodo n, Map<Integer, Persona> mappa) {
        if (n == null) return;
        if (n.getPersona() != null) {
            mappa.put(n.getId(), n.getPersona());
            return;
        }
        trovaPersone(n.getSi(), mappa);
        trovaPersone(n.getNo(), mappa);
    }

    @Override
    public String toString() {
        return "root albero: " + root;
    }
}