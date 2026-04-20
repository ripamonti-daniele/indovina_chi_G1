import java.io.Serial;
import java.io.Serializable;

public abstract class Nodo implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private static int contatore = 0;
    private final int id;
    private Nodo si;
    private Nodo no;

    protected Nodo() {
        this.id = contatore++;
    }

    public int getId() {
        return id;
    }

    public Nodo getSi() {
        return si;
    }

    public Nodo getNo() {
        return no;
    }

    public void setSi(Nodo n) {
        if (si != null) throw new IllegalStateException("Nodo 'si' già occupato");
        si = n;
    }

    public void setNo(Nodo n) {
        if (no != null) throw new IllegalStateException("Nodo 'no' già occupato");
        no = n;
    }

    public boolean isFoglia() {
        return si == null && no == null;
    }

    @Override
    public String toString() {
        return "[" + id + "] ";
    }

    // Per gestire il contatore durante la deSerializzazione
    @Serial
    private Object readResolve() {
        if (this.id >= contatore) contatore = this.id + 1;
        return this;
    }
}