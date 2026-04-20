public enum Domanda {
    SESSO        ("è maschio?",             Categoria.NESSUNA),
    CAPELLI_CAST ("ha i capelli castani?",  Categoria.CAPELLI),
    CAPELLI_NERI ("ha i capelli neri?",     Categoria.CAPELLI),
    CAPELLI_BION ("ha i capelli biondi?",   Categoria.CAPELLI),
    CAPELLI_ROSS ("ha i capelli rossi?",    Categoria.CAPELLI),
    CAPELLI_BIAN ("ha i capelli bianchi?",  Categoria.CAPELLI),
    PELLE_BIANCA ("ha la pelle bianca?",    Categoria.PELLE),
    PELLE_NERA   ("ha la pelle nera?",      Categoria.PELLE),
    PELLE_MUL    ("ha la pelle mulatta?",   Categoria.PELLE),
    OCCHI_MARR   ("ha gli occhi marroni?",  Categoria.OCCHI),
    OCCHI_BLU    ("ha gli occhi blu?",      Categoria.OCCHI),
    OCCHI_VERDI  ("ha gli occhi verdi?",    Categoria.OCCHI),
    OCCHIALI     ("ha gli occhiali?",       Categoria.NESSUNA),
    CAPELLI_LUNG ("ha i capelli lunghi?",   Categoria.NESSUNA),
    BARBA        ("ha la barba o i baffi?", Categoria.NESSUNA),
    CAPPELLO     ("ha il cappello?",        Categoria.NESSUNA),
    PELATO       ("è pelato?",              Categoria.NESSUNA);

    public enum Categoria { CAPELLI, OCCHI, PELLE, NESSUNA }

    private final String testo;
    private final Categoria categoria;

    Domanda(String testo, Categoria categoria) {
        this.testo = testo;
        this.categoria = categoria;
    }

    public String getTesto() {
        return testo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public boolean corrisponde(Persona p) {
        return switch (this) {
            case SESSO        -> p.isSesso();
            case CAPELLI_CAST -> p.getColoreCapelli() == ColoriCrapa.CASTANO;
            case CAPELLI_NERI -> p.getColoreCapelli() == ColoriCrapa.NERO;
            case CAPELLI_BION -> p.getColoreCapelli() == ColoriCrapa.BIONDO;
            case CAPELLI_ROSS -> p.getColoreCapelli() == ColoriCrapa.ROSSO;
            case CAPELLI_BIAN -> p.getColoreCapelli() == ColoriCrapa.BIANCO;
            case PELLE_BIANCA -> p.getColorePelle() == ColoriPelle.BIANCO;
            case PELLE_NERA   -> p.getColorePelle() == ColoriPelle.NERO;
            case PELLE_MUL    -> p.getColorePelle() == ColoriPelle.MULATTO;
            case OCCHI_MARR   -> p.getColoreOcchi() == ColoriOch.MARRONE;
            case OCCHI_BLU    -> p.getColoreOcchi() == ColoriOch.BLU;
            case OCCHI_VERDI  -> p.getColoreOcchi() == ColoriOch.VERDE;
            case OCCHIALI     -> p.isOcchiali();
            case CAPELLI_LUNG -> p.isCapelliLunghi();
            case BARBA        -> p.isBarba();
            case CAPPELLO     -> p.isCappello();
            case PELATO       -> p.isPelato();
        };
    }

    public static Domanda fromTesto(String testo) {
        for (Domanda d : values()) if (d.testo.equals(testo)) return d;
        throw new IllegalArgumentException("Domanda non riconosciuta: " + testo);
    }
}