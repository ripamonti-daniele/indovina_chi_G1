import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;

public class Nodo implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private static int contatore = 0;
    private final int id;

    private String domanda;
    private Persona persona;
    protected Nodo si;
    protected Nodo no;

    public Nodo(String domanda) {
        this.id = contatore++;
        setDomanda(domanda);
        si = null;
        no = null;
    }

    public Nodo(Persona p) {
        this.id = contatore++;
        setPersona(p);
    }

    public int getId() {
        return id;
    }

    public String getDomanda() {
        return domanda;
    }

    private void setDomanda(String domanda) {
        domanda = domanda.trim().toLowerCase();
        if (domanda.isEmpty() || domanda.length() > 40)
            throw new IllegalArgumentException("Lunghezza domanda non valida");
        this.domanda = domanda;
        persona = null;
    }

    private void setPersona(Persona p) {
        if (p == null) throw new IllegalArgumentException("La persona non può essere null");
        this.persona = p;
        domanda = null;
    }

    public Nodo getSi() {
        return si;
    }
    public Nodo getNo() {
        return no;
    }

    public Persona getPersona() {
        if (persona == null) return null;
        return new Persona(persona);
    }

    private void setSi(Nodo n) {
        if (persona != null) throw new IllegalStateException("I nodi persona non possono avere sottonodi");
        if (si != null) throw new IllegalArgumentException("Nodo si di " + domanda + " già occupato");
        si = n;
    }

    private void setNo(Nodo n) {
        if (persona != null) throw new IllegalStateException("I nodi persona non possono avere sottonodi");
        if (no != null) throw new IllegalArgumentException("Nodo no di " + domanda + " già occupato");
        no = n;
    }

//    private Nodo trovaNodoPerDomanda(String domanda, Nodo n) {
//        if (n == null) return null;
//        if (n.getDomanda() != null && n.getDomanda().equals(domanda)) return n;
//        Nodo trovato = trovaNodoPerDomanda(domanda, n.si);
//        if (trovato != null) return trovato;
//        return trovaNodoPerDomanda(domanda, n.no);
//    }

    private Nodo trovaNodoPerID(int id, Nodo n) {
        if (n == null) return null;
        if (n.id == id) return n;
        Nodo trovato = trovaNodoPerID(id, n.si);
        if (trovato != null) return trovato;
        return trovaNodoPerID(id, n.no);
    }

    public Nodo aggiungiNodo(int idRoot, String domanda, boolean si) {
        Nodo padre = trovaNodoPerID(idRoot, this);
        if (padre == null) throw new IllegalArgumentException("Nessun nodo con id " + idRoot + " trovato");
        Nodo nuovo = new Nodo(domanda);
        if (si) padre.setSi(nuovo);
        else    padre.setNo(nuovo);
        return nuovo; // restituisce il nodo creato così si può salvare l'id
    }

    public Nodo aggiungiPersona(int idRoot, Persona persona, boolean si) {
        Nodo padre = trovaNodoPerID(idRoot, this);
        if (padre == null) throw new IllegalArgumentException("Nessun nodo con id " + idRoot + " trovato");
        Nodo nuovo = new Nodo(persona);
        if (si) padre.setSi(nuovo);
        else    padre.setNo(nuovo);
        return nuovo;
    }

//    public Nodo aggiungiNodo(String domandaRoot, String domanda, boolean si) {
//        Nodo padre = trovaNodoPerDomanda(domandaRoot, this);
//        if (padre == null) throw new IllegalArgumentException("Nessun nodo con domanda " + domandaRoot + " trovato");
//        Nodo nuovo = new Nodo(domanda);
//        if (si) padre.setSi(nuovo);
//        else    padre.setNo(nuovo);
//        return nuovo;
//    }
//
//    public Nodo aggiungiPersona(String domandaRoot, Persona persona, boolean si) {
//        Nodo padre = trovaNodoPerDomanda(domandaRoot, this);
//        if (padre == null) throw new IllegalArgumentException("Nessun nodo con domanda " + domandaRoot + " trovato");
//        Nodo nuovo = new Nodo(persona);
//        if (si) padre.setSi(nuovo);
//        else    padre.setNo(nuovo);
//        return nuovo;
//    }

    @Override
    public String toString() {
        if (persona != null) return "[" + id + "] " + persona.getNome() + " (persona)\n";
        String s = "[" + id + "] " + domanda + "\n";
        if (si != null) s += domanda + " --> si: " + si;
        if (no != null) s += domanda + " --> no: " + no;
        return s;
    }
}