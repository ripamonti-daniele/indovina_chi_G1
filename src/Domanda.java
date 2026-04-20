/**
 * Enum che rappresenta tutte le domande utilizzabili nel gioco "Indovina chi?"
 * per identificare il personaggio segreto avversario.
 * <p>
 * Ogni domanda è associata a un testo leggibile e a una {@link Categoria} che
 * raggruppa le domande mutuamente esclusive (es. colore capelli, colore occhi,
 * colore pelle). Le domande senza categoria appartengono a {@link Categoria#NESSUNA}.
 * </p>
 */
public enum Domanda {

    /** Domanda sul sesso: "è maschio?". Nessuna categoria. */
    SESSO("è maschio?", Categoria.NESSUNA),

    /** Domanda sul colore dei capelli: "ha i capelli castani?". Categoria {@link Categoria#CAPELLI}. */
    CAPELLI_CASTANI("ha i capelli castani?", Categoria.CAPELLI),

    /** Domanda sul colore dei capelli: "ha i capelli neri?". Categoria {@link Categoria#CAPELLI}. */
    CAPELLI_NERI("ha i capelli neri?", Categoria.CAPELLI),

    /** Domanda sul colore dei capelli: "ha i capelli biondi?". Categoria {@link Categoria#CAPELLI}. */
    CAPELLI_BIONDI("ha i capelli biondi?", Categoria.CAPELLI),

    /** Domanda sul colore dei capelli: "ha i capelli rossi?". Categoria {@link Categoria#CAPELLI}. */
    CAPELLI_ROSSI("ha i capelli rossi?", Categoria.CAPELLI),

    /** Domanda sul colore dei capelli: "ha i capelli bianchi?". Categoria {@link Categoria#CAPELLI}. */
    CAPELLI_BIANCHI("ha i capelli bianchi?", Categoria.CAPELLI),

    /** Domanda sul colore della pelle: "ha la pelle bianca?". Categoria {@link Categoria#PELLE}. */
    PELLE_BIANCA("ha la pelle bianca?", Categoria.PELLE),

    /** Domanda sul colore della pelle: "ha la pelle nera?". Categoria {@link Categoria#PELLE}. */
    PELLE_NERA("ha la pelle nera?", Categoria.PELLE),

    /** Domanda sul colore della pelle: "ha la pelle mulatta?". Categoria {@link Categoria#PELLE}. */
    PELLE_MULATTA("ha la pelle mulatta?", Categoria.PELLE),

    /** Domanda sul colore degli occhi: "ha gli occhi marroni?". Categoria {@link Categoria#OCCHI}. */
    OCCHI_MARRONI("ha gli occhi marroni?", Categoria.OCCHI),

    /** Domanda sul colore degli occhi: "ha gli occhi blu?". Categoria {@link Categoria#OCCHI}. */
    OCCHI_BLU("ha gli occhi blu?", Categoria.OCCHI),

    /** Domanda sul colore degli occhi: "ha gli occhi verdi?". Categoria {@link Categoria#OCCHI}. */
    OCCHI_VERDI("ha gli occhi verdi?", Categoria.OCCHI),

    /** Domanda sugli occhiali: "ha gli occhiali?". Nessuna categoria. */
    OCCHIALI("ha gli occhiali?", Categoria.NESSUNA),

    /** Domanda sulla lunghezza dei capelli: "ha i capelli lunghi?". Nessuna categoria. */
    CAPELLI_LUNGHI("ha i capelli lunghi?", Categoria.NESSUNA),

    /** Domanda sulla barba: "ha la barba o i baffi?". Nessuna categoria. */
    BARBA("ha la barba o i baffi?", Categoria.NESSUNA),

    /** Domanda sul cappello: "ha il cappello?". Nessuna categoria. */
    CAPPELLO("ha il cappello?", Categoria.NESSUNA),

