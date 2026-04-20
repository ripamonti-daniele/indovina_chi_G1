import java.io.*;
import java.util.List;

public class Serializzatore {
    private static final String PERCORSO_SALVATAGGIO = "files/partita_salvata.ser";

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

    // ---------------------------------------------------------------
    // Salvataggio partita — Object[] senza classi nuove
    //
    // Modalita' "persona":
    //   [0] String  "persona"
    //   [1] String  nome persona segreta G1
    //   [2] String  nome persona segreta G2
    //   [3] Integer turno (1 o 2)
    //   [4] String[] nomi carte abbattute G1
    //   [5] String[] nomi carte abbattute G2
    //   [6] String[] domande fatte G1
    //   [7] String[] domande fatte G2
    //
    // Modalita' "bot":
    //   [0] String  "bot"
    //   [1] String  nome persona segreta giocatore
    //   [2] String  nome persona segreta bot
    //   [3] Integer turno (1=giocatore, 2=bot)
    //   [4] String[] nomi carte abbattute giocatore
    //   [5] String[] nomi carte abbattute bot
    //   [6] String[] domande fatte dal giocatore
    //   [7] Nodo    nodo corrente dell'albero del bot
    //   [8] Boolean difficile
    // ---------------------------------------------------------------

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
