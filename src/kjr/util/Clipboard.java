package kjr.util;

// TODO: make this javadoc better

import kjr.input.Input;
import kjr.input.Keys;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * Gets content from user's clipboard in the form of text.
 */
public final class Clipboard
{
    static
    {
        //sys = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * Gets pasted content from the user's clipboard.
     * @return clipboard text. Will return null if no available pasted text or can not be pasted as a String.
     */
    public static String paste()
    {
        java.awt.datatransfer.Clipboard sys = Toolkit.getDefaultToolkit().getSystemClipboard();
        String pastedString = null;
        Transferable data = sys.getContents(null);

        try
        {
            if(data != null && data.isDataFlavorSupported(DataFlavor.stringFlavor))
                pastedString = (String)data.getTransferData(DataFlavor.stringFlavor);
        }

        catch(Exception e) { e.printStackTrace(); }

        return pastedString;
    }
}
