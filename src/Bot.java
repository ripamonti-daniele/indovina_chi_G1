import java.util.*;

public class Bot {
    private final Map<String, List<Persona>> opzioni = new HashMap<>();
    private final List<Persona> tutte;
    private final String[] domande;

    private Persona personaggioDaScoprire;

    //utilizzo il nodoCorrente così che il bot possa fare le domande al giocatore
    private Nodo nodoCorrente;
    private Albero albero;

    public Bot(List<Persona> persone) {
        if (persone == null || persone.isEmpty()) throw new IllegalArgumentException("La lista non può essere null o vuota");
        this.tutte = persone;
        domande = new String[17];
        inizializza();
    }

    public String[] getDomande() {
        return domande.clone();
    }

    public Persona PersonaRandom(){
        Random r = new Random();
        personaggioDaScoprire = tutte.get(r.nextInt(tutte.size()));
        return personaggioDaScoprire;
    }

    public Persona getPersonaggioDaScoprire(){
        return personaggioDaScoprire;
    }

    public boolean rispostaSuPersonaggioDaScoprire(String domanda){
        if(personaggioDaScoprire == null){
            throw new IllegalArgumentException("il bot non ha scelto il personaggio");
        }
        return corrisponde(personaggioDaScoprire, domanda);
    }

    public boolean risposteSuPersonaSpecifica(String d, Persona p){
        return corrisponde(p, d);
    }

    public String getDomandaCorrente(){
        if(nodoCorrente == null){
            return null;
        }
        return nodoCorrente.getDomanda();
    }

    //va avanti nell'albero in base alla risposta, se il bot indovina restituisce la persona
    public Persona avanzaDentroAlbero(boolean r){
        if(nodoCorrente == null || nodoCorrente.getDomanda() == null){
            return null;
        }

        if(r){
            nodoCorrente = nodoCorrente.getSi();
        }else{
            nodoCorrente = nodoCorrente.getNo();
        }

        if(nodoCorrente != null && nodoCorrente.getPersona() != null){
            return nodoCorrente.getPersona();
        }
        return null;
    }

    public List<String> personeEliminate(boolean r){
        Nodo PersonaScartata;

        if(nodoCorrente == null){
            return new ArrayList<>();
        }
        if(r){
            PersonaScartata = nodoCorrente.getNo();
        }else{
            PersonaScartata = nodoCorrente.getSi();
        }
        return raccogliNomi(PersonaScartata);
    }

    private List<String> raccogliNomi(Nodo n) {
        List<String> nomi = new ArrayList<>();
        raccogliNomiRicorsivo(n, nomi);
        return nomi;
    }

    private void raccogliNomiRicorsivo(Nodo n, List<String> nomi) {
        if (n == null) {
            return;
        }
        if (n.getPersona() != null) {
            nomi.add(n.getPersona().getNome());
            return;
        }
        raccogliNomiRicorsivo(n.getSi(), nomi);
        raccogliNomiRicorsivo(n.getNo(), nomi);
    }

    public Albero creaAlbero() {
        List<String> domandeList = new ArrayList<>(Arrays.asList(domande));
        Collections.shuffle(domandeList);

        String domandaRoot = domandeList.removeFirst();
        Albero albero = new Albero(domandaRoot);

        List<String> domandeUsate = new ArrayList<>();
        domandeUsate.add(domandaRoot);

        List<Persona> personeSI = new ArrayList<>(opzioni.get(domandaRoot + "S"));
        List<Persona> personeNO = new ArrayList<>(opzioni.get(domandaRoot + "N"));


        List<String> confermateSI = new ArrayList<>();
        confermateSI.add(domandaRoot);
        costruisciSottoAlbero(albero, albero.getRootId(), true,  personeSI, new ArrayList<>(domandeUsate), confermateSI);

        costruisciSottoAlbero(albero, albero.getRootId(), false, personeNO, new ArrayList<>(domandeUsate), new ArrayList<>());

        return albero;
    }


    private void costruisciSottoAlbero(Albero albero, int idPadre, boolean rispostaPadre, List<Persona> personeCorrente, List<String> domandeUsate, List<String> domandeConfermate) {
        // ramo morto: nessuna persona rimasta
        if (personeCorrente.isEmpty()) return;

        // unica persona rimasta
        if (personeCorrente.size() == 1) {
            albero.inserisciPersona(idPadre, personeCorrente.getFirst(), rispostaPadre);
            return;
        }

        // scegliamo una domanda random tra quelle non ancora usate
        List<String> disponibili = new ArrayList<>();
        for (String d : domande) {
            if (!domandeUsate.contains(d) && !categoriaGiaConfermata(d, domandeConfermate)) {
                List<Persona> si = interseca(personeCorrente, opzioni.get(d + "S"));
                List<Persona> no = interseca(personeCorrente, opzioni.get(d + "N"));
                if (!si.isEmpty() && !no.isEmpty()) {
                    disponibili.add(d);
                }
            }
        }

        if (disponibili.isEmpty()) {
            for (Persona p : personeCorrente) {
                albero.inserisciPersona(idPadre, p, rispostaPadre);
            }
            return;
        }


        Collections.shuffle(disponibili);
        String domandaScelta = disponibili.getFirst();

        List<Persona> tutteSI = opzioni.get(domandaScelta + "S");
        List<Persona> tutteNO = opzioni.get(domandaScelta + "N");

        List<Persona> filtroSI = interseca(personeCorrente, tutteSI);
        List<Persona> filtroNO = interseca(personeCorrente, tutteNO);


        int idNuovo = albero.inserisciDomanda(idPadre, domandaScelta, rispostaPadre);

        // aggiungo la domandascelta alle domande usate cosi facendo si aggiorna la lista di domandeusate
        List<String> nuoveUsate = new ArrayList<>(domandeUsate);
        nuoveUsate.add(domandaScelta);

        // ramo si: la domanda è confermata quindi la aggiungo a domandeconfermate
        List<String> nuoveConfermateSI = new ArrayList<>(domandeConfermate);
        nuoveConfermateSI.add(domandaScelta);

        // ramo no: la domanda non è confermata quindi domandeconfermate rimane invariata
        List<String> nuoveConformateNO = new ArrayList<>(domandeConfermate);

        costruisciSottoAlbero(albero, idNuovo, true,  filtroSI, new ArrayList<>(nuoveUsate), nuoveConfermateSI);
        costruisciSottoAlbero(albero, idNuovo, false, filtroNO, new ArrayList<>(nuoveUsate), nuoveConformateNO);
    }

