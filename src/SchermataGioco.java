import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.Map;

/**
 * Classe principale per la gestione dell'interfaccia grafica del gioco "Indovina Chi".
 * Gestisce il menu principale, le modalità di gioco (persona vs persona e giocatore vs bot),
 * il salvataggio e il caricamento delle partite.
 */
public class SchermataGioco extends JFrame {
    private final List<Persona> persone;
    private final Domanda[] domandePossibili;
    private Bot bot;
    private boolean difficile;

    /**
     * Costruttore della schermata di gioco.
     * Inizializza la finestra principale e il menu di avvio.
     *
     * @param persone           lista di tutte le persone disponibili nel gioco
     * @param domandePossibili  array delle domande che possono essere poste durante il gioco
     * @throws IllegalArgumentException se domandePossibili è null o vuoto
     */
    public SchermataGioco(List<Persona> persone, Domanda[] domandePossibili) {
        if (domandePossibili == null || domandePossibili.length == 0) {
            throw new IllegalArgumentException("Le domande non possono essere null o vuote");
        }
        this.domandePossibili = domandePossibili;
        this.persone = persone;
        bot = null;
        difficile = false;

        setTitle("IndovinaChi");
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inizializzaMenu();
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    /**
     * Inizializza il menu principale con i pulsanti per le diverse modalità di gioco.
     * Crea l'interfaccia con sfondo personalizzato e quattro opzioni:
     * gioca in persona, gioca contro il computer, carica partita salvata ed esci.
     */
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

        JButton bottoneInPersona = new JButtonCustom("Gioca in persona", new Color(34, 139, 34),  new Color(60, 179, 60));
        JButton bottoneBot = new JButtonCustom("Gioca contro il computer", new Color(30, 144, 255), new Color(100, 180, 255));
        JButton bottoneCarica = new JButtonCustom("Carica partita salvata", new Color(160, 100, 0),  new Color(210, 140, 30));
        JButton bottoneEsci = new JButtonCustom("Esci", new Color(220, 20, 60),  new Color(255, 60, 80));

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        btnPanel.setPreferredSize(new Dimension(0, 180));
        btnPanel.add(bottoneInPersona);
        btnPanel.add(bottoneBot);
        btnPanel.add(bottoneCarica);
        btnPanel.add(bottoneEsci);
        panelSfondo.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(panelSfondo);

        bottoneInPersona.addActionListener(_ -> inizializzaInPersona());
        bottoneBot.addActionListener(_ -> {
            String[] opzioni = {"Normale", "Difficile"};
            int sceltaDiff = JOptionPane.showOptionDialog(null, "Scegli la difficoltà", "Difficoltà", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[0]);
            if (sceltaDiff == JOptionPane.CLOSED_OPTION) return;

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
                this.bot = bot;
                difficile = true;
                inizializzaBot();
            }

            else {
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
                this.bot = bot;
                difficile = true;
                inizializzaBot();
            }
        });

        bottoneCarica.addActionListener(_ -> caricaPartita());
        bottoneEsci.addActionListener(_ -> dispose());