    /** Domanda sulla calvizìe: "è pelato?". Nessuna categoria. */
    PELATO("è pelato?", Categoria.NESSUNA);

    /**
     * Categoria di raggruppamento delle domande mutuamente esclusive.
     * <p>
     * Le domande appartenenti alla stessa categoria si escludono a vicenda:
     * una volta confermata una risposta positiva per una categoria, le altre
     * domande della stessa categoria diventano irrilevanti.
     * </p>
     */
    public enum Categoria {
        /** Raggruppa le domande sul colore dei capelli. */
        CAPELLI,
        /** Raggruppa le domande sul colore degli occhi. */
        OCCHI,
        /** Raggruppa le domande sul colore della pelle. */
        PELLE,
        /** Indica che la domanda non appartiene a nessun gruppo mutuamente esclusivo. */
        NESSUNA
    }

    /** Testo leggibile della domanda, usato nella UI e per la ricerca tramite {@link #fromTesto(String)}. */
    private final String testo;

    /** Categoria di appartenenza della domanda. */
    private final Categoria categoria;

    /**
     * Costruisce una costante {@code Domanda} con il testo e la categoria specificati.
     *
     * @param testo     il testo leggibile della domanda
     * @param categoria la {@link Categoria} di appartenenza
     */
    Domanda(String testo, Categoria categoria) {
        this.testo = testo;
        this.categoria = categoria;
    }

    /**
     * Restituisce il testo leggibile della domanda.
     *
     * @return il testo della domanda
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Restituisce la categoria di appartenenza della domanda.
     *
     * @return la {@link Categoria} della domanda
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Verifica se la {@link Persona} specificata corrisponde alla domanda,
     * ovvero se la risposta alla domanda per quella persona è "sì".
     *
     * @param p la {@link Persona} su cui valutare la domanda
     * @return {@code true} se la persona soddisfa la condizione della domanda,
     *         {@code false} altrimenti
     */
    public boolean corrisponde(Persona p) {
        return switch (this) {
            case SESSO -> p.isSesso();
            case CAPELLI_CASTANI -> p.getColoreCapelli() == ColoriCrapa.CASTANO;
            case CAPELLI_NERI -> p.getColoreCapelli() == ColoriCrapa.NERO;
            case CAPELLI_BIONDI -> p.getColoreCapelli() == ColoriCrapa.BIONDO;
            case CAPELLI_ROSSI -> p.getColoreCapelli() == ColoriCrapa.ROSSO;
            case CAPELLI_BIANCHI -> p.getColoreCapelli() == ColoriCrapa.BIANCO;
            case PELLE_BIANCA -> p.getColorePelle() == ColoriPelle.BIANCO;
            case PELLE_NERA -> p.getColorePelle() == ColoriPelle.NERO;
            case PELLE_MULATTA -> p.getColorePelle() == ColoriPelle.MULATTO;
            case OCCHI_MARRONI -> p.getColoreOcchi() == ColoriOch.MARRONE;
            case OCCHI_BLU -> p.getColoreOcchi() == ColoriOch.BLU;
            case OCCHI_VERDI -> p.getColoreOcchi() == ColoriOch.VERDE;
            case OCCHIALI -> p.isOcchiali();
            case CAPELLI_LUNGHI -> p.isCapelliLunghi();
            case BARBA -> p.isBarba();
            case CAPPELLO -> p.isCappello();
            case PELATO -> p.isPelato();
        };
    }

    /**
     * Restituisce la costante {@code Domanda} il cui testo corrisponde esattamente
     * alla stringa fornita.
     *
     * @param testo il testo della domanda da cercare
     * @return la {@code Domanda} corrispondente al testo
     * @throws IllegalArgumentException se nessuna domanda corrisponde al testo fornito
     */
    public static Domanda fromTesto(String testo) {
        for (Domanda d : values()) if (d.testo.equals(testo)) return d;
        throw new IllegalArgumentException("Domanda non riconosciuta: " + testo);
    }
}