    //serve per controllare se la domanda appartiene a una categoria gia risolta

    private boolean categoriaGiaConfermata(String domanda, List<String> domandeConfermate) {
        //questo array ci permette di eliminare tutte le domande simili a quella a cui abbiamo già risposto
        //esempio: se il bot chiede se ha i capelli rossi e rispondo di si, non mi farà domande inerenti ai capelli
        String[] capelli = {"ha i capelli castani?", "ha i capelli neri?", "ha i capelli biondi?", "ha i capelli rossi?", "ha i capelli bianchi?"};
        String[] occhi   = {"ha gli occhi marroni?", "ha gli occhi blu?", "ha gli occhi verdi?"};
        String[] pelle   = {"ha la pelle bianca?", "ha la pelle nera?", "ha la pelle mulatta?"};

        String[] categoria = null;
        for (String c : capelli)
            if (c.equals(domanda)) {
                categoria = capelli; break;
            }

        if (categoria == null){
            for (String c : occhi)
                if (c.equals(domanda)) {
                    categoria = occhi; break;
                }
        }

        if (categoria == null)
            for (String c : pelle) if (c.equals(domanda)) { categoria = pelle; break; }

        //esempio: categoria calvi, occhiali ecc ecc...
        if (categoria == null) return false;

        for (String confermata : domandeConfermate)
            for (String c : categoria)
                if (c.equals(confermata)) return true;

        return false;
    }

    //intersezione tra 2 liste di persone (in questo caso tra le persone rimaste nel nodo e le persone tutte si/no)
    private List<Persona> interseca(List<Persona> a, List<Persona> b) {
        List<Persona> risultato = new ArrayList<>();
        for (Persona pA : a)
            for (Persona pB : b)
                if (pA.getNome().equals(pB.getNome())) {
                    risultato.add(pA);
                    break;
                }
        return risultato;
    }

    private void inizializza() {
        domande[0] = "è maschio?";
        domande[1] = "ha i capelli castani?";
        domande[2] = "ha i capelli neri?";
        domande[3] = "ha i capelli biondi?";
        domande[4] = "ha i capelli rossi?";
        domande[5] = "ha i capelli bianchi?";
        domande[6] = "ha la pelle bianca?";
        domande[7] = "ha la pelle nera?";
        domande[8] = "ha la pelle mulatta?";
        domande[9] = "ha gli occhi marroni?";
        domande[10] = "ha gli occhi blu?";
        domande[11] = "ha gli occhi verdi?";
        domande[12] = "ha gli occhiali?";
        domande[13] = "ha i capelli lunghi?";
        domande[14] = "ha la barba o i baffi?";
        domande[15] = "ha il cappello?";
        domande[16] = "è pelato?";

        for (String domanda : domande) {
            List<Persona> si = new ArrayList<>();
            List<Persona> no = new ArrayList<>();
            for (Persona p : tutte) {
                if (corrisponde(p, domanda)) si.add(p);
                else no.add(p);
            }
            opzioni.put(domanda + "S", si);
            opzioni.put(domanda + "N", no);
        }
    }

    private boolean corrisponde(Persona p, String domanda) {
        return switch (domanda) {
            case "è maschio?" -> p.isSesso();
            case "ha i capelli castani?" -> p.getColoreCapelli() == ColoriCrapa.CASTANO;
            case "ha i capelli neri?" -> p.getColoreCapelli() == ColoriCrapa.NERO;
            case "ha i capelli biondi?" -> p.getColoreCapelli() == ColoriCrapa.BIONDO;
            case "ha i capelli rossi?" -> p.getColoreCapelli() == ColoriCrapa.ROSSO;
            case "ha i capelli bianchi?" -> p.getColoreCapelli() == ColoriCrapa.BIANCO;
            case "ha la pelle bianca?" -> p.getColorePelle() == ColoriPelle.BIANCO;
            case "ha la pelle nera?" -> p.getColorePelle() == ColoriPelle.NERO;
            case "ha la pelle mulatta?" -> p.getColorePelle() == ColoriPelle.MULATTO;
            case "ha gli occhi marroni?" -> p.getColoreOcchi() == ColoriOch.MARRONE;
            case "ha gli occhi blu?" -> p.getColoreOcchi() == ColoriOch.BLU;
            case "ha gli occhi verdi?" -> p.getColoreOcchi() == ColoriOch.VERDE;
            case "ha gli occhiali?" -> p.isOcchiali();
            case "ha i capelli lunghi?" -> p.isCapelliLunghi();
            case "ha la barba o i baffi?" -> p.isBarba();
            case "ha il cappello?" -> p.isCappello();
            case "è pelato?" -> p.isPelato();
            default -> throw new IllegalArgumentException("Domanda non riconosciuta: " + domanda);
        };
    }
}