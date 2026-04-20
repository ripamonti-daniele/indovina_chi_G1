import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una persona con caratteristiche fisiche, utilizzata nel gioco
 * "Indovina chi?". Ogni persona ha un nome univoco, un sesso, colori di capelli,
 * occhi e pelle, e una serie di attributi booleani (occhiali, capelli lunghi, ecc.).
 * <p>
 * La classe è immutabile dopo la costruzione (tutti i setter sono privati) e
 * implementa {@link Serializable} per la persistenza su file.
 * </p>
 */
public class Persona implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    /** Nome della persona (minuscolo, 3–15 caratteri alfabetici, univoco). */
    private String nome;

    /** Colore dei capelli della persona. */
    private ColoriCrapa coloreCapelli;

    /** Colore degli occhi della persona. */
    private ColoriOch coloreOcchi;

    /** Colore della pelle della persona. */
    private ColoriPelle colorePelle;

    /** {@code true} se la persona porta gli occhiali. */
    private boolean occhiali;

    /** {@code true} se la persona è maschio, {@code false} se femmina. */
    private boolean sesso;

    /** {@code true} se la persona ha i capelli lunghi. */
    private boolean capelliLunghi;

    /** {@code true} se la persona ha la barba o i baffi. */
    private boolean barba;

    /** {@code true} se la persona porta il cappello. */
    private boolean cappello;

    /** {@code true} se la persona è pelata. */
    private boolean pelato;

    /** Lista statica di tutti i nomi già utilizzati, per garantire l'univocità. */
    private final static List<String> nomi = new ArrayList<>();

    /**
     * Crea una nuova {@code Persona} con tutte le caratteristiche specificate.
     *
     * @param nome          nome della persona (3–15 lettere, deve essere univoco)
     * @param cc            colore dei capelli
     * @param co            colore degli occhi
     * @param cp            colore della pelle
     * @param occhiali      {@code true} se porta gli occhiali
     * @param sesso         {@code true} se maschio, {@code false} se femmina
     * @param capelliLunghi {@code true} se ha i capelli lunghi
     * @param barba         {@code true} se ha la barba o i baffi
     * @param cappello      {@code true} se porta il cappello
     * @param pelato        {@code true} se è pelato
     * @throws NullPointerException     se {@code nome}, {@code cc}, {@code co} o {@code cp} sono {@code null}
     * @throws IllegalArgumentException se il nome non rispetta il formato o è già in uso
     */
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

    /**
     * Costruttore di copia. Crea una nuova istanza con gli stessi valori di {@code p}.
     * <p>
     * Non registra il nome nella lista {@code nomi} statica poiché
     * si assume che l'originale lo abbia già fatto.
     * </p>
     *
     * @param p la persona da copiare
     */
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
    }

    /**
     * Restituisce il nome della persona.
     *
     * @return il nome in minuscolo
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome della persona dopo aver verificato formato e unicità.
     *
     * @param nome il nome da impostare
     * @throws NullPointerException     se {@code nome} è {@code null}
     * @throws IllegalArgumentException se il nome non corrisponde al pattern {@code [a-z]{3,15}}
     *                                  (dopo trim e toLowerCase) o è già in uso
     */
    private void setNome(String nome) {
        if (nome == null) throw new NullPointerException("Il nome non può essere null");
        nome = nome.trim().toLowerCase();
        if (!nome.matches("[a-z]{3,15}")) throw new IllegalArgumentException("Formato nome non valido");
        if (nomi.contains(nome)) throw new IllegalArgumentException("Nome già utilizzato");
        this.nome = nome;
        nomi.add(nome);
    }

    /**
     * Restituisce il colore dei capelli della persona.
     *
     * @return il {@link ColoriCrapa} della persona
     */
    public ColoriCrapa getColoreCapelli() {
        return coloreCapelli;
    }

    /**
     * Imposta il colore dei capelli.
     *
     * @param coloreCapelli il colore da impostare
     * @throws NullPointerException se {@code coloreCapelli} è {@code null}
     */
    private void setColoreCapelli(ColoriCrapa coloreCapelli) {
        if (coloreCapelli == null) throw new NullPointerException("Il colore dei capelli non può essere null");
        this.coloreCapelli = coloreCapelli;
    }

    /**
     * Restituisce il colore degli occhi della persona.
     *
     * @return il {@link ColoriOch} della persona
     */
    public ColoriOch getColoreOcchi() {
        return coloreOcchi;
    }

    /**
     * Imposta il colore degli occhi.
     *
     * @param coloreOcchi il colore da impostare
     * @throws NullPointerException se {@code coloreOcchi} è {@code null}
     */
    private void setColoreOcchi(ColoriOch coloreOcchi) {
        if (coloreOcchi == null) throw new NullPointerException("Il colore degli occhi non può essere null");
        this.coloreOcchi = coloreOcchi;
    }

    /**
     * Restituisce il colore della pelle della persona.
     *
     * @return il {@link ColoriPelle} della persona
     */
    public ColoriPelle getColorePelle() {
        return colorePelle;
    }

    /**
     * Imposta il colore della pelle.
     *
     * @param colorePelle il colore da impostare
     * @throws NullPointerException se {@code colorePelle} è {@code null}
     */
    private void setColorePelle(ColoriPelle colorePelle) {
        if (colorePelle == null) throw new NullPointerException("Il colore della pelle non può essere null");
        this.colorePelle = colorePelle;
    }

    /**
     * Restituisce il sesso della persona.
     *
     * @return {@code true} se maschio, {@code false} se femmina
     */
    public boolean isSesso() {
        return sesso;
    }

    /**
     * Imposta il sesso della persona.
     *
     * @param sesso {@code true} per maschio, {@code false} per femmina
     */
    private void setSesso(boolean sesso) {
        this.sesso = sesso;
    }

    /**
     * Indica se la persona porta gli occhiali.
     *
     * @return {@code true} se porta gli occhiali
     */
    public boolean isOcchiali() {
        return occhiali;
    }

    /**
     * Imposta se la persona porta gli occhiali.
     *
     * @param occhiali {@code true} se porta gli occhiali
     */
    private void setOcchiali(boolean occhiali) {
        this.occhiali = occhiali;
    }

    /**
     * Indica se la persona ha i capelli lunghi.
     *
     * @return {@code true} se ha i capelli lunghi
     */
    public boolean isCapelliLunghi() {
        return capelliLunghi;
    }

    /**
     * Imposta se la persona ha i capelli lunghi.
     *
     * @param capelliLunghi {@code true} se ha i capelli lunghi
     */
    private void setCapelliLunghi(boolean capelliLunghi) {
        this.capelliLunghi = capelliLunghi;
    }

    /**
     * Indica se la persona ha la barba o i baffi.
     *
     * @return {@code true} se ha la barba o i baffi
     */
    public boolean isBarba() {
        return barba;
    }

    /**
     * Imposta se la persona ha la barba o i baffi.
     *
     * @param barba {@code true} se ha la barba o i baffi
     */
    private void setBarba(boolean barba) {
        this.barba = barba;
    }

    /**
     * Indica se la persona porta il cappello.
     *
     * @return {@code true} se porta il cappello
     */
    public boolean isCappello() {
        return cappello;
    }

    /**
     * Imposta se la persona porta il cappello.
     *
     * @param cappello {@code true} se porta il cappello
     */
    private void setCappello(boolean cappello) {
        this.cappello = cappello;
    }

    /**
     * Indica se la persona è pelata.
     *
     * @return {@code true} se è pelata
     */
    public boolean isPelato() {
        return pelato;
    }

    /**
     * Imposta se la persona è pelata.
     *
     * @param pelato {@code true} se è pelata
     */
    private void setPelato(boolean pelato) {
        this.pelato = pelato;
    }

    /**
     * Restituisce una copia della lista di tutti i nomi già registrati.
     *
     * @return una nuova {@link ArrayList} contenente i nomi usati finora
     */
    public static List<String> getNomi() {
        return new ArrayList<>(nomi);
    }

    /**
     * Metodo di supporto alla deserializzazione. Garantisce che il nome
     * venga aggiunto alla lista statica {@code nomi} se non è già presente,
     * evitando duplicati dopo un ciclo di serializzazione/deserializzazione.
     *
     * @return questa stessa istanza
     */
    @Serial
    private Object readResolve() {
        if (!nomi.contains(this.nome)) nomi.add(this.nome);
        return this;
    }

    /**
     * Restituisce una stringa con tutte le caratteristiche della persona,
     * formattata in minuscolo.
     *
     * @return descrizione testuale della persona
     */
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