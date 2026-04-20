import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Rappresenta il bot avversario nel gioco "Indovina chi?".
 * <p>
 * Il bot costruisce internamente un albero decisionale binario basato sulle
 * {@link Domanda} disponibili e sulle {@link Persona} in gioco, e lo percorre
 * turno per turno per indovinare il personaggio segreto del giocatore.
 * Supporta due modalità di gioco: normale (domande casuali) e difficile
 * (domande ottimizzate per massimizzare l'entropia).
 * </p>
 * Implementa {@link Serializable} per la persistenza della partita.
 */
public class Bot implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;

    /**
     * Mappa che associa ogni {@link Domanda} alla lista di {@link Persona}
     * che rispondono "sì" a quella domanda.
     */
    private final Map<Domanda, List<Persona>> opzioniSI = new HashMap<>();

    /**
     * Mappa che associa ogni {@link Domanda} alla lista di {@link Persona}
     * che rispondono "no" a quella domanda.
     */
    private final Map<Domanda, List<Persona>> opzioniNO = new HashMap<>();

    /** Lista di tutte le persone in gioco (copie difensive). */
    private List<Persona> tutte;

    /** Il personaggio scelto casualmente dal bot come proprio personaggio segreto. */
    private Persona personaggioDaScoprire;

    /** Il nodo corrente dell'albero decisionale durante la partita. */
    private Nodo nodoCorrente;

    /** L'albero decisionale binario costruito dal bot. */
    private Albero albero;

    /**
     * Crea un nuovo {@code Bot} con la lista di persone e la modalità di gioco specificate.
     *
     * @param persone    la lista di {@link Persona} disponibili nel gioco
     * @param difficile  {@code true} per la modalità difficile (domande ottimizzate),
     *                   {@code false} per la modalità normale (domande casuali)
     * @throws IllegalArgumentException se {@code persone} è {@code null}, vuota,
     *                                  o contiene solo elementi {@code null}
     */
    public Bot(List<Persona> persone, boolean difficile) {
        setPersone(persone);
        setPersonaggioDaScoprire();
        inizializza(difficile);
    }

    /**
     * Crea un nuovo {@code Bot} in modalità normale (domande casuali).
     *
     * @param persone la lista di {@link Persona} disponibili nel gioco
     * @throws IllegalArgumentException se {@code persone} è {@code null}, vuota,
     *                                  o contiene solo elementi {@code null}
     */
    public Bot(List<Persona> persone) {
        this(persone, false);
    }

    /**
     * Inizializza la lista interna delle persone effettuando copie difensive
     * di ogni elemento non {@code null}.
     *
     * @param persone la lista di persone da impostare
     * @throws IllegalArgumentException se {@code persone} è {@code null}, vuota,
     *                                  o contiene solo elementi {@code null}
     */
    private void setPersone(List<Persona> persone) {
        if (persone == null || persone.isEmpty()) throw new IllegalArgumentException("Persone non può essere null o vuota");
        tutte = new ArrayList<>();
        for (Persona p : persone) {
            if (p != null) tutte.add(new Persona(p));
        }
        if (tutte.isEmpty()) throw new IllegalArgumentException("Persone non può contenere solo oggetti null");
    }

    /**
     * Restituisce tutte le domande disponibili nel gioco.
     *
     * @return array di tutte le costanti dell'enum {@link Domanda}
     */
    public static Domanda[] getDomande() {
        return Domanda.values();
    }

    /**
     * Popola le mappe {@code opzioniSI} e {@code opzioniNO} per ogni domanda,
     * quindi costruisce l'albero decisionale.
     *
     * @param difficile {@code true} per la modalità difficile, {@code false} per la normale
     */
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

    /**
     * Restituisce il nodo radice dell'albero decisionale del bot.
     *
     * @return la radice dell'albero come {@link Nodo}
     */
    public Nodo getRoot() {
        return albero.getRoot();
    }

    /**
     * Sceglie casualmente il personaggio segreto del bot tra quelli disponibili
     * e lo imposta come {@code personaggioDaScoprire}.
     */
    private void setPersonaggioDaScoprire() {
        personaggioDaScoprire = tutte.get(new Random().nextInt(tutte.size()));
    }

    /**
     * Restituisce il personaggio segreto scelto dal bot.
     *
     * @return il {@link Persona} scelto dal bot come personaggio segreto
     */
    public Persona getPersonaggioDaScoprire() {
        return personaggioDaScoprire;
    }

    /**
     * Restituisce la risposta del personaggio segreto del bot alla domanda specificata.
     *
     * @param d la {@link Domanda} da porre al personaggio segreto del bot
     * @return {@code true} se il personaggio segreto corrisponde alla domanda,
     *         {@code false} altrimenti
     * @throws IllegalStateException se il bot non ha ancora scelto il personaggio segreto
     */
    public boolean rispostaSuPersonaggioDaScoprire(Domanda d) {
        if (personaggioDaScoprire == null) throw new IllegalStateException("Il bot non ha ancora scelto il personaggio");
        return d.corrisponde(personaggioDaScoprire);
    }

    /**
     * Restituisce il testo della domanda associata al nodo corrente dell'albero,
     * oppure {@code null} se il nodo corrente non è un {@link NodoDomanda}.
     *
     * @return il testo della domanda corrente, o {@code null} se il nodo è una foglia
     */
    public String getDomandaCorrente() {
        if (nodoCorrente instanceof NodoDomanda nd) return nd.getDomanda();
        return null;
    }

    /**
     * Avanza nel percorso dell'albero decisionale in base alla risposta fornita,
     * e restituisce la {@link Persona} se si raggiunge un nodo foglia.
     *
     * @param risposta {@code true} per seguire il ramo "sì", {@code false} per il ramo "no"
     * @return la {@link Persona} del nodo foglia raggiunto, o {@code null} se il nodo
     *         successivo è ancora una domanda o se il nodo corrente non è un {@link NodoDomanda}
     */
    public Persona avanzaDentroAlbero(boolean risposta) {
        if (!(nodoCorrente instanceof NodoDomanda)) return null;
        nodoCorrente = risposta ? nodoCorrente.getSi() : nodoCorrente.getNo();
        if (nodoCorrente instanceof NodoPersona np) return np.getPersona();
        return null;
    }

    /**
     * Seleziona la domanda migliore tra quelle disponibili in base al criterio
     * dell'entropia minima, ovvero quella che divide più equamente le persone
     * correnti tra i rami "sì" e "no".
     * <p>
     * Se esiste una domanda che divide perfettamente (entropia 0, entrambi i rami
     * non vuoti), viene restituita immediatamente.
     * </p>
     *
     * @param disponibili     la lista di {@link Domanda} ancora utilizzabili
     * @param personeCorrente la lista di {@link Persona} ancora in gioco nel sottoalbero
     * @return la {@link Domanda} con la minore differenza assoluta tra risposte "sì" e "no"
     */
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

    /**
     * Costruisce l'albero decisionale completo scegliendo la domanda radice
     * in base alla modalità (difficile o normale) e avviando la costruzione
     * ricorsiva dei sottoalberi per entrambi i rami.
     *
     * @param difficile {@code true} per scegliere la domanda radice ottimale,
     *                  {@code false} per sceglierla casualmente
     */
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

    /**
     * Costruisce ricorsivamente un sottoalbero a partire dal nodo padre indicato.
     * <p>
     * Casi base:
     * <ul>
     *   <li>Se {@code personeCorrente} è vuota, non inserisce nulla.</li>
     *   <li>Se {@code personeCorrente} contiene una sola persona, inserisce un
     *       {@link NodoPersona} foglia.</li>
     *   <li>Se non ci sono domande disponibili che dividano le persone correnti,
     *       inserisce tutte le persone rimanenti come foglie dello stesso padre.</li>
     * </ul>
     * </p>
     *
     * @param idPadre             id del nodo padre a cui collegare il sottoalbero
     * @param rispostaPadre       {@code true} per collegarlo come figlio "sì", {@code false} per "no"
     * @param personeCorrente     lista di {@link Persona} ancora distinguibili in questo ramo
     * @param usate               insieme delle {@link Domanda} già utilizzate nel percorso corrente
     * @param categorieConfermate insieme delle {@link Domanda.Categoria} già confermate nel percorso
     * @param difficile           {@code true} per scegliere la domanda ottimale, {@code false} per casuale
     */
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

    /**
     * Restituisce l'intersezione tra due liste di {@link Persona}, confrontando
     * i personaggi per nome.
     *
     * @param a la prima lista di persone
     * @param b la seconda lista di persone
     * @return una nuova lista contenente le persone presenti in entrambe le liste
     *         (confronto per nome)
     */
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