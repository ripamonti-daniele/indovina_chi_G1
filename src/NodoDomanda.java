import java.io.Serial;

public class NodoDomanda extends Nodo {
    @Serial
    private static final long serialVersionUID = 3L;

    private String domanda;

    public NodoDomanda(String domanda) {
        super();
        setDomanda(domanda);
    }

    public String getDomanda() {
        return domanda;
    }

    private void setDomanda(String domanda) {
        domanda = domanda.trim().toLowerCase();
        if (domanda.isEmpty() || domanda.length() > 40) throw new IllegalArgumentException("Lunghezza domanda non valida");
        this.domanda = domanda;
    }

    @Override
    public String toString() {
        return super.toString() + domanda;
    }
}