import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class LapbookPanel extends JPanel
    {
    private CustomDisplay textDisplay;
    private JLabel numberLabel;
    private JButton previousButton;
    private JButton nextButton;
    private ImagePanel imageDisplay;

    private Lapbook currentLapbook;
    private int currentIndex;

    LapbookPanel ()
        {
        setOpaque (false);
        setLayout (new BorderLayout ());

        JPanel imageView = new JPanel (); // !!!
            imageView.setLayout (new GridLayout (1, 1));
            imageDisplay = new ImagePanel ();
                imageView.add (imageDisplay);
            add (imageView, BorderLayout.CENTER);

        JPanel imageSouth = new JPanel ();
            imageSouth.setOpaque (false);
            imageSouth.setLayout (new GridLayout (1, 2));

            CustomJPanel imageControl = new CustomJPanel ();
            imageControl.setLayout (new BorderLayout ());
            JPanel labelPanel = new JPanel ();
                labelPanel.setOpaque (false);
                numberLabel = new JLabel ();
                    numberLabel.setForeground (Color.black);
                    numberLabel.setBackground (Color.white);
                    numberLabel.setOpaque (true);
                    numberLabel.setVisible (false);
                    numberLabel.setBorder (new EtchedBorder (EtchedBorder.RAISED));
                    labelPanel.add (numberLabel);
                imageControl.add (labelPanel, BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel ();
                buttonPanel.setOpaque (false);
                buttonPanel.setLayout (new GridLayout (2, 1));
                previousButton = new JButton (" PREVIOUS ");
                    previousButton.setBorder
                        (
                        new CompoundBorder
                            (
                            new LineBorder (CustomJPanel.defaultColor, 6),
                            new EtchedBorder (EtchedBorder.RAISED)
                            )
                        );
                    previousButton.addActionListener
                        (
                        new ActionListener ()
                            {
                            public void actionPerformed (ActionEvent e)
                                {
                                --currentIndex;
                                if (currentIndex < 0)
                                    {
                                    currentIndex = (currentLapbook.totalImages () - 1);
                                    }
                                showImage (false);
                                }
                            }
                        );
                    buttonPanel.add (previousButton);
                nextButton = new JButton (" NEXT ");
                    nextButton.setBorder
                        (
                        new CompoundBorder
                            (
                            new LineBorder (CustomJPanel.defaultColor, 6),
                            new EtchedBorder (EtchedBorder.RAISED)
                            )
                        );
                    nextButton.addActionListener
                        (
                        new ActionListener ()
                            {
                            public void actionPerformed (ActionEvent e)
                                {
                                ++currentIndex;
                                if (currentIndex >= currentLapbook.totalImages ())
                                    {
                                    currentIndex = 0;
                                    }
                                showImage (false);
                                }
                            }
                        );
                    buttonPanel.add (nextButton);
                imageControl.add (buttonPanel, BorderLayout.EAST);
            imageSouth.add (imageControl);

            JPanel imageText = new JPanel ();
            imageText.setOpaque (false);
            imageText.setLayout (new GridLayout (1, 1));
            textDisplay = new CustomDisplay ();
                imageText.add (textDisplay);
            imageSouth.add (imageText);

            add (imageSouth, BorderLayout.SOUTH);
        }

    void show (Lapbook input)
        {
        clear ();

        if (input != null)
            {
            currentLapbook = input;
            textDisplay.display (currentLapbook.getCaption ());
            if (input.totalImages() > 1)
                {
                previousButton.setEnabled (true);
                nextButton.setEnabled (true);
                numberLabel.setVisible (true);
                }
            currentIndex = 0;
            numberLabel.setText ("  PHOTO #" + (currentIndex + 1) + "  ");
            showImage (true);
            }
            // ERROR MESSAGE For else??? !!!
        }

    void clear ()
        {
        previousButton.setEnabled (false);
        nextButton.setEnabled (false);
        numberLabel.setVisible (false);
        textDisplay.clear ();
        imageDisplay.showImage (null);
        }

    private int findPlace (int goal)
        {
        String caption = currentLapbook.getCaption ();
        StringTokenizer tokenizer = new StringTokenizer (caption, "\n", true);
        int overallLength = 0;
        int tokens = tokenizer.countTokens ();
        for (int index = 0; index < tokens; ++index)
            {
            String currentToken = tokenizer.nextToken ();
            if (!currentToken.equals ("\n"))
                {
                if (currentToken.length() >= 2)
                    {
                    String prefix = currentToken.substring (0, 2);
                    prefix = prefix.trim();
                    try
                        {
                        int value = new Integer (prefix).intValue ();
                        if (value == goal)
                            {
                            return overallLength;
                            }
                        }
                    catch (NumberFormatException e)
                        {
                        // !!! ANYTHING HERE ???
                        }
                    }
                else
                    {
                    // !!! ANYTHING HERE ???
                    }
                }
            overallLength += currentToken.length();
            }
        return -1;
        }

    private void showImage (boolean ignoreOne)
        {
        numberLabel.setText ("  PHOTO #" + (currentIndex + 1) + "  ");
        textDisplay.setCaretPosition (findPlace (currentIndex + 1)); // !!!
        if ((currentIndex + 1) == 1)
            {
            if (ignoreOne == false)
                {
                textDisplay.setCaretPosition (findPlace (currentIndex + 1));
                }
            else
                {
                textDisplay.setCaretPosition (0);
                }
            }
        else
            {
            textDisplay.setCaretPosition (findPlace (currentIndex + 1));
            }
        imageDisplay.showImage (currentLapbook.getImage (currentIndex));
        }
    }