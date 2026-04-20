import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.Map;

public class SchermataGioco extends JFrame {
    private final List<Persona> persone;
    private final Domanda[] domandePossibili;

    public SchermataGioco(List<Persona> persone, Domanda[] domandePossibili) {
        if (domandePossibili == null || domandePossibili.length == 0) {
            throw new IllegalArgumentException("Le domande non possono essere null o vuote");
        }
        this.domandePossibili = domandePossibili;
        this.persone = persone;
        setTitle("IndovinaChi");
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inizializzaMenu();
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void inizializzaMenu() {
        getContentPane().removeAll();

        ImageIcon icona = new ImageIcon("img/sfondo_indovina_chi.png");
        JPanel panelSfondo = new JPanel(new BorderLayout()) {
            private final Image image = icona.getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int larghezzaPanel = getWidth(), pannelloAltezza = getHeight();
                int larghezzaImg = image.getWidth(null), immagineAltezza = image.getHeight(null);
                double scala = Math.max((double) larghezzaPanel / larghezzaImg, (double) pannelloAltezza / immagineAltezza);
                int larghezzaScalata = (int)(larghezzaImg * scala);
                int altezzaScalata   = (int)(immagineAltezza * scala);
                g.drawImage(image, (larghezzaPanel - larghezzaScalata) / 2, (pannelloAltezza - altezzaScalata) / 2, larghezzaScalata, altezzaScalata, this);
            }
        };

        JButton bottone1      = new JButtonCustom("Gioca in persona", new Color(34, 139, 34),  new Color(60, 179, 60));
        JButton bottone2      = new JButtonCustom("Gioca contro il computer", new Color(30, 144, 255), new Color(100, 180, 255));
        JButton bottoneCarica = new JButtonCustom("Carica partita salvata", new Color(160, 100, 0),  new Color(210, 140, 30));
        JButton bottone3      = new JButtonCustom("Esci", new Color(220, 20, 60),  new Color(255, 60, 80));

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        btnPanel.setPreferredSize(new Dimension(0, 180));
        btnPanel.add(bottone1);
        btnPanel.add(bottone2);
        btnPanel.add(bottoneCarica);
        btnPanel.add(bottone3);
        panelSfondo.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(panelSfondo);

        bottone1.addActionListener(_ -> inizializzaInPersona());
        bottone2.addActionListener(_ -> {
            String[] opzioni = {"Normale", "Difficile"};
            int sceltaDiff = JOptionPane.showOptionDialog(null, "Scegli la difficoltà", "Difficoltà",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[0]);

            if (sceltaDiff == JOptionPane.CLOSED_OPTION) return;

            Bot bot;
            if (sceltaDiff == 0) {
                try {
                    bot = new Bot(persone);
                } catch (Exception _) {
                    try {
                        bot = Serializzatore.deSerializzaBot("files/bot.ser");
                    } catch (RuntimeException _) {
                        JOptionPane.showMessageDialog(null,
                                "<html>Impossibile giocare contro il computer:<br>Riprova</html>",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                inizializzaBot(bot, false);
            } else {
                try {
                    bot = Serializzatore.deSerializzaBot("files/botDifficile.ser");
                } catch (RuntimeException _) {
                    try {
                        bot = new Bot(persone, true);
                    } catch (Exception _) {
                        JOptionPane.showMessageDialog(null,
                                "<html>Impossibile giocare contro il computer:<br>Riprova</html>",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                inizializzaBot(bot, true);
            }
        });

        bottoneCarica.addActionListener(_ -> caricaPartita());
        bottone3.addActionListener(_ -> dispose());

        revalidate();
        repaint();
    }

    // -----------------------------------------------------------------
    //  SALVATAGGIO E CARICAMENTO
    // -----------------------------------------------------------------

    private void salvaPartitaInPersona(Persona pg1, Persona pg2, int turno,
                                       List<String> abbG1, List<String> abbG2,
                                       List<String> dfG1, List<String> dfG2) {
        Object[] stato = {
                "persona",
                pg1.getNome(), pg2.getNome(),
                turno,
                abbG1.toArray(new String[0]),
                abbG2.toArray(new String[0]),
                dfG1.toArray(new String[0]),
                dfG2.toArray(new String[0])
        };
        try {
            Serializzatore.serializzaPartita(stato);
            JOptionPane.showMessageDialog(this, "Partita salvata!", "Salvataggio",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio:\n" + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvaPartitaBot(Persona pgGiocatore, Persona pgBot, int turno,
                                 List<String> abbGiocatore, List<String> abbBot,
                                 List<String> dfGiocatore, Nodo nodoCorrente, boolean difficile) {
        Object[] stato = {
                "bot",
                pgGiocatore.getNome(), pgBot.getNome(),
                turno,
                abbGiocatore.toArray(new String[0]),
                abbBot.toArray(new String[0]),
                dfGiocatore.toArray(new String[0]),
                nodoCorrente,
                difficile
        };
        try {
            Serializzatore.serializzaPartita(stato);
            JOptionPane.showMessageDialog(this, "Partita salvata!", "Salvataggio", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio:\n" + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void caricaPartita() {
        Object[] stato;
        try {
            stato = Serializzatore.deSerializzaPartita();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Caricamento", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (stato == null || stato.length < 8) {
            JOptionPane.showMessageDialog(this, "File di salvataggio non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String modalita = (String) stato[0];
        if ("persona".equals(modalita)) {
            ripristinaInPersona(stato);
        }
        else if ("bot".equals(modalita)) {
            ripristinaBot(stato);
        }
    }

    private void ripristinaInPersona(Object[] stato) {
        String nomeG1        = (String)   stato[1];
        String nomeG2        = (String)   stato[2];
        int    turnoIniziale = (Integer)  stato[3];
        String[] abbG1Array  = (String[]) stato[4];
        String[] abbG2Array  = (String[]) stato[5];
        String[] dfG1Array   = (String[]) stato[6];
        String[] dfG2Array   = (String[]) stato[7];

        Persona pg1 = cercaPersona(nomeG1);
        Persona pg2 = cercaPersona(nomeG2);
        if (pg1 == null || pg2 == null) {
            JOptionPane.showMessageDialog(this, "Personaggi del salvataggio non trovati.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        avviaPartitaInPersona(pg1, pg2, turnoIniziale,
                new ArrayList<>(Arrays.asList(abbG1Array)),
                new ArrayList<>(Arrays.asList(abbG2Array)),
                new ArrayList<>(Arrays.asList(dfG1Array)),
                new ArrayList<>(Arrays.asList(dfG2Array)));
    }

    private void ripristinaBot(Object[] stato) {
        if (stato.length < 9) {
            JOptionPane.showMessageDialog(this, "File di salvataggio bot non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nomeGiocatore = (String) stato[1];
        String nomeBot = (String) stato[2];
        int turnoIniziale = (Integer)  stato[3];
        String[] abbattuteGiocatore = (String[]) stato[4];
        String[] abbattuteBot = (String[]) stato[5];
        String[] domandeFatteGiocatore = (String[]) stato[6];
        Nodo    nodoCorrente = (Nodo) stato[7];
        boolean difficile = (Boolean) stato[8];

        Persona pgGiocatore = cercaPersona(nomeGiocatore);
        Persona pgBot       = cercaPersona(nomeBot);
        if (pgGiocatore == null || pgBot == null) {
            JOptionPane.showMessageDialog(this, "Personaggi del salvataggio non trovati.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Bot bot;
        try {
            bot = Serializzatore.deSerializzaBot(difficile ? "files/botDifficile.ser" : "files/bot.ser");
        } catch (RuntimeException _) {
            bot = null; // rigioca disabilitato se il file non è disponibile
        }

        avviaPartitaBot(pgGiocatore, pgBot, turnoIniziale,
                new ArrayList<>(Arrays.asList(abbattuteGiocatore)),
                new ArrayList<>(Arrays.asList(abbattuteBot)),
                new ArrayList<>(Arrays.asList(domandeFatteGiocatore)),
                nodoCorrente, bot, difficile);
    }

    private Persona cercaPersona(String nome) {
        return persone.stream()
                .filter(p -> p.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

    // -----------------------------------------------------------------
    //  MODALITÀ IN PERSONA
    // -----------------------------------------------------------------

    private void inizializzaInPersona() {
        Persona personaSegretaG1 = mostraSceltaPersona(1);
        if (personaSegretaG1 == null) return;
        Persona personaSegretaG2 = mostraSceltaPersona(2);
        if (personaSegretaG2 == null) return;

        avviaPartitaInPersona(personaSegretaG1, personaSegretaG2, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private void avviaPartitaInPersona(Persona personaSegretaG1, Persona personaSegretaG2,
                                       int turnoIniziale,
                                       List<String> abbattuteG1, List<String> abbattuteG2,
                                       List<String> domandeFatteG1, List<String> domandeFatteG2) {
        final int[] turno = {turnoIniziale};

        List<Boolean> risposteG1 = new ArrayList<>();
        List<Boolean> risposteG2 = new ArrayList<>();

        Map<String, JPanel> cartaPerNomeG1 = new HashMap<>();
        Map<String, JPanel> cartaPerNomeG2 = new HashMap<>();

        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(new Color(158, 26, 14));
        setContentPane(pannelloPrincipale);

        JPanel colonnaG1 = creaColonna("Giocatore 1", creaGriglia(cartaPerNomeG1), true);
        JPanel colonnaG2 = creaColonna("Giocatore 2", creaGriglia(cartaPerNomeG2), false);

        JPanel pannelloCentrale = new JPanel(new GridLayout(1, 2, 0, 0));
        pannelloCentrale.setOpaque(false);
        pannelloCentrale.add(colonnaG1);
        pannelloCentrale.add(colonnaG2);

        for (String nome : abbattuteG1) abbattiCarta(nome, cartaPerNomeG1);
        for (String nome : abbattuteG2) abbattiCarta(nome, cartaPerNomeG2);

        JPanel pannelloLaterale = new JPanel(new BorderLayout());
        pannelloLaterale.setBackground(new Color(180, 180, 180));
        pannelloLaterale.setPreferredSize(new Dimension(220, 0));

        JPanel headerLaterale = new JPanel(new BorderLayout());
        headerLaterale.setBackground(new Color(30, 100, 200));
        headerLaterale.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel labelTurno = new JLabel("Turno: Giocatore " + turnoIniziale, SwingConstants.CENTER);
        labelTurno.setForeground(Color.WHITE);
        labelTurno.setFont(labelTurno.getFont().deriveFont(Font.BOLD, 14f));
        headerLaterale.add(labelTurno, BorderLayout.CENTER);

        JPanel pannelloControlli = new JPanel();
        pannelloControlli.setLayout(new BoxLayout(pannelloControlli, BoxLayout.Y_AXIS));
        pannelloControlli.setBackground(new Color(180, 180, 180));
        pannelloControlli.setBorder(BorderFactory.createEmptyBorder(12, 15, 20, 15));

        JComboBox<String> comboDomande = new JComboBox<>();
        comboDomande.setMaximumSize(new Dimension(200, 28));
        comboDomande.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFaiDomanda = creaBottoneLaterale("Fai domanda",          new Color(30, 100, 200));
        JButton btnIndovina   = creaBottoneLaterale("Tenta di indovinare!", new Color(46, 160, 67));
        JButton btnSalva      = creaBottoneLaterale("Salva partita",        new Color(160, 100, 0));

        pannelloControlli.add(btnIndovina);
        pannelloControlli.add(Box.createVerticalStrut(10));
        pannelloControlli.add(comboDomande);
        pannelloControlli.add(Box.createVerticalStrut(8));
        pannelloControlli.add(btnFaiDomanda);
        pannelloControlli.add(Box.createVerticalStrut(10));
        pannelloControlli.add(btnSalva);

        pannelloLaterale.add(headerLaterale,    BorderLayout.NORTH);
        pannelloLaterale.add(pannelloControlli, BorderLayout.CENTER);

        pannelloPrincipale.add(pannelloCentrale, BorderLayout.CENTER);
        pannelloPrincipale.add(pannelloLaterale, BorderLayout.EAST);

        Runnable aggiornaCombo = () -> {
            List<String>  fatte;
            List<Boolean> risposte;

            if (turno[0] == 1) {
                fatte    = domandeFatteG1;
                risposte = risposteG1;
            } else {
                fatte    = domandeFatteG2;
                risposte = risposteG2;
            }

            comboDomande.removeAllItems();
            for (Domanda d : domandePossibili) {
                if (!fatte.contains(d.getTesto()) && !categoriaConfermata(d, fatte, risposte)) {
                    comboDomande.addItem(d.getTesto());
                }
            }
        };

        aggiornaCombo.run();

        btnSalva.addActionListener(_ ->
                salvaPartitaInPersona(personaSegretaG1, personaSegretaG2, turno[0], abbattuteG1, abbattuteG2, domandeFatteG1, domandeFatteG2)
        );

        btnIndovina.addActionListener(_ -> {
            int turnoCorrente = turno[0];
            String input = JOptionPane.showInputDialog(this,
                    "Giocatore " + turnoCorrente + ", inserisci il nome della persona:",
                    "Tenta di indovinare", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;

            String nomeInput = input.trim().toLowerCase();
            boolean esiste   = persone.stream().anyMatch(p -> p.getNome().equals(nomeInput));
            if (!esiste) {
                JOptionPane.showMessageDialog(this,
                        "\"" + nomeInput + "\" non è un personaggio valido.\nControlla l'ortografia e riprova.",
                        "Nome non valido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Persona segreta = (turnoCorrente == 1) ? personaSegretaG2 : personaSegretaG1;
            if (segreta.getNome().equals(nomeInput)) {
                JOptionPane.showMessageDialog(this,
                        "Giocatore " + turnoCorrente + " ha vinto!\nLa persona era: " + segreta.getNome(),
                        "Hai vinto!", JOptionPane.INFORMATION_MESSAGE);
                finePartita(pannelloLaterale, this::inizializzaInPersona);
            } else {
                if (turnoCorrente == 1) {
                    turno[0] = 2;
                } else {
                    turno[0] = 1;
                }
                labelTurno.setText("Turno: Giocatore " + turno[0]);
                headerLaterale.setBackground(new Color(30, 100, 200));
                aggiornaCombo.run();
                JOptionPane.showMessageDialog(this, "Risposta sbagliata! Tocca al Giocatore " + turno[0], "Sbagliato!", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnFaiDomanda.addActionListener(_ -> {
            String testoDomanda = (String) comboDomande.getSelectedItem();
            if (testoDomanda == null) return;

            int turnoCorrente = turno[0];
            List<String>  domandeFatteCorrente;
            List<Boolean> risposteCorrenti;

            if (turnoCorrente == 1) {
                domandeFatteCorrente = domandeFatteG1;
                risposteCorrenti     = risposteG1;
            } else {
                domandeFatteCorrente = domandeFatteG2;
                risposteCorrenti     = risposteG2;
            }

            Domanda domanda = Domanda.fromTesto(testoDomanda);

            int avversario;
            if (turnoCorrente == 1) {
                avversario = 2;
            } else {
                avversario = 1;
            }

            Persona segretaAvversario;
            if (avversario == 1) {
                segretaAvversario = personaSegretaG1;
            } else {
                segretaAvversario = personaSegretaG2;
            }

            boolean rispostaCorretta = domanda.corrisponde(segretaAvversario);

            boolean rispostaUtente;
            do {
                int sc = JOptionPane.showOptionDialog(this,
                        "Giocatore " + avversario + ", rispondi alla domanda:\n\"" + testoDomanda + "\"",
                        "Rispondi", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, new String[]{"Sì", "No"}, "Sì");
                if (sc == JOptionPane.CLOSED_OPTION) return;
                rispostaUtente = (sc == 0);
                if (rispostaUtente != rispostaCorretta)
                    JOptionPane.showMessageDialog(this,
                            "Risposta sbagliata! Devi rispondere correttamente.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
            } while (rispostaUtente != rispostaCorretta);

            domandeFatteCorrente.add(testoDomanda);
            risposteCorrenti.add(rispostaCorretta);

            Map<String, JPanel> cartaDaAggiornare;
            List<String>        abbattuteDaAggiornare;

            if (turnoCorrente == 1) {
                cartaDaAggiornare     = cartaPerNomeG1;
                abbattuteDaAggiornare = abbattuteG1;
            } else {
                cartaDaAggiornare     = cartaPerNomeG2;
                abbattuteDaAggiornare = abbattuteG2;
            }
            for (Persona p : persone) {
                if (domanda.corrisponde(p) != rispostaCorretta) {
                    abbattiCarta(p.getNome(), cartaDaAggiornare);
                    if (!abbattuteDaAggiornare.contains(p.getNome()))
                        abbattuteDaAggiornare.add(p.getNome());
                }
            }

            turno[0] = avversario;
            labelTurno.setText("Turno: Giocatore " + turno[0]);
            aggiornaCombo.run();
        });

        revalidate();
        repaint();
    }

    // -----------------------------------------------------------------
    //  MODALITÀ BOT
    // -----------------------------------------------------------------

    private void inizializzaBot(Bot bot, boolean difficile) {
        Persona personaSegreta    = mostraSceltaPersona(1);
        if (personaSegreta == null) return;
        Persona personaSegretaBot = persone.get(new Random().nextInt(persone.size()));

        avviaPartitaBot(personaSegreta, personaSegretaBot, 1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                bot.getRoot(), bot, difficile);
    }

    private void avviaPartitaBot(Persona personaSegreta, Persona personaSegretaBot,
                                 int turnoIniziale,
                                 List<String> abbattuteGiocatore, List<String> abbattuteBot,
                                 List<String> domandeFatteGiocatore,
                                 Nodo nodoIniziale, Bot bot, boolean difficile) {

        getContentPane().removeAll();
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(158, 26, 14));
        setContentPane(root);

        Nodo[]        sceltaCorrente    = {nodoIniziale};
        List<Boolean> risposteGiocatore = new ArrayList<>();
        int[]         turno             = {turnoIniziale};

        Map<String, JPanel> cartaPerNomeGiocatore = new HashMap<>();
        Map<String, JPanel> cartaPerNomeBot2      = new HashMap<>();

        JPanel colonnaGiocatore = creaColonna("Giocatore", creaGriglia(cartaPerNomeGiocatore), true);
        JPanel colonnaBot       = creaColonna("Bot",       creaGriglia(cartaPerNomeBot2),      false);

        JPanel pannelloCentrale = new JPanel(new GridLayout(1, 2, 0, 0));
        pannelloCentrale.setOpaque(false);
        pannelloCentrale.add(colonnaGiocatore);
        pannelloCentrale.add(colonnaBot);

        for (String nome : abbattuteGiocatore) abbattiCarta(nome, cartaPerNomeGiocatore);
        for (String nome : abbattuteBot)       abbattiCarta(nome, cartaPerNomeBot2);

        // Pannello laterale con header fisso in cima
        JPanel pannelloLaterale = new JPanel(new BorderLayout());
        pannelloLaterale.setBackground(new Color(180, 180, 180));
        pannelloLaterale.setPreferredSize(new Dimension(220, 0));

        JPanel headerLaterale = new JPanel(new BorderLayout());
        headerLaterale.setBackground(new Color(30, 100, 200));
        headerLaterale.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel labelTurno = new JLabel("Turno: Giocatore", SwingConstants.CENTER);
        labelTurno.setForeground(Color.WHITE);
        labelTurno.setFont(labelTurno.getFont().deriveFont(Font.BOLD, 14f));
        headerLaterale.add(labelTurno, BorderLayout.CENTER);

        JPanel pannelloControlli = new JPanel();
        pannelloControlli.setLayout(new BoxLayout(pannelloControlli, BoxLayout.Y_AXIS));
        pannelloControlli.setBackground(new Color(180, 180, 180));
        pannelloControlli.setBorder(BorderFactory.createEmptyBorder(12, 15, 20, 15));

        JComboBox<String> comboDomande = new JComboBox<>();
        comboDomande.setMaximumSize(new Dimension(200, 28));
        comboDomande.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFaiDomanda = creaBottoneLaterale("Fai domanda",          new Color(30, 100, 200));
        JButton btnIndovina   = creaBottoneLaterale("Tenta di indovinare!", new Color(46, 160, 67));
        JButton btnSalva      = creaBottoneLaterale("Salva partita",        new Color(160, 100, 0));

        JLabel labelSegreta = new JLabel("La tua persona segreta:");
        labelSegreta.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelSegreta.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));

        JPanel cartaSegreta = new JPanel(new BorderLayout(0, 4));
        cartaSegreta.setBackground(new Color(210, 210, 210));
        cartaSegreta.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        cartaSegreta.setMaximumSize(new Dimension(110, 150));
        cartaSegreta.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel imgSegreta = new JLabel(getImmagine(personaSegreta.getNome(), 90, 110));
        imgSegreta.setHorizontalAlignment(SwingConstants.CENTER);
        cartaSegreta.add(imgSegreta, BorderLayout.CENTER);

        pannelloControlli.add(comboDomande);
        pannelloControlli.add(Box.createVerticalStrut(8));
        pannelloControlli.add(btnFaiDomanda);
        pannelloControlli.add(Box.createVerticalStrut(8));
        pannelloControlli.add(btnIndovina);
        pannelloControlli.add(Box.createVerticalStrut(8));
        pannelloControlli.add(btnSalva);
        pannelloControlli.add(Box.createVerticalGlue());
        pannelloControlli.add(labelSegreta);
        pannelloControlli.add(cartaSegreta);

        pannelloLaterale.add(headerLaterale,    BorderLayout.NORTH);
        pannelloLaterale.add(pannelloControlli, BorderLayout.CENTER);

        root.add(pannelloCentrale, BorderLayout.CENTER);
        root.add(pannelloLaterale, BorderLayout.EAST);

        Runnable aggiornaCombo = () -> {
            comboDomande.removeAllItems();
            for (Domanda d : domandePossibili) {
                if (!domandeFatteGiocatore.contains(d.getTesto()) &&
                        !categoriaConfermata(d, domandeFatteGiocatore, risposteGiocatore))
                    comboDomande.addItem(d.getTesto());
            }
        };

        aggiornaCombo.run();

        Runnable aggiornaUI = () -> {
            boolean turnoGiocatore = (turno[0] == 1);
            comboDomande.setEnabled(turnoGiocatore);
            btnFaiDomanda.setEnabled(turnoGiocatore);
            btnIndovina.setEnabled(turnoGiocatore);
            btnSalva.setEnabled(turnoGiocatore);
            if (turnoGiocatore) {
                labelTurno.setText("Turno: Giocatore");
            } else {
                labelTurno.setText("Turno: Bot");
            }
            headerLaterale.setBackground(turnoGiocatore ? new Color(30, 100, 200) : new Color(180, 60, 60));
        };

        Runnable onRigioca = () -> inizializzaBot(bot, difficile);

        btnSalva.addActionListener(_ ->
                salvaPartitaBot(personaSegreta, personaSegretaBot, turno[0], abbattuteGiocatore, abbattuteBot, domandeFatteGiocatore, sceltaCorrente[0], difficile));

        btnFaiDomanda.addActionListener(_ -> {
            String testoDomanda = (String) comboDomande.getSelectedItem();
            if (testoDomanda == null) return;

            Domanda domanda  = Domanda.fromTesto(testoDomanda);
            boolean risposta = domanda.corrisponde(personaSegretaBot);
            JOptionPane.showMessageDialog(this,
                    "Bot risponde: " + (risposta ? "Sì" : "No"),
                    "Risposta del bot", JOptionPane.INFORMATION_MESSAGE);

            domandeFatteGiocatore.add(testoDomanda);
            risposteGiocatore.add(risposta);

            for (Persona p : persone) {
                if (domanda.corrisponde(p) != risposta) {
                    abbattiCarta(p.getNome(), cartaPerNomeGiocatore);
                    if (!abbattuteGiocatore.contains(p.getNome()))
                        abbattuteGiocatore.add(p.getNome());
                }
            }

            aggiornaCombo.run();
            turno[0] = 2;
            aggiornaUI.run();
            eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot,
                    turno, sceltaCorrente, aggiornaUI, pannelloLaterale, onRigioca);
        });

        btnIndovina.addActionListener(_ -> {
            String input = JOptionPane.showInputDialog(this,
                    "Inserisci il nome della persona segreta del bot:",
                    "Tenta di indovinare", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;

            String nomeInput = input.trim().toLowerCase();
            boolean esiste   = persone.stream().anyMatch(p -> p.getNome().equals(nomeInput));
            if (!esiste) {
                JOptionPane.showMessageDialog(this,
                        "\"" + nomeInput + "\" non è un personaggio valido.",
                        "Nome non valido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (personaSegretaBot.getNome().equals(nomeInput)) {
                JOptionPane.showMessageDialog(this,
                        "Hai vinto! La persona del bot era: " + personaSegretaBot.getNome(),
                        "Hai vinto!", JOptionPane.INFORMATION_MESSAGE);
                finePartita(pannelloLaterale, onRigioca);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sbagliato! Tocca al bot.", "Risposta errata", JOptionPane.WARNING_MESSAGE);
                turno[0] = 2;
                aggiornaUI.run();
                eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot,
                        turno, sceltaCorrente, aggiornaUI, pannelloLaterale, onRigioca);
            }
        });

        if (turnoIniziale == 2) {
            aggiornaUI.run();
            eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot,
                    turno, sceltaCorrente, aggiornaUI, pannelloLaterale, onRigioca);
        } else {
            aggiornaUI.run();
        }

        revalidate();
        repaint();
    }

    // -----------------------------------------------------------------
    //  TURNO BOT
    // -----------------------------------------------------------------

    // FIX: sceltaCorrente è ora un parametro (Nodo[]) invece del campo di istanza
    private void eseguiTurnoBot(Persona personaSegreta,
                                Map<String, JPanel> cartaPerNomeBot,
                                List<String> abbattuteBot,
                                int[] turno, Nodo[] sceltaCorrente,
                                Runnable aggiornaUI, JPanel pannelloLaterale,
                                Runnable onRigioca) {
        if (sceltaCorrente[0] == null) return;

        if (sceltaCorrente[0] instanceof NodoPersona np) {
            String tentativo = np.getPersona().getNome();
            if (tentativo.equals(personaSegreta.getNome())) {
                JOptionPane.showMessageDialog(this,
                        "Il bot ha indovinato! Hai perso.\nLa tua persona era: " + personaSegreta.getNome(),
                        "Il bot ha vinto!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Il bot ha tentato con \"" + tentativo + "\" ma ha sbagliato!\nTocca a te.",
                        "Bot sbagliato", JOptionPane.INFORMATION_MESSAGE);
            }
            finePartita(pannelloLaterale, onRigioca);
            return;
        }

        if (!(sceltaCorrente[0] instanceof NodoDomanda nd)) return;
        String  domandaBot       = nd.getDomanda();
        Domanda domanda          = Domanda.fromTesto(domandaBot);
        boolean rispostaCorretta = domanda.corrisponde(personaSegreta);
        boolean rispostaGiocatore;

        do {
            int sc = JOptionPane.showOptionDialog(this,
                    "Il bot ti chiede:\n\"" + domandaBot + "\"",
                    "Domanda del Bot", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new String[]{"Sì", "No"}, "Sì");
            if (sc == JOptionPane.CLOSED_OPTION) return;
            rispostaGiocatore = (sc == 0);
            if (rispostaGiocatore != rispostaCorretta)
                JOptionPane.showMessageDialog(this,
                        "Risposta sbagliata! Devi rispondere correttamente.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
        } while (rispostaGiocatore != rispostaCorretta);

        Nodo ramoScartato;
        if (rispostaGiocatore) {
            ramoScartato = sceltaCorrente[0].getNo();
        } else {
            ramoScartato = sceltaCorrente[0].getSi();
        }

        for (String nome : personeRaggiungibili(ramoScartato)) {
            abbattiCarta(nome, cartaPerNomeBot);
            if (!abbattuteBot.contains(nome)) abbattuteBot.add(nome);
        }

        if (rispostaGiocatore) {
            sceltaCorrente[0] = sceltaCorrente[0].getSi();
        } else {
            sceltaCorrente[0] = sceltaCorrente[0].getNo();
        }
        turno[0] = 1;
        aggiornaUI.run();
    }

    // -----------------------------------------------------------------
    //  SCELTA PERSONA SEGRETA
    // -----------------------------------------------------------------

    private Persona mostraSceltaPersona(int numeroGiocatore) {
        final Persona[] sceltaP = {null};

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

            JLabel immagine = new JLabel(getImmagine(persona.getNome(), 65, 80));
            immagine.setHorizontalAlignment(SwingConstants.CENTER);
            carta.add(immagine, BorderLayout.CENTER);
            cella.add(carta);
            griglia.add(cella);

            carta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    for (Component comp : griglia.getComponents()) {
                        JPanel pannelloCella = (JPanel) comp;
                        Component[] figli    = pannelloCella.getComponents();
                        if (figli.length > 0 && figli[0] instanceof JPanel cartaInterna) {
                            cartaInterna.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                            cartaInterna.setBackground(new Color(210, 210, 210));
                        }
                    }
                    carta.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    sceltaP[0] = persona;
                }
            });
        }

        while (sceltaP[0] == null) {
            int risultato = JOptionPane.showConfirmDialog(this, griglia,
                    "Giocatore " + numeroGiocatore + " - scegli la tua persona segreta",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (risultato != JOptionPane.OK_OPTION) return null;
            if (sceltaP[0] == null)
                JOptionPane.showMessageDialog(this,
                        "Devi selezionare una persona prima di confermare.",
                        "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
        return sceltaP[0];
    }

    // -----------------------------------------------------------------
    //  METODI DI SUPPORTO UI
    // -----------------------------------------------------------------

    private JPanel creaGriglia(Map<String, JPanel> cartaPerNome) {
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

            JLabel immagine = new JLabel(getImmagine(persona.getNome(), 65, 80));
            immagine.setHorizontalAlignment(SwingConstants.CENTER);
            carta.add(immagine, BorderLayout.CENTER);
            carta.setToolTipText("<html>" + persona.toString().replace("\n", "<br>") + "</html>");

            if (cartaPerNome != null) cartaPerNome.put(persona.getNome(), carta);
            cella.add(carta);
            griglia.add(cella);
        }
        return griglia;
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

    private void finePartita(JPanel pannelloLaterale, Runnable onRigioca) {
        pannelloLaterale.removeAll();
        pannelloLaterale.setLayout(new BoxLayout(pannelloLaterale, BoxLayout.Y_AXIS));

        JButton btnRigioca = creaBottoneLaterale("Rigioca",                  new Color(30, 100, 200));
        JButton btnMenu    = creaBottoneLaterale("Torna al menu principale",  new Color(46, 160, 67));

        if (onRigioca == null) btnRigioca.setEnabled(false);
        if (onRigioca != null) btnRigioca.addActionListener(_ -> onRigioca.run());
        btnMenu.addActionListener(_ -> inizializzaMenu());

        pannelloLaterale.add(Box.createVerticalGlue());
        pannelloLaterale.add(btnRigioca);
        pannelloLaterale.add(Box.createVerticalStrut(12));
        pannelloLaterale.add(btnMenu);
        pannelloLaterale.add(Box.createVerticalGlue());

        pannelloLaterale.revalidate();
        pannelloLaterale.repaint();
    }

    private JPanel creaColonna(String titolo, JPanel griglia, boolean bordo) {
        JPanel colonna = new JPanel(new BorderLayout());
        colonna.setOpaque(false);
        if (bordo) colonna.setBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(60, 60, 60)));
        JLabel label = new JLabel(titolo, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        colonna.add(label, BorderLayout.NORTH);
        colonna.add(griglia, BorderLayout.CENTER);
        return colonna;
    }

    private JButton creaBottoneLaterale(String testo, Color sfondo) {
        JButton btn = new JButton(testo);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(sfondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(200, 40));
        return btn;
    }

    //la categoria è "confermata" solo se quella domanda ha avuto risposta si
    private boolean categoriaConfermata(Domanda domanda, List<String> fatte, List<Boolean> risposte) {
        if (domanda.getCategoria() == Domanda.Categoria.NESSUNA) return false;
        for (int i = 0; i < fatte.size(); i++) {
            Domanda df = Domanda.fromTesto(fatte.get(i));
            if (df.getCategoria() == domanda.getCategoria() && i < risposte.size() && risposte.get(i)) return true;
        }
        return false;
    }

    private List<String> personeRaggiungibili(Nodo n) {
        List<String> nomi = new ArrayList<>();
        raccogliPersone(n, nomi);
        return nomi;
    }

    private void raccogliPersone(Nodo n, List<String> nomi) {
        if (n == null) return;
        if (n instanceof NodoPersona np) {
            nomi.add(np.getPersona().getNome());
            return;
        }
        raccogliPersone(n.getSi(), nomi);
        raccogliPersone(n.getNo(), nomi);
    }

    private ImageIcon getImmagine(String nome, int larghezza, int altezza) {
        String percorso = "img/" + nome.trim().toLowerCase() + ".png";
        Path path = Paths.get(percorso);
        if (!Files.exists(path))
            throw new IllegalArgumentException("Percorso non trovato: " + path);
        Image scalata = new ImageIcon(percorso).getImage()
                .getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
        return new ImageIcon(scalata);
    }
}