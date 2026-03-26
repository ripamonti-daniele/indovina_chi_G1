void main() {
//    Persona p = new Persona("giuse", 17, ColoriCrapa.BILATO, ColoriÖch.VERDE, ColoriPelle.NERO, false, true, true);
//    System.out.println(p);
//    System.out.println("/**************************************************************************\n * *\n * ██████╗            ██████╗           ██████╗                        *\n * ██╔══██╗          ██╔═══██╗         ██╔════╝                        *\n * ██║  ██║          ██║   ██║         ██║                             *\n * ██║  ██║          ██║   ██║         ██║                             *\n * ██████╔╝          ╚██████╔╝         ╚██████╗                        *\n * ╚═════╝            ╚═════╝           ╚═════╝                        *\n * *\n *                                                                     *\n * *\n **************************************************************************/");


    //non va
    Albero a = new Albero("Prova");

    Map<String[], Boolean> nodi = new HashMap<>();
    nodi.put(new String[]{"Prova", "a"}, true);
    nodi.put(new String[]{"Prova", "b"}, false);
//    nodi.put(new String[]{"a", "c"}, true);
//    nodi.put(new String[]{"c", "d"}, false);

    a.inserisciNodi(nodi);

    System.out.println(a);

}
