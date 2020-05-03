package profiscendum.components;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class IndependentBlock extends JPanel {
    private static final long serialVersionUID = 1L;

    public IndependentBlock() {
        setLayout(new BorderLayout());
        add(new Block());
    }

}