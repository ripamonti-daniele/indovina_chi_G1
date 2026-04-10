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

        //il trattino basso viene utilizzato quando il parametro non è utilizzato
        bottone1.addActionListener(_ -> inizializzaInPersona(persone));

        bottone2.addActionListener(_ -> {
            getContentPane().removeAll();
            inizializzaBot(persone, albero);
            revalidate();
            repaint();
        });

        bottone3.addActionListener(_ -> dispose());

        setVisible(true);
    }

    private void inizializzaInPersona(List<Persona> persone) {
        this.persone = persone;

        Persona personaSegretaG1 = mostraSceltaPersona(1, persone);
        if (personaSegretaG1 == null) {
            return;
        }

        Persona personaSegretaG2 = mostraSceltaPersona(2, persone);
        if (personaSegretaG2 == null) {
            return;
        }

        //array per tenere conto del turno
        final int[] turno = {1};


        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(new Color(158, 26, 14));
        setContentPane(pannelloPrincipale);

        //colonna sinistra
        JPanel colonnaG1 = new JPanel(new BorderLayout());
        colonnaG1.setOpaque(false);
        colonnaG1.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(60, 60, 60)));
        JLabel labelG1 = new JLabel("Giocatore 1", SwingConstants.CENTER);
        labelG1.setForeground(Color.WHITE);
        colonnaG1.add(labelG1, BorderLayout.NORTH);
        colonnaG1.add(creaGriglia(persone, turno, 1), BorderLayout.CENTER);

        //colonna destra
        JPanel colonnaG2 = new JPanel(new BorderLayout());
        colonnaG2.setOpaque(false);
        JLabel labelG2 = new JLabel("Giocatore 2", SwingConstants.CENTER);
        labelG2.setForeground(Color.WHITE);
        colonnaG2.add(labelG2, BorderLayout.NORTH);
        colonnaG2.add(creaGriglia(persone, turno, 2), BorderLayout.CENTER);

        JPanel pannelloCentrale = new JPanel(new GridLayout(1, 2, 0, 0));
        pannelloCentrale.setOpaque(false);
        pannelloCentrale.add(colonnaG1);
        pannelloCentrale.add(colonnaG2);

        //pannello per cambiare turno e risposte
        JPanel pannelloLaterale = new JPanel();
        pannelloLaterale.setLayout(new BoxLayout(pannelloLaterale, BoxLayout.Y_AXIS));
        pannelloLaterale.setBackground(new Color(180, 180, 180));
        pannelloLaterale.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        pannelloLaterale.setPreferredSize(new Dimension(220, 0));

        JLabel labelTurno = new JLabel("Turno: Giocatore 1");
        labelTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTurno.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton btnIndovina = new JButton("Tenta di indovinare!");
        btnIndovina.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIndovina.setBackground(new Color(46, 160, 67));
        btnIndovina.setForeground(Color.WHITE);
        btnIndovina.setFocusPainted(false);
        btnIndovina.setMaximumSize(new Dimension(200, 40));

        JButton btnSaltaTurno = new JButton("Cambia turno");
        btnSaltaTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSaltaTurno.setBackground(new Color(120, 120, 120));
        btnSaltaTurno.setForeground(Color.WHITE);
        btnSaltaTurno.setFocusPainted(false);
        btnSaltaTurno.setMaximumSize(new Dimension(200, 40));

        pannelloLaterale.add(labelTurno);
        pannelloLaterale.add(btnIndovina);
        pannelloLaterale.add(Box.createVerticalStrut(10));
        pannelloLaterale.add(btnSaltaTurno);

        pannelloPrincipale.add(pannelloCentrale, BorderLayout.CENTER);
        pannelloPrincipale.add(pannelloLaterale, BorderLayout.EAST);

        //funzione anonima che viene chiamata quando viene schiacciato il bottone che ci permette di creare
        //una finestra di dialogo per indovinare il personaggio scelto dall'avversario
        btnIndovina.addActionListener(_ -> {
            int turnoCorrente = turno[0];

            String input = JOptionPane.showInputDialog(
                    this, //intende che si riferisce a questo elemento
                    "Giocatore " + turnoCorrente + ", inserisci il nome della persona:",
                    "Tenta di indovinare",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) {
                return;
            }

            //confronto le due persone se quella inserita è uguale a quella da indovinare
            Persona segreta;
            if (turnoCorrente == 1) {
                segreta = personaSegretaG2;
            } else {
                segreta = personaSegretaG1;
            }

            boolean haVinto = segreta.getNome().equals(input.trim().toLowerCase());

            if (haVinto) {
                JOptionPane.showMessageDialog(
                        this,
                        "Giocatore " + turnoCorrente + " ha vinto!\nLa persona era: " + segreta.getNome(),
                        "Hai vinto!",
                        JOptionPane.INFORMATION_MESSAGE
                );
                btnIndovina.setEnabled(false);
                btnSaltaTurno.setEnabled(false);
            } else {
                //se non ha indovinato cambia il turno
                if (turnoCorrente == 1) {
                    turno[0] = 2;
                } else {
                    turno[0] = 1;
                }

                labelTurno.setText("Turno: Giocatore " + turno[0]);

                JOptionPane.showMessageDialog(
                        this,
                        "Risposta sbagliata! Tocca al Giocatore " + turno[0],
                        "Sbagliato!",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        //modifica l'array con il turno dell'avversario
        btnSaltaTurno.addActionListener(_ -> {
            if (turno[0] == 1) {
                turno[0] = 2;
            } else {
                turno[0] = 1;
            }

            labelTurno.setText("Turno: Giocatore " + turno[0]);
        });

        revalidate();
        repaint();
    }

    private Persona mostraSceltaPersona(int numeroGiocatore, List<Persona> persone) {
        //dato che abbiamo classi anonime (addMouseListener), che non possono modificare le variabili locali normali, le bisogna mettere final
        final Persona[] scelta = {null};

        JPanel griglia = new JPanel(new GridLayout(4, 7, 8, 8));
        griglia.setBackground(new Color(158, 26, 14));
        griglia.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Persona persona : persone) {
            JPanel cella = new JPanel(new GridBagLayout());
            cella.setOpaque(false);

            JPanel carta = new JPanel(new BorderLayout());
            carta.setBackground(new Color(210, 210, 210));
            carta.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            carta.setPreferredSize(new Dimension(80, 100));

            JLabel immagine = new JLabel(persona.getImmagine(65, 80));
            immagine.setHorizontalAlignment(SwingConstants.CENTER);
            carta.add(immagine, BorderLayout.CENTER);
            carta.setToolTipText(creaStringaToolTip(persona));

            cella.add(carta);
            griglia.add(cella);

            carta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    for (Component comp : griglia.getComponents()) {
                        JPanel pannelloCella = (JPanel) comp;
                        Component[] figli = pannelloCella.getComponents();
                        if (figli.length > 0 && figli[0] instanceof JPanel) {
                            JPanel cartaInterna = (JPanel) figli[0];
                            cartaInterna.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                            cartaInterna.setBackground(new Color(210, 210, 210));
                        }
                    }

                    carta.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    scelta[0] = persona;
                }
            });
        }

        while (scelta[0] == null) {
            int risultato = JOptionPane.showConfirmDialog(
                    this,
                    griglia,
                    "Giocatore " + numeroGiocatore + " - scegli la tua persona segreta",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (risultato != JOptionPane.OK_OPTION) {
                return null;
            }

            if (scelta[0] == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Devi selezionare una persona prima di confermare.",
                        "Attenzione",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }

        return scelta[0];
    }


    private JPanel creaGriglia(List<Persona> persone, int[] turno, int mioNumero) {
        JPanel griglia = new JPanel(new GridLayout(4, 7, 8, 8));
        griglia.setOpaque(false);
        griglia.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Persona persona : persone) {
            JPanel cella = new JPanel(new GridBagLayout());
            cella.setOpaque(false);

            JPanel carta = new JPanel(new BorderLayout());
            carta.setBackground(new Color(210, 210, 210));
            carta.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            carta.setPreferredSize(new Dimension(80, 100));

            JLabel immagine = new JLabel(persona.getImmagine(65, 80));
            immagine.setHorizontalAlignment(SwingConstants.CENTER);
            carta.add(immagine, BorderLayout.CENTER);

            cella.add(carta);
            griglia.add(cella);

            carta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    // puoi oscurare solo nel tuo turno
                    if (turno[0] != mioNumero) return;

                    carta.setBackground(Color.YELLOW);
                    carta.removeAll();
                    carta.revalidate();
                    carta.repaint();
                }
            });
        }

        return griglia;
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