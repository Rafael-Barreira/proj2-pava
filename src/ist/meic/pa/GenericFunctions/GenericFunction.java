package ist.meic.pa.GenericFunctions;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
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
		
		for(GFMethod m : methods){//INFO: corre todos os GFmethod presentes na classe
			for(Method dm : m.getClass().getDeclaredMethods()){//INFO: corre todos os metodos que estao declarados no GFmethod
				if(args.length == dm.getParameters().length){//INFO: Ve se os esses metodos declaros tem o mesmo numero de argumentos que a chamada a funcao call
					Parameter[] types =  dm.getParameters();//INFO: vai buscar os parametros
					int l = args.length;//INFO: variavel qe serve so para garantir que todos os argumentos sao testados
					for(int i = 0; i<args.length; i++){
						Class<?> paramTypes = types[i].getType();
						if(args[i].getClass().equals(paramTypes)){
							l --;
							continue;
						}//INFO: ve se o argumento e o parametro do metodo sao do mesmo tipo se nao vai ver se e da mesma super class
						//Porque se tiveres um string um metodo que receba um object pode receber um string
						Class<?> obj = args[i].getClass();
					/*	while(obj.getSuperclass() != null){
							obj = obj.getSuperclass();
							if(obj.equals(types[i].getType())){
								l --;
								break;
							}
						}*/
					//	System.out.println(obj.getName());
					//	System.out.println(paramTypes.getName());
						if(paramTypes.isAssignableFrom(obj)){
							l--;
						}
					}
					if (l == 0){ list.add(m);}//INFO: se todos os argumentos foram testados com sucesso entao esse metodo e aplicavel
				}
			}
		}
		
		return list;
	}
	
	private <T> List<GFMethod> selectBeforeMethods(T...args){
		List<GFMethod> list = new ArrayList<GFMethod>();
		//INFO: faz o mesmo so que para os befmethods
		for(GFMethod m : beforeMethods){
			for(Method dm : m.getClass().getDeclaredMethods()){
				//System.out.println(dm.toString());
				if(args.length == dm.getParameters().length){
					Parameter[] types =  dm.getParameters();
					int l = args.length;
					for(int i = 0; i<args.length; i++){
						if(args[i].getClass() == types[i].getType()){
							l --;
							continue;
						}
						Class<?> obj = args[i].getClass();
						/*while(obj.getSuperclass() != null){
							obj = obj.getSuperclass();
							if(obj.equals(types[i].getType())){
								l --;
								break;
							}
						}*/
						
						Class<?> paramTypes = types[i].getType();
						boolean isAssignedFrom = paramTypes.isAssignableFrom(obj);
						if(isAssignedFrom){
							l--;
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
		//INFO: faz o mesmo so que para os after methods
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
		//INFO: ve o nivel de cada metodo aplicavel
		//por exemplo um metodo que receba duas strings e mais especifico que um metodo que receba uma string e um object
		//logo o metodo que recebe duas strings tem um nivel mais alto
		
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
						//System.out.println(param.toGenericString() + " #### " + level);
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
/*IS ASSIGNABLE GG WELL PLAYED
 * 
 * */ 
								m.getLevelsMap().add(level);
								//System.out.println(param.toGenericString() + " #### " + level);
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
								//System.out.println(param.toGenericString() + " #### " + level);
							}
						}
					}
				}
	}
	
	private HashMap<GFMethod, Integer> sortHashMapByValues(HashMap<GFMethod, Integer> passedMap){
		HashMap<GFMethod, Integer> result = new LinkedHashMap<>();
		//INFO: funcao que ordena um hash... e so para ordenar a hash dos metodos por niveis
	    Stream<Map.Entry<GFMethod, Integer>> st = passedMap.entrySet().stream();

	    st.sorted( Map.Entry.comparingByValue() )
	        .forEachOrdered( e -> result.put(e.getKey(), e.getValue()) );

	    return result;
	}
	
	public <T> Object methodCombination( Object result, List<GFMethod> methods, List<GFMethod> befMethods, List<GFMethod> aftMethods, T...args){
		//INFO: realiza a method combination... ve a magem que esta no chat de Pava
		Object[] arguments = new Object[args.length];
		for(int i = 0; i< args.length; i++){
			arguments[i] = args[i];
		}
		
		if(befMethods.size() != 0){
			HashMap<GFMethod, Integer> orderedBefMethods = new HashMap<GFMethod, Integer>();
			for(GFMethod m : befMethods){
				int level = 0;
				for(Integer i : m.getLevelsMap()){
					level += (int)i;
				}
				//System.out.println(level);
				 orderedBefMethods.put(m, level);
			}
			orderedBefMethods = sortHashMapByValues(orderedBefMethods);
			ArrayList<GFMethod> keys = new ArrayList<GFMethod>(orderedBefMethods.keySet());
		    for(int i=keys.size()-1; i>=0;i--){
		    //	System.out.println("methodcombination before "+(int)orderedBefMethods.get(keys.get(i)));
			
				//TO-TEST (Verificar q argumentos esxistem nos metodos chamados.
				GFMethod gf_method = keys.get(i);

				try {
					for(Method mm : gf_method.getClass().getDeclaredMethods()){
						Object[] paramsPrimary = new Object[args.length];
						mm.setAccessible(true);
						for(int p = 0; p < args.length; p++) {
							 paramsPrimary[p] = mm.getParameters()[p];
							// System.out.println( mm.getParameters()[p].getType() + " %%% " + arguments[p].toString());
							// System.out.println(mm.toString());
						}
						
						mm.invoke(gf_method,  arguments);
					}
				//	System.out.println("Invoking!");
				} catch (IllegalAccessException iae) {
				    System.out.println(iae.toString());
				} catch (IllegalArgumentException iare) {
				    System.out.println(iare.toString());
				} catch (InvocationTargetException ite) {
				    System.out.println(ite.toString());
				}
		    }
		}
		
		if(methods.size() != 0){
			HashMap<GFMethod, Integer> orderedMethods = new HashMap<GFMethod, Integer>();
			for(GFMethod m : methods){
				int level = 0;
				for(Integer i : m.getLevelsMap()){
					level += (int)i;
				}
				 orderedMethods.put(m, level);
			}
			 orderedMethods = sortHashMapByValues(orderedMethods);
			 ArrayList<GFMethod> keys = new ArrayList<GFMethod>(orderedMethods.keySet());
		   //  System.out.println("methodcombination "+(int)orderedMethods.get(keys.get(keys.size()-1)));
		     GFMethod gf_method = keys.get(keys.size()-1);
		     for(Method mm : gf_method.getClass().getDeclaredMethods()){
		    	 mm.setAccessible(true);
		    	 try {
		    		 for(Parameter p : mm.getParameters()){
		    			 System.out.println("Parametet Type: " + p.getType());
		    		 }
		    		 
					result = mm.invoke(gf_method, arguments);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		     
		}
		
		if(aftMethods.size() != 0){
			HashMap<GFMethod, Integer> orderedAftMethods = new HashMap<GFMethod, Integer>();
			for(GFMethod m : aftMethods){
				int level = 0;
				for(Integer i : m.getLevelsMap()){
					level += (int)i;
				}
				orderedAftMethods.put(m, level);
			}
			orderedAftMethods = sortHashMapByValues(orderedAftMethods);
			ArrayList<GFMethod> keys = new ArrayList<GFMethod>(orderedAftMethods.keySet());
			
			for(int i = 0; i<keys.size() ;i++){
		   // 	System.out.println("Methodcombination After "+(int)orderedAftMethods.get(keys.get(i)));
			
				//TO-TEST (Verificar q argumentos esxistem nos metodos chamados.
				GFMethod gf_method = keys.get(i);

				try {
					for(Method mm : gf_method.getClass().getDeclaredMethods()){
						Object[] paramsPrimary = new Object[args.length];
						mm.setAccessible(true);
						for(int p = 0; p < args.length; p++) {
							 paramsPrimary[p] = mm.getParameters()[p];
						// System.out.println( mm.getParameters()[p].getType() + " %%% " + arguments[p].toString());
							// System.out.println(mm.toString());
						}
						
						mm.invoke(gf_method,  arguments);
					}
					//System.out.println("Invoking!");
				} catch (IllegalAccessException iae) {
				    System.out.println(iae.toString());
				} catch (IllegalArgumentException iare) {
				    System.out.println(iare.toString());
				} catch (InvocationTargetException ite) {
				    System.out.println(ite.toString());
				}
		    }
			/*
			Iterator itt = orderedAftMethods.entrySet().iterator();
			 
			 while (itt.hasNext()) {
				 Map.Entry pair = (Map.Entry)itt.next();
				 System.out.println("methodcombination after "+(int)pair.getValue());
				// itt.invoke(gf_method,  arguments);
				 //METHOD CALL
			 }*/
		}
		return result;
	}
	
	
	public <T> Object call (T...args){
		//INFO: ESTA E A FUNCAOO PRINCIPAL
		List<GFMethod> applicableMethods = selectGenericMethods(args); //INFO: Selecciona os metodos que aplicaveis a chamada da funcao call.
		List<GFMethod> applicableBeforeMethods = selectBeforeMethods(args);//INFO: Selecciona os before metodos que aplicaveis a chamada da funcao call.
		List<GFMethod> applicableAfterMethods = selectAfterMethods(args);//INFO: Selecciona os after metodos que aplicaveis a chamada da funcao call.
		Object result = new Object();
		
		if(applicableMethods.size() != 0){
		//	System.out.println("Method size: "+applicableMethods.size());
		}
		
		if(applicableBeforeMethods.size() != 0){
		//	System.out.println("BEF SIZE: "+applicableBeforeMethods.size());
		}
		
		if(applicableAfterMethods.size() != 0){
		//	System.out.println("AFTER SIZE: " +applicableAfterMethods.size());
		}
		
		levelCalculation(applicableMethods, applicableBeforeMethods, applicableAfterMethods);//INFO: Calcula o nivel de especifidade de todos os metodos que sao aplicaveis
		result = methodCombination(result, applicableMethods, applicableBeforeMethods, applicableAfterMethods, args);//INFO: faz a method combination e invoca os metodos
		//System.out.println(result);
		return result;
	}
	
	
	//TODO
		// method combination
		// profit

	public static void main(String args[]) {

		final GenericFunction add = new GenericFunction("add");

		add.addMethod(new GFMethod() {
			Object call(Integer a, Integer b) {
				return a + b;
			}});

		add.addMethod(new GFMethod() {
			Object call(Object[] a, Object[] b) {
				Object[] r = new Object[a.length];
				for (int i = 0; i < a.length; i++) {
					r[i] = add.call(a[i], b[i]);
				}
				return r;
			}});

		add.addMethod(new GFMethod() {
			Object call(Object[] a, Object b) {
				Object[] ba = new Object[a.length];
				Arrays.fill(ba, b);
				return add.call(a, ba);
			}});

		add.addMethod(new GFMethod() {
			Object call(Object a, Object b[]) {
				Object[] aa = new Object[b.length];
				Arrays.fill(aa, a);
				return add.call(aa, b);
			}});

		add.addMethod(new GFMethod() {
			Object call(String a, Object b) {
				return add.call(Integer.decode(a), b);
			}});

		add.addMethod(new GFMethod() {
			Object call(Object a, String b) {
				return add.call(a, Integer.decode(b));
			}});

		add.addMethod(new GFMethod() {
			Object call(Object[] a, List b) {
				return add.call(a, b.toArray());
			}});

		println(add.call(new Object[] { 1, 2 }, 3));
		println(add.call(1, new Object[][] { { 1, 2 }, { 3, 4 } }));
		println(add.call("12", "34"));
		println(add.call(new Object[] { "123", "4" }, 5));
		println(add.call(new Object[] { 1, 2, 3 }, Arrays.asList(4, 5, 6)));

	}

	public static void println(Object obj) {

		if (obj instanceof Object[]) {
			System.err.println(Arrays.deepToString((Object[])obj));
		} else {
			System.err.println(obj);
		}
	}
}
