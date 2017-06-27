package modelcounting.caching;
import java.util.Properties;


public class SecondLevelCacheDefaultProperties {
	private static final Properties properties = createSingleton();
	
	public static final Properties getDefaultProperties(){
		return new Properties(properties);
	}
	
	private static final Properties createSingleton(){
		Properties properties = new Properties();
		properties.setProperty("jcs.default","indexedDiskCache");
		properties.setProperty("jcs.default.cacheattributes","org.apache.commons.jcs.engine.CompositeCacheAttributes");
		properties.setProperty("jcs.default.cacheattributes.MaxObjects","100000");
		properties.setProperty("jcs.default.cacheattributes.MemoryCacheName","org.apache.commons.jcs.engine.memory.lru.LRUMemoryCache");

		properties.setProperty("jcs.region.latte","indexedDiskCache");
		properties.setProperty("jcs.region.latte.cacheattributes","org.apache.commons.jcs.engine.CompositeCacheAttributes");
		properties.setProperty("jcs.region.latte.cacheattributes.MaxObjects","100000");
		properties.setProperty("jcs.region.latte.cacheattributes.MemoryCacheName","org.apache.commons.jcs.engine.memory.lru.LRUMemoryCache");

		properties.setProperty("jcs.region.omega","indexedDiskCache");
		properties.setProperty("jcs.region.omega.cacheattributes","org.apache.commons.jcs.engine.CompositeCacheAttributes");
		properties.setProperty("jcs.region.omega.cacheattributes.MaxObjects","100000");
		properties.setProperty("jcs.region.omega.cacheattributes.MemoryCacheName","org.apache.commons.jcs.engine.memory.lru.LRUMemoryCache");


		properties.setProperty("jcs.auxiliary.indexedDiskCache","org.apache.commons.jcs.auxiliary.disk.indexed.IndexedDiskCacheFactory");
		properties.setProperty("jcs.auxiliary.indexedDiskCache.attributes","org.apache.commons.jcs.auxiliary.disk.indexed.IndexedDiskCacheAttributes");
		properties.setProperty("jcs.auxiliary.indexedDiskCache.attributes.DiskPath","cache");
		properties.setProperty("jcs.auxiliary.indexedDiskCache.attributes.MaxPurgatorySize","100000");
		properties.setProperty("jcs.auxiliary.indexedDiskCache.attributes.MaxKeySize","100000");
		properties.setProperty("jcs.auxiliary.indexedDiskCache.attributes.MaxRecycleBinSize","500000");
		properties.setProperty("jcs.auxiliary.indexedDiskCache.attributes.OptimizeAtRemoveCount","300000");
		properties.setProperty("jcs.auxiliary.indexedDiskCache.attributes.EventQueueType","SINGLE");
		properties.setProperty("jcs.auxiliary.indexedDiskCache.attributes.EventQueuePoolName","disk_cache_event_queue");


		properties.setProperty("thread_pool.default.boundarySize","20000");
		properties.setProperty("thread_pool.default.maximumPoolSize","150");
		properties.setProperty("thread_pool.default.minimumPoolSize","4");
		properties.setProperty("thread_pool.default.keepAliveTime","3500000000");

		properties.setProperty("thread_pool.default.whenBlockedPolicy","RUN");
		properties.setProperty("thread_pool.default.startUpSize","4");


		properties.setProperty("thread_pool.cache_event_queue.useBoundary","false");
		properties.setProperty("thread_pool.cache_event_queue.minimumPoolSize","1");
		properties.setProperty("thread_pool.cache_event_queue.keepAliveTime","3500000");
		properties.setProperty("thread_pool.cache_event_queue.startUpSize","1");

		properties.setProperty("thread_pool.disk_cache_event_queue.useBoundary","false");
		properties.setProperty("thread_pool.disk_cache_event_queue.minimumPoolSize","2");
		properties.setProperty("thread_pool.disk_cache_event_queue.keepAliveTime","3500000");
		properties.setProperty("thread_pool.disk_cache_event_queue.startUpSize","10");

		properties.setProperty("thread_pool.remote_cache_client.boundarySize","75");
		properties.setProperty("thread_pool.remote_cache_client.maximumPoolSize","150");
		properties.setProperty("thread_pool.remote_cache_client.minimumPoolSize","4");
		properties.setProperty("thread_pool.remote_cache_client.keepAliveTime","350000");
		properties.setProperty("thread_pool.remote_cache_client.whenBlockedPolicy","RUN");
		properties.setProperty("thread_pool.remote_cache_client.startUpSize","4");

		return properties;
	}
	
}
