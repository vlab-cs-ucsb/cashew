package modelcounting.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelcounting.analysis.exceptions.AnalysisException;
import modelcounting.utils.BigRational;
import modelcounting.utils.trie.ScheduleTrie;
import modelcounting.utils.trie.TrieVisitor;
import modelcounting.analysis.Analyzer;
import modelcounting.analysis.SchedulesHolder;

import com.google.common.collect.HashMultimap;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class SchedulesHolder {

	private HashMultimap<Schedule, String> successfulCollector;
	private HashMultimap<Schedule, String> failedCollector;

	private int numOfGreyPCs = 0;

	private HashMap<Integer, Schedule> idToSchedule;

	private ScheduleTrie scheduleTrie;
	private TrieVisitor trieVisitor = null;
	private Map<Schedule, ScheduleStatistics> statistics = null;

	public SchedulesHolder() {
		this.successfulCollector = HashMultimap.<SchedulesHolder.Schedule, String> create();
		this.failedCollector = HashMultimap.<SchedulesHolder.Schedule, String> create();

		this.scheduleTrie = new ScheduleTrie();
		this.idToSchedule = new HashMap<Integer, SchedulesHolder.Schedule>();
	}

	public void capturedGrey() {
		this.numOfGreyPCs++;
	}

	public int getNumOfGreyPCs() {
		return numOfGreyPCs;
	}

	public void addSuccessfulPC(Schedule schedule, String pc) {
		registerSchedule(schedule);
		// System.out.println("Added successful schedule: "+schedule+"\t"+pc);
		this.successfulCollector.put(schedule, pc);
	}

	public void addFailedPC(Schedule schedule, String pc) {
		registerSchedule(schedule);
		// System.out.println("Added failed schedule: "+schedule+"\t"+pc);
		this.failedCollector.put(schedule, pc);
	}

	private void registerSchedule(Schedule schedule) {
		this.idToSchedule.put(schedule.hashCode(), schedule);
		this.scheduleTrie.insert(schedule);
	}

	public Set<String> getSuccessfulPCs(Schedule schedule) {
		return this.successfulCollector.get(schedule);
	}

	public Set<String> getFailedPCs(Schedule schedule) {
		return this.failedCollector.get(schedule);
	}

	public int getNumOfSuccessfulPCsRecorded() {
		return this.successfulCollector.values().size();
	}

	public int getNumOfFailurePCsRecorded() {
		return this.failedCollector.values().size();
	}

	public Schedule getScheduleByPath(List<Integer> thIds) {
		Integer[] asArray = new Integer[thIds.size()];
		asArray = thIds.toArray(asArray);
		int correspondingHash = Schedule.hashTheSequence(asArray);
		Schedule schedule = idToSchedule.get(correspondingHash);
		return schedule;
	}

	public void analyze(Analyzer analyzer) throws AnalysisException {
		if (statistics == null) {
			if (trieVisitor == null) {
				trieVisitor = new TrieVisitor(analyzer, this, scheduleTrie);
			}
			if (!trieVisitor.isDone()) {
				trieVisitor.visit();
			}
			this.statistics = new HashMap<SchedulesHolder.Schedule, SchedulesHolder.ScheduleStatistics>(trieVisitor.getAnalyzedSchedules().size());
			computeStats(trieVisitor);
		}
	}

	public Set<Schedule> getMaximalSchedules() {
		if (trieVisitor.isDone()) {
			return trieVisitor.getMaximalSchedules();
		}
		return null;
	}

	public Set<Schedule> getAllSchedules() {
		if (trieVisitor.isDone()) {
			return trieVisitor.getAnalyzedSchedules();
		}
		return null;
	}

	public ScheduleStatistics getStatisticsPerSchedule(Schedule schedule) {
		if (statistics != null) {
			return statistics.get(schedule);
		}
		return null;
	}

	private void computeStats(TrieVisitor trieVisitor) {
		Set<Schedule> allSchedules = trieVisitor.getAnalyzedSchedules();
		for (Schedule schedule : allSchedules) {
			BigRational successProb = trieVisitor.getSuccessProbability(schedule);
			BigRational failureProb = trieVisitor.getFailureProbability(schedule);
			BigRational greyProb = trieVisitor.getGreyProbability(schedule);
			ScheduleStatistics stat = new ScheduleStatistics(successProb, failureProb, greyProb);
			statistics.put(schedule, stat);
		}
	}

	@Override
	public String toString() {
		String stats = "Collected " + this.successfulCollector.size() + " success, " + failedCollector.size() + " failed\n";
		return stats + scheduleTrie.toString();
	}

	public static class ScheduleStatistics {
		private final BigRational successProbability;
		private final BigRational failureProbability;
		private final BigRational greyProbability;
		private String lazyToString = null;
		private Integer hashCode = null;

		public ScheduleStatistics(BigRational successProbability, BigRational failureProbability, BigRational greyProbability) {
			super();
			this.successProbability = successProbability;
			this.failureProbability = failureProbability;
			this.greyProbability = greyProbability;
		}

		public BigRational getSuccessProbability() {
			return successProbability;
		}

		public BigRational getFailureProbability() {
			return failureProbability;
		}

		public BigRational getGreyProbability() {
			return greyProbability;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ScheduleStatistics) {
				if (!successProbability.equals(((ScheduleStatistics) obj).successProbability)) {
					return false;
				}
				if (!failureProbability.equals(((ScheduleStatistics) obj).failureProbability)) {
					return false;
				}
				if (!greyProbability.equals(((ScheduleStatistics) obj).greyProbability)) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			if (this.hashCode == null) {
				HashFunction hf = Hashing.sha512();
				List<HashCode> hashCodes = new ArrayList<HashCode>(3);
				Hasher hasher = hf.newHasher();
				hasher.putInt(successProbability.hashCode());
				HashCode hc = hasher.hash();
				hashCodes.add(hc);
				hasher = hf.newHasher();
				hasher.putInt(failureProbability.hashCode());
				hc = hasher.hash();
				hashCodes.add(hc);
				hasher = hf.newHasher();
				hasher.putInt(greyProbability.hashCode());
				hc = hasher.hash();
				hashCodes.add(hc);
				HashCode combinedOrdered = Hashing.combineOrdered(hashCodes);
				this.hashCode = combinedOrdered.asInt();
			}
			return this.hashCode;
		}

		@Override
		public String toString() {
			if (lazyToString == null) {
				StringBuilder stringBuilder = new StringBuilder();
				double successDouble = (successProbability == null) ? -1.0 : successProbability.doubleValue();
				stringBuilder.append("\tSuccess probability: " + successProbability + " (" + successDouble + ")");

				double failureDouble = (failureProbability == null) ? -1.0 : failureProbability.doubleValue();
				stringBuilder.append("\tFailure probability: " + failureProbability + " (" + failureDouble + ")");

				double greyDouble = (greyProbability == null) ? -1.0 : greyProbability.doubleValue();
				stringBuilder.append("\tGrey probability:    " + greyProbability + " (" + greyDouble + ")");

				stringBuilder.append("\tSum: " + (successProbability.plus(failureProbability).plus(greyProbability)) + '\n');
				lazyToString = stringBuilder.toString();
			}
			return lazyToString;
		}
	}

	public static class Schedule {
		final int scheduleId;
		final int[] sequenceOfChoices;
		String lazyToString = null;

		public Schedule(int[] threadIdsSequence) {
			super();
			this.sequenceOfChoices = threadIdsSequence;
			this.scheduleId = hashTheSequence(threadIdsSequence);
		}

		public int getScheduleId() {
			return scheduleId;
		}

		public int[] getSequenceOfChoices() {
			return sequenceOfChoices;
		}

		@Override
		public int hashCode() {
			return scheduleId;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Schedule) {
				if (Arrays.equals(sequenceOfChoices, ((Schedule) obj).sequenceOfChoices)) {
					// TODO: Just for debugging hashcode
					assert (hashCode() == obj.hashCode());
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			if (lazyToString == null) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(scheduleId + ":{");
				for (Integer threadId : sequenceOfChoices) {
					stringBuilder.append(threadId + ",");
				}
				stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
				stringBuilder.append('}');
				lazyToString = stringBuilder.toString();
			}
			return lazyToString;
		}

		public static final int hashTheSequence(int[] seq) {
			HashFunction hf = Hashing.sha512();
			Hasher hasher = hf.newHasher();
			for (int i = 0; i < seq.length; i++) {
				hasher.putInt(seq[i]);
			}
			HashCode hashCode = hasher.hash();
			return hashCode.asInt();
		}

		public static final int hashTheSequence(Integer[] seq) {
			HashFunction hf = Hashing.sha512();
			Hasher hasher = hf.newHasher();
			for (int i = 0; i < seq.length; i++) {
				hasher.putInt(seq[i]);
			}
			HashCode hashCode = hasher.hash();
			return hashCode.asInt();
		}
	}

}
