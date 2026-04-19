//TODO
// 1: grafica migliore
// 2: fare in modo che se la partita termina il giocatore può decidere se giocare ancora, uscire o tornare al menu principale
// 3: riorganizzazione classe SchermataGioco
// 4: documentazione

List<Persona> creaPersone() {
    List<Persona> persone = new ArrayList<>();
    persone.add(new Persona("patrick", ColoriCrapa.CASTANO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, true, true, true, false, false, "img/patrick.png"));
    persone.add(new Persona("sarah", ColoriCrapa.CASTANO, ColoriOch.MARRONE, ColoriPelle.NERO, false, false, true, false, false, false,"img/sarah.png"));
    persone.add(new Persona("roger", ColoriCrapa.BIANCO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, true, false, false, false, true,"img/roger.png"));
    persone.add(new Persona("nicholas", ColoriCrapa.NERO, ColoriOch.MARRONE, ColoriPelle.BIANCO, true, true, false, false, false, false,"img/nicholas.png"));
    persone.add(new Persona("norah", ColoriCrapa.NERO, ColoriOch.MARRONE, ColoriPelle.NERO, false, false, true, false, false, false,"img/norah.png"));
    persone.add(new Persona("lindsey", ColoriCrapa.NERO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, false, false,"img/lindsey.png"));
    persone.add(new Persona("laura", ColoriCrapa.BIONDO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, false, false, false, false, false,"img/laura.png"));
    persone.add(new Persona("chris", ColoriCrapa.NERO, ColoriOch.MARRONE, ColoriPelle.NERO, false, true, false, false, false, false,"img/chris.png"));
    persone.add(new Persona("daisy", ColoriCrapa.BIANCO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, false, false, false, false, false,"img/daisy.png"));
    persone.add(new Persona("matt", ColoriCrapa.BIONDO, ColoriOch.BLU, ColoriPelle.BIANCO, true, true, false, true, false, false,"img/matt.png"));
    persone.add(new Persona("glenda", ColoriCrapa.BIANCO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, true, false,"img/glenda.png"));
    persone.add(new Persona("timothy", ColoriCrapa.CASTANO, ColoriOch.BLU, ColoriPelle.BIANCO, false, true, false, false, false, false,"img/timothy.png"));
    persone.add(new Persona("thomas", ColoriCrapa.ROSSO, ColoriOch.BLU, ColoriPelle.BIANCO, false, true, false, true, false, false,"img/thomas.png"));
    persone.add(new Persona("theodor", ColoriCrapa.NERO, ColoriOch.VERDE, ColoriPelle.NERO, false, true, false, false, true, false,"img/theodor.png"));
    persone.add(new Persona("marc", ColoriCrapa.BIONDO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, true, false, false, false, false,"img/marc.png"));
    persone.add(new Persona("john", ColoriCrapa.BIANCO, ColoriOch.MARRONE, ColoriPelle.BIANCO, true, true, false, true, false, true,"img/john.png"));
    persone.add(new Persona("doris", ColoriCrapa.NERO, ColoriOch.BLU, ColoriPelle.NERO, false, false, true, false, false, false,"img/doris.png"));
    persone.add(new Persona("megan", ColoriCrapa.ROSSO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, false, true, false, false, false,"img/megan.png"));
    persone.add(new Persona("daniel", ColoriCrapa.NERO, ColoriOch.VERDE, ColoriPelle.NERO, false, true, false, false, false, false,"img/daniel.png"));
    persone.add(new Persona("hajar", ColoriCrapa.NERO, ColoriOch.BLU, ColoriPelle.MULATTO, false, false, false, false, false, false,"img/hajar.png"));
    persone.add(new Persona("cindy", ColoriCrapa.BIONDO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, true, false,"img/cindy.png"));
    persone.add(new Persona("scarlett", ColoriCrapa.ROSSO, ColoriOch.BLU, ColoriPelle.BIANCO, true, false, true, false, false, false,"img/scarlett.png"));
    persone.add(new Persona("nathan", ColoriCrapa.BIONDO, ColoriOch.BLU, ColoriPelle.BIANCO, false, true, false, true, true, false,"img/nathan.png"));
    persone.add(new Persona("carol", ColoriCrapa.NERO, ColoriOch.VERDE, ColoriPelle.NERO, true, false, true, false, false, false,"img/carol.png"));
    persone.add(new Persona("tina", ColoriCrapa.CASTANO, ColoriOch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, false, false,"img/tina.png"));
    persone.add(new Persona("robert", ColoriCrapa.CASTANO, ColoriOch.MARRONE, ColoriPelle.BIANCO, true, true, false, false, false, false,"img/robert.png"));
    persone.add(new Persona("mallory", ColoriCrapa.CASTANO, ColoriOch.BLU, ColoriPelle.BIANCO, false, false, true, false, false, false,"img/mallory.png"));
    persone.add(new Persona("zachary", ColoriCrapa.ROSSO, ColoriOch.VERDE, ColoriPelle.BIANCO, false, true, true, false, false, false,"img/zachary.png"));
    return persone;
}

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

    SchermataGioco sg = new SchermataGioco(persone, Bot.getDomande());
}
