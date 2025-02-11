import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

// !!! ® (ANSI 174) // TITLE ISSUE !!!

// Sept 8/2004 Design Note: !!! INTERNAL COMPONENTS (MENU STUFF) IS VUNERABLE TO NULL FILES (super() call prevents check) CAN CALL WITH OUT TITLE AND SET TITLE LATER...

public class LapbookIdeas extends JFrame
    {
    private final LapbookPanel lapbook;
    private final RegistrationManager RM;

    private static final String programDataFileName = "data001.tlf";
    private int width = 0;
    private int height = 0;
    private static final int standardWidth = 800;
    private static final int standardHeight = 600;

    public static void main (String [] arguments)
        {
        if (arguments != null)
            {
            if (arguments.length > 1)
                {
                if (arguments[0] != null)
                    {
                    if (arguments[0].equals ("ndsf"))
                        {
                        if (arguments[1].equals ("Encode_ALL_PRESENT"))
                            {
                            System.out.println ("\n\nEncoding Files... \n");
                            FileManager.encodeCurrentDir ();
                            }
                        if (arguments[1].equals ("Encode_STRUCTURE_EXCLUSIVE"))
                            {
                            System.out.println ("\n\nEncoding Sub-Directories... \n");
                            FileManager.encodeSubDirs (new File ("."));
                            }
                        if (arguments[1].equals ("Encode_STRUCTURE_INCLUSIVE"))
                            {
                            System.out.println ("\n\nEncoding Files... \n");
                            FileManager.encodeCurrentDir ();
                            FileManager.encodeSubDirs (new File ("."));
                            }
                        }

                    System.exit (0);
                    }
                }
            }

        new LapbookIdeas ();
        }

    LapbookIdeas ()
        {
        super (" Lap Book tm Ideas by Tobin's Lab");

        loadProgramData ();
        setIconImage (new ImageIcon ("programIcon.gif").getImage());
        setSize (width, height);
        addComponentListener
            (
            new ComponentAdapter ()
                {
                public void componentResized (ComponentEvent e)
                    {
                    Component temp = e.getComponent ();
                    width = temp.getWidth ();
                    height = temp.getHeight ();
                    temp.setSize (width, height);
                    }
                }
            );

        setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener
            (
            new WindowAdapter ()
                {
                public void windowClosing (WindowEvent e)
                    {
                    saveProgramData ();
                    dispose ();
                    System.exit (0);
                    }
                }
            );

        Container ROOT = getContentPane ();
        ROOT.setLayout (new BorderLayout ());
        ROOT.setBackground (Color.black);

        lapbook = new LapbookPanel ();
            ROOT.add (lapbook, BorderLayout.CENTER);

        final CustomJPanel westPanel = new CustomJPanel ();
        final CardLayout westControl = new CardLayout ();
            westPanel.setLayout (westControl);
            ROOT.add (westPanel, BorderLayout.WEST);

            JPanel register = new JPanel ();
            register.setOpaque (false);
            JButton registerNow = new JButton ("REGISTER");
                registerNow.addActionListener
                    (
                    new ActionListener ()
                        {
                        public void actionPerformed (ActionEvent e)
                            {
                            if (RM.attemptRegister ())
                                {
                                lapbook.clear ();
                                westControl.show (westPanel, "CATEGORIES");
                                }
                            }
                        }
                    );
                register.add (registerNow);
            westPanel.add (register, "REGISTER");

            JPanel categories = new JPanel ();
            categories.setOpaque (false);
            String [] topLevel = FileManager.getGrouping (new File ("."));
            for (int index = 0; index < topLevel.length; ++index)
                {
                categories.add (new CategoryButton (topLevel[index]));
                }
            categories.setLayout (new GridLayout (topLevel.length + 1, 1, 6, 3));
            JButton exit = new JButton ("EXIT");
                exit.setBorder
                    (
                    new CompoundBorder
                        (
                        new LineBorder (CustomJPanel.defaultColor, 6),
                        new BevelBorder (BevelBorder.RAISED)
                        )
                    );
                exit.setForeground (Color.red);
                exit.addActionListener
                    (
                    new ActionListener ()
                        {
                        public void actionPerformed (ActionEvent e)
                            {
                            saveProgramData ();
                            dispose ();
                            System.exit (0);
                            }
                        }
                    );
                categories.add (exit); // !!!
            westPanel.add (categories, "CATEGORIES");

        RM = new RegistrationManager (this);

        if (RM.checkRegistration ())
            {
            lapbook.clear ();
            westControl.show (westPanel, "CATEGORIES");
            }
        else
            {
            westControl.show (westPanel, "REGISTER");
            lapbook.show (FileManager.getSample ());
            }

        setVisible (true);
        }

    private void loadProgramData ()
        {
        File programDataFile = new File (programDataFileName);
        if (programDataFile.exists ())
            {
            try
                {
                long dateModified = programDataFile.lastModified ();
                ObjectInputStream input = new ObjectInputStream
                    (
                    new FileInputStream (programDataFileName)
                    );
                width = input.readInt ();
                height = input.readInt ();
                input.close ();
                programDataFile.setLastModified (dateModified);
                return;
                }
            catch (IOException e)
                {
                System.out.println ("ERROR: Could not load Program Data File. Reverting to defaults.");
                }
            }
        width = standardWidth;
        height = standardHeight;
        }

    private void saveProgramData ()
        {
        try
            {
            ObjectOutputStream output = new ObjectOutputStream
                (
                new FileOutputStream (programDataFileName)
                );
            output.writeInt (getWidth ());
            output.writeInt (getHeight ());
            output.close ();
            }
        catch (IOException e)
            {
            System.out.println ("ERROR: Could not save Program Data File. Reverting to defaults.");
            }
        }

    private class CategoryButton extends JButton
        {
        private static final int borderWidth = 6;

        private JPopupMenu subCategoryMenu;

        CategoryButton (String source)
            {
            super (" " + new File (source).getName() + " ");
            setBorder
                (
                new CompoundBorder
                    (
                    new LineBorder (CustomJPanel.defaultColor, borderWidth),
                    new BevelBorder (BevelBorder.RAISED)
                    )
                );
            addActionListener
                (
                new ActionListener ()
                    {
                    public void actionPerformed (ActionEvent e) { showMenu (); }
                    }
                );

            subCategoryMenu = new JPopupMenu ();
                String [] subCategories = FileManager.getGrouping (new File (source));
                for (int index = 0; index < subCategories.length; ++index)
                    {
                    subCategoryMenu.add (new SubCategoryMenu (subCategories[index]));
                    }
           }

        private void showMenu ()
            {
            subCategoryMenu.show (this, this.getWidth (), borderWidth);
            }

        private class SubCategoryMenu extends JMenu
            {
            private File fileSource;

            SubCategoryMenu (String source)
                {
                super (new File (source).getName());
                fileSource = new File(source);

                addActionListener
                    (
                    new ActionListener ()
                        {
                        public void actionPerformed (ActionEvent e) { }
                        }
                    );

                String [] nextGrouping = FileManager.getGrouping (fileSource);
                for (int index = 0; index < nextGrouping.length; ++index)
                    {
                    add (new LapbookMenuItem (nextGrouping[index]));
                    }
                }
            }

        private class LapbookMenuItem extends JMenuItem
            {
            private File fileSource;

            LapbookMenuItem (String source)
                {
                super (new File (source).getName());
                fileSource = new File (source);

                addActionListener
                    (
                    new ActionListener ()
                        {
                        public void actionPerformed (ActionEvent e) { loadLapbook (); }
                        }
                    );
                }

            private void loadLapbook () { lapbook.show (FileManager.buildLapbook (fileSource)); }
            }
        }
    }