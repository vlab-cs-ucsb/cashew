package modelcounting.utils.random;

import modelcounting.utils.random.RandomIdGenerator;

public class TimeBasedRandomIdGenerator implements RandomIdGenerator {

	public String getRandomId() {
		return Long.valueOf(System.currentTimeMillis() + System.nanoTime()).toString();
	}

}
