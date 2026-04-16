import java.io.*;
import java.util.List;

public class Serializzatore {
    public static void serializzaAlbero(Albero albero, String percorso) {
        try {
            FileOutputStream file = new FileOutputStream(percorso);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(albero);
            output.close();
            file.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Errore nella serializzazione dell'albero sul file " + percorso);
        }
    }

    public static List<Persona> deSerializzaPersone(String percorso) {
        try {
            FileInputStream file = new FileInputStream(percorso);
            ObjectInputStream input = new ObjectInputStream(file);
            Object o = input.readObject();
            input.close();
            file.close();

            if (o instanceof List) {
                List lista = (List) o;
                if (!lista.isEmpty()) {
                    return (List<Persona>) lista;
                }
            }
            return null;
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Errore nel caricamento delle persone. File non trovato!");
        }
    }


    @SuppressWarnings("unchecked")
    public static List<Albero> deSerializza(String percorso) {
        try {
            FileInputStream file = new FileInputStream(percorso);
            ObjectInputStream input = new ObjectInputStream(file);
            Object o = input.readObject();
            input.close();
            file.close();
            if (o instanceof List<?>) {
                if (((List<?>) o).isEmpty()) return null;
                if (((List<?>) o).stream().allMatch(e -> e instanceof Albero)) return (List<Albero>) o;
            }
            return null;
        }
        catch (IOException e) {
            if (!percorso.equals("alberiBase.ser")) return deSerializza("alberiBase.ser");
            throw new RuntimeException("Errore nella deSerializzazione del file " + percorso);
        }
        catch (ClassNotFoundException e) {
            if (!percorso.equals("alberiBase.ser")) return deSerializza("alberiBase.ser");
            throw new RuntimeException("Errore: non è stato deSerializzato un Object");
        }
    }
}
