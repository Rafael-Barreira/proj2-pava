package ist.meic.pa.GenericFunctions;

import java.util.List;
import java.util.TreeMap;

public class GenericFunction {
	
	TreeMap<String, List<GFMethod>> functionMethods; 
	
	GenericFunction (String functionName){
		functionMethods.put(functionName, null);
	}
}
