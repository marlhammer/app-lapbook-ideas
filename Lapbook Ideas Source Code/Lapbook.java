import javax.swing.*;
import java.io.*;

class Lapbook
    {
    private String caption;
    private ImageIcon [] images;

    Lapbook (String caption, ImageIcon [] images)
        {
        this.caption = caption;
        if (images == null)
            {
            this.images = null;
            if (caption == null)
                {
                this.caption = "ERROR: Images are not present";
                } // !!!
            }
        else { this.images = images; }
        }

    String getCaption () { return caption; }

    ImageIcon getImage (int index)
        {
        if (images != null)
            {
            if (index < images.length && index >= 0)
                {
                return images[index];
                }
            else
                {
                System.out.println ("ERROR: \'getImage ()\' in Lapbook class sent invalid index");
                return null;
                }
            }
        else { return null; }
        }

    int totalImages ()
        {
        if (images != null)
            {
            return images.length;
            }
        return -1;
        }
    }
