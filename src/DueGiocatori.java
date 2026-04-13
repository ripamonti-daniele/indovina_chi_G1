import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DueGiocatori {
    private List<Persona> personeGiocatore1;
    private List<Persona> personeGiocatore2;

    private Persona personaSegretaG1; // la persona che deve indovinare il giocatore 2
    private Persona personaSegretaG2; // la persona che deve indovinare il giocatore 1

    public DueGiocatori(List<Persona> persone, Persona segretaG1, Persona segretaG2) {
        inizializza(persone, segretaG1, segretaG2);
    }

    private void inizializza(List<Persona> persone, Persona segretaG1, Persona segretaG2) {
        // creo le liste per i due giocatori (copie separate)
        personeGiocatore1 = new ArrayList<>();
        personeGiocatore2 = new ArrayList<>();

        for (Persona p : persone) {
            personeGiocatore1.add(new Persona(p));
            personeGiocatore2.add(new Persona(p));
        }

        // imposto le persone segrete scelte dai giocatori
        this.personaSegretaG1 = segretaG1;
        this.personaSegretaG2 = segretaG2;
    }

    public List<Persona> getPersoneGiocatore1() {
        return new ArrayList<>(personeGiocatore1);
    }

    public List<Persona> getPersoneGiocatore2() {
        return new ArrayList<>(personeGiocatore2);
    }

    public Persona getPersonaSegretaG1() {
        return personaSegretaG1;
    }

    public Persona getPersonaSegretaG2() {
        return personaSegretaG2;
    }

    public void eliminaPersona(int giocatore, String nomePersona) {
        List<Persona> persone;

        if (giocatore == 1) {
            persone = personeGiocatore1;
        } else if (giocatore == 2) {
            persone = personeGiocatore2;
        } else {
            throw new IllegalArgumentException("Giocatore non valido: " + giocatore);
        }

        nomePersona = nomePersona.trim().toLowerCase();

        Persona daRimuovere = null;
        for (Persona p : persone) {
            if (p.getNome().equals(nomePersona)) {
                daRimuovere = p;
            }
        }

        if (daRimuovere != null) {
            persone.remove(daRimuovere);
        }
    }

}


