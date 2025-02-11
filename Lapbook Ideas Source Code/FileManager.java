import javax.swing.*;
import java.io.*;
import java.util.*;

class FileManager
    {
    private static final String sampleDir = "sample";
    private static final String encodedExtension = ".tlf";

    private static final String captionFileName = "caption.txt";

    static Lapbook getSample ()
        {
        return buildLapbook (new File ("." + File.separator + sampleDir));
        }

    static String [] getGrouping (File start)
        {
        File [] files = start.listFiles (new DirectoryFilterSansSample ());
        String [] result = new String [files.length];
        for (int index = 0; index < files.length; ++index)
            {
            result[index] = files[index].getPath ();
            }
        Arrays.sort (result);
        return result;
        }

    static Lapbook buildLapbook (File source)
        {
        String caption = extractCaption (source);
        ImageIcon [] images = decodeImages (source);
        return new Lapbook (caption, images);
        }

    static void encodeSubDirs (File source)
        {
        if (source == null)
            {
            System.out.println ("ERROR: \'source\' in \'encodeOneDir ()\' is NULL");
            return;
            }
        File [] subDirs = source.listFiles (new DirectoryFilter ());
        for (int index = 0; index < subDirs.length; ++index)
            {
            encodeOneDir (subDirs[index]);
            encodeSubDirs (subDirs[index]);
            }
        }

    static void encodeCurrentDir () { encodeOneDir (new File (".")); }

    static void encodeOneDir (File source)
        {
        if (source == null)
            {
            System.out.println ("ERROR: \'source\' in \'encodeOneDir ()\' is NULL");
            return;
            }
        System.out.println (" " + source.getPath ());
        File [] targets = source.listFiles (new ImageFileFilter ());
        for (int fileIndex = 0; fileIndex < targets.length; ++fileIndex)
            {
            String currentName = targets[fileIndex].getName();
            System.out.println ("    " + currentName);
            try
                {
                String newName = (currentName.substring (0, (currentName.length() - 4))) + encodedExtension;
                DataInputStream input = new DataInputStream
                    (
                    new FileInputStream (targets[fileIndex])
                    );
                DataOutputStream output = new DataOutputStream
                    (
                    new FileOutputStream (source.getPath() + "\\" + newName)
                    );

                int validBytes = input.available ();

                byte bogusBytes = (byte)((Math.random() + 1) * 100);
                output.writeByte (bogusBytes);

                for (int bogusIndex = 0; bogusIndex < bogusBytes; ++bogusIndex)
                    {
                    output.writeByte ((byte)(Math.random() * 100));
                    }
                for (int validIndex = 0; validIndex < validBytes; ++validIndex)
                    {
                    byte currentByte = input.readByte ();
                    output.writeByte (currentByte);
                    }
                input.close ();
                output.close ();
                }
            catch (IOException e)
                {
                System.out.println ("ERROR: \'encodeImages ()\'  in FileManager class\n\n");
                e.printStackTrace ();
                }
            }
        }

    private static ImageIcon [] decodeImages (File source)
        {
        if (source == null)
            {
            System.out.println ("ERROR: \'source\' in \'decodeImages ()\' is NULL");
            return null;
            }
        String [] targets = source.list (new EncodedFileFilter ());
        if (targets == null) { return null; }
        if (targets.length == 0)
            {
            System.out.println ("No image files detected in " + source.getPath ());
            return null;
            }
        Arrays.sort (targets);
        ImageIcon [] results = new ImageIcon [targets.length];
        for (int fileIndex = 0; fileIndex < targets.length; ++fileIndex)
            {
            try
                {
                DataInputStream input = new DataInputStream
                    (
                    new FileInputStream (source.getPath() + "\\" + targets[fileIndex])
                    );

                byte bogusBytes = input.readByte ();
                input.skipBytes (bogusBytes);

                int remainingBytes = input.available ();

                byte [] currentFile = new byte [remainingBytes];

                input.readFully (currentFile);
                input.close ();

                results[fileIndex] = new ImageIcon (currentFile);
                }
            catch (IOException e)
                {
                System.out.println ("ERROR: \'decodeImages ()\' in FileManager class\n\n");
                e.printStackTrace ();
                }
            }
        return results;
        }

    private static String extractCaption (File source)
        {
        if (source == null)
            {
            System.out.println ("ERROR: \'source\' in \'extractCaption ()\' is NULL");
            return null;
            }
        if (new File (source.getPath() + File.separator + captionFileName).exists() == false)
            {
            System.out.println ("ERROR: No caption file detected in " + source.getPath ());
            return null;
            }

        StringBuffer holder = new StringBuffer ();
        try
            {
            BufferedReader input = new BufferedReader
                (
                new FileReader (source.getPath() + File.separator + captionFileName)
                );
            String currentLine = input.readLine ();
            while (currentLine != null)
                {
                holder.append (currentLine + "\n");
                currentLine = input.readLine ();
                }
            input.close ();
            }
        catch (IOException e)
            {
            System.out.println ("ERROR: \'extractCaption ()\' in FileManager class\n\n");
            e.printStackTrace ();
            }
        return holder.toString ();
        }

    private static class DirectoryFilter implements FileFilter
        {
        public boolean accept (File input) { return input.isDirectory (); }
        }

    private static class DirectoryFilterSansSample implements FileFilter
        {
        public boolean accept (File input)
            {
            if (input.getName ().equals (sampleDir))
                {
                return false;
                }
            return input.isDirectory ();
            }
        }

    private static class EncodedFileFilter implements FilenameFilter
        {
        public boolean accept (File dir, String name)
            {
            if (name.length () < 4) { return false; }
            if (name.endsWith (encodedExtension))
                {
                return true;
                }
            return false;
            }
        }

    private static class ImageFileFilter implements FilenameFilter
        {
        public boolean accept (File dir, String name)
            {
            if (name.length () >= 4)
                {
                String extension = name.substring(name.length() - 4);
                if (extension.equalsIgnoreCase (".jpg"))
                    {
                    return true;
                    }
                }
            return false;
            }
        }
    }