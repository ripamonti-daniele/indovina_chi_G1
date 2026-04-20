import java.io.*;
import java.util.List;

/**
 * Classe di utilità per la serializzazione e deserializzazione degli oggetti
 * di gioco su file, tra cui {@link Bot}, liste di {@link Persona} e lo stato
 * completo di una partita.
 * <p>
 * Il percorso predefinito per il salvataggio della partita è
 * {@code files/partita_salvata.ser}.
 * </p>
 * <p>
 * Il formato dell'array {@code Object[]} usato per salvare una partita è:
 * </p>
 * <p><b>Modalità "persona":</b></p>
 * <ul>
 *   <li>[0] {@link String} {@code "persona"}</li>
 *   <li>[1] {@link String} nome persona segreta G1</li>
 *   <li>[2] {@link String} nome persona segreta G2</li>
 *   <li>[3] {@link Integer} turno (1 o 2)</li>
 *   <li>[4] {@code String[]} nomi carte abbattute G1</li>
 *   <li>[5] {@code String[]} nomi carte abbattute G2</li>
 *   <li>[6] {@code String[]} domande fatte G1</li>
 *   <li>[7] {@code String[]} domande fatte G2</li>
 * </ul>
 * <p><b>Modalità "bot":</b></p>
 * <ul>
 *   <li>[0] {@link String} {@code "bot"}</li>
 *   <li>[1] {@link String} nome persona segreta giocatore</li>
 *   <li>[2] {@link String} nome persona segreta bot</li>
 *   <li>[3] {@link Integer} turno (1=giocatore, 2=bot)</li>
 *   <li>[4] {@code String[]} nomi carte abbattute giocatore</li>
 *   <li>[5] {@code String[]} nomi carte abbattute bot</li>
 *   <li>[6] {@code String[]} domande fatte dal giocatore</li>
 *   <li>[7] {@link Nodo} nodo corrente dell'albero del bot</li>
 *   <li>[8] {@link Boolean} difficile</li>
 * </ul>
 */
public class Serializzatore {

    /** Percorso predefinito per il file di salvataggio della partita. */
    private static final String PERCORSO_SALVATAGGIO = "files/partita_salvata.ser";

    /**
     * Serializza un oggetto {@link Bot} sul file indicato dal percorso.
     *
     * @param bot     il {@link Bot} da serializzare
     * @param percorso il percorso del file di destinazione
     * @throws RuntimeException se si verifica un errore di I/O durante la scrittura
     */
    public static void serializzaBot(Bot bot, String percorso) {
        try {
            FileOutputStream file = new FileOutputStream(percorso);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(bot);
            output.close();
            file.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Errore nella serializzazione del bot sul file " + percorso);
        }
    }

    /**
     * Serializza una lista di {@link Persona} sul file indicato dal percorso.
     *
     * @param persone  la lista di persone da serializzare
     * @param percorso il percorso del file di destinazione
     * @throws RuntimeException se si verifica un errore di I/O durante la scrittura
     */
    public static void serializzaPersone(List<Persona> persone, String percorso) {
        try {
            FileOutputStream file = new FileOutputStream(percorso);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(persone);
            output.close();
            file.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Errore nella serializzazione delle persone");
        }
    }

    /**
     * Deserializza una lista di {@link Persona} dal file indicato dal percorso.
     * <p>
     * Se il percorso fornito non è quello predefinito e si verifica un errore,
     * tenta automaticamente la deserializzazione dal percorso predefinito
     * {@code files/persone.ser}.
     * </p>
     *
     * @param percorso il percorso del file da cui deserializzare
     * @return la lista di {@link Persona} deserializzata, o {@code null} se l'oggetto
     *         letto non è una lista non vuota di {@link Persona}
     * @throws RuntimeException se si verifica un errore di I/O o la classe non viene trovata
     *                          durante la deserializzazione dal percorso predefinito
     */
    @SuppressWarnings("unchecked")
    public static List<Persona> deSerializzaPersone(String percorso) {
        try {
            FileInputStream file = new FileInputStream(percorso);
            ObjectInputStream input = new ObjectInputStream(file);
            Object o = input.readObject();
            input.close();
            file.close();
            if (o instanceof List<?>) {
                if (((List<?>) o).isEmpty()) return null;
                if (((List<?>) o).stream().allMatch(e -> e instanceof Persona)) return (List<Persona>) o;
            }
            return null;
        }
        catch (IOException e) {
            String p = "files/persone.ser";
            if (!percorso.equals(p)) return deSerializzaPersone(p);
            throw new RuntimeException("Errore nella deSerializzazione del file " + percorso);
        }
        catch (ClassNotFoundException e) {
            String p = "files/persone.ser";
            if (!percorso.equals(p)) return deSerializzaPersone(p);
            throw new RuntimeException("Errore: non è stato deSerializzato un Object");
        }
    }

    /**
     * Deserializza un oggetto {@link Bot} dal file indicato dal percorso.
     *
     * @param percorso il percorso del file da cui deserializzare
     * @return il {@link Bot} deserializzato, o {@code null} se l'oggetto letto
     *         non è un'istanza di {@link Bot}
     * @throws RuntimeException se si verifica un errore di I/O o la classe non viene trovata
     */
    public static Bot deSerializzaBot(String percorso) {
        try {
            FileInputStream file = new FileInputStream(percorso);
            ObjectInputStream input = new ObjectInputStream(file);
            Object o = input.readObject();
            input.close();
            file.close();
            if (o instanceof Bot) return (Bot) o;
            return null;
        }
        catch (IOException e) {
            throw new RuntimeException("Errore nella deSerializzazione del file " + percorso);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Errore: non è stato deSerializzato un Object");
        }
    }

    /**
     * Serializza lo stato completo di una partita sul file predefinito
     * {@code files/partita_salvata.ser}.
     *
     * @param stato l'array {@code Object[]} contenente lo stato della partita
     *              nel formato descritto nella documentazione della classe
     * @throws RuntimeException se si verifica un errore di I/O durante la scrittura
     */
    public static void serializzaPartita(Object[] stato) {
        try {
            FileOutputStream file = new FileOutputStream(PERCORSO_SALVATAGGIO);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(stato);
            output.close();
            file.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Errore nel salvataggio della partita: " + e.getMessage());
        }
    }

    /**
     * Deserializza lo stato completo di una partita dal file predefinito
     * {@code files/partita_salvata.ser}.
     *
     * @return l'array {@code Object[]} contenente lo stato della partita
     *         nel formato descritto nella documentazione della classe,
     *         o {@code null} se l'oggetto letto non è un {@code Object[]}
     * @throws RuntimeException se non viene trovato nessun file di salvataggio
     *                          o se il file non è valido
     */
    public static Object[] deSerializzaPartita() {
        try {
            FileInputStream file = new FileInputStream(PERCORSO_SALVATAGGIO);
            ObjectInputStream input = new ObjectInputStream(file);
            Object o = input.readObject();
            input.close();
            file.close();
            if (o instanceof Object[]) return (Object[]) o;
            return null;
        }
        catch (IOException e) {
            throw new RuntimeException("Nessuna partita salvata trovata.");
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("File di salvataggio non valido.");
        }
    }
}