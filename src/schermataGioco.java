import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class schermataGioco extends JFrame {

    private JPanel pannelloDomanda;
    private JPanel pannelloGriglia;
    private JPanel pannelloBottoni;

    private JButton si;
   private JButton no;

   private List<Persona> persone;
   private List<JPanel> carte = new ArrayList<>();

  private Nodo Scelta;

   public schermataGioco(Albero albero, List<Persona> persone){

      this.persone = persone;

        setTitle("IndovinaChi");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        pannelloDomanda = new JPanel();
        pannelloDomanda.add(new JLabel());
 }
}
