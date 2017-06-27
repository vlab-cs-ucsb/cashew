package modelcounting.caching;

import java.util.Properties;
import java.util.Set;

import modelcounting.domain.Problem;
import modelcounting.utils.BigRational;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.behavior.IElementAttributes;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;

public class SecondLevelCache {
	public final CacheAccess<String, BigRational> jcsLatte;
	public final CacheAccess<String, Set<Problem>> jcsOmega;
	private final IElementAttributes latteAttributes;
	private final IElementAttributes omegaAttributes;

	public SecondLevelCache(String path) {
		Properties cacheProperties = SecondLevelCacheDefaultProperties.getDefaultProperties();
		cacheProperties.setProperty("jcs.auxiliary.indexedDiskCache.attributes.DiskPath", path);
		JCS.setConfigProperties(cacheProperties);
		this.jcsLatte = JCS.getInstance("latte");
		this.jcsOmega = JCS.getInstance("omega");
		
		IElementAttributes attributes = jcsLatte.getDefaultElementAttributes();
		attributes.setIsEternal(true);
		latteAttributes=attributes;
		attributes = jcsOmega.getDefaultElementAttributes();
		attributes.setIsEternal(true);
		omegaAttributes=attributes;
		
	}

	public final void putLatte(Problem problem, BigRational count) {
		jcsLatte.put("problem"+problem.toString().hashCode(), count, latteAttributes);
	}

	public final BigRational getLatte(Problem problem) {
		return jcsLatte.get("problem"+problem.toString().hashCode());
	}

	public final void putOmega(Problem problem, Set<Problem> subProblems) {
		jcsOmega.put("problem"+problem.toString().hashCode(), subProblems,omegaAttributes);
	}

	public final Set<Problem> getOmega(Problem problem) {
		return jcsOmega.get("problem"+problem.toString().hashCode());
	}

	public final void shutdown() {
		CompositeCacheManager cacheMgr = CompositeCacheManager.getInstance();
		cacheMgr.shutDown();
	}

	public final String stats(){
		StringBuilder stats = new StringBuilder();
		stats.append("Omega stats:\n");
		stats.append(jcsOmega.getStats()+'\n');
		stats.append("Latte stats:\n");
		stats.append(jcsLatte.getStats()+'\n');
		return stats.toString();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		shutdown();
	}
}
