package modelcounting.analysis;

import java.util.Set;

import modelcounting.analysis.exceptions.AnalysisException;
import modelcounting.analysis.exceptions.EmptyDomainException;
import modelcounting.domain.Problem;
import modelcounting.utils.BigRational;

public interface Analyzer {

	public BigRational analyzeSpfPC(String pc) throws AnalysisException;

	public BigRational analyzeSetOfSpfPC(Set<String> pcs) throws AnalysisException;

	public BigRational countPointsOfPC(String pc) throws AnalysisException;

	public BigRational countPointsOfSetOfPCs(Set<String> pcs) throws AnalysisException;

	public BigRational getDomainSize() throws AnalysisException;

	public Set<Problem> excludeFromDomain(String pc) throws AnalysisException, EmptyDomainException;

	public Set<Problem> excludeFromDomain(Set<String> pcs) throws AnalysisException, EmptyDomainException;

	// public Set<Problem> complementProblem(Problem problem) throws
	// AnalysisException;

	public void terminate();

}