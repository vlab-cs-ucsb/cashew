package modelcounting.utils.trie;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import modelcounting.analysis.SchedulesHolder.Schedule;

import com.google.common.collect.Sets;

public class ScheduleTrie {

	final TrieNode root;
	// current number of unique schedules in trie
	private int size;

	public ScheduleTrie() {
		root = new TrieNode(-1);
		size = 0;
	}

	public int insert(Schedule schedule) {
		if (schedule == null) {
			return 0;
		}

		int i = root.insert(schedule, 0);

		if (i == 1) {
			size++;
		}
		return i;
	}

	public boolean remove(Schedule schedule) {
		if (schedule == null) {
			return false;
		}

		if (root.remove(schedule, 0)) {
			size--;
			return true;
		}

		return false;
	}

	public int frequency(Schedule schedule) {
		if (schedule == null) {
			return 0;
		}

		TrieNode n = root.lookup(schedule, 0);
		return n == null ? 0 : n.occurences;
	}

	public boolean contains(Schedule schedule) {
		if (schedule == null) {
			return false;
		}

		return root.lookup(schedule, 0) != null;
	}

	public int size() {
		return size;
	}

	public TrieNode getRoot() {
		return this.root;
	}

	@Override
	public String toString() {
		return root.toString();
	}

	public static class TrieNode {
		private int currentId;
		private int occurences;
		Map<Integer, TrieNode> children;

		TrieNode(int currentId) {
			this.currentId = currentId;
			occurences = 0;
			children = null;
		}

		public int getCurrentId() {
			return this.currentId;
		}

		public Set<TrieNode> getChildren() {
			if (this.children == null) {
				return null;
			}
			return Sets.newHashSet(this.children.values());
		}

		public int getNumOccurrences() {
			return this.occurences;
		}

		int insert(Schedule schedule, int pos) {
			if (schedule == null || pos >= schedule.getSequenceOfChoices().length) {
				return 0;
			}

			if (children == null) {
				children = new HashMap<Integer, TrieNode>();
			}

			int threadIdAtCurrentPos = schedule.getSequenceOfChoices()[pos];
			TrieNode candidateChildNode = children.get(threadIdAtCurrentPos);

			if (candidateChildNode == null) {
				candidateChildNode = new TrieNode(threadIdAtCurrentPos);
				children.put(threadIdAtCurrentPos, candidateChildNode);
			}

			if (pos == schedule.getSequenceOfChoices().length - 1) {
				candidateChildNode.occurences++;
				return candidateChildNode.occurences;
			} else
				return candidateChildNode.insert(schedule, pos + 1);
		}

		boolean remove(Schedule schedule, int pos) {
			if (children == null || schedule == null) {
				return false;
			}

			int threadIdAtCurrentPos = schedule.getSequenceOfChoices()[pos];
			TrieNode candidateChildNode = children.get(threadIdAtCurrentPos);

			if (candidateChildNode == null) {
				return false;
			}

			boolean ret;
			if (pos == schedule.getSequenceOfChoices().length - 1) {
				int before = candidateChildNode.occurences;
				candidateChildNode.occurences = 0;
				ret = before > 0;
			} else {
				ret = candidateChildNode.remove(schedule, pos + 1);
			}

			// if we just removed a leaf, prune upwards
			if (candidateChildNode.children == null && candidateChildNode.occurences == 0) {
				children.remove(candidateChildNode.currentId);
				if (children.size() == 0)
					children = null;
			}

			return ret;
		}

		TrieNode lookup(Schedule schedule, int pos) {
			if (schedule == null)
				return null;

			if (pos >= schedule.getSequenceOfChoices().length || children == null)
				return null;
			else if (pos == schedule.getSequenceOfChoices().length - 1)
				return children.get(schedule.getSequenceOfChoices()[pos]);
			else {
				TrieNode candidateChildNode = children.get(schedule.getSequenceOfChoices()[pos]);
				return candidateChildNode == null ? null : candidateChildNode.lookup(schedule, pos + 1);
			}
		}

		@Override
		public String toString() {
			return toString("");
		}

		private String toString(String prefix) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(prefix + '[' + this.currentId + "]\n");
			if (children != null) {
				for (TrieNode trieNode : children.values()) {
					stringBuilder.append(prefix + '\t' + trieNode.toString(prefix + '\t'));
				}
			}
			return stringBuilder.toString();
		}
	}

}
