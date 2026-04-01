import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchermataGioco extends JFrame {

    private JPanel pannelloDomanda;
    private JPanel pannelloGriglia;
    private JPanel pannelloBottoni;

    private JButton si;
    private JButton no;

    private final List<Persona> persone;
    private List<JPanel> carte = new ArrayList<>();

    private Nodo Scelta;

    public SchermataGioco(Albero albero){
        setTitle("IndovinaChi");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 7, 5, 5));

        pannelloDomanda = new JPanel();
        pannelloDomanda.add(new JLabel());

        persone = new ArrayList<>();
        Map<Integer, Persona> p = albero.getPersone();

        for (int id : p.keySet()) {
            Persona persona = p.get(id);
            persone.add(persona);
            JPanel panel = new JPanel();
//            p.setBackground(Color.green);
            JLabel l = new JLabel();
            l.setIcon(persona.getImmagine());
            panel.add(l);
            this.add(panel);
            carte.add(new JPanel());
        }

        setVisible(true);
    }
}
