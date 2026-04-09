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

    private void inizializzaDomande() {
        domandePossibili.add("è maschio?");
        domandePossibili.add("ha i capelli castani?");
        domandePossibili.add("ha i capelli neri?");
        domandePossibili.add("ha i capelli biondi?");
        domandePossibili.add("ha i capelli rossi?");
        domandePossibili.add("ha i capelli bianchi?");
        domandePossibili.add("ha la pelle bianca?");
        domandePossibili.add("ha la pelle nera?");
        domandePossibili.add("ha la pelle mulatta?");
        domandePossibili.add("ha gli occhi marroni?");
        domandePossibili.add("ha gli occhi blu?");
        domandePossibili.add("ha gli occhi verdi?");
        domandePossibili.add("ha gli occhiali?");
        domandePossibili.add("ha i capelli lunghi?");
        domandePossibili.add("ha la barba o i baffi?");
        domandePossibili.add("ha il cappello?");
        domandePossibili.add("è pelato?");
    }

}
