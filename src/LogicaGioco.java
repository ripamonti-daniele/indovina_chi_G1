import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LogicaGioco {
    private List<Persona> personeAttive;
    private Persona personaggioSegreto;

    //Seleziona casualmente il personaggio da indovinare
    public Persona selezionaPersonaggioSegreto(List<Persona> tutteLePersone) {
        Random random = new Random();
        int indice = random.nextInt(tutteLePersone.size());
        personaggioSegreto = tutteLePersone.get(indice);
        personeAttive = new ArrayList<>(tutteLePersone);
        return personaggioSegreto;
    }

    //Elimina le carte in base alla risposta a una domanda
    public int eliminaPersone(String tipoDomanda, boolean risposta) {
        int iniziali = personeAttive.size();
        List<Persona> nuoveAttive = new ArrayList<>();

        for (Persona p : personeAttive) {
            boolean caratteristica = verificaCaratteristica(p, tipoDomanda);
            if (caratteristica == risposta) {
                nuoveAttive.add(p);
            }
        }

        personeAttive = nuoveAttive;
        return iniziali - personeAttive.size();
    }

    //Verifica se il tentativo finale è corretto
    public boolean verificaTentativo(String nomePersona) {
        return personaggioSegreto.getNome().equalsIgnoreCase(nomePersona);
    }

    //elimina le carte che non corrispondono alla risposta data.

    //da adattare ai nuovi attributi di persona
    private boolean verificaCaratteristica(Persona p, String domanda) {
        boolean risultato = false;
        switch (domanda.toLowerCase()) {
            case "maschio":
                risultato = p.isSesso();
                break;
            case "occhiali":
                risultato = p.isOcchiali();
                break;
            case "capelli_lunghi":
                risultato = p.isCapelliLunghi();
                break;
            case "capelli_biondi":
                risultato = p.getColoreCapelli() == ColoriCrapa.BIONDO;
                break;
            case "capelli_castani":
                risultato = p.getColoreCapelli() == ColoriCrapa.CASTANO;
                break;
            case "capelli_neri":
                risultato = p.getColoreCapelli() == ColoriCrapa.NERO;
                break;
            case "capelli_rossi":
                risultato = p.getColoreCapelli() == ColoriCrapa.ROSSO;
                break;
//            case "capelli_bilati":
//                risultato = p.getColoreCapelli() == ColoriCrapa.BILATO;
//                break;
            case "occhi_blu":
                risultato = p.getColoreOcchi() == ColoriÖch.BLU;
                break;
            case "occhi_verdi":
                risultato = p.getColoreOcchi() == ColoriÖch.VERDE;
                break;
            case "occhi_marroni":
                risultato = p.getColoreOcchi() == ColoriÖch.MARRONE;
                break;
            case "pelle_bianca":
                risultato = p.getColorePelle() == ColoriPelle.BIANCO;
                break;
            case "pelle_mulatta":
                risultato = p.getColorePelle() == ColoriPelle.MULATTO;
                break;
            case "pelle_nera":
                risultato = p.getColorePelle() == ColoriPelle.NERO;
                break;
        }
        return risultato;
    }
}