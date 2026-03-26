import java.io.Serial;
import java.io.Serializable;

public class Nodo implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private String domanda;
    private Nodo si;
    private Nodo no;

    public Nodo(String domanda) {
        setDomanda(domanda);
        si = null;
        no = null;
    }

    public String getDomanda() {
        return domanda;
    }

    private void setDomanda(String domanda) {
        domanda = domanda.trim().toLowerCase();
        if (domanda.length() < 3 || domanda.length() > 25) throw new IllegalArgumentException("Lunghezza domanda non valida");
        this.domanda = domanda;
    }

    private Nodo getSi() {
        return si;
    }

    private Nodo getNo() {
        return no;
    }

    private void setSi(Nodo n) {
        if (si != null) throw new IllegalArgumentException("Nodo si già occupato");
        si = n;
    }

    private void setNo(Nodo n) {
        if (no != null) throw new IllegalArgumentException("Nodo no già occupato");
        no = n;
    }

    private Nodo trovaNodo(String domanda, Nodo n) {
        if (n.getDomanda().equals(domanda)) return n;
        Nodo si = n.getSi();
        Nodo no = n.getNo();
        if (si != null){
            if (!si.getDomanda().equals(domanda)) return trovaNodo(domanda, si);
            else return si;
        }
        if (no != null) {
            if (!no.getDomanda().equals(domanda)) return trovaNodo(domanda, no);
            else return no;
        }

        return null;
    }

    public void aggiungiNodo(String domandaPrecedente, String domanda, boolean si) {
        Nodo n = trovaNodo(domandaPrecedente, this);
        if (n == null) throw new IllegalArgumentException("Nessun nodo con domanda " + domanda + " trovato");
        else if (si) n.setSi(new Nodo(domanda));
        else n.setNo(new Nodo(domanda));
    }

    @Override
    public String toString() {
        String s = domanda + "\n";
        if (si != null) s += "si (" + domanda + "): " + si;
        if (no != null) s += "no (" + domanda + "): " + no;
        return s;
    }
}
