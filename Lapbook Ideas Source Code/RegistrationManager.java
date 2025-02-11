import javax.swing.*;
import java.io.*;
import java.util.*;

class RegistrationManager
    {
    private static final String regTitle = " REGISTRATION";
    private static final String regMessage =
        "To register please call:\n\n" +
        "         1-800-522-4776\n\n" +
        "Your program seed is:   ";
    private static final String regIncorrect = "That input is invalid... Please try again.";
    private static final String regSucceed = "Thanks for registering!";
    private static final String regFailed = "That is incorrect... Please try again.";
    private static final String regFileName = "data002.tlf";

    private int seed = -1;
    private JFrame dialogOwner;

    RegistrationManager (JFrame dialogOwner)
        {
        this.dialogOwner = dialogOwner;
        if (dialogOwner == null)
            {
            System.out.println ("ERROR: RegistrationManager sent null dialogOwner");
            }
        }

    boolean checkRegistration ()
        {
        File regFile = new File (regFileName);
        if (regFile.exists ())
            {
            try
                {
                ObjectInputStream input = new ObjectInputStream
                    (
                    new FileInputStream (regFileName)
                    );
                boolean result = input.readBoolean ();
                input.close ();
                return result;
                }
            catch (IOException e)
                {
                System.out.println ("ERROR: \'checkRegistration ()\' in RegistrationManager class ");
                e.printStackTrace ();
                }
            }
        else
            {
            setRegistration (false);
            }
        return false;
        }

    boolean attemptRegister ()
        {
        if (seed == -1)
            {
            seed = (int)(Math.random () * 1000000);
            }

        Random keyGenerator = new Random (seed);
        keyGenerator.nextInt ();
        keyGenerator.nextInt ();

        int key = Math.abs (keyGenerator.nextInt ());

        int answer = getNumber (seed);
        if (answer == -1) { return false; }
        if (answer == key)
            {
            setRegistration (true);
            showMessage (regSucceed);
            return true;
            }
        else
            {
            showMessage (regFailed);
            }

        return false;
        }

    private void setRegistration (boolean status)
        {
        try
            {
            File regFile = new File (regFileName);
            long dateModified = -1;
            if (regFile.exists())
                {
                dateModified = regFile.lastModified ();
                }
            ObjectOutputStream output = new ObjectOutputStream
                (
                new FileOutputStream (regFileName)
                );
            output.writeBoolean (status);
            output.close ();
            if (dateModified != -1)
                {
                regFile.setLastModified (dateModified);
                }
            }
        catch (IOException e)
            {
            System.out.println ("ERROR: \'setRegistration ()\' in RegistrationManager class ");
            e.printStackTrace ();
            }
        }

   private int getNumber (int seed)
      {
      for (;;)
         {
         try
            {
            String input = JOptionPane.showInputDialog (dialogOwner, regMessage + seed + "\n\n", regTitle, JOptionPane.PLAIN_MESSAGE);
            if (input == null) { return -1; }
            return (new Integer (input).intValue ());
            }
         catch (NumberFormatException e) { showMessage (regIncorrect); }
         }
      }

    private void showMessage (String message) { JOptionPane.showMessageDialog (dialogOwner, message, regTitle, JOptionPane.PLAIN_MESSAGE); }
    }