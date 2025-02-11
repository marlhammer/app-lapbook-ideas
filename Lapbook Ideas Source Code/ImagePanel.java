import javax.swing.*;
import java.awt.*;

class ImagePanel extends JScrollPane
    {
    private ImageIcon currentImage = new ImageIcon ();
    private JPanel underLay;
    private static final Color viewportColor = new Color (73, 182, 232);

    ImagePanel ()
        {
        underLay = new JPanel ();
            underLay.setLayout (new BorderLayout ());
            underLay.setBackground (viewportColor);
            underLay.add (new JLabel (currentImage), BorderLayout.CENTER);
        getViewport().setBackground (viewportColor);
        setViewportView (underLay);
        setBorder (null);
        }

    void showImage (ImageIcon newImage)
        {
        if (newImage != null)
            {
            underLay.setVisible (false);
            currentImage.setImage (newImage.getImage());
            underLay.setVisible (true);
            }
        else { underLay.setVisible (false); }
        }
    }