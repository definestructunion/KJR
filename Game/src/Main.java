import java.io.IOException;
import java.io.InputStream;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        // Run a java app in a separate system process
        Process proc = Runtime.getRuntime().exec("java -jar KJR.jar");
        // Then retreive the process output
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
    }
}
