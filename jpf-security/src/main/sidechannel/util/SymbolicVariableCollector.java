package sidechannel.util;

import gov.nasa.jpf.symbc.numeric.Constraint;
import gov.nasa.jpf.symbc.numeric.ConstraintExpressionVisitor;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;

import java.util.HashSet;
import java.util.Set;

/**
 * A visitor to collect all symbolic variables
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class SymbolicVariableCollector extends ConstraintExpressionVisitor {

	public HashSet<String> setOfSymVar;
	
	public SymbolicVariableCollector(HashSet<String> set){
		setOfSymVar = set;
	}
	
	@Override
	public void preVisit(SymbolicInteger expr) {
		String name = cleanSymbol(expr.toString());
		setOfSymVar.add(name);
	}

	public void collectVariables(PathCondition pc){
		Constraint c = pc.header;
		while(c != null){
			c.accept(this);
			c = c.getTail();
		}
	}
	
	private static String cleanSymbol(String str) {
		return str.replaceAll("\\[(.*?)\\]", ""); // remove e.g. [-1000000]
	}
	
	public Set<String> getListOfVariables(){
		return setOfSymVar;
	}
	
	public SymbolicVariableCollector projectH(){
		HashSet<String> result = new HashSet<String>();
		for(String var : setOfSymVar){
			if(var.contains("h")){
				result.add(var);
			}
		}
		return new SymbolicVariableCollector(result);
	}
	
	public int size(){
		return setOfSymVar.size();
	}
}
