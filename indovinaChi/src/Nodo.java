import java.io.Serial;
import java.io.Serializable;

public class Nodo implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private String domanda;
    private Persona persona;
    private Nodo si;
    private Nodo no;

    public Nodo(String domanda) {
        setDomanda(domanda);
        si = null;
        no = null;
    }

    public Nodo(Persona p) {
        setPersona(p);
    }

    public String getDomanda() {
        return domanda;
    }

    private void setDomanda(String domanda) {
        domanda = domanda.trim().toLowerCase();
        if (domanda.isEmpty() || domanda.length() > 25) throw new IllegalArgumentException("Lunghezza domanda non valida");
        this.domanda = domanda;
        persona = null;
    }

    private void setPersona(Persona p) {
        if (p == null) throw new IllegalArgumentException("La persona non può essere null");
        this.persona = p;
        domanda = null;
    }

    private Nodo getSi() {
        return si;
    }

    private Nodo getNo() {
        return no;
    }

    private void setSi(Nodo n) {
        if (persona != null) throw new IllegalStateException("I nodi persona non possono avere sottonodi");
        if (si != null) throw new IllegalArgumentException("Nodo si di " + domanda + " già occupato");
        si = n;
    }

    private void setNo(Nodo n) {
        if (persona != null) throw new IllegalStateException("I nodi persona non possono avere sottonodi");
        if (no != null) throw new IllegalArgumentException("Nodo no di" + domanda + " già occupato");
        no = n;
    }

    private Nodo trovaNodo(String domanda, Nodo n) {
        if (n == null) return null;
        if (n.getDomanda() != null && n.getDomanda().equals(domanda)) return n; //Fixato il NullPointException
        Nodo si = n.getSi();
        Nodo no = n.getNo();
        if (si != null) {
            Nodo nodo = trovaNodo(domanda, si);
            if (nodo != null) return nodo;
        }
        return trovaNodo(domanda, no);
    }

    public void aggiungiNodo(String domandaRoot, String domanda, boolean si) {
        Nodo n = trovaNodo(domandaRoot, this);
        if (n == null) throw new IllegalArgumentException("Nessun nodo con domanda " + domandaRoot + " trovato");
        else if (si) n.setSi(new Nodo(domanda));
        else n.setNo(new Nodo(domanda));
    }

    public void aggiungiPersona(String domandaRoot, Persona persona, boolean si) {
        Nodo n = trovaNodo(domandaRoot, this);
        if (n == null) throw new IllegalArgumentException("Nessun nodo con domanda " + domandaRoot + " trovato");
        else if (si) n.setSi(new Nodo(persona));
        else n.setNo(new Nodo(persona));
    }

    @Override
    public String toString() {
        if (persona != null) return persona.getNome() + " (persona)\n";
        String s = domanda + "\n";
        if (si != null) s += domanda + " --> si: " + si;
        if (no != null) s += domanda + " --> no: " + no;
        return s;
    }
}
