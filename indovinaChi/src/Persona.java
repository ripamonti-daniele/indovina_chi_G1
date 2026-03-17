import java.io.Serial;
import java.io.Serializable;

public class Persona implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private String nome;
    private ColoriCrapa coloreCapelli;
    private ColoriÖch coloreOcchi;
    private ColoriPelle colorePelle;
    private boolean occhiali;
    private boolean sesso;
    private boolean capelliLunghi;
    private boolean giovane;
    private static String nomi;

    public Persona(String nome, ColoriCrapa cc, ColoriÖch co, ColoriPelle cp, boolean occhiali, boolean sesso, boolean capelliLunghi, boolean giovane) {

    }
}
