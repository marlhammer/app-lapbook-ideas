import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class CustomDisplay extends CustomJPanel
    {
    private JTextArea display;

    CustomDisplay ()
        {
        setLayout (new GridLayout (1, 1));

        display = new JTextArea (3, 6);
            JScrollPane displayPane = new JScrollPane
                (
                display,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                );
                displayPane.setBackground (Color.darkGray);
                displayPane.setBorder
                    (
                    new CompoundBorder
                        (
                        new LineBorder (defaultColor, 6),
                        new BevelBorder (BevelBorder.LOWERED)
                        )
                    );
            display.setBackground (Color.darkGray);
            display.setEnabled (false);
            display.setDisabledTextColor (Color.white);
            display.setLineWrap (true);
            display.setWrapStyleWord (true);
            display.setBorder (new LineBorder (Color.darkGray, 3));
            add (displayPane);
        }

    void setCaretPosition (int position)
        {
        if (position != -1)
            {
            display.setCaretPosition (position);
            }
        }

    void display (String text)
        {
        clear ();
        display.setEnabled (true);
        display.setText (text);
        display.setEnabled (false);
        }

    void clear ()
        {
        display.setText (null);
        }
    }