void main() {
    Persona p = new Persona("giuse", 17, ColoriCrapa.BILATO, ColoriÖch.VERDE, ColoriPelle.NERO, false, true, true);
//    System.out.println(p);
//    System.out.println("/**************************************************************************\n * *\n * ██████╗            ██████╗           ██████╗                        *\n * ██╔══██╗          ██╔═══██╗         ██╔════╝                        *\n * ██║  ██║          ██║   ██║         ██║                             *\n * ██║  ██║          ██║   ██║         ██║                             *\n * ██████╔╝          ╚██████╔╝         ╚██████╗                        *\n * ╚═════╝            ╚═════╝           ╚═════╝                        *\n * *\n *                                                                     *\n * *\n **************************************************************************/");


//    Albero a = new Albero("Prova");
//
//    List<InfoNodo> nodi = new ArrayList<>();
//    nodi.add(new InfoNodo("prova", "a", true));
//    nodi.add(new InfoNodo("a", "b", true));
//    nodi.add(new InfoNodo("b", "c", true));
//    nodi.add(new InfoNodo("prova", "d", false));
//    nodi.add(new InfoNodo("d", "f", false));
//    nodi.add(new InfoNodo("d", "e", true));
//
//    a.inserisciNodi(nodi);
//    a.inserisciPersona("b", p, false);
//
//    System.out.println(a);
//
//    List<Albero> alberi = new ArrayList<>();
//    alberi.add(a);
//
//    Serializzatore.serializza(alberi, "alberiBase.ser");

    List<Albero> alberi = Serializzatore.deSerializza("alberiBase.ser");
    if (alberi != null && !alberi.isEmpty()) {
        Albero a = alberi.getFirst();
        System.out.println(a);
    }
}
