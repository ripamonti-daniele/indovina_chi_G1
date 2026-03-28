import java.util.ArrayList;
import java.util.List;

void main() {
    Persona mario  = new Persona("mario",  35, ColoriCrapa.NERO,    ColoriÖch.MARRONE, ColoriPelle.BIANCO, false, true,  false);
    Persona luca   = new Persona("luca",   28, ColoriCrapa.BIONDO,  ColoriÖch.BLU,     ColoriPelle.BIANCO, true,  true,  true);
    Persona omar   = new Persona("omar",   22, ColoriCrapa.NERO,    ColoriÖch.MARRONE, ColoriPelle.NERO,   false, true,  false);
    Persona gianni = new Persona("gianni", 40, ColoriCrapa.CASTANO, ColoriÖch.VERDE,   ColoriPelle.BIANCO, false, true,  true);
    Persona giulia = new Persona("giulia", 25, ColoriCrapa.BIONDO,  ColoriÖch.BLU,     ColoriPelle.BIANCO, false, false, true);
    Persona anna   = new Persona("anna",   30, ColoriCrapa.CASTANO, ColoriÖch.VERDE,   ColoriPelle.BIANCO, true,  false, false);
    Persona sara   = new Persona("sara",   27, ColoriCrapa.ROSSO,   ColoriÖch.VERDE,   ColoriPelle.BIANCO, false, false, true);
    Persona fatima = new Persona("fatima", 23, ColoriCrapa.NERO,    ColoriÖch.MARRONE, ColoriPelle.NERO,   false, false, false);

    System.out.println("/**************************************************************************\n * *\n * ██████╗            ██████╗           ██████╗                        *\n * ██╔══██╗          ██╔═══██╗         ██╔════╝                        *\n * ██║  ██║          ██║   ██║         ██║                             *\n * ██║  ██║          ██║   ██║         ██║                             *\n * ██████╔╝          ╚██████╔╝         ╚██████╗                        *\n * ╚═════╝            ╚═════╝           ╚═════╝                        *\n * *\n *                                                                     *\n * *\n **************************************************************************/");


    Albero a = new Albero("e' maschio?");

    // ramo maschi
    a.inserisciNodo("e' maschio?",           "cap lunghi m?",      true);
    a.inserisciNodo("cap lunghi m?",         "cap castani m?",     true);
    a.inserisciPersona("cap castani m?",     gianni, true);
    a.inserisciPersona("cap castani m?",     luca,   false);
    a.inserisciNodo("cap lunghi m?",         "pelle nera m?",      false);
    a.inserisciPersona("pelle nera m?",      omar,   true);
    a.inserisciPersona("pelle nera m?",      mario,  false);

    // ramo femmine
    a.inserisciNodo("e' maschio?",           "pelle nera f?",      false);
    a.inserisciPersona("pelle nera f?",      fatima, true);
    a.inserisciNodo("pelle nera f?",         "cap lunghi f?",      false);
    a.inserisciPersona("cap lunghi f?",      anna,   false);
    a.inserisciNodo("cap lunghi f?",         "cap rossi f?",       true);
    a.inserisciPersona("cap rossi f?",       sara,   true);
    a.inserisciPersona("cap rossi f?",       giulia, false);

    System.out.println("STRUTTURA ALBERO");
    System.out.println(a);


    List<Albero> alberi = new ArrayList<>();
    alberi.add(a);

    Serializzatore.serializza(alberi, "alberiBase.ser");

//    List<Albero> alberi = Serializzatore.deSerializza("alberiBase.ser");
//    if (alberi != null && !alberi.isEmpty()) {
//        Albero a = alberi.getFirst();
//        System.out.println(a);
//    }
    //
}
