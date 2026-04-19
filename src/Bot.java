import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Bot implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private final Map<String, List<Persona>> opzioni = new HashMap<>();
    private List<Persona> tutte;
    private static final String[] domande = {
        "è maschio?",
        "ha i capelli castani?",
        "ha i capelli neri?",
        "ha i capelli biondi?",
        "ha i capelli rossi?",
        "ha i capelli bianchi?",
        "ha la pelle bianca?",
        "ha la pelle nera?",
        "ha la pelle mulatta?",
        "ha gli occhi marroni?",
        "ha gli occhi blu?",
        "ha gli occhi verdi?",
        "ha gli occhiali?",
        "ha i capelli lunghi?",
        "ha la barba o i baffi?",
        "ha il cappello?",
        "è pelato?"
    };

    private Persona personaggioDaScoprire;

    //utilizzo il nodoCorrente così che il bot possa fare le domande al giocatore
    private Nodo nodoCorrente;
    private Albero albero;

    public Bot(List<Persona> persone, boolean difficile) {
        setPersone(persone);
        inizializza(difficile);
    }

    public Bot(List<Persona> persone) {
        this(persone, false);
    }

    private void setPersone(List<Persona> persone) {
        if (persone == null) throw new IllegalArgumentException("Persone non può essere null");
        if (persone.isEmpty()) throw new IllegalArgumentException("Persone non può essere vuota");
        tutte = new ArrayList<>();
        for (Persona p : persone) if (p != null) tutte.add(new Persona(p));
        if (tutte.isEmpty()) throw new IllegalArgumentException("Persone non può contenere soltanto oggetti null");
    }

    public static String[] getDomande() {
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

    private String domandaMigliore(List<String> domande, List<Persona> personeCorrente) {
        String domanda = "";
        int entropia = Integer.MAX_VALUE;
        for (String d : domande) {
            List<Persona> si = interseca(personeCorrente, opzioni.get(d + "S"));
            List<Persona> no = interseca(personeCorrente, opzioni.get(d + "N"));
            int entr = Math.abs(si.size() - no.size());
            if (entr == 0 && !si.isEmpty() && !no.isEmpty()){
                return d;
            }
            if (entr < entropia) {
                domanda = d;
                entropia = entr;
            }
        }
        return domanda;
    }

    private void creaAlbero(boolean difficile) {
        List<String> domandeList = new ArrayList<>(Arrays.asList(domande));
        String domandaRoot;
        if (difficile) {
            domandaRoot = domandaMigliore(domandeList, tutte);
            domandeList.remove(domandaRoot);
        }
        else {
            Collections.shuffle(domandeList);
            domandaRoot = domandeList.removeFirst();
        }
        albero = new Albero(domandaRoot);

        List<String> domandeUsate = new ArrayList<>();
        domandeUsate.add(domandaRoot);

        List<Persona> personeSI = new ArrayList<>(opzioni.get(domandaRoot + "S"));
        List<Persona> personeNO = new ArrayList<>(opzioni.get(domandaRoot + "N"));


        List<String> confermateSI = new ArrayList<>();
        confermateSI.add(domandaRoot);
        costruisciSottoAlbero(albero.getRootId(), true,  personeSI, new ArrayList<>(domandeUsate), confermateSI, difficile);

        costruisciSottoAlbero(albero.getRootId(), false, personeNO, new ArrayList<>(domandeUsate), new ArrayList<>(), difficile);
    }

    private void costruisciSottoAlbero(int idPadre, boolean rispostaPadre, List<Persona> personeCorrente, List<String> domandeUsate, List<String> domandeConfermate, boolean difficile) {
        // ramo morto: nessuna persona rimasta
        if (personeCorrente.isEmpty()) return;

        // unica persona rimasta
        if (personeCorrente.size() == 1) {
            albero.inserisciPersona(idPadre, personeCorrente.getFirst(), rispostaPadre);
            return;
        }

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

        String domandaScelta = "";

        // scegliamo una domanda tra quelle non ancora usate
        if (!difficile) {
            Collections.shuffle(disponibili);
            domandaScelta = disponibili.getFirst();
        }
        else domandaScelta = domandaMigliore(disponibili, personeCorrente);

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

        costruisciSottoAlbero(idNuovo, true,  filtroSI, new ArrayList<>(nuoveUsate), nuoveConfermateSI, difficile);
        costruisciSottoAlbero(idNuovo, false, filtroNO, new ArrayList<>(nuoveUsate), nuoveConformateNO, difficile);
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

        //esempio: categoria calvi, occhiali ecc...
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

    private void inizializza(boolean difficile) {
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
        creaAlbero(difficile);
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

    public Nodo getRoot() {
        return albero.getRoot();
    }
}