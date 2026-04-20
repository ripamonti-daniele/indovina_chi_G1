/**
 * Crea e restituisce la lista completa delle {@link Persona} predefinite del gioco,
 * ognuna con le proprie caratteristiche fisiche.
 *
 * @return la lista di tutte le {@link Persona} disponibili nel gioco
 */
List<Persona> creaPersone() {
    List<Persona> persone = new ArrayList<>();
    persone.add(new Persona("patrick", ColoriCrapa.CASTANO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, true, true, true, false, false));
    persone.add(new Persona("sarah", ColoriCrapa.CASTANO, ColoriOch.MARRONE, ColoriPelle.NERO, false, false, true, false, false, false));
    persone.add(new Persona("roger", ColoriCrapa.BIANCO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, true, false, false, false, true));
    persone.add(new Persona("nicholas", ColoriCrapa.NERO, ColoriOch.MARRONE, ColoriPelle.BIANCO, true, true, false, false, false, false));
    persone.add(new Persona("norah", ColoriCrapa.NERO, ColoriOch.MARRONE, ColoriPelle.NERO, false, false, true, false, false, false));
    persone.add(new Persona("lindsey", ColoriCrapa.NERO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, false, false));
    persone.add(new Persona("laura", ColoriCrapa.BIONDO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, false, false, false, false, false));
    persone.add(new Persona("chris", ColoriCrapa.NERO, ColoriOch.MARRONE, ColoriPelle.NERO, false, true, false, false, false, false));
    persone.add(new Persona("daisy", ColoriCrapa.BIANCO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, false, false, false, false, false));
    persone.add(new Persona("matt", ColoriCrapa.BIONDO, ColoriOch.BLU, ColoriPelle.BIANCO, true, true, false, true, false, false));
    persone.add(new Persona("glenda", ColoriCrapa.BIANCO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, true, false));
    persone.add(new Persona("timothy", ColoriCrapa.CASTANO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, true, false, false, false, false));
    persone.add(new Persona("thomas", ColoriCrapa.ROSSO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, true, false, true, false, false));
    persone.add(new Persona("theodor", ColoriCrapa.NERO, ColoriOch.VERDE, ColoriPelle.NERO, false, true, false, false, true, false));
    persone.add(new Persona("marc", ColoriCrapa.BIONDO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, true, false, false, false, false));
    persone.add(new Persona("john", ColoriCrapa.BIANCO, ColoriOch.MARRONE, ColoriPelle.BIANCO, true, true, false, true, false, true));
    persone.add(new Persona("doris", ColoriCrapa.NERO, ColoriOch.BLU, ColoriPelle.NERO, false, false, true, false, false, false));
    persone.add(new Persona("megan", ColoriCrapa.ROSSO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, false, true, false, false, false));
    persone.add(new Persona("daniel", ColoriCrapa.NERO, ColoriOch.BLU, ColoriPelle.NERO, false, true, false, false, false, false));
    persone.add(new Persona("hajar", ColoriCrapa.NERO, ColoriOch.BLU, ColoriPelle.MULATTO, false, false, false, false, false, false));
    persone.add(new Persona("cindy", ColoriCrapa.BIONDO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, true, false));
    persone.add(new Persona("scarlett", ColoriCrapa.ROSSO, ColoriOch.BLU, ColoriPelle.BIANCO, true, false, true, false, false, false));
    persone.add(new Persona("nathan", ColoriCrapa.BIONDO, ColoriOch.BLU, ColoriPelle.BIANCO, false, true, false, true, true, false));
    persone.add(new Persona("carol", ColoriCrapa.NERO, ColoriOch.VERDE, ColoriPelle.NERO, true, false, true, false, false, false));
    persone.add(new Persona("tina", ColoriCrapa.CASTANO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, false, true, false, false, false));
    persone.add(new Persona("robert", ColoriCrapa.CASTANO, ColoriOch.MARRONE, ColoriPelle.BIANCO, true, true, false, false, false, false));
    persone.add(new Persona("mallory", ColoriCrapa.CASTANO, ColoriOch.BLU, ColoriPelle.BIANCO, false, false, true, false, false, false));
    persone.add(new Persona("zachary", ColoriCrapa.ROSSO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, true, true, false, false, false));
    return persone;
}

/**
 * Punto di ingresso dell'applicazione.
 * <p>
 * Imposta la proprietà di sistema {@code sun.java2d.uiScale} a {@code "1"} per
 * mantenere le immagini ad alta qualità su tutti i monitor, quindi tenta di
 * caricare la lista delle persone dal file serializzato {@code files/persone.ser}.
 * In caso di errore, ricrea la lista tramite {@link #creaPersone()}.
 * Infine, avvia la {@link SchermataGioco} passando le persone e le domande disponibili.
 * </p>
 */
void main() {
    System.setProperty("sun.java2d.uiScale", "1"); //mantiene le immagini a qualità alta su tutti i monitor

    List<Persona> persone;

    try {
        persone = Serializzatore.deSerializzaPersone("files/persone.ser");
    }
    catch (RuntimeException e) {
        System.out.println(e.getMessage());
        persone = creaPersone();
    }

    try {
        SchermataGioco sg = new SchermataGioco(persone, Bot.getDomande());
    }
    catch (Exception e) {
        System.out.println("C'è stato un errore nella creazione della finestra:");
        System.out.println(e.getMessage());
    }
}