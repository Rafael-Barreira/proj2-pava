package ist.meic.pa.GenericFunctions;

import java.util.TreeMap;

public class GFMethod {
	
	private TreeMap <Class<?> , Integer> levels;
	
	GFMethod(){ levels = new TreeMap<Class<?>, Integer> (); }
	
	public TreeMap <Class<?> , Integer> getLevelsMap(){
		return levels;
	}

}
