import java.util.*;

public class Bot {
    private final Map<String, List<Persona>> opzioni = new HashMap<>();
    private final List<Persona> tutte;
    private List<Persona> rimaste;
    private final String[] domande;

    public Bot(List<Persona> persone) {
        if (persone == null || persone.isEmpty()) throw new IllegalArgumentException("La lista non può essere null o vuota");
        this.tutte = persone;
        this.rimaste = new ArrayList<>(persone);
        domande = new String[17];
        inizializza();
    }

    private void creaAlbero() {
        List<Integer> indici = new ArrayList<>();
        for (int i = 0; i < domande.length; i++) indici.add(i);
        Collections.shuffle(indici);

        Albero a = new Albero(domande[indici.getFirst()]);

        //da implementare algoritmo per creare l'albero in modoi automatico

//        for (int i : indici) {
//            if (i == indici.getFirst()) continue;
//
//        }
    }

    private void inizializza() {
        domande[0] = "è maschio?";
        domande[1] = "ha i capelli castani?";
        domande[2] = "ha i capelli neri?";
        domande[3] = "ha i capelli biondi?";
        domande[4] = "ha i capelli rossi?";
        domande[5] = "ha i capelli bianchi?";
        domande[6] = "ha la pelle bianca?";
        domande[7] = "ha la pelle nera?";
        domande[8] = "ha la pelle mulatta?";
        domande[9] = "ha gli occhi marroni?";
        domande[10] = "ha gli occhi blu?";
        domande[11] = "ha gli occhi verdi?";
        domande[12] = "ha gli occhiali?";
        domande[13] = "ha i capelli lunghi?";
        domande[14] = "ha la barba?";
        domande[15] = "ha il cappello?";
        domande[16] = "è pelato?";

        for (String domanda : domande) {
            List<Persona> si = new ArrayList<>();
            List<Persona> no = new ArrayList<>();
            for (Persona p : tutte) {
                if (corrisponde(p, domanda)) si.add(p);
                else no.add(p);
            }
            opzioni.put(domanda + "S", si);
            opzioni.put(domanda + "N", no);
        }
    }

    private boolean corrisponde(Persona p, String domanda) {
        return switch (domanda) {
            case "è maschio?" -> p.isSesso();
            case "ha i capelli castani?" -> p.getColoreCapelli() == ColoriCrapa.CASTANO;
            case "ha i capelli neri?" -> p.getColoreCapelli() == ColoriCrapa.NERO;
            case "ha i capelli biondi?" -> p.getColoreCapelli() == ColoriCrapa.BIONDO;
            case "ha i capelli rossi?" -> p.getColoreCapelli() == ColoriCrapa.ROSSO;
            case "ha i capelli bianchi?" -> p.getColoreCapelli() == ColoriCrapa.BIANCO;
            case "ha la pelle bianca?" -> p.getColorePelle() == ColoriPelle.BIANCO;
            case "ha la pelle nera?" -> p.getColorePelle() == ColoriPelle.NERO;
            case "ha la pelle mulatta?" -> p.getColorePelle() == ColoriPelle.MULATTO;
            case "ha gli occhi marroni?" -> p.getColoreOcchi() == ColoriÖch.MARRONE;
            case "ha gli occhi blu?" -> p.getColoreOcchi() == ColoriÖch.BLU;
            case "ha gli occhi verdi?" -> p.getColoreOcchi() == ColoriÖch.VERDE;
            case "ha gli occhiali?" -> p.isOcchiali();
            case "ha i capelli lunghi?" -> p.isCapelliLunghi();
            case "ha la barba?" -> p.isBarba();
            case "ha il cappello?" -> p.isCappello();
            case "è pelato?" -> p.isPelato();
            default -> throw new IllegalArgumentException("Domanda non riconosciuta: " + domanda);
        };
    }
}
