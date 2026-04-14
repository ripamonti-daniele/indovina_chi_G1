import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Persona implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;

    private String nome;
    private ColoriCrapa coloreCapelli;
    private ColoriOch coloreOcchi;
    private ColoriPelle colorePelle;
    private boolean occhiali;
    private boolean sesso;
    private boolean capelliLunghi;
    private boolean barba;
    private boolean cappello;
    private boolean pelato;
    private String percorsoImmagine;
    private final static List<String> nomi = new ArrayList<>();

    public Persona(String nome, ColoriCrapa cc, ColoriOch co, ColoriPelle cp, boolean occhiali, boolean sesso, boolean capelliLunghi, boolean barba, boolean cappello, boolean pelato, String percorsoImmagine) {
        this(nome, cc, co, cp, occhiali, sesso, capelliLunghi, barba, cappello, pelato);
        setPercorsoImmagine(percorsoImmagine);
    }

    public Persona(String nome, ColoriCrapa cc, ColoriOch co, ColoriPelle cp, boolean occhiali, boolean sesso, boolean capelliLunghi, boolean barba, boolean cappello, boolean pelato) {
        setNome(nome);
        setColoreCapelli(cc);
        setColoreOcchi(co);
        setColorePelle(cp);
        setOcchiali(occhiali);
        setSesso(sesso);
        setCapelliLunghi(capelliLunghi);
        setBarba(barba);
        setCappello(cappello);
        setPelato(pelato);
    }

    public Persona(Persona p) {
        this.nome = p.nome;
        this.coloreCapelli = p.coloreCapelli;
        this.coloreOcchi = p.coloreOcchi;
        this.colorePelle = p.colorePelle;
        this.occhiali = p.occhiali;
        this.sesso = p.sesso;
        this.capelliLunghi = p.capelliLunghi;
        this.barba = p.barba;
        this.cappello = p.cappello;
        this.pelato = p.pelato;
        this.percorsoImmagine = p.percorsoImmagine;
    }

    private void setPercorsoImmagine(String percorso) {
        if (percorso == null || percorso.isBlank())
            throw new IllegalArgumentException("Il percorso non può essere vuoto");
        this.percorsoImmagine = percorso;
    }

    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }

    public ImageIcon getImmagine() {
        return new ImageIcon(percorsoImmagine);
    }
    //utilizzo per swing
    public ImageIcon getImmagine(int larghezza, int altezza) {
        Image scalata = getImmagine().getImage().getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
        return new ImageIcon(scalata);
    }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        if (nome == null) throw new NullPointerException("Il nome non può essere null");
        nome = nome.trim().toLowerCase();
        if (!nome.matches("[a-z]{3,15}")) throw new IllegalArgumentException("Formato nome non valido");
        if (nomi.contains(nome)) throw new IllegalArgumentException("Nome già utilizzato");
        this.nome = nome;
        nomi.add(nome);
    }

    public ColoriCrapa getColoreCapelli() {
        return coloreCapelli;
    }

    private void setColoreCapelli(ColoriCrapa coloreCapelli) {
        if (coloreCapelli == null) throw new NullPointerException("Il colore dei capelli non può essere null");
        this.coloreCapelli = coloreCapelli;
    }

    public ColoriOch getColoreOcchi() {
        return coloreOcchi;
    }

    private void setColoreOcchi(ColoriOch coloreOcchi) {
        if (coloreOcchi == null) throw new NullPointerException("Il colore degli occhi non può essere null");
        this.coloreOcchi = coloreOcchi;
    }

    public ColoriPelle getColorePelle() {
        return colorePelle;
    }

    private void setColorePelle(ColoriPelle colorePelle) {
        if (colorePelle == null) throw new NullPointerException("Il colore della pelle non può essere null");
        this.colorePelle = colorePelle;
    }

    public boolean isSesso() {
        return sesso;
    }

    private void setSesso(boolean sesso) {
        this.sesso = sesso;
    }

    public boolean isOcchiali() {
        return occhiali;
    }

    private void setOcchiali(boolean occhiali) {
        this.occhiali = occhiali;
    }

    public boolean isCapelliLunghi() {
        return capelliLunghi;
    }

    private void setCapelliLunghi(boolean capelliLunghi) {
        this.capelliLunghi = capelliLunghi;
    }

    public boolean isBarba() {
        return barba;
    }

    private void setBarba(boolean barba) {
        this.barba = barba;
    }

    public boolean isCappello() {
        return cappello;
    }

    private void setCappello(boolean cappello) {
        this.cappello = cappello;
    }

    public boolean isPelato() {
        return pelato;
    }

    private void setPelato(boolean pelato) {
        this.pelato = pelato;
    }

    public static List<String> getNomi() {
        return new ArrayList<>(nomi);
    }

    @Override
    public String toString() {
        String s = "caratteristiche " + nome + ":\n";
        if (sesso) s += "maschio\n";
        else s += "femmina\n";
        s += "colore capelli: " + coloreCapelli + "\ncolore occhi: " + coloreOcchi + "\ncolore pelle: " + colorePelle + "\n";
        if (occhiali) s += "ha gli occhiali\n";
        if (capelliLunghi) s += "ha i capelli lunghi\n";
        if (barba) s += "ha la barba o i baffi\n";
        if (cappello) s += "ha il cappello\n";
        if (pelato) s += "è peltato";

        return s.toLowerCase();
    }
}
