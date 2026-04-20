import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.Map;

public class SchermataGioco extends JFrame {

    private List<Persona> persone;
    private String[] domandePossibili;
    private Nodo scelta;

    public SchermataGioco(List<Persona> persone, String[] domandePossibili) {
        if (domandePossibili == null || domandePossibili.length == 0) throw new IllegalArgumentException("Le domande non possono essere null o vuote");
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
                int larghezzaScalata = (int)(larghezzaImg * scala), altezzaScalata = (int)(immagineAltezza * scala);
                g.drawImage(image, (larghezzaPanel - larghezzaScalata) / 2, (pannelloAltezza - altezzaScalata) / 2, larghezzaScalata, altezzaScalata, this);
            }
        };

        JButton bottone1 = new JButtonCustom("Gioca in persona", new Color(34, 139, 34), new Color(60, 179, 60));
        JButton bottone2 = new JButtonCustom("Gioca contro il computer", new Color(30, 144, 255), new Color(100, 180, 255));
        JButton bottoneCarica = new JButtonCustom("Carica partita salvata", new Color(160, 100, 0), new Color(210, 140, 30));
        JButton bottone3 = new JButtonCustom("Esci", new Color(220, 20, 60), new Color(255, 60, 80));

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
            int sceltaDiff = JOptionPane.showOptionDialog(null, "Scegli la difficoltà", "Difficoltà", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[0]);
            Bot bot;
            if (sceltaDiff == 0) {
                try {
                    bot = new Bot(persone);
                }
                catch (Exception _) {
                    try {
                        bot = Serializzatore.deSerializzaBot("files/bot.ser");
                    }
                    catch (RuntimeException _) {
                        JOptionPane.showMessageDialog(null, "<html>Impossibile giocare contro il computer:<br>Riprova</html>", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                inizializzaBot(bot, false);
            }
            else if (sceltaDiff == 1) {
                try {
                    bot = Serializzatore.deSerializzaBot("files/botDifficile.ser");
                }
                catch (RuntimeException _) {
                    try {
                        bot = new Bot(persone, true);
                    }
                    catch (Exception _) {
                        JOptionPane.showMessageDialog(null, "<html>Impossibile giocare contro il computer:<br>Riprova</html>", "Errore", JOptionPane.ERROR_MESSAGE);
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


    //salvataggio e caricamento partita


    private void salvaPartitaInPersona(Persona pg1, Persona pg2, int turno,
                                       List<String> abbG1, List<String> abbG2,
                                       List<String> dfG1, List<String> dfG2) {
        Object[] stato = new Object[8];
        stato[0] = "persona";
        stato[1] = pg1.getNome();
        stato[2] = pg2.getNome();
        stato[3] = turno;
        stato[4] = abbG1.toArray(new String[0]);
        stato[5] = abbG2.toArray(new String[0]);
        stato[6] = dfG1.toArray(new String[0]);
        stato[7] = dfG2.toArray(new String[0]);
        try {
            Serializzatore.serializzaPartita(stato);
            JOptionPane.showMessageDialog(this, "Partita salvata!", "Salvataggio", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio:\n" + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvaPartitaBot(Persona pgGiocatore, Persona pgBot, int turno,
                                 List<String> abbGiocatore, List<String> abbBot,
                                 List<String> dfGiocatore, Nodo nodoCorrente, boolean difficile) {
        Object[] stato = new Object[9];
        stato[0] = "bot";
        stato[1] = pgGiocatore.getNome();
        stato[2] = pgBot.getNome();
        stato[3] = turno;
        stato[4] = abbGiocatore.toArray(new String[0]);
        stato[5] = abbBot.toArray(new String[0]);
        stato[6] = dfGiocatore.toArray(new String[0]);
        stato[7] = nodoCorrente;
        stato[8] = difficile;
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
        } else if ("bot".equals(modalita)) {
            ripristinaBot(stato);
        }
    }

    private void ripristinaInPersona(Object[] stato) {
        String nomeG1 = (String) stato[1];
        String nomeG2 = (String) stato[2];
        int turnoIniziale = (Integer) stato[3];
        String[] abbattutiG1Array = (String[]) stato[4];
        String[] abbattutiG2Array = (String[]) stato[5];
        String[] domFatteG1Array  = (String[]) stato[6];
        String[] domFatteG2Array  = (String[]) stato[7];

        Persona pg1 = cercaPersona(nomeG1);
        Persona pg2 = cercaPersona(nomeG2);
        if (pg1 == null || pg2 == null) {
            JOptionPane.showMessageDialog(this, "Personaggi del salvataggio non trovati.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> abbattuteG1  = new ArrayList<>(Arrays.asList(abbattutiG1Array));
        List<String> abbattuteG2  = new ArrayList<>(Arrays.asList(abbattutiG2Array));
        List<String> domandeFatteG1 = new ArrayList<>(Arrays.asList(domFatteG1Array));
        List<String> domandeFatteG2 = new ArrayList<>(Arrays.asList(domFatteG2Array));

        avviaPartitaInPersona(pg1, pg2, turnoIniziale, abbattuteG1, abbattuteG2, domandeFatteG1, domandeFatteG2);
    }

    private void ripristinaBot(Object[] stato) {
        if (stato.length < 9) {
            JOptionPane.showMessageDialog(this, "File di salvataggio bot non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nomeGiocatore = (String) stato[1];
        String nomeBot       = (String) stato[2];
        int turnoIniziale    = (Integer) stato[3];
        String[] abbGarr     = (String[]) stato[4];
        String[] abbBarr     = (String[]) stato[5];
        String[] dfGarr      = (String[]) stato[6];
        Nodo nodoCorrente    = (Nodo) stato[7];
        boolean difficile    = (Boolean) stato[8];

        Persona pgGiocatore = cercaPersona(nomeGiocatore);
        Persona pgBot       = cercaPersona(nomeBot);
        if (pgGiocatore == null || pgBot == null) {
            JOptionPane.showMessageDialog(this, "Personaggi del salvataggio non trovati.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> abbattuteGiocatore = new ArrayList<>(Arrays.asList(abbGarr));
        List<String> abbattuteBot       = new ArrayList<>(Arrays.asList(abbBarr));
        List<String> domandeFatteGiocatore = new ArrayList<>(Arrays.asList(dfGarr));

        avviaPartitaBot(pgGiocatore, pgBot, turnoIniziale, abbattuteGiocatore, abbattuteBot, domandeFatteGiocatore, nodoCorrente, difficile);
    }

    private Persona cercaPersona(String nome) {
        for (Persona p : persone) {
            if (p.getNome().equals(nome)) return p;
        }
        return null;
    }

    // -----------------------------------------------------------------
    //  MODALITÀ IN PERSONA
    // -----------------------------------------------------------------

    private void inizializzaInPersona() {
        Persona personaSegretaG1 = mostraSceltaPersona(1);
        if (personaSegretaG1 == null) return;
        Persona personaSegretaG2 = mostraSceltaPersona(2);
        if (personaSegretaG2 == null) return;

        avviaPartitaInPersona(personaSegretaG1, personaSegretaG2, 1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private void avviaPartitaInPersona(Persona personaSegretaG1, Persona personaSegretaG2,
                                       int turnoIniziale,
                                       List<String> abbattuteG1, List<String> abbattuteG2,
                                       List<String> domandeFatteG1, List<String> domandeFatteG2) {
        final int[] turno = {turnoIniziale};

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

        // Ripristina visivamente le carte abbattute
        for (String nome : abbattuteG1) abbattiCarta(nome, cartaPerNomeG1);
        for (String nome : abbattuteG2) abbattiCarta(nome, cartaPerNomeG2);

        JPanel pannelloLaterale = new JPanel();
        pannelloLaterale.setLayout(new BoxLayout(pannelloLaterale, BoxLayout.Y_AXIS));
        pannelloLaterale.setBackground(new Color(180, 180, 180));
        pannelloLaterale.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        pannelloLaterale.setPreferredSize(new Dimension(220, 0));

        JLabel labelTurno = new JLabel("Turno: Giocatore " + turnoIniziale);
        labelTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTurno.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Costruisce la combobox filtrata in base alle domande già fatte
        List<String> domandeDisponibili = new ArrayList<>(Arrays.asList(domandePossibili));
        List<String> dfAttuale = (turnoIniziale == 1) ? domandeFatteG1 : domandeFatteG2;
        domandeDisponibili.removeAll(dfAttuale);
        JComboBox<String> comboDomande = new JComboBox<>(domandeDisponibili.toArray(new String[0]));
        comboDomande.setMaximumSize(new Dimension(200, 28));
        comboDomande.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFaiDomanda = creaBottoneLaterale("Fai domanda", new Color(30, 100, 200));
        JButton btnIndovina   = creaBottoneLaterale("Tenta di indovinare!", new Color(46, 160, 67));
        JButton btnSalva      = creaBottoneLaterale("Salva partita", new Color(160, 100, 0));

        pannelloLaterale.add(labelTurno);
        pannelloLaterale.add(btnIndovina);
        pannelloLaterale.add(Box.createVerticalStrut(10));
        pannelloLaterale.add(comboDomande);
        pannelloLaterale.add(Box.createVerticalStrut(8));
        pannelloLaterale.add(btnFaiDomanda);
        pannelloLaterale.add(Box.createVerticalStrut(10));
        pannelloLaterale.add(btnSalva);
        pannelloLaterale.add(Box.createVerticalStrut(10));

        pannelloPrincipale.add(pannelloCentrale, BorderLayout.CENTER);
        pannelloPrincipale.add(pannelloLaterale, BorderLayout.EAST);

        btnSalva.addActionListener(_ ->
                salvaPartitaInPersona(personaSegretaG1, personaSegretaG2, turno[0],
                        abbattuteG1, abbattuteG2, domandeFatteG1, domandeFatteG2));

        btnIndovina.addActionListener(_ -> {
            int turnoCorrente = turno[0];
            String input = JOptionPane.showInputDialog(
                    this,
                    "Giocatore " + turnoCorrente + ", inserisci il nome della persona:",
                    "Tenta di indovinare",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (input == null) return;

            String nomeInput = input.trim().toLowerCase();
            boolean esiste = false;
            for (Persona p : persone) {
                if (p.getNome().equals(nomeInput)) { esiste = true; break; }
            }
            if (!esiste) {
                JOptionPane.showMessageDialog(this,
                        "\"" + nomeInput + "\" non è un personaggio valido.\nControlla l'ortografia e riprova.",
                        "Nome non valido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Persona segreta = (turnoCorrente == 1) ? personaSegretaG2 : personaSegretaG1;
            boolean haVinto = segreta.getNome().equals(nomeInput);

            if (haVinto) {
                JOptionPane.showMessageDialog(this, "Giocatore " + turnoCorrente + " ha vinto!\nLa persona era: " + segreta.getNome(), "Hai vinto!", JOptionPane.INFORMATION_MESSAGE);
                finePartita(pannelloLaterale);
            } else {
                turno[0] = (turnoCorrente == 1) ? 2 : 1;
                labelTurno.setText("Turno: Giocatore " + turno[0]);

                // aggiorna la combobox per il nuovo giocatore
                List<String> dfNuovo = (turno[0] == 1) ? domandeFatteG1 : domandeFatteG2;
                comboDomande.removeAllItems();
                for (String d : domandePossibili) {
                    if (!dfNuovo.contains(d)) comboDomande.addItem(d);
                }

                JOptionPane.showMessageDialog(this, "Risposta sbagliata! Tocca al Giocatore " + turno[0], "Sbagliato!", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnFaiDomanda.addActionListener(_ -> {
            String domanda = (String) comboDomande.getSelectedItem();
            if (domanda == null) return;
            int turnoCorrente = turno[0];

            if (turnoCorrente == 1) domandeFatteG1.add(domanda);
            else domandeFatteG2.add(domanda);

            int avversario = (turnoCorrente == 1) ? 2 : 1;
            Persona segretaAvversario = (avversario == 1) ? personaSegretaG1 : personaSegretaG2;
            boolean rispostaCorretta = rispondeDomanda(segretaAvversario, domanda);

            Boolean rispostaUtente = null;
            while (rispostaUtente == null || rispostaUtente != rispostaCorretta) {
                int sc = JOptionPane.showOptionDialog(this,
                        "Giocatore " + avversario + ", rispondi alla domanda:\n\"" + domanda + "\"",
                        "Rispondi", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, new String[]{"Sì", "No"}, "Sì");
                if (sc == JOptionPane.CLOSED_OPTION) return;
                rispostaUtente = (sc == 0);
                if (rispostaUtente != rispostaCorretta) {
                    JOptionPane.showMessageDialog(this, "Risposta sbagliata! Devi rispondere correttamente.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }

            Map<String, JPanel> cartaDaAggiornare = (turnoCorrente == 1) ? cartaPerNomeG1 : cartaPerNomeG2;
            List<String> abbattuteDaAggiornare    = (turnoCorrente == 1) ? abbattuteG1 : abbattuteG2;
            for (Persona p : persone) {
                if (rispondeDomanda(p, domanda) != rispostaCorretta) {
                    abbattiCarta(p.getNome(), cartaDaAggiornare);
                    if (!abbattuteDaAggiornare.contains(p.getNome())) abbattuteDaAggiornare.add(p.getNome());
                }
            }

            // ricostruisce combobox del giocatore che ha appena fatto la domanda
            List<String> domandeGiocatore = (turnoCorrente == 1) ? domandeFatteG1 : domandeFatteG2;
            comboDomande.removeAllItems();
            for (String d : domandePossibili) {
                if (!domandeGiocatore.contains(d) && !(rispostaCorretta && categoriaGiaConfermata(d, domandeGiocatore))) {
                    comboDomande.addItem(d);
                }
            }

            turno[0] = avversario;
            labelTurno.setText("Turno: Giocatore " + turno[0]);

            // Ricarica la combo con le domande del nuovo giocatore corrente
            List<String> dfNuovo = (turno[0] == 1) ? domandeFatteG1 : domandeFatteG2;
            comboDomande.removeAllItems();
            for (String d : domandePossibili) {
                if (!dfNuovo.contains(d)) comboDomande.addItem(d);
            }
        });

        revalidate();
        repaint();
    }


    //  MODALITÀ BOT
    private void inizializzaBot(Bot bot, boolean difficile) {
        Persona personaSegreta = mostraSceltaPersona(1);
        if (personaSegreta == null) return;
        Persona personaSegretaBot = persone.get(new Random().nextInt(persone.size()));

        avviaPartitaBot(personaSegreta, personaSegretaBot, 1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                bot.getRoot(), difficile);
    }

    private void avviaPartitaBot(Persona personaSegreta, Persona personaSegretaBot,
                                 int turnoIniziale,
                                 List<String> abbattuteGiocatore, List<String> abbattuteBot,
                                 List<String> domandeFatteGiocatore,
                                 Nodo nodoIniziale, boolean difficile) {

        getContentPane().removeAll();
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(158, 26, 14));
        setContentPane(root);

        Map<String, JPanel> cartaPerNomeGiocatore = new HashMap<>();
        Map<String, JPanel> cartaPerNomeBot2      = new HashMap<>();

        int[] turno = {turnoIniziale};
        scelta = nodoIniziale;

        JPanel colonnaGiocatore = creaColonna("Giocatore", creaGriglia(cartaPerNomeGiocatore), true);
        JPanel colonnaBot       = creaColonna("Bot",       creaGriglia(cartaPerNomeBot2),      false);

        JPanel pannelloCentrale = new JPanel(new GridLayout(1, 2, 0, 0));
        pannelloCentrale.setOpaque(false);
        pannelloCentrale.add(colonnaGiocatore);
        pannelloCentrale.add(colonnaBot);

        // Ripristina carte abbattute
        for (String nome : abbattuteGiocatore) abbattiCarta(nome, cartaPerNomeGiocatore);
        for (String nome : abbattuteBot)       abbattiCarta(nome, cartaPerNomeBot2);

        JPanel pannelloLaterale = new JPanel();
        pannelloLaterale.setLayout(new BoxLayout(pannelloLaterale, BoxLayout.Y_AXIS));
        pannelloLaterale.setBackground(new Color(180, 180, 180));
        pannelloLaterale.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        pannelloLaterale.setPreferredSize(new Dimension(220, 0));

        JLabel labelTurno = new JLabel("Turno: Giocatore");
        labelTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTurno.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        // Combobox filtrata con domande già fatte
        List<String> domandeDisp = new ArrayList<>(Arrays.asList(domandePossibili));
        domandeDisp.removeAll(domandeFatteGiocatore);
        JComboBox<String> comboDomande = new JComboBox<>(domandeDisp.toArray(new String[0]));
        comboDomande.setMaximumSize(new Dimension(200, 28));
        comboDomande.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFaiDomanda = creaBottoneLaterale("Fai domanda",      new Color(30, 100, 200));
        JButton btnIndovina   = creaBottoneLaterale("Tenta di indovinare!", new Color(46, 160, 67));
        JButton btnSalva      = creaBottoneLaterale("Salva partita",    new Color(160, 100, 0));

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

        pannelloLaterale.add(comboDomande);
        pannelloLaterale.add(Box.createVerticalStrut(8));
        pannelloLaterale.add(btnFaiDomanda);
        pannelloLaterale.add(Box.createVerticalStrut(8));
        pannelloLaterale.add(btnIndovina);
        pannelloLaterale.add(Box.createVerticalStrut(8));
        pannelloLaterale.add(btnSalva);
        pannelloLaterale.add(Box.createVerticalStrut(12));
        pannelloLaterale.add(labelTurno);
        pannelloLaterale.add(Box.createVerticalGlue());
        pannelloLaterale.add(labelSegreta);
        pannelloLaterale.add(cartaSegreta);

        root.add(pannelloCentrale, BorderLayout.CENTER);
        root.add(pannelloLaterale, BorderLayout.EAST);

        Runnable aggiornaUI = () -> {
            boolean turnoGiocatore = turno[0] == 1;
            comboDomande.setEnabled(turnoGiocatore);
            btnFaiDomanda.setEnabled(turnoGiocatore);
            btnIndovina.setEnabled(turnoGiocatore);
            btnSalva.setEnabled(turnoGiocatore);
            labelTurno.setText(turnoGiocatore ? "Turno: Giocatore" : "Turno: Bot");
        };

        btnSalva.addActionListener(_ ->
                salvaPartitaBot(personaSegreta, personaSegretaBot, turno[0],
                        abbattuteGiocatore, abbattuteBot, domandeFatteGiocatore, scelta, difficile));

        btnFaiDomanda.addActionListener(_ -> {
            String domanda = (String) comboDomande.getSelectedItem();
            if (domanda == null) return;

            domandeFatteGiocatore.add(domanda);

            boolean risposta = rispondeDomanda(personaSegretaBot, domanda);
            JOptionPane.showMessageDialog(this, "Bot risponde: " + (risposta ? "Sì" : "No"), "Risposta del bot", JOptionPane.INFORMATION_MESSAGE);

            for (Persona p : persone) {
                if (rispondeDomanda(p, domanda) != risposta) {
                    abbattiCarta(p.getNome(), cartaPerNomeGiocatore);
                    if (!abbattuteGiocatore.contains(p.getNome())) abbattuteGiocatore.add(p.getNome());
                }
            }

            for (int i = comboDomande.getItemCount() - 1; i >= 0; i--) {
                String d = comboDomande.getItemAt(i);
                if (risposta && categoriaGiaConfermata(d, List.of(domanda))) comboDomande.removeItem(d);
            }
            comboDomande.removeItem(domanda);

            turno[0] = 2;
            aggiornaUI.run();
            eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot, turno, aggiornaUI, pannelloLaterale);
        });

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
                finePartita(pannelloLaterale);
            } else {
                JOptionPane.showMessageDialog(this, "Sbagliato! Tocca al bot.", "Risposta errata", JOptionPane.WARNING_MESSAGE);
                turno[0] = 2;
                aggiornaUI.run();
                eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot, turno, aggiornaUI, pannelloLaterale);
            }
        });

        // Se al momento del caricamento era il turno del bot, lo fa giocare subito
        if (turnoIniziale == 2) {
            aggiornaUI.run();
            eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot, turno, aggiornaUI, pannelloLaterale);
        } else {
            aggiornaUI.run();
        }

        revalidate();
        repaint();
    }

    //  TURNO BOT

    private void eseguiTurnoBot(Persona personaSegreta, Map<String, JPanel> cartaPerNomeBot,
                                List<String> abbattuteBot, int[] turno,
                                Runnable aggiornaUI, JPanel pannelloLaterale) {
        if (scelta == null) return;

        if (scelta.getPersona() != null) {
            String tentativo = scelta.getPersona().getNome();
            if (tentativo.equals(personaSegreta.getNome())) {
                JOptionPane.showMessageDialog(this, "Il bot ha indovinato! Hai perso.\nLa tua persona era: " + personaSegreta.getNome(), "Il bot ha vinto!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Il bot ha tentato con \"" + tentativo + "\" ma ha sbagliato!\nTocca a te.", "Bot sbagliato", JOptionPane.INFORMATION_MESSAGE);
            }
            finePartita(pannelloLaterale);
            return;
        }

        String domandaBot = scelta.getDomanda();
        int sceltaUtente = JOptionPane.showOptionDialog(this,
                "Il bot ti chiede:\n\"" + domandaBot + "\"",
                "Domanda del Bot", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new String[]{"Sì", "No"}, "Sì");
        if (sceltaUtente == JOptionPane.CLOSED_OPTION) return;

        boolean rispostaGiocatore = (sceltaUtente == 0);
        boolean rispostaCorretta  = rispondeDomanda(personaSegreta, domandaBot);

        while (rispostaGiocatore != rispostaCorretta) {
            JOptionPane.showMessageDialog(this, "Risposta sbagliata! Devi rispondere correttamente.", "Errore", JOptionPane.ERROR_MESSAGE);
            sceltaUtente = JOptionPane.showOptionDialog(this,
                    "Il bot ti chiede:\n\"" + domandaBot + "\"",
                    "Domanda del Bot", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new String[]{"Sì", "No"}, "Sì");
            if (sceltaUtente == JOptionPane.CLOSED_OPTION) return;
            rispostaGiocatore = (sceltaUtente == 0);
        }

        Nodo ramoScartato = rispostaGiocatore ? scelta.getNo() : scelta.getSi();
        List<String> daAbbattere = personeRaggiungibili(ramoScartato);
        for (String nome : daAbbattere) {
            abbattiCarta(nome, cartaPerNomeBot);
            if (!abbattuteBot.contains(nome)) abbattuteBot.add(nome);
        }

        scelta = rispostaGiocatore ? scelta.getSi() : scelta.getNo();

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

            JLabel immagine = new JLabel(persona.getImmagine(65, 80));
            immagine.setHorizontalAlignment(SwingConstants.CENTER);
            carta.add(immagine, BorderLayout.CENTER);


            cella.add(carta);
            griglia.add(cella);

            carta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    for (Component comp : griglia.getComponents()) {
                        JPanel pannelloCella = (JPanel) comp;
                        Component[] figli = pannelloCella.getComponents();
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
            if (sceltaP[0] == null) {
                JOptionPane.showMessageDialog(this, "Devi selezionare una persona prima di confermare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
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


            JLabel immagine = new JLabel(persona.getImmagine(65, 80));
            immagine.setHorizontalAlignment(SwingConstants.CENTER);
            carta.add(immagine, BorderLayout.CENTER);

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

    private void finePartita(JPanel pannelloLaterale) {
        pannelloLaterale.removeAll();

        JButton btnRigioca = creaBottoneLaterale("Rigioca", new Color(30, 100, 200));
        JButton btnMenu    = creaBottoneLaterale("Torna al menu principale", new Color(46, 160, 67));

        pannelloLaterale.add(Box.createVerticalGlue());
        pannelloLaterale.add(btnRigioca);
        pannelloLaterale.add(Box.createVerticalStrut(12));
        pannelloLaterale.add(btnMenu);
        pannelloLaterale.add(Box.createVerticalGlue());

        btnRigioca.addActionListener(_ -> {
            dispose();
            new SchermataGioco(persone, domandePossibili);
        });
        btnMenu.addActionListener(_ -> inizializzaMenu());

        pannelloLaterale.revalidate();
        pannelloLaterale.repaint();
    }

    private JPanel creaColonna(String titolo, JPanel griglia, boolean bordo) {
        JPanel colonna = new JPanel(new BorderLayout());
        colonna.setOpaque(false);
        if (bordo) colonna.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(60, 60, 60)));
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



    private boolean categoriaGiaConfermata(String domanda, List<String> domandeConfermate) {
        String[] capelli = {"ha i capelli castani?", "ha i capelli neri?", "ha i capelli biondi?", "ha i capelli rossi?", "ha i capelli bianchi?"};
        String[] occhi   = {"ha gli occhi marroni?", "ha gli occhi blu?", "ha gli occhi verdi?"};
        String[] pelle   = {"ha la pelle bianca?", "ha la pelle nera?", "ha la pelle mulatta?"};

        String[] categoria = null;
        for (String c : capelli) if (c.equals(domanda)) { categoria = capelli; break; }
        if (categoria == null) for (String c : occhi) if (c.equals(domanda)) { categoria = occhi; break; }
        if (categoria == null) for (String c : pelle) if (c.equals(domanda)) { categoria = pelle; break; }
        if (categoria == null) return false;

        for (String confermata : domandeConfermate)
            for (String c : categoria)
                if (c.equals(confermata)) return true;
        return false;
    }

    private List<String> personeRaggiungibili(Nodo n) {
        List<String> nomi = new ArrayList<>();
        raccogliPersone(n, nomi);
        return nomi;
    }

    private void raccogliPersone(Nodo n, List<String> nomi) {
        if (n == null) return;
        if (n.getPersona() != null) { nomi.add(n.getPersona().getNome()); return; }
        raccogliPersone(n.getSi(), nomi);
        raccogliPersone(n.getNo(), nomi);
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
