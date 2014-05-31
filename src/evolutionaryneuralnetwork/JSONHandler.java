package evolutionaryneuralnetwork;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Kiddi on 31.5.2014.
 */
public class JSONHandler {

    public JSONObject readJSONFile(String filename)
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

    public void saveJSONString(String filename, String jsonString)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
            writer.write(jsonString);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveGenerationToJSON(ArrayList<String> jsonChromosome, String filename)
    {
        JSONObject generation = new JSONObject();
        int id = 0;
        for(String s : jsonChromosome)
        {
            generation.put(id,s);
        }

        saveJSONString(filename,generation.toJSONString());
    }

}