        revalidate();
        repaint();
    }

    /**
     * Salva lo stato corrente di una partita in modalità persona vs persona.
     *
     * @param pg1      persona segreta del giocatore 1
     * @param pg2      persona segreta del giocatore 2
     * @param turno    numero del turno corrente (1 o 2)
     * @param abbG1    lista delle carte abbattute dal giocatore 1
     * @param abbG2    lista delle carte abbattute dal giocatore 2
     * @param dfG1     lista delle domande fatte dal giocatore 1
     * @param dfG2     lista delle domande fatte dal giocatore 2
     */
    private void salvaPartitaInPersona(Persona pg1, Persona pg2, int turno, List<String> abbG1, List<String> abbG2, List<String> dfG1, List<String> dfG2) {
        Object[] stato = {"persona", pg1.getNome(), pg2.getNome(), turno, abbG1.toArray(new String[0]), abbG2.toArray(new String[0]), dfG1.toArray(new String[0]), dfG2.toArray(new String[0])};

        try {
            Serializzatore.serializzaPartita(stato);
            JOptionPane.showMessageDialog(this, "Partita salvata!", "Salvataggio", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio:\n" + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Salva lo stato corrente di una partita in modalità giocatore vs bot.
     *
     * @param pgGiocatore      persona segreta del giocatore
     * @param pgBot            persona segreta del bot
     * @param turno            numero del turno corrente (1 per giocatore, 2 per bot)
     * @param abbGiocatore     lista delle carte abbattute dal giocatore
     * @param abbBot           lista delle carte abbattute dal bot
     * @param dfGiocatore      lista delle domande fatte dal giocatore
     * @param nodoCorrente     nodo corrente dell'albero decisionale del bot
     * @param difficile        true se la modalità è difficile, false altrimenti
     */
    private void salvaPartitaBot(Persona pgGiocatore, Persona pgBot, int turno, List<String> abbGiocatore, List<String> abbBot, List<String> dfGiocatore, Nodo nodoCorrente, boolean difficile) {
        Object[] stato = {"bot", pgGiocatore.getNome(), pgBot.getNome(), turno, abbGiocatore.toArray(new String[0]), abbBot.toArray(new String[0]), dfGiocatore.toArray(new String[0]), nodoCorrente, difficile};

        try {
            Serializzatore.serializzaPartita(stato);
            JOptionPane.showMessageDialog(this, "Partita salvata!", "Salvataggio", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio:\n" + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carica una partita salvata e ripristina lo stato del gioco.
     * Determina automaticamente se si tratta di una partita persona vs persona
     * o giocatore vs bot e chiama il metodo di ripristino appropriato.
     */
    private void caricaPartita() {
        Object[] stato;
        try {
            stato = Serializzatore.deSerializzaPartita();
        }
        catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Caricamento", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (stato == null || stato.length < 8) {
            JOptionPane.showMessageDialog(this, "File di salvataggio non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String modalita = (String) stato[0];
        if ("persona".equals(modalita)) ripristinaInPersona(stato);
        else if ("bot".equals(modalita)) ripristinaBot(stato);
    }

    /**
     * Ripristina una partita salvata in modalità persona vs persona.
     *
     * @param stato array contenente lo stato salvato della partita
     */
    private void ripristinaInPersona(Object[] stato) {
        String nomeG1 = (String) stato[1];
        String nomeG2 = (String) stato[2];
        int turnoIniziale = (Integer) stato[3];
        String[] abbG1Array = (String[]) stato[4];
        String[] abbG2Array = (String[]) stato[5];
        String[] dfG1Array = (String[]) stato[6];
        String[] dfG2Array = (String[]) stato[7];

        Persona pg1 = cercaPersona(nomeG1);
        Persona pg2 = cercaPersona(nomeG2);
        if (pg1 == null || pg2 == null) {
            JOptionPane.showMessageDialog(this, "Personaggi del salvataggio non trovati.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        avviaPartitaInPersona(pg1, pg2, turnoIniziale, new ArrayList<>(Arrays.asList(abbG1Array)), new ArrayList<>(Arrays.asList(abbG2Array)), new ArrayList<>(Arrays.asList(dfG1Array)), new ArrayList<>(Arrays.asList(dfG2Array)));
    }

    /**
     * Ripristina una partita salvata in modalità giocatore vs bot.
     *
     * @param stato array contenente lo stato salvato della partita
     */
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
        Nodo nodoCorrente = (Nodo) stato[7];
        boolean difficile = (Boolean) stato[8];

        Persona pgGiocatore = cercaPersona(nomeGiocatore);
        Persona pgBot = cercaPersona(nomeBot);
        if (pgGiocatore == null || pgBot == null) {
            JOptionPane.showMessageDialog(this, "Personaggi del salvataggio non trovati.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Bot bot;
        try {
            String percorso = "files/bot.ser";
            if (difficile) percorso = "files/botDifficile.ser";
            bot = Serializzatore.deSerializzaBot(percorso);
        }
        catch (RuntimeException _) {
            bot = null; // rigioca disabilitato se il file non è disponibile
        }

        avviaPartitaBot(pgGiocatore, pgBot, turnoIniziale, new ArrayList<>(Arrays.asList(abbattuteGiocatore)), new ArrayList<>(Arrays.asList(abbattuteBot)), new ArrayList<>(Arrays.asList(domandeFatteGiocatore)), nodoCorrente, bot, difficile);
    }

    /**
     * Cerca una persona nella lista delle persone disponibili per nome.
     *
     * @param nome nome della persona da cercare
     * @return la Persona corrispondente, null se non trovata
     */
    private Persona cercaPersona(String nome) {
        for (Persona p : persone) {
            if (p.getNome().equals(nome)) return p;
        }
        return null;
    }

    // -----------------------------------------------------------------
    //  MODALITÀ IN PERSONA
    // -----------------------------------------------------------------

    /**
     * Inizializza una nuova partita in modalità persona vs persona.
     * Chiede ai due giocatori di scegliere le loro persone segrete
     * e avvia la partita.
     */
    private void inizializzaInPersona() {
        Persona personaSegretaG1 = mostraSceltaPersona(1);
        if (personaSegretaG1 == null) return;
        Persona personaSegretaG2 = mostraSceltaPersona(2);
        if (personaSegretaG2 == null) return;

        avviaPartitaInPersona(personaSegretaG1, personaSegretaG2, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Avvia o riprende una partita in modalità persona vs persona.
     * Gestisce il turno dei giocatori, le domande, le risposte e le carte abbattute.
     *
     * @param personaSegretaG1  persona segreta del giocatore 1
     * @param personaSegretaG2  persona segreta del giocatore 2
     * @param turnoIniziale     turno iniziale (1 o 2)
     * @param abbattuteG1       lista delle carte già abbattute dal giocatore 1
     * @param abbattuteG2       lista delle carte già abbattute dal giocatore 2
     * @param domandeFatteG1    lista delle domande già fatte dal giocatore 1
     * @param domandeFatteG2    lista delle domande già fatte dal giocatore 2
     */
    private void avviaPartitaInPersona(Persona personaSegretaG1, Persona personaSegretaG2, int turnoIniziale, List<String> abbattuteG1, List<String> abbattuteG2, List<String> domandeFatteG1, List<String> domandeFatteG2) {
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

        JButton btnFaiDomanda = creaBottoneLaterale("Fai domanda", new Color(30, 100, 200));
        JButton btnIndovina = creaBottoneLaterale("Tenta di indovinare!", new Color(46, 160, 67));
        JButton btnSalva = creaBottoneLaterale("Salva partita", new Color(160, 100, 0));

        pannelloControlli.add(btnIndovina);
        pannelloControlli.add(Box.createVerticalStrut(10));
        pannelloControlli.add(comboDomande);
        pannelloControlli.add(Box.createVerticalStrut(8));
        pannelloControlli.add(btnFaiDomanda);
        pannelloControlli.add(Box.createVerticalStrut(10));
        pannelloControlli.add(btnSalva);

        pannelloLaterale.add(headerLaterale, BorderLayout.NORTH);
        pannelloLaterale.add(pannelloControlli, BorderLayout.CENTER);

        pannelloPrincipale.add(pannelloCentrale, BorderLayout.CENTER);
        pannelloPrincipale.add(pannelloLaterale, BorderLayout.EAST);

        aggiornaCombo(turno[0], domandeFatteG1, risposteG1, domandeFatteG2, risposteG2, comboDomande);

        btnSalva.addActionListener(_ -> salvaPartitaInPersona(personaSegretaG1, personaSegretaG2, turno[0], abbattuteG1, abbattuteG2, domandeFatteG1, domandeFatteG2));

        btnIndovina.addActionListener(_ -> {
            int turnoCorrente = turno[0];
            String input = JOptionPane.showInputDialog(this, "Giocatore " + turnoCorrente + ", inserisci il nome della persona:", "Tenta di indovinare", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;

            String nomeInput = input.trim().toLowerCase();
            boolean esiste = false;
            for (Persona p : persone) {
                if (p.getNome().equals(nomeInput)) {
                    esiste = true;
                    break;
                }
            }
            if (!esiste) {
                JOptionPane.showMessageDialog(this, "\"" + nomeInput + "\" non è un personaggio valido.\nControlla l'ortografia e riprova.", "Nome non valido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Persona segreta = (turnoCorrente == 1) ? personaSegretaG2 : personaSegretaG1;
            if (segreta.getNome().equals(nomeInput)) {
                JOptionPane.showMessageDialog(this, "Giocatore " + turnoCorrente + " ha vinto!\nLa persona era: " + segreta.getNome(), "Hai vinto!", JOptionPane.INFORMATION_MESSAGE);
                finePartita(pannelloLaterale, false);
            }
            else {
                if (turnoCorrente == 1) turno[0] = 2;
                else turno[0] = 1;
                labelTurno.setText("Turno: Giocatore " + turno[0]);
                headerLaterale.setBackground(new Color(30, 100, 200));
                aggiornaCombo(turno[0], domandeFatteG1, risposteG1, domandeFatteG2, risposteG2, comboDomande);
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
                risposteCorrenti = risposteG1;
            }
            else {
                domandeFatteCorrente = domandeFatteG2;
                risposteCorrenti = risposteG2;
            }

            Domanda domanda = Domanda.fromTesto(testoDomanda);

            int avversario;
            if (turnoCorrente == 1) avversario = 2;
            else avversario = 1;

            Persona segretaAvversario;
            if (avversario == 1) segretaAvversario = personaSegretaG1;
            else segretaAvversario = personaSegretaG2;

            boolean rispostaCorretta = domanda.corrisponde(segretaAvversario);
            boolean rispostaUtente;

            do {
                int sc = JOptionPane.showOptionDialog(this, "Giocatore " + avversario + ", rispondi alla domanda:\n\"" + testoDomanda + "\"", "Rispondi", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sì", "No"}, "Sì");
                if (sc == JOptionPane.CLOSED_OPTION) return;
                rispostaUtente = (sc == 0);
                if (rispostaUtente != rispostaCorretta) {
                    JOptionPane.showMessageDialog(this, "Risposta sbagliata! Devi rispondere correttamente.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } while (rispostaUtente != rispostaCorretta);

            domandeFatteCorrente.add(testoDomanda);
            risposteCorrenti.add(rispostaCorretta);

            Map<String, JPanel> cartaDaAggiornare;
            List<String> abbattuteDaAggiornare;

            if (turnoCorrente == 1) {
                cartaDaAggiornare = cartaPerNomeG1;
                abbattuteDaAggiornare = abbattuteG1;
            }
            else {
                cartaDaAggiornare = cartaPerNomeG2;
                abbattuteDaAggiornare = abbattuteG2;
            }
            for (Persona p : persone) {
                if (domanda.corrisponde(p) != rispostaCorretta) {
                    abbattiCarta(p.getNome(), cartaDaAggiornare);
                    if (!abbattuteDaAggiornare.contains(p.getNome())) abbattuteDaAggiornare.add(p.getNome());
                }
            }

            turno[0] = avversario;
            labelTurno.setText("Turno: Giocatore " + turno[0]);
            aggiornaCombo(turno[0], domandeFatteG1, risposteG1, domandeFatteG2, risposteG2, comboDomande);
        });

        revalidate();
        repaint();
    }

    /**
     * Aggiorna la combo box con le domande disponibili per il giocatore corrente.
     * Esclude le domande già fatte e quelle la cui categoria è stata confermata.
     *
     * @param turno             numero del giocatore corrente (1 o 2)
     * @param domandeFatteG1    domande già fatte dal giocatore 1
     * @param risposteG1        risposte ricevute dal giocatore 1
     * @param domandeFatteG2    domande già fatte dal giocatore 2
     * @param risposteG2        risposte ricevute dal giocatore 2
     * @param comboDomande      combo box da aggiornare
     */
    private void aggiornaCombo(int turno, List<String> domandeFatteG1, List<Boolean> risposteG1, List<String> domandeFatteG2, List<Boolean> risposteG2, JComboBox<String> comboDomande) {
        List<String> fatte;
        List<Boolean> risposte;

        if (turno == 1) {
            fatte = domandeFatteG1;
            risposte = risposteG1;
        }
        else {
            fatte = domandeFatteG2;
            risposte = risposteG2;
        }

        comboDomande.removeAllItems();
        for (Domanda d : domandePossibili) {
            if (!fatte.contains(d.getTesto()) && !categoriaConfermata(d, fatte, risposte)) {
                comboDomande.addItem(d.getTesto());
            }
        }
    }

    /**
     * Inizializza una nuova partita in modalità giocatore vs bot.
     * Chiede al giocatore di scegliere la propria persona segreta,
     * sceglie casualmente quella del bot e avvia la partita.
     */
    private void inizializzaBot() {
        Persona personaSegreta = mostraSceltaPersona(1);
        if (personaSegreta == null) return;
        Persona personaSegretaBot = persone.get(new Random().nextInt(persone.size()));
        avviaPartitaBot(personaSegreta, personaSegretaBot, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), bot.getRoot(), bot, difficile);
    }

    /**
     * Avvia o riprende una partita in modalità giocatore vs bot.
     * Gestisce i turni, le domande del giocatore e le mosse del bot.
     *
     * @param personaSegreta        persona segreta del giocatore
     * @param personaSegretaBot     persona segreta del bot
     * @param turnoIniziale         turno iniziale (1 per giocatore, 2 per bot)
     * @param abbattuteGiocatore    carte già abbattute dal giocatore
     * @param abbattuteBot          carte già abbattute dal bot
     * @param domandeFatteGiocatore domande già fatte dal giocatore
     * @param nodoIniziale          nodo iniziale dell'albero decisionale del bot
     * @param bot                   istanza del bot (può essere null)
     * @param difficile             true se la modalità è difficile
     */
    private void avviaPartitaBot(Persona personaSegreta, Persona personaSegretaBot, int turnoIniziale, List<String> abbattuteGiocatore, List<String> abbattuteBot, List<String> domandeFatteGiocatore, Nodo nodoIniziale, Bot bot, boolean difficile) {
        getContentPane().removeAll();
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(158, 26, 14));
        setContentPane(root);

        Nodo[] sceltaCorrente = {nodoIniziale};
        List<Boolean> risposteGiocatore = new ArrayList<>();
        int[] turno = {turnoIniziale};

        Map<String, JPanel> cartaPerNomeGiocatore = new HashMap<>();
        Map<String, JPanel> cartaPerNomeBot2      = new HashMap<>();

        JPanel colonnaGiocatore = creaColonna("Giocatore", creaGriglia(cartaPerNomeGiocatore), true);
        JPanel colonnaBot = creaColonna("Bot", creaGriglia(cartaPerNomeBot2),false);

        JPanel pannelloCentrale = new JPanel(new GridLayout(1, 2, 0, 0));
        pannelloCentrale.setOpaque(false);
        pannelloCentrale.add(colonnaGiocatore);
        pannelloCentrale.add(colonnaBot);

        for (String nome : abbattuteGiocatore) abbattiCarta(nome, cartaPerNomeGiocatore);
        for (String nome : abbattuteBot) abbattiCarta(nome, cartaPerNomeBot2);

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

        JButton btnFaiDomanda = creaBottoneLaterale("Fai domanda", new Color(30, 100, 200));
        JButton btnIndovina = creaBottoneLaterale("Tenta di indovinare!", new Color(46, 160, 67));
        JButton btnSalva = creaBottoneLaterale("Salva partita", new Color(160, 100, 0));

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

        pannelloLaterale.add(headerLaterale, BorderLayout.NORTH);
        pannelloLaterale.add(pannelloControlli, BorderLayout.CENTER);

        root.add(pannelloCentrale, BorderLayout.CENTER);
        root.add(pannelloLaterale, BorderLayout.EAST);

        comboDomande.removeAllItems();
        for (Domanda d : domandePossibili) {
            if (!domandeFatteGiocatore.contains(d.getTesto()) && !categoriaConfermata(d, domandeFatteGiocatore, risposteGiocatore)) comboDomande.addItem(d.getTesto());
        }

        aggiornaUI(turno[0], comboDomande, btnFaiDomanda, btnIndovina, btnSalva, labelTurno, headerLaterale);

        btnSalva.addActionListener(_ -> salvaPartitaBot(personaSegreta, personaSegretaBot, turno[0], abbattuteGiocatore, abbattuteBot, domandeFatteGiocatore, sceltaCorrente[0], difficile));

        btnFaiDomanda.addActionListener(_ -> {
            String testoDomanda = (String) comboDomande.getSelectedItem();
            if (testoDomanda == null) return;

            Domanda domanda  = Domanda.fromTesto(testoDomanda);
            boolean risposta = domanda.corrisponde(personaSegretaBot);
            JOptionPane.showMessageDialog(this, "Bot risponde: " + (risposta ? "Sì" : "No"), "Risposta del bot", JOptionPane.INFORMATION_MESSAGE);

            domandeFatteGiocatore.add(testoDomanda);
            risposteGiocatore.add(risposta);

            for (Persona p : persone) {
                if (domanda.corrisponde(p) != risposta) {
                    abbattiCarta(p.getNome(), cartaPerNomeGiocatore);
                    if (!abbattuteGiocatore.contains(p.getNome())) abbattuteGiocatore.add(p.getNome());
                }
            }

            comboDomande.removeAllItems();
            for (Domanda d : domandePossibili) {
                if (!domandeFatteGiocatore.contains(d.getTesto()) && !categoriaConfermata(d, domandeFatteGiocatore, risposteGiocatore)) comboDomande.addItem(d.getTesto());
            }
            turno[0] = 2;
            aggiornaUI(turno[0], comboDomande, btnFaiDomanda, btnIndovina, btnSalva, labelTurno, headerLaterale);
            eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot, turno, sceltaCorrente, pannelloLaterale);
            aggiornaUI(turno[0], comboDomande, btnFaiDomanda, btnIndovina, btnSalva, labelTurno, headerLaterale);
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
                finePartita(pannelloLaterale, true);
            }
            else {
                JOptionPane.showMessageDialog(this, "Sbagliato! Tocca al bot.", "Risposta errata", JOptionPane.WARNING_MESSAGE);
                turno[0] = 2;
                aggiornaUI(turno[0], comboDomande, btnFaiDomanda, btnIndovina, btnSalva, labelTurno, headerLaterale);
                eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot, turno, sceltaCorrente, pannelloLaterale);
                aggiornaUI(turno[0], comboDomande, btnFaiDomanda, btnIndovina, btnSalva, labelTurno, headerLaterale);
            }
        });

        if (turnoIniziale == 2) {
            aggiornaUI(turno[0], comboDomande, btnFaiDomanda, btnIndovina, btnSalva, labelTurno, headerLaterale);
            eseguiTurnoBot(personaSegreta, cartaPerNomeBot2, abbattuteBot, turno, sceltaCorrente, pannelloLaterale);
        }
        aggiornaUI(turno[0], comboDomande, btnFaiDomanda, btnIndovina, btnSalva, labelTurno, headerLaterale);

        revalidate();
        repaint();
    }

    /**
     * Aggiorna lo stato dell'interfaccia utente in base al turno corrente.
     * Abilita o disabilita i controlli e aggiorna il colore dell'header.
     *
     * @param turno             numero del turno (1 per giocatore, 2 per bot)
     * @param comboDomande      combo box delle domande
     * @param btnFaiDomanda     pulsante per fare una domanda
     * @param btnIndovina       pulsante per tentare di indovinare
     * @param btnSalva          pulsante per salvare la partita
     * @param labelTurno        etichetta che mostra di chi è il turno
     * @param headerLaterale    pannello header da colorare
     */
    private void aggiornaUI(int turno, JComboBox<String> comboDomande, JButton btnFaiDomanda, JButton btnIndovina, JButton btnSalva, JLabel labelTurno, JPanel headerLaterale) {
        boolean turnoGiocatore = (turno == 1);
        comboDomande.setEnabled(turnoGiocatore);
        btnFaiDomanda.setEnabled(turnoGiocatore);
        btnIndovina.setEnabled(turnoGiocatore);
        btnSalva.setEnabled(turnoGiocatore);
        if (turnoGiocatore) labelTurno.setText("Turno: Giocatore");
        else labelTurno.setText("Turno: Bot");
        headerLaterale.setBackground(turnoGiocatore ? new Color(30, 100, 200) : new Color(180, 60, 60));
    }

    /**
     * Esegue il turno del bot nella partita giocatore vs bot.
     * Il bot può fare una domanda o tentare di indovinare.
     *
     * @param personaSegreta      persona segreta del giocatore
     * @param cartaPerNomeBot     mappa delle carte del bot
     * @param abbattuteBot        lista delle carte abbattute dal bot
     * @param turno               array contenente il numero del turno corrente
     * @param sceltaCorrente      array contenente il nodo corrente dell'albero decisionale
     * @param pannelloLaterale    pannello laterale da modificare in caso di fine partita
     */
    private void eseguiTurnoBot(Persona personaSegreta, Map<String, JPanel> cartaPerNomeBot, List<String> abbattuteBot, int[] turno, Nodo[] sceltaCorrente, JPanel pannelloLaterale) {
        if (sceltaCorrente[0] == null) return;

        if (sceltaCorrente[0] instanceof NodoPersona np) {
            String tentativo = np.getPersona().getNome();
            if (tentativo.equals(personaSegreta.getNome())) JOptionPane.showMessageDialog(this, "Il bot ha indovinato! Hai perso.\nLa tua persona era: " + personaSegreta.getNome(), "Il bot ha vinto!", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this, "Il bot ha tentato con \"" + tentativo + "\" ma ha sbagliato!\nTocca a te.", "Bot sbagliato", JOptionPane.INFORMATION_MESSAGE);
            finePartita(pannelloLaterale, true);
            return;
        }

        if (!(sceltaCorrente[0] instanceof NodoDomanda nd)) return;
        String  domandaBot = nd.getDomanda();
        Domanda domanda = Domanda.fromTesto(domandaBot);
        boolean rispostaCorretta = domanda.corrisponde(personaSegreta);
        boolean rispostaGiocatore;

        do {
            int sc = JOptionPane.showOptionDialog(this, "Il bot ti chiede:\n\"" + domandaBot + "\"", "Domanda del Bot", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sì", "No"}, "Sì");
            if (sc == JOptionPane.CLOSED_OPTION) return;
            rispostaGiocatore = (sc == 0);
            if (rispostaGiocatore != rispostaCorretta) JOptionPane.showMessageDialog(this, "Risposta sbagliata! Devi rispondere correttamente.", "Errore", JOptionPane.ERROR_MESSAGE);
        } while (rispostaGiocatore != rispostaCorretta);

        Nodo ramoScartato;
        if (rispostaGiocatore) ramoScartato = sceltaCorrente[0].getNo();
        else ramoScartato = sceltaCorrente[0].getSi();

        for (String nome : personeRaggiungibili(ramoScartato)) {
            abbattiCarta(nome, cartaPerNomeBot);
            if (!abbattuteBot.contains(nome)) abbattuteBot.add(nome);
        }

        if (rispostaGiocatore) sceltaCorrente[0] = sceltaCorrente[0].getSi();
        else sceltaCorrente[0] = sceltaCorrente[0].getNo();
        turno[0] = 1;
    }

    /**
     * Mostra una finestra di dialogo per permettere al giocatore di scegliere
     * la propria persona segreta.
     *
     * @param numeroGiocatore numero del giocatore (1 o 2)
     * @return la Persona scelta, null se il giocatore annulla la selezione
     */
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
            int risultato = JOptionPane.showConfirmDialog(this, griglia, "Giocatore " + numeroGiocatore + " - scegli la tua persona segreta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (risultato != JOptionPane.OK_OPTION) return null;
            if (sceltaP[0] == null) JOptionPane.showMessageDialog(this, "Devi selezionare una persona prima di confermare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
        return sceltaP[0];
    }

    /**
     * Crea una griglia di carte rappresentanti tutte le persone del gioco.
     * Ogni carta mostra l'immagine della persona e il tooltip con le sue caratteristiche.
     *
     * @param cartaPerNome mappa che associa il nome della persona al suo pannello carta
     *                     (può essere null se non serve mantenere i riferimenti)
     * @return il pannello contenente la griglia di carte
     */
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

    /**
     * Abbatte una carta nella griglia, rimuovendo l'immagine e colorandola di giallo.
     *
     * @param nome           nome della persona la cui carta deve essere abbattuta
     * @param cartaPerNome   mappa che associa i nomi delle persone ai pannelli carta
     */
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

    /**
     * Gestisce la fine della partita mostrando le opzioni per rigiocare
     * o tornare al menu principale.
     *
     * @param pannelloLaterale pannello laterale da modificare con i nuovi pulsanti
     * @param bot              true se la partita era contro il bot, false se era persona vs persona
     */
    private void finePartita(JPanel pannelloLaterale, boolean bot) {
        pannelloLaterale.removeAll();
        pannelloLaterale.setLayout(new BoxLayout(pannelloLaterale, BoxLayout.Y_AXIS));

        JButton btnRigioca = creaBottoneLaterale("Rigioca", new Color(30, 100, 200));
        JButton btnMenu = creaBottoneLaterale("Torna al menu principale", new Color(46, 160, 67));

        if (bot) btnRigioca.addActionListener(_ -> inizializzaBot());
        else btnRigioca.addActionListener(_ -> inizializzaInPersona());
        btnMenu.addActionListener(_ -> inizializzaMenu());

        pannelloLaterale.add(Box.createVerticalGlue());
        pannelloLaterale.add(btnRigioca);
        pannelloLaterale.add(Box.createVerticalStrut(12));
        pannelloLaterale.add(btnMenu);
        pannelloLaterale.add(Box.createVerticalGlue());

        pannelloLaterale.revalidate();
        pannelloLaterale.repaint();
    }

    /**
     * Crea una colonna con titolo e griglia di carte.
     *
     * @param titolo   titolo della colonna (es. "Giocatore 1")
     * @param griglia  pannello contenente la griglia di carte
     * @param bordo    true se aggiungere un bordo destro alla colonna
     * @return il pannello colonna completo
     */
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

    /**
     * Crea un pulsante stilizzato per il pannello laterale.
     *
     * @param testo   testo da visualizzare sul pulsante
     * @param sfondo  colore di sfondo del pulsante
     * @return il pulsante creato
     */
    private JButton creaBottoneLaterale(String testo, Color sfondo) {
        JButton btn = new JButton(testo);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(sfondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(200, 40));
        return btn;
    }

    /**
     * Verifica se una categoria di domanda è stata confermata (ha ricevuto risposta positiva).
     * Una categoria è confermata solo se una domanda di quella categoria ha ricevuto risposta "sì".
     *
     * @param domanda   la domanda da verificare
     * @param fatte     lista delle domande già fatte
     * @param risposte  lista delle risposte ricevute
     * @return true se la categoria è confermata, false altrimenti
     */
    private boolean categoriaConfermata(Domanda domanda, List<String> fatte, List<Boolean> risposte) {
        if (domanda.getCategoria() == Domanda.Categoria.NESSUNA) return false;
        for (int i = 0; i < fatte.size(); i++) {
            Domanda df = Domanda.fromTesto(fatte.get(i));
            if (df.getCategoria() == domanda.getCategoria() && i < risposte.size() && risposte.get(i)) return true;
        }
        return false;
    }

    /**
     * Raccoglie i nomi di tutte le persone raggiungibili a partire da un nodo dell'albero.
     *
     * @param n nodo da cui partire
     * @return lista dei nomi delle persone raggiungibili
     */
    private List<String> personeRaggiungibili(Nodo n) {
        List<String> nomi = new ArrayList<>();
        raccogliPersone(n, nomi);
        return nomi;
    }

    /**
     * Metodo ricorsivo per raccogliere i nomi delle persone da un albero di nodi.
     *
     * @param n     nodo corrente
     * @param nomi  lista in cui aggiungere i nomi trovati
     */
    private void raccogliPersone(Nodo n, List<String> nomi) {
        if (n == null) return;
        if (n instanceof NodoPersona np) {
            nomi.add(np.getPersona().getNome());
            return;
        }
        raccogliPersone(n.getSi(), nomi);
        raccogliPersone(n.getNo(), nomi);
    }

    /**
     * Carica e scala un'immagine dal file system.
     *
     * @param nome       nome della persona (usato per costruire il percorso del file)
     * @param larghezza  larghezza desiderata dell'immagine scalata
     * @param altezza    altezza desiderata dell'immagine scalata
     * @return ImageIcon contenente l'immagine scalata
     * @throws IllegalArgumentException se il percorso dell'immagine non esiste
     */
    private ImageIcon getImmagine(String nome, int larghezza, int altezza) {
        String percorso = "img/" + nome.trim().toLowerCase() + ".png";
        Path path = Paths.get(percorso);
        if (!Files.exists(path)) throw new IllegalArgumentException("Percorso non trovato: " + path);
        Image scalata = new ImageIcon(percorso).getImage().getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
        return new ImageIcon(scalata);
    }
}