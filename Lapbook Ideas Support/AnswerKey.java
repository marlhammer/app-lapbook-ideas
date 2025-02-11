import java.util.*;
import java.io.*;

public class AnswerKey
    {
    public static void main (String [] arguments) { new AnswerKey (); }

    AnswerKey () // !!!
        {
        int seed = getAnInt ("Enter the seed number from the customer: ");

        Random keyGenerator = new Random (seed);
        keyGenerator.nextInt ();
        keyGenerator.nextInt ();

        int key = Math.abs (keyGenerator.nextInt ());
        System.out.println (key);

        getAnInt ("Enter -1 to close: ");
        }

    private int getAnInt (String message)
        {
        BufferedReader inputReader = new BufferedReader
            (
            new InputStreamReader (System.in)
            );

        int selection = -1;
        while (selection == -1)
            {
            System.out.print (message);
            try
                {
                selection = new Integer (inputReader.readLine ()).intValue();
                if (selection == -1)
                    {
                    System.out.println ("\n\n");
                    System.exit (0);
                    }
                }
            catch (NumberFormatException e)
                {
                selection = -1;
                System.out.println ("\n\nPlease enter a number (type -1 to exit)...\n");
                }
            catch (IOException e)
                {
                System.out.println ("ERROR: Could not receive input\n");
                e.printStackTrace ();
                }
            }
        return selection;
        }
    }