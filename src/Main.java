void main() {
    List<Persona> persone = new ArrayList<>();
    persone.add(new Persona("patrick", ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, false, true, true, true, false, false, "img/patrick.png"));
    persone.add(new Persona("sarah", ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.NERO, false, false, true, false, false, false,"img/sarah.png"));
    persone.add(new Persona("roger", ColoriCrapa.BIANCO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, false, true, false, false, false, true,"img/roger.png"));
    persone.add(new Persona("nicholas", ColoriCrapa.NERO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, true, true, false, false, false, false,"img/nicholas.png"));
    persone.add(new Persona("norah", ColoriCrapa.NERO, ColoriÖch.MARRONE, ColoriPelle.NERO, false, false, true, false, false, false,"img/norah.png"));
    persone.add(new Persona("lindsey", ColoriCrapa.NERO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, false, false,"img/lindsey.png"));
    persone.add(new Persona("laura", ColoriCrapa.BIONDO, ColoriÖch.VERDE, ColoriPelle.BIANCO, false, false, false, false, false, false,"img/laura.png"));
    persone.add(new Persona("chris", ColoriCrapa.NERO, ColoriÖch.MARRONE, ColoriPelle.NERO, false, true, false, false, false, false,"img/chris.png"));
    persone.add(new Persona("daisy", ColoriCrapa.BIANCO, ColoriÖch.VERDE, ColoriPelle.BIANCO, false, false, false, false, false, false,"img/daisy.png"));
    persone.add(new Persona("matt", ColoriCrapa.BIONDO, ColoriÖch.BLU, ColoriPelle.BIANCO, true, true, false, true, false, false,"img/matt.png"));
    persone.add(new Persona("glenda", ColoriCrapa.BIANCO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, true, false,"img/glenda.png"));
    persone.add(new Persona("timothy", ColoriCrapa.CASTANO, ColoriÖch.BLU, ColoriPelle.BIANCO, false, true, false, false, false, false,"img/timothy.png"));
    persone.add(new Persona("thomas", ColoriCrapa.ROSSO, ColoriÖch.BLU, ColoriPelle.BIANCO, false, true, false, true, false, false,"img/thomas.png"));
    persone.add(new Persona("theodor", ColoriCrapa.NERO, ColoriÖch.VERDE, ColoriPelle.NERO, false, true, false, false, true, false,"img/theodor.png"));
    persone.add(new Persona("marc", ColoriCrapa.BIONDO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, false, true, false, false, false, false,"img/marc.png"));
    persone.add(new Persona("john", ColoriCrapa.BIANCO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, true, true, false, true, false, true,"img/john.png"));
    persone.add(new Persona("doris", ColoriCrapa.NERO, ColoriÖch.BLU, ColoriPelle.NERO, false, false, true, false, false, false,"img/doris.png"));
    persone.add(new Persona("megan", ColoriCrapa.ROSSO, ColoriÖch.VERDE, ColoriPelle.BIANCO, false, false, true, false, false, false,"img/megan.png"));
    persone.add(new Persona("daniel", ColoriCrapa.NERO, ColoriÖch.VERDE, ColoriPelle.NERO, false, true, false, false, false, false,"img/daniel.png"));
    persone.add(new Persona("hajar", ColoriCrapa.NERO, ColoriÖch.BLU, ColoriPelle.MULATTO, false, false, false, false, false, false,"img/hajar.png"));
    persone.add(new Persona("cindy", ColoriCrapa.BIONDO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, true, false,"img/cindy.png"));
    persone.add(new Persona("scarlett", ColoriCrapa.ROSSO, ColoriÖch.BLU, ColoriPelle.BIANCO, true, false, true, false, false, false,"img/scarlett.png"));
    persone.add(new Persona("nathan", ColoriCrapa.BIONDO, ColoriÖch.BLU, ColoriPelle.BIANCO, false, true, false, true, true, false,"img/nathan.png"));
    persone.add(new Persona("carol", ColoriCrapa.NERO, ColoriÖch.VERDE, ColoriPelle.NERO, true, false, true, false, false, false,"img/carol.png"));
    persone.add(new Persona("tina", ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, false, false, true, false, false, false,"img/tina.png"));
    persone.add(new Persona("robert", ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.BIANCO, true, true, false, false, false, false,"img/robert.png"));
    persone.add(new Persona("mallory", ColoriCrapa.CASTANO, ColoriÖch.BLU, ColoriPelle.BIANCO, false, false, true, false, false, false,"img/mallory.png"));
    persone.add(new Persona("zachary", ColoriCrapa.ROSSO, ColoriÖch.VERDE, ColoriPelle.BIANCO, false, true, true, false, false, false,"img/zachary.png"));

    SchermataGioco sg = new SchermataGioco(persone);

//    Albero alb = null;
//    try {
//        List<Albero> alberi = Serializzatore.deSerializza("src/alberiBase.ser");
//        if (alberi != null && !alberi.isEmpty()) alb = alberi.getFirst();
//    }
//    catch (Exception e) {
//        System.out.println(e.getMessage());
//    }

//    SchermataGioco sg;
//    if (albero != null) sg = new SchermataGioco(albero);
//    System.out.println(a);
}
