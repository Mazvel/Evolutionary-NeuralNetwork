package evolutionaryneuralnetwork;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Kiddi on 31.5.2014.
 */
public class Logger {

    private String filename;
    private boolean append;
    private Date date;

    public Logger(String filename, boolean append)
    {
        this.filename = filename;
        this.append = append;
        date = new Date();
    }

    public void Log(String message)
    {
        try
        {
            FileWriter writer = new FileWriter(filename,append);
            writer.write(new Timestamp(date.getTime()) + ": " + message + "\n");
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
        e.printStackTrace();
        }
    }


}
