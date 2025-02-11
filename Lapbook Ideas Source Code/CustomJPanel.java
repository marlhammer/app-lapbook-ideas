import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class CustomJPanel extends JPanel
    {
    static final Color defaultColor = new Color (71, 74, 190);

    CustomJPanel ()
        {
        setBackground (defaultColor);
        setBorder (new BevelBorder (BevelBorder.RAISED));
        }
    }