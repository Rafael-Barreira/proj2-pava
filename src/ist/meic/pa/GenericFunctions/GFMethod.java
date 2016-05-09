package ist.meic.pa.GenericFunctions;

import java.util.HashMap;

public class GFMethod {
	
	private HashMap <Class<?> , Integer> levels;
	
	GFMethod(){ levels = new HashMap<Class<?>, Integer> (); }
	
	public HashMap <Class<?> , Integer> getLevelsMap(){
		return levels;
	}

}
