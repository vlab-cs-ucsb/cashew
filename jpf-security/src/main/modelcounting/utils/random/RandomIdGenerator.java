package modelcounting.utils.random;

import modelcounting.utils.random.TimeBasedRandomIdGenerator;

import com.google.inject.ImplementedBy;

@ImplementedBy(TimeBasedRandomIdGenerator.class)
public interface RandomIdGenerator {
	public String getRandomId();
}
