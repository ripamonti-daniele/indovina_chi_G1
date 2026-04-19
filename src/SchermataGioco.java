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
        JButton bottone3 = new JButtonCustom("Esci", new Color(220, 20, 60), new Color(255, 60, 80));

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        btnPanel.setPreferredSize(new Dimension(0, 180));
        btnPanel.add(bottone1);
        btnPanel.add(bottone2);
        btnPanel.add(bottone3);
        panelSfondo.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(panelSfondo);

        bottone1.addActionListener(_ -> inizializzaInPersona());
        bottone2.addActionListener(_ -> {
            String[] opzioni = {"Normale", "Difficile"};
            int scelta = JOptionPane.showOptionDialog(null, "Scegli la difficoltà", "Difficoltà", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[0]);
            Bot bot;
            if (scelta == 0) {
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
                inizializzaBot(bot);
            }

            else if (scelta == 1) {
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
                inizializzaBot(bot);
            }
        });

        bottone3.addActionListener(_ -> dispose());

        revalidate();
        repaint();
    }

    private void inizializzaInPersona() {

        Persona personaSegretaG1 = mostraSceltaPersona(1);
        if (personaSegretaG1 == null) {
            return;
        }

        Persona personaSegretaG2 = mostraSceltaPersona(2);
        if (personaSegretaG2 == null) {
            return;
        }

        //array per tenere conto del turno
        final int[] turno = {1};
        List<String> domandeFatteG1 = new ArrayList<>();
        List<String> domandeFatteG2 = new ArrayList<>();

        //Creo i 2 dizionari perche poi cosi posso modificare l'immagine della persona (abbassando la casella)
        Map<String, JPanel> cartaPerNomeG1 = new HashMap<>();
        Map<String, JPanel> cartaPerNomeG2 = new HashMap<>();


        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(new Color(158, 26, 14));
        setContentPane(pannelloPrincipale);

        //colonna sinistra
        JPanel colonnaG1 = creaColonna("Giocatore 1", creaGriglia(cartaPerNomeG1), true);
        JPanel colonnaG2 = creaColonna("Giocatore 2", creaGriglia(cartaPerNomeG2), false);

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

        JButton btnFaiDomanda = creaBottoneLaterale("Fai domanda", new Color(30, 100, 200));
        JButton btnIndovina   = creaBottoneLaterale("Tenta di indovinare!", new Color(46, 160, 67));

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
                finePartita(pannelloLaterale);

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
            int turnoCorrente = turno[0];
            if (turnoCorrente == 1) domandeFatteG1.add(domanda);
            else if (turnoCorrente == 2) domandeFatteG2.add(domanda);

            // calcola chi deve rispondere (l'avversario)
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

            List<String> domandeGiocatore = domandeFatteG1;
            if (turnoCorrente == 1) domandeGiocatore = domandeFatteG2;

            comboDomande.removeAllItems();
            // qui puoi usare categoriaGiaConfermata
            for (String d : domandePossibili) {
                if (!domandeGiocatore.contains(d) && !(rispostaCorretta && categoriaGiaConfermata(d, domandeGiocatore))) {
                    comboDomande.addItem(d);
                }
            }
//            comboDomande.removeItem(domanda);

            // passa il turno all'avversario
            turno[0] = avversario;
            labelTurno.setText("Turno: Giocatore " + turno[0]);
        });
        repaint();
    }

    private Persona mostraSceltaPersona(int numeroGiocatore) {
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
                        if (figli.length > 0 && figli[0] instanceof JPanel cartaInterna) {
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
            carta.setToolTipText(creaStringaToolTip(persona));

            JLabel immagine = new JLabel(persona.getImmagine(65, 80));
            immagine.setHorizontalAlignment(SwingConstants.CENTER);
            carta.add(immagine, BorderLayout.CENTER);

            if (cartaPerNome != null) cartaPerNome.put(persona.getNome(), carta);

            cella.add(carta);
            griglia.add(cella);
        }

        return griglia;
    }

    private void inizializzaBot(Bot bot) {
        Persona personaSegreta = mostraSceltaPersona(1);
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


        JPanel colonnaGiocatore = creaColonna("Giocatore", creaGriglia(cartaPerNomeGiocatore), true);
        JPanel colonnaBot = creaColonna("Bot", creaGriglia(cartaPerNomeBot), false);

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

        JButton btnFaiDomanda = creaBottoneLaterale("Fai domanda", new Color(30, 100, 200));
        JButton btnIndovina   = creaBottoneLaterale("Tenta di indovinare!", new Color(46, 160, 67));

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
        scelta = bot.getRoot();

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

            // Rimuove le domande della stessa categoria
            for (int i = comboDomande.getItemCount() - 1; i >= 0; i--) {
                String d = comboDomande.getItemAt(i);
                if (risposta && categoriaGiaConfermata(d, List.of(domanda))) {
                    comboDomande.removeItem(d);
                }
            }

            // Rimuove la domanda appena fatta
            comboDomande.removeItem(domanda);

            // Turno del bot
            turno[0] = 2;
            aggiornaUI.run();
            eseguiTurnoBot(personaSegreta, cartaPerNomeBot, turno, aggiornaUI, pannelloLaterale);
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
                finePartita(pannelloLaterale);

            } else {
                JOptionPane.showMessageDialog(this, "Sbagliato! Tocca al bot.", "Risposta errata", JOptionPane.WARNING_MESSAGE);
                turno[0] = 2;
                aggiornaUI.run();
                eseguiTurnoBot(personaSegreta, cartaPerNomeBot, turno, aggiornaUI, pannelloLaterale);
            }
        });

        aggiornaUI.run();
        revalidate();
        repaint();
    }

    // Turno del bot: usa l'albero per fare la domanda, il giocatore risponde
    private void eseguiTurnoBot(Persona personaSegreta, Map<String, JPanel> cartaPerNomeBot, int[] turno, Runnable aggiornaUI, JPanel pannelloLaterale) {
        if (scelta == null) return;

        // Il bot ha trovato la persona
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

    private boolean categoriaGiaConfermata(String domanda, List<String> domandeConfermate) {
        //questo array ci permette di eliminare tutte le domande simili a quella a cui abbiamo già risposto
        //esempio: se il bot chiede se ha i capelli rossi e rispondo di si, non mi farà domande inerenti ai capelli
        String[] capelli = {"ha i capelli castani?", "ha i capelli neri?", "ha i capelli biondi?", "ha i capelli rossi?", "ha i capelli bianchi?"};
        String[] occhi   = {"ha gli occhi marroni?", "ha gli occhi blu?", "ha gli occhi verdi?"};
        String[] pelle   = {"ha la pelle bianca?", "ha la pelle nera?", "ha la pelle mulatta?"};

        String[] categoria = null;
        for (String c : capelli)
            if (c.equals(domanda)) {
                categoria = capelli; break;
            }

        if (categoria == null){
            for (String c : occhi)
                if (c.equals(domanda)) {
                    categoria = occhi; break;
                }
        }

        if (categoria == null)
            for (String c : pelle) if (c.equals(domanda)) { categoria = pelle; break; }

        //esempio: categoria calvi, occhiali ecc...
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

    private void finePartita(JPanel pannelloLaterale) {
        pannelloLaterale.removeAll();

        JButton btnRigioca = creaBottoneLaterale("Rigioca", new Color(30, 100, 200));
        JButton btnMenu = creaBottoneLaterale("torna al menu principale", new Color(46, 160, 67));

        pannelloLaterale.add(Box.createVerticalGlue());
        pannelloLaterale.add(btnRigioca);
        pannelloLaterale.add(Box.createVerticalStrut(12));
        pannelloLaterale.add(btnMenu);
        pannelloLaterale.add(Box.createVerticalGlue());

        btnRigioca.addActionListener(_ -> {
            dispose();
            new SchermataGioco(persone, domandePossibili);
        });
        btnMenu.addActionListener(_ -> inizializzaMenu());  // <-- una sola riga

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