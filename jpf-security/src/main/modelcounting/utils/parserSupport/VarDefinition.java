package modelcounting.utils.parserSupport;

public class VarDefinition {
	private final String name;
	private final Long lowerBound;
	private final Long upperBound;

	public VarDefinition(String name, Long lowerBound, Long upperBound) {
		super();
		this.name = name;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public String getName() {
		return name;
	}

	public Long getLowerBound() {
		return lowerBound;
	}

	public Long getUpperBound() {
		return upperBound;
	}

}
