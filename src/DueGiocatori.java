import java.util.ArrayList;
import java.util.List;

public class DueGiocatori {
    private List<Persona> personeGiocatore1;
    private List<Persona> personeGiocatore2;

    private Persona personaSegretaG1; // la persona che deve indovinare il giocatore 2
    private Persona personaSegretaG2; // la persona che deve indovinare il giocatore 1

    private final List<String> domandePossibili;

    public DueGiocatori(List<Persona> persone) {
        domandePossibili = new ArrayList<>();
        inizializzaDomande();
        inizializza(persone);
    }



}
