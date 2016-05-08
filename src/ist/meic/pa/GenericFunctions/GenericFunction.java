package ist.meic.pa.GenericFunctions;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.ClassPool;
import javassist.NotFoundException;

public class GenericFunction {
	
	private List<GFMethod> methods = new ArrayList<GFMethod>(); 
	private static List<GFMethod> beforeMethods = new ArrayList<GFMethod>(); 
	private static List<GFMethod> afterMethods = new ArrayList<GFMethod>(); 
	private static String functionName;
	
	GenericFunction (String functionName){
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
	
	private <T> List<GFMethod> selectGenericMethods(T...args){
		List<GFMethod> list = new ArrayList<GFMethod>();
		for(GFMethod m : methods){
			for(Method dm : m.getClass().getDeclaredMethods()){
				if(args.length == dm.getParameterTypes().length){
					Class<?>[] types =  dm.getParameterTypes();
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i]){
							list.add(m);
						}
					}
				}
			}
		}
		
		return list;
	}
	
	private <T> List<GFMethod> selectBeforeMethods(T...args){
		List<GFMethod> list = new ArrayList<GFMethod>();
		for(GFMethod m : beforeMethods){
			for(Method dm : m.getClass().getDeclaredMethods()){
				if(args.length == dm.getParameterTypes().length){
					Class<?>[] types =  dm.getParameterTypes();
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i]){
							list.add(m);
						}
					}
				}
			}
		}
		
		return list;
	}
	
	private <T> List<GFMethod> selectAfterMethods(T...args){
		List<GFMethod> list = new ArrayList<GFMethod>();
		for(GFMethod m : afterMethods){
			for(Method dm : m.getClass().getDeclaredMethods()){
				if(args.length == dm.getParameterTypes().length){
					Class<?>[] types =  dm.getParameterTypes();
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i]){
							list.add(m);
						}
					}
				}
			}
		}
		
		return list;
	}
	
	public <T> Object call (T...args){
		List<GFMethod> applicableMethods = selectGenericMethods(args);
		List<GFMethod> applicableBeforeMethods = selectBeforeMethods(args);
		List<GFMethod> applicableAfterMethods = selectAfterMethods(args);
		
		if(applicableMethods.size() != 0){
			System.out.println(applicableMethods.size());
		}
		
		if(applicableBeforeMethods.size() != 0){
			System.out.println("2");
		}
		
		if(applicableAfterMethods.size() != 0){
			System.out.println("3");
		}
		
		
		return args[0];
	}
	
	
	//TODO
	// get declared methods from every instance of GFMethod in every list
		// compare arguments 
		// method combination
		// profit

	public static void main(String[] args){
		GenericFunction gf = new GenericFunction("add");
		GFMethod gm = new GFMethod() {
			Object call(String a, String b) {
			return gf.call(Integer.decode(a), Integer.decode(b));
		}};
		
		gf.addMethod(new GFMethod() {
			Object call(String a, String b) {
				return gf.call(Integer.decode(a), Integer.decode(b));
			}});
		gf.addMethod(new GFMethod() {
			Object call(String a, String b) {
				return gf.call(Integer.decode(a), Integer.decode(b));
			}});
		gf.call("3", "ADD");
	}
}
