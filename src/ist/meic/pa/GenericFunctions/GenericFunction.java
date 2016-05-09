package ist.meic.pa.GenericFunctions;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

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
				if(args.length == dm.getParameters().length){
					Parameter[] types =  dm.getParameters();
					int l = args.length;
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i].getType()){
							l --;
							continue;
						}
						Class<?> obj = args[i].getClass();
						while(obj.getSuperclass() != null){
							obj = obj.getSuperclass();
							if(obj.equals(types[i].getType())){
								l --;
								break;
							}
						}
					}
					if (l == 0){ list.add(m);}
				}
			}
		}
		
		return list;
	}
	
	private <T> List<GFMethod> selectBeforeMethods(T...args){
		List<GFMethod> list = new ArrayList<GFMethod>();
		for(GFMethod m : beforeMethods){
			for(Method dm : m.getClass().getDeclaredMethods()){
				if(args.length == dm.getParameters().length){
					Parameter[] types =  dm.getParameters();
					int l = args.length;
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i].getType()){
							l --;
							continue;
						}
						Class<?> obj = args[i].getClass();
						while(obj.getSuperclass() != null){
							obj = obj.getSuperclass();
							if(obj.equals(types[i].getType())){
								l --;
								break;
							}
						}
					}
					if (l == 0){ list.add(m);}
				}
			}
		}
		
		return list;
	}
	
	private <T> List<GFMethod> selectAfterMethods(T...args){
		List<GFMethod> list = new ArrayList<GFMethod>();
		for(GFMethod m : afterMethods){
			for(Method dm : m.getClass().getDeclaredMethods()){
				if(args.length == dm.getParameters().length){
					Parameter[] types =  dm.getParameters();
					int l = args.length;
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i].getType()){
							l --;
							continue;
						}
						Class<?> obj = args[i].getClass();
						while(obj.getSuperclass() != null){
							obj = obj.getSuperclass();
							if(obj.equals(types[i].getType())){
								l --;
								break;
							}
						}
					}
					if (l == 0){ list.add(m);}
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
						Class<?> obj = param;
						while(obj.getSuperclass() != null){
							obj =  obj.getSuperclass();
							level ++;
						}	

						m.getLevelsMap().add(level);
						System.out.println(param.toGenericString() + " #### " + level);
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
								Class<?> obj = param;
								while(obj.getSuperclass() != null){
									obj =  obj.getSuperclass();
									level ++;
								}	

								m.getLevelsMap().add(level);
								System.out.println(param.toGenericString() + " #### " + level);
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
								Class<?> obj = param;
								while(obj.getSuperclass() != null){
									obj =  obj.getSuperclass();
									level ++;
								}	

								m.getLevelsMap().add(level);
								System.out.println(param.toGenericString() + " #### " + level);
							}
						}
					}
				}
	}
	
	private HashMap<GFMethod, Integer> sortHashMapByValues(HashMap<GFMethod, Integer> passedMap){
		HashMap<GFMethod, Integer> result = new LinkedHashMap<>();
	    Stream<Map.Entry<GFMethod, Integer>> st = passedMap.entrySet().stream();

	    st.sorted( Map.Entry.comparingByValue() )
	        .forEachOrdered( e -> result.put(e.getKey(), e.getValue()) );

	    return result;
	}
	
	public void methodCombination(List<GFMethod> methods, List<GFMethod> befMethods, List<GFMethod> aftMethods){
		if(befMethods.size() != 0){
			HashMap<GFMethod, Integer> orderedBefMethods = new HashMap<GFMethod, Integer>();
			for(GFMethod m : befMethods){
				int level = 0;
				for(Integer i : m.getLevelsMap()){
					level += (int)m.getLevelsMap().get(i);
				}
				 orderedBefMethods.put(m, level);
			}
			 orderedBefMethods = sortHashMapByValues(orderedBefMethods);
			 ArrayList<GFMethod> keys = new ArrayList<GFMethod>(orderedBefMethods.keySet());
		     for(int i=keys.size()-1; i>=0;i--){
		       System.out.println("methodcombination "+(int)orderedBefMethods.get(keys.get(i)));
		       //MethodCall
		     }
		}
		
		if(methods.size() != 0){
			HashMap<GFMethod, Integer> orderedBefMethods = new HashMap<GFMethod, Integer>();
			for(GFMethod m : methods){
				int level = 0;
				for(Integer i : m.getLevelsMap()){
					level += (int)m.getLevelsMap().get(i);
				}
				 orderedBefMethods.put(m, level);
			}
			 orderedBefMethods = sortHashMapByValues(orderedBefMethods);
			 ArrayList<GFMethod> keys = new ArrayList<GFMethod>(orderedBefMethods.keySet());
		     for(int i=keys.size()-1; i>=0;i--){
		       System.out.println("methodcombination "+(int)orderedBefMethods.get(keys.get(i)));
		       //MethodCall
		     }
		}
		
		if(aftMethods.size() != 0){
			HashMap<GFMethod, Integer> orderedBefMethods = new HashMap<GFMethod, Integer>();
			for(GFMethod m : aftMethods){
				int level = 0;
				for(Integer i : m.getLevelsMap()){
					level += (int)m.getLevelsMap().get(i);
				}
				 orderedBefMethods.put(m, level);
			}
			 orderedBefMethods = sortHashMapByValues(orderedBefMethods);
			 Iterator itt = orderedBefMethods.entrySet().iterator();
			 
			 while (itt.hasNext()) {
				 Map.Entry pair = (Map.Entry)itt.next();
				 System.out.println("methodcombination "+(int)pair.getValue());
				 //METHOD CALL
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
		methodCombination(applicableMethods, applicableBeforeMethods, applicableAfterMethods);
		
		return args[0];
	}
	
	
	//TODO
		// method combination
		// profit

	public static void main(String[] args){
		GenericFunction gf = new GenericFunction("add");
		
		gf.addBeforeMethod(new GFMethod() {
			Object call(String a, Object b) {
				return gf.call(Integer.decode(a), b);
			}});
		gf.addBeforeMethod(new GFMethod() {
			Object call(String a, String b) {
				return gf.call(Integer.decode(a), Integer.decode(b));
			}});
		
		gf.addBeforeMethod(new GFMethod() {
			Object call(Object a, Object b) {
				return gf.call(a, b);
			}});
		gf.call("3", "0");
	}
}
