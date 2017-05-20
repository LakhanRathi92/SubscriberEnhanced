
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.update.UpdateAction;

import java.util.Calendar;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;


public class UpdaterTest {
	
	public static void main(String [] args){
	
		
    	//create dataset
		Dataset ds = DatasetFactory.create();
		
		Model modelBase = ModelFactory.createDefaultModel();
		Model modelDusk = ModelFactory.createDefaultModel();
		Model modelWindSpeed = ModelFactory.createDefaultModel();
		
		modelBase.read("base.ttl", "TTL");
		modelDusk.read("dusk.ttl", "TTL");
		modelWindSpeed.read("windSpeed.ttl", "TTL");		
	
		
		ds.addNamedModel("base", modelBase);
		ds.addNamedModel("dusk", modelDusk);
		ds.addNamedModel("windSpeed", modelWindSpeed);
		
		
		
		/**
		 * I get data, from construct query, which is actually a model.
		 * Add Time Stamp
		 * Add literal value
		 * Push it in DataSet
		 * */
		
		String WindSpeed = "123";
		Float ws = Float.parseFloat(WindSpeed); 
				
		Calendar cal = Calendar.getInstance();
		
	 	XSDDateTime timeStamp = new XSDDateTime(cal);
    	String time = timeStamp.toString();
    	
    	/*
    	 * Assuming windSpeed only
  
    	 * */
  
    	String ns = "http://devices.dfki.de/mySensor/properties/windSpeed/values/latest";
    	String ts = "http://devices.dfki.de/mySensor/properties/windSpeed/values/latest/timeStamp";
    	String nVal = "http://vocab.arvida.de/2015/06/vom/vocab/numericalValue";
    	
    	//TimeStamp
    	Resource r = ResourceFactory.createResource(ns);
    	Literal l = ResourceFactory.createTypedLiteral(timeStamp);
    	Property p = modelWindSpeed.createProperty(ts);    	    	
    	Statement timeUpdate = modelWindSpeed.createLiteralStatement(r, p, l);    	
    	
    	//latestValue
    	Resource value = ResourceFactory.createResource(ns);
    	Literal literalVal = ResourceFactory.createTypedLiteral(ws);
    	Property litPro = modelWindSpeed.createProperty(nVal);
    
    	Statement valueUpdate = modelWindSpeed.createLiteralStatement(value, litPro, literalVal);
    	
    	
    	
    	modelWindSpeed.add(timeUpdate);
    	modelWindSpeed.add(valueUpdate);
    	
    	
    	ds.addNamedModel("windSpeed", modelWindSpeed);
    	
    	
    	
    	//CHECK
    	Model updated = ds.getNamedModel("windSpeed");
    	updated.write(System.out, "TTL");
    	
	}
}

