import java.io.*;
import java.util.List;

public class Serializzatore {
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
        catch (ClassNotFoundException e){
            throw new RuntimeException("Errore: non è stato deSerializzato un Object");
        }
    }
}
