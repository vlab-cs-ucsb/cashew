package modelcounting.analysis.exceptions;

public class EmptyDomainException extends Exception {

	private static final long serialVersionUID = 2148886892219159025L;

	public EmptyDomainException() {
		super();
	}

	public EmptyDomainException(String message) {
		super(message);
	}
}