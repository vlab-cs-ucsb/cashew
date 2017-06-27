package modelcounting.domain;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class VarDomain {

	public String var;
	public long upperBound;
	public long lowerBound;
	
	public VarDomain(String var, long upperBound, long lowerBound){
		this.var = var;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}
}
