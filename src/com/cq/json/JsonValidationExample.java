package com.cq.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.JsonLoader;


public class JsonValidationExample  
{

public boolean validate(String jsonData, String jsonSchema) {
    ProcessingReport report = null;
    boolean result = false;
    try {
        System.out.println("Applying schema: <<"+jsonSchema+">> \nto data: \n<<"+jsonData+">>");
        JsonNode schemaNode = JsonLoader.fromString(jsonSchema);
        JsonNode data = JsonLoader.fromString(jsonData);         
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault(); 
        JsonSchema schema = factory.getJsonSchema(schemaNode);
        report = schema.validate(data);
    } catch (JsonParseException jpex) {
        System.out.println("Error. Something went wrong trying to parse json data: #<#<"+jsonData+
                ">#># or json schema: @<@<"+jsonSchema+">@>@. Are the double quotes included? "+jpex.getMessage());
        //jpex.printStackTrace();
    } catch (ProcessingException pex) {  
        System.out.println("Error. Something went wrong trying to process json data: #<#<"+jsonData+
                ">#># with json schema: @<@<"+jsonSchema+">@>@ "+pex.getMessage());
        //pex.printStackTrace();
    } catch (IOException e) {
        System.out.println("Error. Something went wrong trying to read json data: #<#<"+jsonData+
                ">#># or json schema: @<@<"+jsonSchema+">@>@");
        //e.printStackTrace();
    }
    if (report != null) {
        Iterator<ProcessingMessage> iter = report.iterator();
        while (iter.hasNext()) {
            ProcessingMessage pm = iter.next();
            System.out.println("Processing Message: "+pm.getMessage());
        }
        result = report.isSuccess();
    }
    System.out.println(" Result=" +result);
    return result;
}

public static void main(String[] args)
{	
    System.out.println( "Starting Json Validation." );
    JsonValidationExample app = new JsonValidationExample();
    
    JSONObject object = new JSONObject();
    object.put("firstName", "Andrew");
    object.put("lastName", "Chen");
    object.put("Age", 20);
    object.put("sex", "man");
    
    String jsonData = object.toString();
    File schema = new File("resources\\json.schema");
    byte[] b = null;
    InputStream inputStream = null;
    try {
		inputStream = new FileInputStream(schema);
		int len = inputStream.available();
		b = new byte[len];
		inputStream.read(b, 0, len);
		inputStream.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally{
		if (null != inputStream)
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
    String jsonSchema = new String(b);
    app.validate(jsonData, jsonSchema);
}

}
