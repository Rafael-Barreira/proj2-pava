package ist.meic.pa.GenericFunctions;

import java.util.List;

public class GenericFunction {
	
	private static List<GFMethod> methods; 
	private static List<GFMethod> beforeMethods; 
	private static List<GFMethod> afterMethods; 
	private static String functionName;
	
	GenericFunction (String functionName){
		methods = null;
		this.functionName = functionName;
	}
	
	public void addMethod(GFMethod method){
		methods.add(method);
		
	}
	
	public void addBeforeMethod(GFMethod method){
		beforeMethods.add(method);
	}
	
	public void addAfterMethod(GFMethod method){
		afterMethods.add(method);
	}
	
	Object call (Object...objects ){
		
		return objects;
	}
}
