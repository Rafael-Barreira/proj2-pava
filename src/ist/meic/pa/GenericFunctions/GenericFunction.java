package ist.meic.pa.GenericFunctions;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
					int l = args.length;
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i]){
							System.out.println("yo");
							l --;
						}
					}
					if (l== 0){ list.add(m); System.out.println("ya");}
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
					int l = args.length;
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i]){
							l --;
						}
					}
					if (l== 0){ list.add(m);}
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
					int l = args.length;
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i]){
							l --;
						}
					}
					if (l== 0){ list.add(m);}
				}
			}
		}
		return list;
	}
	
	private void levelCalculation(List<GFMethod> methods, List<GFMethod> befMethods, List<GFMethod> aftMethods){
		
		//get levels of parameters in before methods
		if(befMethods.size() != 0){
			for (GFMethod m : befMethods){
				for(Method dm : m.getClass().getDeclaredMethods()){
					for(Class<?> param : dm.getParameterTypes()){
						Integer level = 0;
						Class<?> obj = param.getSuperclass();
						while(obj.getSuperclass() != null){
							obj =  obj.getSuperclass();
							level ++;
						}	
						m.getLevelsMap().put(param, level);
					}
				}
			}
		}
		
		//get levels of parameters in methods
				if(methods.size() != 0){
					for (GFMethod m : methods){
						for(Method dm : m.getClass().getDeclaredMethods()){
							for(Class<?> param : dm.getParameterTypes()){
								Integer level = 0;
								Class<?> obj = param.getSuperclass();
								while(obj.getSuperclass() != null){
									obj =  obj.getSuperclass();
									level ++;
								}	
								m.getLevelsMap().put(param, level);
							}
						}
					}
				}
				
				//get levels of parameters in after methods
				if(afterMethods.size() != 0){
					for (GFMethod m : afterMethods){
						for(Method dm : m.getClass().getDeclaredMethods()){
							for(Class<?> param : dm.getParameterTypes()){
								Integer level = 0;
								Class<?> obj = param.getSuperclass();
								while(obj.getSuperclass() != null){
									obj =  obj.getSuperclass();
									level ++;
								}	
								m.getLevelsMap().put(param, level);
							}
						}
					}
				}
		
	}
	
	public <T> Object call (T...args){
		List<GFMethod> applicableMethods = selectGenericMethods(args);
		List<GFMethod> applicableBeforeMethods = selectBeforeMethods(args);
		List<GFMethod> applicableAfterMethods = selectAfterMethods(args);
		
		if(applicableMethods.size() != 0){
			System.out.println(applicableMethods.size());
		}
		
		if(applicableBeforeMethods.size() != 0){
			System.out.println(applicableBeforeMethods.size());
		}
		
		if(applicableAfterMethods.size() != 0){
			System.out.println(applicableAfterMethods.size());
		}
		
		levelCalculation(applicableMethods, applicableBeforeMethods, applicableAfterMethods);
		
		return args[0];
	}
	
	
	//TODO
		// method combination
		// profit

	public static void main(String[] args){
		GenericFunction gf = new GenericFunction("add");
		
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
