import java.io.*;
import java.util.List;

public class Serializzatore {
    public static void serializza(List<Albero> a, String percorso) {
        try {
            FileOutputStream file = new FileOutputStream(percorso);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(a);
            output.close();
            file.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Errore nella serializzazione sul file " + percorso);
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
            throw new RuntimeException("Errore: non è stato deSerializzato un Object");
        }
    }
}
