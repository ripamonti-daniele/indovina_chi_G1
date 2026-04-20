import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Bot implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;

    private final Map<Domanda, List<Persona>> opzioniSI = new HashMap<>();
    private final Map<Domanda, List<Persona>> opzioniNO = new HashMap<>();
    private List<Persona> tutte;

    private Persona personaggioDaScoprire;
    private Nodo nodoCorrente;
    private Albero albero;

    public Bot(List<Persona> persone, boolean difficile) {
        setPersone(persone);
        setPersonaggioDaScoprire();
        inizializza(difficile);
    }

    public Bot(List<Persona> persone) {
        this(persone, false);
    }

    private void setPersone(List<Persona> persone) {
        if (persone == null || persone.isEmpty()) throw new IllegalArgumentException("Persone non può essere null o vuota");
        tutte = new ArrayList<>();
        for (Persona p : persone) {
            if (p != null) tutte.add(new Persona(p));
        }
        if (tutte.isEmpty()) throw new IllegalArgumentException("Persone non può contenere solo oggetti null");
    }

    public static Domanda[] getDomande() {
        return Domanda.values();
    }

    private void inizializza(boolean difficile) {
        for (Domanda d : Domanda.values()) {
            List<Persona> si = new ArrayList<>();
            List<Persona> no = new ArrayList<>();
            for (Persona p : tutte) {
                if (d.corrisponde(p)) si.add(p);
                else no.add(p);
            }
            opzioniSI.put(d, si);
            opzioniNO.put(d, no);
        }
        creaAlbero(difficile);
    }

    public Nodo getRoot() {
        return albero.getRoot();
    }

    private void setPersonaggioDaScoprire() {
        personaggioDaScoprire = tutte.get(new Random().nextInt(tutte.size()));
    }

    public Persona getPersonaggioDaScoprire() {
        return personaggioDaScoprire;
    }

    public boolean rispostaSuPersonaggioDaScoprire(Domanda d) {
        if (personaggioDaScoprire == null) throw new IllegalStateException("Il bot non ha ancora scelto il personaggio");
        return d.corrisponde(personaggioDaScoprire);
    }

//    public static boolean rispostaSuPersona(Domanda d, Persona p) {
//        return d.corrisponde(p);
//    }

    public String getDomandaCorrente() {
        if (nodoCorrente instanceof NodoDomanda nd) return nd.getDomanda();
        return null;
    }

    public Persona avanzaDentroAlbero(boolean risposta) {
        if (!(nodoCorrente instanceof NodoDomanda)) return null;
        nodoCorrente = risposta ? nodoCorrente.getSi() : nodoCorrente.getNo();
        if (nodoCorrente instanceof NodoPersona np) return np.getPersona();
        return null;
    }

