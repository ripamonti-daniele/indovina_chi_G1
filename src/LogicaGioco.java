import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LogicaGioco {
    private List<Persona> personeAttive;
    private Persona personaggioSegreto;

    public Persona selezionaPersonaggioSegreto(List<Persona> tutteLePersone) {
        Random random = new Random();
        int indice = random.nextInt(tutteLePersone.size());
        personaggioSegreto = tutteLePersone.get(indice);
        personeAttive = new ArrayList<>(tutteLePersone);
        return personaggioSegreto;
    }
}
