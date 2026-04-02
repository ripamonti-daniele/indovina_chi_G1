import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchermataGioco extends JFrame {

    private JPanel pannelloDomanda;
    private JPanel pannelloGriglia;
    private JPanel pannelloBottoni;
    private JTextArea domande;

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
        setLayout(new BorderLayout());

        //PARTE DELLE CARTE
        pannelloGriglia = new JPanel(new GridLayout(4, 7, 15, 15));
        pannelloGriglia.setOpaque(false);
        pannelloGriglia.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        persone = new ArrayList<>();
        Map<Integer, Persona> p = albero.getPersone();

        for (int id : p.keySet()) {
            Persona persona = p.get(id);
            persone.add(persona);

            //mostra il rosso tra le carte
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
            pannelloGriglia.add(cella);
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

        //PARTE DOMANDE E RISPOSTE
        pannelloDomanda = new JPanel();
        pannelloDomanda.setLayout(new BoxLayout(pannelloDomanda, BoxLayout.Y_AXIS));
        pannelloDomanda.setBackground(new Color(180, 180, 180));
        pannelloDomanda.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        pannelloDomanda.setPreferredSize(new Dimension(220, 0));

        JLabel titoloLabel = new JLabel("Domanda");
        titoloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titoloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        //questa parte serve per le domande, per chi continua con il bot per cambiare ciò che c'è scritto dentro basta basta fare domande.setText(...);
        domande = new JTextArea("");
        domande.setLineWrap(true); //va a capo automaticamente
        domande.setWrapStyleWord(true); //va a capo tutta la parola
        domande.setEditable(false); //il testo non è modificabile
        domande.setFocusable(false);//il testo non è selezionabile
        domande.setOpaque(false); //testo trasparente dietro
        domande.setAlignmentX(Component.CENTER_ALIGNMENT);
        domande.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); //spazia tra domande e risposte

        pannelloBottoni = new JPanel(new GridLayout(1, 2, 12, 0));
        pannelloBottoni.setOpaque(false);
        pannelloBottoni.setMaximumSize(new Dimension(190, 50));

        si = new JButton("Sì");
        si.setBackground(new Color(46, 160, 67));
        si.setForeground(Color.WHITE); //mette il testo bianco
        si.setFocusPainted(false); //toglie il quadratino intorno al button

        no = new JButton("No");
        no.setBackground(new Color(200, 40, 30));
        no.setForeground(Color.WHITE);
        no.setFocusPainted(false);


        pannelloBottoni.add(si);
        pannelloBottoni.add(no);

        pannelloDomanda.add(titoloLabel);
        pannelloDomanda.add(domande);
        pannelloDomanda.add(pannelloBottoni);

        add(pannelloGriglia, BorderLayout.CENTER);
        add(pannelloDomanda, BorderLayout.EAST);

        setVisible(true);
    }
}