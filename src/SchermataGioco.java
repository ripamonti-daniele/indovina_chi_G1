import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.Map;

public class SchermataGioco extends JFrame {

    private JPanel pannelloDomanda;
    private JPanel pannelloGriglia;
    private JPanel pannelloBottoni;
    private JTextArea domande;

    private JButton si;
    private JButton no;

    private List<Persona> persone;
    private List<JPanel> carte = new ArrayList<>();
    private final Map<String, JPanel> cartaPerNome = new HashMap<>();

    private Nodo scelta;

    public SchermataGioco(List<Persona> persone, Albero albero) {
        setTitle("IndovinaChi");
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);

        ImageIcon icona = new ImageIcon("img/sfondo_indovina_chi.png");

        JPanel panelSfondo = new JPanel(new BorderLayout()) {
            private final Image image = icona.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int panelWidth = getWidth();
                int panelHeight = getHeight();

                int imgWidth = image.getWidth(null);
                int imgHeight = image.getHeight(null);

                // scala mantenendo proporzioni (cover style)
                double scala = Math.max((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);

                int larghezzaScalata = (int) (imgWidth * scala);
                int altezzaScalata = (int) (imgHeight * scala);

                int x = (panelWidth - larghezzaScalata) / 2;
                int y = (panelHeight - altezzaScalata) / 2;

                g.drawImage(image, x, y, larghezzaScalata, altezzaScalata, this);
            }
        };

        setContentPane(panelSfondo);

        JButton bottone1 = new JButtonCustom("Gioca in persona", new Color(34, 139, 34), new Color(60, 179, 60));
        JButton bottone2 = new JButtonCustom("Gioca contro il computer", new Color(30, 144, 255), new Color(100, 180, 255));
        JButton bottone3 = new JButtonCustom("Esci", new Color(220, 20, 60), new Color(255, 60, 80));

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        btnPanel.setPreferredSize(new Dimension(0, 180));

        btnPanel.add(bottone1);
        btnPanel.add(bottone2);
        btnPanel.add(bottone3);

        panelSfondo.add(btnPanel, BorderLayout.SOUTH);

        bottone1.addActionListener(_ -> inizializzaInPersona());

        bottone2.addActionListener(_ -> {
            getContentPane().removeAll();
            inizializzaBot(persone, albero);
            revalidate();
            repaint();
        });

        bottone3.addActionListener(_ -> dispose());

        setVisible(true);
    }

    private void inizializzaInPersona() {

    }

    private void inizializzaBot(List<Persona> persone, Albero albero) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(158, 26, 14));
        setContentPane(root);  // sostituisci il content pane

        //PARTE DELLE CARTE
        pannelloGriglia = new JPanel(new GridLayout(4, 7, 15, 15));
        pannelloGriglia.setOpaque(false);
        pannelloGriglia.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        this.persone = persone;

        for (Persona persona : persone) {
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
            carta.setToolTipText(creaStringaToolTip(persona));

            cella.add(carta);
            pannelloGriglia.add(cella);
            carte.add(cella);
            cartaPerNome.put(persona.getNome(), carta);

            carta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    carta.setBackground(Color.YELLOW);
                    carta.removeAll();
                    carta.revalidate();
                    carta.repaint();
                    carta.setToolTipText(null);
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

        //questa parte serve per le domande, per chi continua con il bot per cambiare ciò che c'è scritto dentro basta fare domande.setText(...);
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

        //vado a mostrare la prima domanda

        scelta = albero.getRoot();
        aggiornadomanda();

        si.addActionListener(_ -> avanza(true));
        no.addActionListener(_ -> avanza(false));

        pannelloBottoni.add(si);
        pannelloBottoni.add(no);

        pannelloDomanda.add(titoloLabel);
        pannelloDomanda.add(domande);
        pannelloDomanda.add(pannelloBottoni);

        root.add(pannelloGriglia, BorderLayout.CENTER);
        root.add(pannelloDomanda, BorderLayout.EAST);
    }

    //aggiorno il testo all'interno del pannello delle domande
    private void aggiornadomanda(){
        if(scelta != null){
            if(scelta.getDomanda() != null){
                domande.setText(scelta.getDomanda());
            }
        }else{
            throw new IllegalArgumentException("scegli prima il personaggio");
        }
    }

    private void avanza(boolean risposta) {
        if (scelta == null || scelta.getDomanda() == null) return;

        // raccoglie le persone raggiungibili dal ramo SCARTATO
        Nodo ramoScartato;
        if (risposta){
            ramoScartato = scelta.getNo();
        }else{
            ramoScartato = scelta.getSi();
        }
        List<String> daAbbattere = personeRaggiungibili(ramoScartato);

        // abbatte le carte corrispondenti
        for (String nome : daAbbattere) {
            abbattiCarta(nome);
        }

        // avanza al nodo scelto
        if (risposta) scelta = scelta.getSi();
        else scelta = scelta.getNo();

        if (scelta == null) {
            // ramo vuoto nell'albero: non dovrebbe succedere con un albero ben costruito
            domande.setText("Nessun ramo disponibile.");
            si.setEnabled(false);
            no.setEnabled(false);
            return;
        }

        if (scelta.getPersona() != null) {
            // siamo arrivati a una persona: fine partita
            domande.setText("La persona è: " + scelta.getPersona().getNome());
            si.setEnabled(false);
            no.setEnabled(false);
        } else {
            aggiornadomanda();
        }
    }

    private List<String> personeRaggiungibili(Nodo n) {
        List<String> nomi = new ArrayList<>();
        raccogliPersone(n, nomi);
        return nomi;
    }

    private void raccogliPersone(Nodo n, List<String> nomi) {
        if (n == null) {
            return;
        }
        if (n.getPersona() != null) {
            nomi.add(n.getPersona().getNome());
            return;
        }
        raccogliPersone(n.getSi(), nomi);
        raccogliPersone(n.getNo(), nomi);
    }

    private void abbattiCarta(String nome) {
        JPanel carta = cartaPerNome.get(nome);
        if (carta == null) return;
        carta.setBackground(Color.YELLOW);
        carta.removeAll();
        carta.revalidate();
        carta.repaint();
        carta.setToolTipText(null);
    }

    private String creaStringaToolTip(Persona p) {
        String s = p.toString();
        s = s.replaceAll("\n", "<br>");
        return "<html>" + s + "</html>";
    }
}