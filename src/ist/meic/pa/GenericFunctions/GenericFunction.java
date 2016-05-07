package ist.meic.pa.GenericFunctions;

import g14.src.ist.meic.pa.ClassPool;

import java.util.List;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.ClassPool;

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
		List<GFMethod> applicableMethods = null;
		
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.getCtClass("GFMethod");
		
		// get declared methods from every instance of GFMethod in every list
		// compare arguments 
		// method combination
		// profit
		
		return objects;
	}
}
