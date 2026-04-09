import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DueGiocatori {
    private List<Persona> personeGiocatore1;
    private List<Persona> personeGiocatore2;

    private Persona personaSegretaG1; // la persona che deve indovinare il giocatore 2
    private Persona personaSegretaG2; // la persona che deve indovinare il giocatore 1

    private Bot botG1;
    private Bot botG2;

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

    private void inizializza(List<Persona> persone) {
        // creo le liste per i due giocatori (copie separate)
        personeGiocatore1 = new ArrayList<>();
        personeGiocatore2 = new ArrayList<>();

        for (Persona p : persone) {
            personeGiocatore1.add(new Persona(p));
            personeGiocatore2.add(new Persona(p));
        }

        // scelgo le persone segrete random
        Random rand = new Random();
        personaSegretaG1 = persone.get(rand.nextInt(persone.size()));
        personaSegretaG2 = persone.get(rand.nextInt(persone.size()));

        // creo i bot per aiutare con le domande
        botG1 = new Bot(persone);
        botG2 = new Bot(persone);
    }

    public boolean tentaIndovinare(String nomePersona, int giocatore) {
        if (giocatore == 1) return personaSegretaG2.getNome().equals(nomePersona.trim().toLowerCase());
        if (giocatore == 2) return personaSegretaG1.getNome().equals(nomePersona.trim().toLowerCase());
        throw new IllegalArgumentException("Giocatore non valido: " + giocatore);
    }

    public Persona rivelaSegreto(int giocatore) {
        if (giocatore == 1) return personaSegretaG2;
        if (giocatore == 2) return personaSegretaG1;
        throw new IllegalArgumentException("Giocatore non valido: " + giocatore);
    }

    public Persona getPersonaSegretaG1() {
        return personaSegretaG1;
    }

    public Persona getPersonaSegretaG2() {
        return personaSegretaG2;
    }
}

