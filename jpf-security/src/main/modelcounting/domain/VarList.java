package modelcounting.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import modelcounting.domain.VarList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

public class VarList implements Serializable{
	private static final long serialVersionUID = -218361512;
	private final ImmutableList<String> list;

	public VarList() {
		this.list = ImmutableList.<String> of();
	}

	public VarList(String var) {
		this.list = ImmutableList.<String> of(var);
	}

	public VarList(Collection<String> vars) {
		HashSet<String> duplicateCleaner = new HashSet<String>(vars);
		List<String> sorted = new ArrayList<String>(duplicateCleaner);
		Collections.sort(sorted);
		this.list = ImmutableList.<String> copyOf(sorted);
	}

	public List<String> asList() {
		return this.list;
	}

	public VarList add(String... newVars) {
		HashSet<String> temp = Sets.newHashSet(newVars);
		temp.addAll(list);
		return new VarList(temp);
	}

	public VarList add(Collection<String> newVars) {
		HashSet<String> temp = Sets.newHashSet(newVars);
		temp.addAll(list);
		return new VarList(temp);
	}

	@Override
	public String toString() {
		return list.toString();
	}
}
