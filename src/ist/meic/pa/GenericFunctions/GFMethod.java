package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GFMethod {
	
	private TreeMap <Class<?> , Integer> levels;
	private List<Integer> lvl = new ArrayList<Integer>();
	
	GFMethod(){ levels = new TreeMap<Class<?>, Integer> (); }
	
	public List<Integer> getLevelsMap(){
		return lvl;
	}
	
	public TreeMap<Class<?> , Integer> getLevels(){
		return levels;
	}

}
