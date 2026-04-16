import javax.swing.*;
import javax.swing.Timer;
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

    private String[] domandePossibili;

    private Nodo scelta;

    public SchermataGioco(List<Persona> persone, Albero albero, String[] domandePossibili) {
        if (domandePossibili == null || domandePossibili.length == 0) throw new IllegalArgumentException("Le domande non possono essere null o vuote");
        this.domandePossibili = domandePossibili;
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

        bottone2.addActionListener(_ -> inizializzaBot(persone, albero));

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

        //Creo i 2 dizionari perche poi cosi posso modificare l'immagine della persona (abbassando la casella)
        Map<String, JPanel> cartaPerNomeG1 = new HashMap<>();
        Map<String, JPanel> cartaPerNomeG2 = new HashMap<>();


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
        colonnaG1.add(creaGriglia(persone, turno, 1, cartaPerNomeG1), BorderLayout.CENTER);


        //colonna destra
        JPanel colonnaG2 = new JPanel(new BorderLayout());
        colonnaG2.setOpaque(false);
        JLabel labelG2 = new JLabel("Giocatore 2", SwingConstants.CENTER);
        labelG2.setForeground(Color.WHITE);
        colonnaG2.add(labelG2, BorderLayout.NORTH);
        colonnaG2.add(creaGriglia(persone, turno, 2, cartaPerNomeG2), BorderLayout.CENTER);

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

        JComboBox<String> comboDomande = new JComboBox<>(domandePossibili);
        comboDomande.setMaximumSize(new Dimension(200, 28));
        comboDomande.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFaiDomanda = new JButton("Fai domanda");
        btnFaiDomanda.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFaiDomanda.setBackground(new Color(30, 100, 200));
        btnFaiDomanda.setForeground(Color.WHITE);
        btnFaiDomanda.setFocusPainted(false);
        btnFaiDomanda.setMaximumSize(new Dimension(200, 40));

        JButton btnIndovina = new JButton("Tenta di indovinare!");
        btnIndovina.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIndovina.setBackground(new Color(46, 160, 67));
        btnIndovina.setForeground(Color.WHITE);
        btnIndovina.setFocusPainted(false);
        btnIndovina.setMaximumSize(new Dimension(200, 40));

        pannelloLaterale.add(labelTurno);
        pannelloLaterale.add(btnIndovina);
        pannelloLaterale.add(Box.createVerticalStrut(10));
        pannelloLaterale.add(comboDomande);
        pannelloLaterale.add(Box.createVerticalStrut(8));
        pannelloLaterale.add(btnFaiDomanda);
        pannelloLaterale.add(Box.createVerticalStrut(10));

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

            String nomeInput = input.trim().toLowerCase();

            boolean esiste = false;
            for (Persona p : persone) {
                if (p.getNome().equals(nomeInput)) { esiste = true; break; }
            }
            if (!esiste) {
                JOptionPane.showMessageDialog(
                        this,
                        "\"" + nomeInput + "\" non è un personaggio valido.\nControlla l'ortografia e riprova.",
                        "Nome non valido",
                        JOptionPane.WARNING_MESSAGE
                );
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
                JOptionPane.showMessageDialog(this, "Giocatore " + turnoCorrente + " ha vinto!\nLa persona era: " + segreta.getNome(), "Hai vinto!", JOptionPane.INFORMATION_MESSAGE);
                btnIndovina.setEnabled(false);
            } else {
                //se non ha indovinato cambia il turno
                if (turnoCorrente == 1) {
                    turno[0] = 2;
                } else {
                    turno[0] = 1;
                }

                labelTurno.setText("Turno: Giocatore " + turno[0]);

                JOptionPane.showMessageDialog(this, "Risposta sbagliata! Tocca al Giocatore " + turno[0], "Sbagliato!", JOptionPane.WARNING_MESSAGE);
            }
        });

        revalidate();
        btnFaiDomanda.addActionListener(_ -> {
            // prende la domanda selezionata dalla combobox
            String domanda = (String) comboDomande.getSelectedItem();
            if (domanda == null) return;

            // calcola chi deve rispondere (l'avversario)
            int turnoCorrente = turno[0];
            int avversario = (turnoCorrente == 1) ? 2 : 1;

            // calcola la risposta corretta in base alla persona segreta dell'avversario
            Persona segretaAvversario = (avversario == 1) ? personaSegretaG1 : personaSegretaG2;
            boolean rispostaCorretta = rispondeDomanda(segretaAvversario, domanda);

            Boolean rispostaUtente = null;

            while (rispostaUtente == null || rispostaUtente != rispostaCorretta) {

                int scelta = JOptionPane.showOptionDialog(
                        this,
                        "Giocatore " + avversario + ", rispondi alla domanda:\n\"" + domanda + "\"",
                        "Rispondi",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Sì", "No"},
                        "Sì"
                );

                if (scelta == JOptionPane.CLOSED_OPTION) return;

                rispostaUtente = (scelta == 0); // 0 è Si, 1 è No

                if (rispostaUtente != rispostaCorretta) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Risposta sbagliata! Devi rispondere correttamente.",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            // sceglie la mappa del giocatore che ha fatto la domanda
            // è lui che deve abbattere le carte, non l'avversario
            Map<String, JPanel> cartaDaAggiornare = (turnoCorrente == 1) ? cartaPerNomeG1 : cartaPerNomeG2;

            // scorre tutte le persone e abbatte quelle che non corrispondono alla risposta
            for (Persona p : persone) {
                if (rispondeDomanda(p, domanda) != rispostaCorretta) {
                    abbattiCarta(p.getNome(), cartaDaAggiornare);
                }
            }

            // passa il turno all'avversario
            turno[0] = avversario;
            labelTurno.setText("Turno: Giocatore " + turno[0]);
        });
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
            int risultato = JOptionPane.showConfirmDialog(this, griglia, "Giocatore " + numeroGiocatore + " - scegli la tua persona segreta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (risultato != JOptionPane.OK_OPTION) {
                return null;
            }

            if (scelta[0] == null) {
                JOptionPane.showMessageDialog(this, "Devi selezionare una persona prima di confermare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        }

        return scelta[0];
    }

    private JPanel creaGriglia(List<Persona> persone, int[] turno, int mioNumero, Map<String, JPanel> cartaPerNome) {
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

            if (cartaPerNome != null) cartaPerNome.put(persona.getNome(), carta);

            cella.add(carta);
            griglia.add(cella);
        }

        return griglia;
    }

    private void inizializzaBot(List<Persona> persone, Albero albero) {
        Persona personaSegreta = mostraSceltaPersona(1, persone);
        if (personaSegreta == null) return;

        // Scegli una persona segreta per il bot (casuale)
        Persona personaSegretaBot = persone.get(new Random().nextInt(persone.size()));

        getContentPane().removeAll();
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(158, 26, 14));
        setContentPane(root);

        Map<String, JPanel> cartaPerNomeGiocatore = new HashMap<>();
        Map<String, JPanel> cartaPerNomeBot = new HashMap<>();

        // 1 = giocatore; 2 = bot
        int[] turno = {1};

        // Colonna giocatore (sinistra)
        JPanel colonnaGiocatore = new JPanel(new BorderLayout());
        colonnaGiocatore.setOpaque(false);
        colonnaGiocatore.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(60, 60, 60)));
        JLabel labelG1 = new JLabel("Giocatore", SwingConstants.CENTER);
        labelG1.setForeground(Color.WHITE);
        colonnaGiocatore.add(labelG1, BorderLayout.NORTH);
        colonnaGiocatore.add(creaGriglia(persone, turno, 1, cartaPerNomeGiocatore), BorderLayout.CENTER);

        // Colonna bot (destra)
        JPanel colonnaBot = new JPanel(new BorderLayout());
        colonnaBot.setOpaque(false);
        JLabel labelG2 = new JLabel("Bot", SwingConstants.CENTER);
        labelG2.setForeground(Color.WHITE);
        colonnaBot.add(labelG2, BorderLayout.NORTH);
        colonnaBot.add(creaGriglia(persone, turno, -1, cartaPerNomeBot), BorderLayout.CENTER);

        JPanel pannelloCentrale = new JPanel(new GridLayout(1, 2, 0, 0));
        pannelloCentrale.setOpaque(false);
        pannelloCentrale.add(colonnaGiocatore);
        pannelloCentrale.add(colonnaBot);

        // Pannello laterale
        JPanel pannelloLaterale = new JPanel();
        pannelloLaterale.setLayout(new BoxLayout(pannelloLaterale, BoxLayout.Y_AXIS));
        pannelloLaterale.setBackground(new Color(180, 180, 180));
        pannelloLaterale.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        pannelloLaterale.setPreferredSize(new Dimension(220, 0));

        JLabel labelTurno = new JLabel("Turno: Giocatore");
        labelTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTurno.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        // Sezione turno giocatore
        JComboBox<String> comboDomande = new JComboBox<>(domandePossibili);
        comboDomande.setMaximumSize(new Dimension(200, 28));
        comboDomande.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFaiDomanda = new JButton("Fai domanda");
        btnFaiDomanda.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFaiDomanda.setBackground(new Color(30, 100, 200));
        btnFaiDomanda.setForeground(Color.WHITE);
        btnFaiDomanda.setFocusPainted(false);
        btnFaiDomanda.setMaximumSize(new Dimension(200, 40));

        JButton btnIndovina = new JButton("Tenta di indovinare");
        btnIndovina.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIndovina.setBackground(new Color(46, 160, 67));
        btnIndovina.setForeground(Color.WHITE);
        btnIndovina.setFocusPainted(false);
        btnIndovina.setMaximumSize(new Dimension(200, 40));

        // Persona segreta del giocatore mostrata in basso
        JLabel labelSegreta = new JLabel("La tua persona segreta:");
        labelSegreta.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelSegreta.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));

        JPanel cartaSegreta = new JPanel(new BorderLayout(0, 4));
        cartaSegreta.setBackground(new Color(210, 210, 210));
        cartaSegreta.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        cartaSegreta.setMaximumSize(new Dimension(110, 150));
        cartaSegreta.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel imgSegreta = new JLabel(personaSegreta.getImmagine(90, 110));
        imgSegreta.setHorizontalAlignment(SwingConstants.CENTER);
        cartaSegreta.add(imgSegreta, BorderLayout.CENTER);

        // Combo e bottoni in cima, poi labelTurno, poi in fondo la carta segreta
        pannelloLaterale.add(comboDomande);
        pannelloLaterale.add(Box.createVerticalStrut(8));
        pannelloLaterale.add(btnFaiDomanda);
        pannelloLaterale.add(Box.createVerticalStrut(8));
        pannelloLaterale.add(btnIndovina);
        pannelloLaterale.add(Box.createVerticalStrut(12));
        pannelloLaterale.add(labelTurno);
        pannelloLaterale.add(Box.createVerticalGlue());
        pannelloLaterale.add(labelSegreta);
        pannelloLaterale.add(cartaSegreta);

        root.add(pannelloCentrale, BorderLayout.CENTER);
        root.add(pannelloLaterale, BorderLayout.EAST);

        // Stato dell'albero per il bot
        scelta = albero.getRoot();

        // Abilita/disabilita i controlli del giocatore in base al turno
        Runnable aggiornaUI = () -> {
            boolean turnoGiocatore = turno[0] == 1;
            comboDomande.setEnabled(turnoGiocatore);
            btnFaiDomanda.setEnabled(turnoGiocatore);
            btnIndovina.setEnabled(turnoGiocatore);
            if (turnoGiocatore) labelTurno.setText("Turno: Giocatore");
            else labelTurno.setText("Turno: Bot");
        };

        // Fai domanda
        btnFaiDomanda.addActionListener(_ -> {
            String domanda = (String) comboDomande.getSelectedItem();
            if (domanda == null) return;

            // Il bot risponde automaticamente in base alla sua persona segreta
            boolean risposta = rispondeDomanda(personaSegretaBot, domanda);
            String rispostaStr = risposta ? "Sì" : "No";

            JOptionPane.showMessageDialog(this, "Bot risponde: " + rispostaStr, "Risposta del bot", JOptionPane.INFORMATION_MESSAGE);

            // Filtra la griglia del giocatore
            for (Persona p : persone) {
                if (rispondeDomanda(p, domanda) != risposta) {
                    abbattiCarta(p.getNome(), cartaPerNomeGiocatore);
                }
            }

            // Turno del bot
            turno[0] = 2;
            aggiornaUI.run();
            eseguiTurnoBot(personaSegreta, cartaPerNomeBot, turno, btnFaiDomanda, comboDomande, btnIndovina, aggiornaUI);
        });

        // Tenta di indovinare (giocatore)
        btnIndovina.addActionListener(_ -> {
            String input = JOptionPane.showInputDialog(this, "Inserisci il nome della persona segreta del bot:", "Tenta di indovinare", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;

            String nomeInput = input.trim().toLowerCase();

            boolean esiste = persone.stream().anyMatch(p -> p.getNome().equals(nomeInput));
            if (!esiste) {
                JOptionPane.showMessageDialog(this, "\"" + nomeInput + "\" non è un personaggio valido.", "Nome non valido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (personaSegretaBot.getNome().equals(nomeInput)) {
                JOptionPane.showMessageDialog(this, "Hai vinto! La persona del bot era: " + personaSegretaBot.getNome(), "Hai vinto!", JOptionPane.INFORMATION_MESSAGE);
                btnFaiDomanda.setEnabled(false);
                btnIndovina.setEnabled(false);
                comboDomande.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Sbagliato! Tocca al bot.", "Risposta errata", JOptionPane.WARNING_MESSAGE);
                turno[0] = 2;
                aggiornaUI.run();
                eseguiTurnoBot(personaSegreta, cartaPerNomeBot, turno, btnFaiDomanda, comboDomande, btnIndovina, aggiornaUI);
            }
        });

        aggiornaUI.run();
        revalidate();
        repaint();
    }

    // Turno del bot: usa l'albero per fare la domanda, il giocatore risponde
    private void eseguiTurnoBot(Persona personaSegreta, Map<String, JPanel> cartaPerNomeBot, int[] turno, JButton btnFaiDomanda, JComboBox<String> comboDomande, JButton btnIndovina, Runnable aggiornaUI) {
        if (scelta == null) return;

        // Il bot ha trovato la persona
        if (scelta.getPersona() != null) {
            String tentativo = scelta.getPersona().getNome();
            if (tentativo.equals(personaSegreta.getNome())) {
                JOptionPane.showMessageDialog(this, "Il bot ha indovinato! Hai perso.\nLa tua persona era: " + personaSegreta.getNome(), "Il bot ha vinto!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Il bot ha tentato con \"" + tentativo + "\" ma ha sbagliato!\nTocca a te.", "Bot sbagliato", JOptionPane.INFORMATION_MESSAGE);
            }
            btnFaiDomanda.setEnabled(false);
            btnIndovina.setEnabled(false);
            comboDomande.setEnabled(false);
            return;
        }

        // Il bot fa la domanda dall'albero
        String domandaBot = scelta.getDomanda();

        // Il giocatore risponde manualmente
        int sceltaUtente = JOptionPane.showOptionDialog(this, "Il bot ti chiede:\n\"" + domandaBot + "\"", "Domanda del Bot", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sì", "No"}, "Sì");
        if (sceltaUtente == JOptionPane.CLOSED_OPTION) return;

        boolean rispostaGiocatore = (sceltaUtente == 0);

        // Verifica che il giocatore risponda correttamente (come nella modalità 2 persone)
        boolean rispostaCorretta = rispondeDomanda(personaSegreta, domandaBot);
        while (rispostaGiocatore != rispostaCorretta) {
            JOptionPane.showMessageDialog(this, "Risposta sbagliata! Devi rispondere correttamente.", "Errore", JOptionPane.ERROR_MESSAGE);
            sceltaUtente = JOptionPane.showOptionDialog(this, "Il bot ti chiede:\n\"" + domandaBot + "\"", "Domanda del Bot", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sì", "No"}, "Sì");
            if (sceltaUtente == JOptionPane.CLOSED_OPTION) return;
            rispostaGiocatore = (sceltaUtente == 0);
        }

        // Il bot filtra la sua griglia
        Nodo ramoScartato;
        if (rispostaGiocatore) {
            ramoScartato = scelta.getNo();
        } else {
            ramoScartato = scelta.getSi();
        }
        List<String> daAbbattere = personeRaggiungibili(ramoScartato);
        for (String nome : daAbbattere) {
            abbattiCarta(nome, cartaPerNomeBot);
        }

        // Avanza l'albero del bot
        if (rispostaGiocatore) {
            scelta = scelta.getSi();
        } else {
            scelta = scelta.getNo();
        }

        // Torna il turno al giocatore
        turno[0] = 1;
        aggiornaUI.run();
    }

    //aggiorno il testo all'interno del pannello delle domande
    private void aggiornadomanda() {
        if (scelta != null) {
            if (scelta.getDomanda() != null) {
                domande.setText(scelta.getDomanda());
            }
        } else {
            throw new IllegalArgumentException("scegli prima il personaggio");
        }
    }

    private void avanza(boolean risposta, Map<String, JPanel> cartaPerNome) {
        if (scelta == null || scelta.getDomanda() == null) return;

        // raccoglie le persone raggiungibili dal ramo SCARTATO
        Nodo ramoScartato;
        if (risposta) {
            ramoScartato = scelta.getNo();
        } else {
            ramoScartato = scelta.getSi();
        }
        List<String> daAbbattere = personeRaggiungibili(ramoScartato);

        // abbatte le carte corrispondenti
        for (String nome : daAbbattere) {
            abbattiCarta(nome, cartaPerNome);
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

    private void abbattiCarta(String nome, Map<String, JPanel> cartaPerNome) {
        JPanel carta = cartaPerNome.get(nome);
        if (carta == null) return;
        Dimension dim = carta.getPreferredSize();
        carta.removeAll();
        carta.setBackground(Color.YELLOW);
        carta.setPreferredSize(dim);
        carta.setToolTipText(null);
        carta.revalidate();
        carta.repaint();
    }

    private String creaStringaToolTip(Persona p) {
        String s = p.toString();
        s = s.replaceAll("\n", "<br>");
        return "<html>" + s + "</html>";
    }

    private boolean rispondeDomanda(Persona p, String domanda) {
        return switch (domanda) {
            case "è maschio?"              -> p.isSesso();
            case "ha i capelli castani?"   -> p.getColoreCapelli() == ColoriCrapa.CASTANO;
            case "ha i capelli neri?"      -> p.getColoreCapelli() == ColoriCrapa.NERO;
            case "ha i capelli biondi?"    -> p.getColoreCapelli() == ColoriCrapa.BIONDO;
            case "ha i capelli rossi?"     -> p.getColoreCapelli() == ColoriCrapa.ROSSO;
            case "ha i capelli bianchi?"   -> p.getColoreCapelli() == ColoriCrapa.BIANCO;
            case "ha la pelle bianca?"     -> p.getColorePelle() == ColoriPelle.BIANCO;
            case "ha la pelle nera?"       -> p.getColorePelle() == ColoriPelle.NERO;
            case "ha la pelle mulatta?"    -> p.getColorePelle() == ColoriPelle.MULATTO;
            case "ha gli occhi marroni?"   -> p.getColoreOcchi() == ColoriOch.MARRONE;
            case "ha gli occhi blu?"       -> p.getColoreOcchi() == ColoriOch.BLU;
            case "ha gli occhi verdi?"     -> p.getColoreOcchi() == ColoriOch.VERDE;
            case "ha gli occhiali?"        -> p.isOcchiali();
            case "ha i capelli lunghi?"    -> p.isCapelliLunghi();
            case "ha la barba o i baffi?"  -> p.isBarba();
            case "ha il cappello?"         -> p.isCappello();
            case "è pelato?"               -> p.isPelato();
            default -> false;
        };
    }
}