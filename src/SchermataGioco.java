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

    private Nodo scelta;

    public SchermataGioco(Albero albero) {
        setTitle("IndovinaChi");
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(158, 26, 14));
        setLayout(new GridLayout(4, 7, 15, 15));

        persone = new ArrayList<>();
        Map<Integer, Persona> p = albero.getPersone();

        for (int id : p.keySet()) {
            Persona persona = p.get(id);
            persone.add(persona);

            // pannello esterno trasparente → mostra il rosso tra le carte
            JPanel cella = new JPanel(new GridBagLayout());
            cella.setOpaque(false);

            // pannello carta grigio con bordo
            JPanel carta = new JPanel(new BorderLayout(0, 4));
            carta.setBackground(new Color(210, 210, 210));
            carta.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            carta.setPreferredSize(new Dimension(110, 140));

            JLabel immagine = new JLabel();
            immagine.setIcon(persona.getImmagine(90, 110));
            immagine.setHorizontalAlignment(SwingConstants.CENTER);
            carta.add(immagine, BorderLayout.CENTER);

            cella.add(carta);
            this.add(cella);
            carte.add(cella);

            carta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    carta.setBackground(Color.YELLOW);
                    carta.removeAll();
                    carta.revalidate();
                    carta.repaint();
                }
            });
        }

        setVisible(true);
    }
}