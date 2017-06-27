package modelcounting.utils.trie;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import modelcounting.analysis.Analyzer;
import modelcounting.analysis.SchedulesHolder;
import modelcounting.analysis.SchedulesHolder.Schedule;
import modelcounting.analysis.exceptions.AnalysisException;
import modelcounting.utils.BigRational;
import modelcounting.utils.trie.ScheduleTrie.TrieNode;
import modelcounting.utils.trie.ScheduleTrie;

import com.google.common.collect.Sets;

public class TrieVisitor {
	private final Analyzer analyzer;
	private final HashMap<Schedule, BigRational> successProbabilities;
	private final HashMap<Schedule, BigRational> failureProbabilities;
	private final HashMap<Schedule, BigRational> greyProbabilities;
	private final ScheduleTrie scheduleTrie;
	private final SchedulesHolder schedulesHolder;

	private Set<Schedule> analyzedSchedules;
	private Set<Schedule> maximalSchedules;

	private boolean successOnly = true;

	private boolean done = false;

	public TrieVisitor(Analyzer analyzer, SchedulesHolder schedulesHolder, ScheduleTrie scheduleTrie) {
		super();
		this.analyzer = analyzer;
		this.scheduleTrie = scheduleTrie;
		this.successProbabilities = new HashMap<SchedulesHolder.Schedule, BigRational>();
		this.failureProbabilities = new HashMap<SchedulesHolder.Schedule, BigRational>();
		this.greyProbabilities = new HashMap<SchedulesHolder.Schedule, BigRational>();
		this.schedulesHolder = schedulesHolder;
		this.analyzedSchedules = new HashSet<SchedulesHolder.Schedule>();
		this.maximalSchedules = new HashSet<SchedulesHolder.Schedule>();
	}

	public Set<Schedule> getAnalyzedSchedules() {
		return Sets.newHashSet(analyzedSchedules);
	}

	public Set<Schedule> getMaximalSchedules() {
		return Sets.newHashSet(maximalSchedules);
	}

	public BigRational getSuccessProbability(Schedule schedule) {
		return this.successProbabilities.get(schedule);
	}

	public BigRational getFailureProbability(Schedule schedule) {
		return this.failureProbabilities.get(schedule);
	}

	public BigRational getGreyProbability(Schedule schedule) {
		return this.greyProbabilities.get(schedule);
	}

	public void visit() throws AnalysisException {
		if (!done) {
			this.successOnly = schedulesHolder.getNumOfFailurePCsRecorded() > schedulesHolder.getNumOfSuccessfulPCsRecorded();
			if (scheduleTrie == null || scheduleTrie.size() == 0 || scheduleTrie.getRoot() == null) {
				throw new RuntimeException("No success or failure collected. Search depth must be incresed otherwise everything is grey.");
			}
			visit(new LinkedList<Integer>(), scheduleTrie.getRoot(), BigRational.ZERO, BigRational.ZERO);
			done = true;
		}
	}

	public boolean isDone() {
		return done;
	}

	private void visit(List<Integer> prefix, modelcounting.utils.trie.ScheduleTrie.TrieNode trieNode, BigRational successSoFarI, BigRational failureSoFarI) throws AnalysisException {
		if (trieNode == null) {
			return;
		}
		BigRational successProbability = successSoFarI;
		BigRational failureProbability = failureSoFarI;

		List<Integer> path = new LinkedList<Integer>();
		path.addAll(prefix);
		path.add(trieNode.getCurrentId());

		if (path.get(0).equals(-1)) {
			// -1 is only for root, do not represent any real id
			path.remove(0);
		}
		Schedule schedule = schedulesHolder.getScheduleByPath(path);
		Set<String> successPCs = schedulesHolder.getSuccessfulPCs(schedule);
		Set<String> failedPCs = schedulesHolder.getFailedPCs(schedule);

		if (schedule != null) {

			if (schedulesHolder.getNumOfGreyPCs() > 0 || (schedulesHolder.getNumOfGreyPCs() == 0 && successOnly)) {
				if (successPCs != null && !successPCs.isEmpty()) {
					BigRational successTracesProb = analyzer.analyzeSetOfSpfPC(successPCs);
					successProbability = successProbability.plus(successTracesProb);
				}
			}
			if (schedulesHolder.getNumOfGreyPCs() > 0 || (schedulesHolder.getNumOfGreyPCs() == 0 && !successOnly)) {
				if (failedPCs != null && !failedPCs.isEmpty()) {
					BigRational failedTracesProb = analyzer.analyzeSetOfSpfPC(failedPCs);
					failureProbability = failureProbability.plus(failedTracesProb);
				}
			}
		}

		if (trieNode != null) {
			Set<TrieNode> children = trieNode.getChildren();
			if (children == null || children.isEmpty()) {
				if (schedule == null) {
					String scheduleRepresentation = (schedule == null) ? "null" : schedule.toString();
					throw new RuntimeException("A maximal schedule cannot be invalid: " + scheduleRepresentation);
				}

				if (schedulesHolder.getNumOfGreyPCs() == 0 && successOnly) {
					failureProbability = BigRational.ONE.minus(successProbability);
				}

				if (schedulesHolder.getNumOfGreyPCs() == 0 && !successOnly) {
					successProbability = BigRational.ONE.minus(failureProbability);
				}
				// this way only maximal are recorded!
				BigRational greyProbability = BigRational.ONE.minus(successProbability).minus(failureProbability);
				this.successProbabilities.put(schedule, successProbability);
				this.failureProbabilities.put(schedule, failureProbability);
				this.greyProbabilities.put(schedule, greyProbability);
				this.analyzedSchedules.add(schedule);
				this.maximalSchedules.add(schedule);
			} else {
				for (TrieNode node : children) {
					visit(path, node, successProbability, failureProbability);
				}
			}
		}
	}

}
