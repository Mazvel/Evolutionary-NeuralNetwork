package evolutionaryneuralnetwork;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kiddi on 31.5.2014.
 */
public class JSONHandler {

    public static JSONObject readJSONFile(String filename)
    {
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader(filename));
            JSONObject jObj = (JSONObject) obj;

            return jObj;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveJSONString(String filename, String jsonString)
    {
        try
        {
            FileWriter writer = new FileWriter(filename);
            writer.write(jsonString);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveGenerationToJSON(ArrayList<String> jsonChromosome, String filename)
    {
        JSONObject generation = new JSONObject();
        int id = 0;
        for(String s : jsonChromosome)
        {
            generation.put(id,s);
        }

        saveJSONString(filename,generation.toJSONString());
    }

    public static Integer jsonNumberToInt(Object obj)
    {
        Long l = (Long)obj;
        return Integer.valueOf(l.intValue());
    }

}
