import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Pulsante personalizzato che estende {@link JButton} con uno stile grafico
 * predefinito e un effetto hover che cambia il colore di sfondo al passaggio
 * del mouse.
 */
public class JButtonCustom extends JButton {

    /**
     * Crea un nuovo {@code JButtonCustom} con testo, colore di sfondo e colore
     * hover specificati.
     * <p>
     * Il pulsante viene configurato con:
     * <ul>
     *   <li>Font Arial grassetto, dimensione 26</li>
     *   <li>Testo bianco</li>
     *   <li>Bordo e focus painting disabilitati</li>
     *   <li>Area contenuto riempita</li>
     *   <li>Dimensione preferita di 150×40 pixel</li>
     *   <li>Effetto hover: al passaggio del mouse lo sfondo diventa {@code hover},
     *       al ritiro torna a {@code sfondo}</li>
     * </ul>
     * </p>
     *
     * @param text   il testo da visualizzare sul pulsante
     * @param sfondo il colore di sfondo predefinito del pulsante
     * @param hover  il colore di sfondo quando il mouse è sopra il pulsante
     */
    public JButtonCustom(String text, Color sfondo, Color hover) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 26));
        setBackground(sfondo);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(true);

        addMouseListener(new MouseAdapter() {
            /**
             * Invocato quando il cursore del mouse entra nell'area del pulsante.
             * Cambia il colore di sfondo al colore {@code hover}.
             *
             * @param e l'evento del mouse
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hover);
            }

            /**
             * Invocato quando il cursore del mouse esce dall'area del pulsante.
             * Ripristina il colore di sfondo al colore {@code sfondo}.
             *
             * @param e l'evento del mouse
             */
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(sfondo);
            }
        });

        setPreferredSize(new Dimension(150, 40));
    }
}