//    public List<String> personeEliminate(boolean risposta) {
//        if (nodoCorrente == null) return new ArrayList<>();
//        Nodo scartato = risposta ? nodoCorrente.getNo() : nodoCorrente.getSi();
//        return raccogliNomi(scartato);
//    }
//
//    private List<String> raccogliNomi(Nodo n) {
//        List<String> nomi = new ArrayList<>();
//        raccogliNomiRicorsivo(n, nomi);
//        return nomi;
//    }
//
//    private void raccogliNomiRicorsivo(Nodo n, List<String> nomi) {
//        if (n == null) return;
//        if (n instanceof NodoPersona np) {
//            nomi.add(np.getPersona().getNome());
//            return;
//        }
//        raccogliNomiRicorsivo(n.getSi(), nomi);
//        raccogliNomiRicorsivo(n.getNo(), nomi);
//    }

    private Domanda domandaMigliore(List<Domanda> disponibili, List<Persona> personeCorrente) {
        Domanda migliore = null;
        int entropia = Integer.MAX_VALUE;
        for (Domanda d : disponibili) {
            List<Persona> si = interseca(personeCorrente, opzioniSI.get(d));
            List<Persona> no = interseca(personeCorrente, opzioniNO.get(d));
            int entr = Math.abs(si.size() - no.size());
            if (entr == 0 && !si.isEmpty() && !no.isEmpty()) return d;
            if (entr < entropia) {
                migliore = d;
                entropia = entr;
            }
        }
        return migliore;
    }

    private void creaAlbero(boolean difficile) {
        List<Domanda> domandeList = new ArrayList<>(Arrays.asList(Domanda.values()));
        Domanda domandaRoot;
        if (difficile) domandaRoot = domandaMigliore(domandeList, tutte);

        else {
            Collections.shuffle(domandeList);
            domandaRoot = domandeList.getFirst();
        }
        domandeList.remove(domandaRoot);

        albero = new Albero(domandaRoot.getTesto());
        nodoCorrente = albero.getRoot();

        Set<Domanda> usate = new HashSet<>();
        usate.add(domandaRoot);

        Set<Domanda.Categoria> categorieConfermateSI = new HashSet<>();
        if (domandaRoot.getCategoria() != Domanda.Categoria.NESSUNA) categorieConfermateSI.add(domandaRoot.getCategoria());

        costruisciSottoAlbero(albero.getRootId(), true, interseca(tutte, opzioniSI.get(domandaRoot)), new HashSet<>(usate), new HashSet<>(categorieConfermateSI), difficile);

        costruisciSottoAlbero(albero.getRootId(), false, interseca(tutte, opzioniNO.get(domandaRoot)), new HashSet<>(usate), new HashSet<>(), difficile);
    }

    private void costruisciSottoAlbero(int idPadre, boolean rispostaPadre, List<Persona> personeCorrente, Set<Domanda> usate, Set<Domanda.Categoria> categorieConfermate, boolean difficile) {
        if (personeCorrente.isEmpty()) return;

        if (personeCorrente.size() == 1) {
            albero.inserisciPersona(idPadre, personeCorrente.getFirst(), rispostaPadre);
            return;
        }

        List<Domanda> disponibili = new ArrayList<>();
        for (Domanda d : Domanda.values()) {
            if (usate.contains(d)) continue;
            if (d.getCategoria() != Domanda.Categoria.NESSUNA && categorieConfermate.contains(d.getCategoria())) continue;
            List<Persona> si = interseca(personeCorrente, opzioniSI.get(d));
            List<Persona> no = interseca(personeCorrente, opzioniNO.get(d));
            if (!si.isEmpty() && !no.isEmpty()) disponibili.add(d);
        }

        if (disponibili.isEmpty()) {
            for (Persona p : personeCorrente) albero.inserisciPersona(idPadre, p, rispostaPadre);
            return;
        }

        Domanda scelta = difficile ? domandaMigliore(disponibili, personeCorrente) : disponibili.get(new Random().nextInt(disponibili.size()));
        int idNuovo = albero.inserisciDomanda(idPadre, scelta.getTesto(), rispostaPadre);

        Set<Domanda> nuoveUsate = new HashSet<>(usate);
        nuoveUsate.add(scelta);

        Set<Domanda.Categoria> confermateSI = new HashSet<>(categorieConfermate);
        if (scelta.getCategoria() != Domanda.Categoria.NESSUNA) confermateSI.add(scelta.getCategoria());

        costruisciSottoAlbero(idNuovo, true,  interseca(personeCorrente, opzioniSI.get(scelta)), new HashSet<>(nuoveUsate), confermateSI, difficile);
        costruisciSottoAlbero(idNuovo, false, interseca(personeCorrente, opzioniNO.get(scelta)), new HashSet<>(nuoveUsate), new HashSet<>(categorieConfermate), difficile);
    }

    private List<Persona> interseca(List<Persona> a, List<Persona> b) {
        List<Persona> risultato = new ArrayList<>();
        for (Persona pA : a) {
            for (Persona pB : b) {
                if (pA.getNome().equals(pB.getNome())) {
                    risultato.add(pA);
                    break;
                }
            }
        }
        return risultato;
    }

}