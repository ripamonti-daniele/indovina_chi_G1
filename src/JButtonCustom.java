import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JButtonCustom extends JButton {
    public JButtonCustom(String text, Color sfondo, Color hover) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 26));
        setBackground(sfondo);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(sfondo);
            }
        });

        setPreferredSize(new Dimension(150, 40));
    }

}