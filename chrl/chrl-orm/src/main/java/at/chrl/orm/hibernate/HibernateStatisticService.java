package at.chrl.orm.hibernate;

import static java.util.Objects.isNull;

import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.CollectionStatistics;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.NaturalIdCacheStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;

import at.chrl.nutils.ArrayUtils;
import at.chrl.nutils.JVMInfoUtil;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;

/**
 * 
 * @author Vinzynth
 * 26.02.2015 - 22:47:01
 *
 */
public final class HibernateStatisticService {
	
	public static String[] getFullStatistics(final IHibernateConfig hconfig){
		if(isNull(hconfig))
			throw new NullPointerException("Parameter hconfig is null");
		
		SessionFactory sf = HibernateService.getInstance().getSessionFactory(hconfig);
		
		if(isNull(sf))
			throw new NullPointerException("Could not obtain SessionFactory for Config: " + hconfig);
		
		String[] header = new String[]{ JVMInfoUtil.printSection(hconfig.toString()) };
		
		return
		ArrayUtils.addAll(
		ArrayUtils.addAll(
		ArrayUtils.addAll(
		ArrayUtils.addAll(
		ArrayUtils.addAll(header, 	getSessionFactoryStatistics(sf)),
									getEntityStatistics(hconfig)),
									getCollectionStatistics(hconfig)),
									getCacheStatistics(hconfig)),
									getQueryStatistics(hconfig));
	}
	
	public static String[] getSessionFactoryStatistics(final IHibernateConfig hconfig){
		if(isNull(hconfig))
			throw new NullPointerException("Parameter hconfig is null");
		
		SessionFactory sf = HibernateService.getInstance().getSessionFactory(hconfig);
		
		if(isNull(sf))
			throw new NullPointerException("Could not obtain SessionFactory for Config: " + hconfig);
		
		String[] header = new String[]{ JVMInfoUtil.printSection(hconfig.toString()) };
		
		return ArrayUtils.addAll(header, getSessionFactoryStatistics(sf));
	}
	
	public static String[] getEntityStatistics(final IHibernateConfig hconfig){
		if(isNull(hconfig))
			throw new NullPointerException("Parameter hconfig is null");
		
		SessionFactory sf = HibernateService.getInstance().getSessionFactory(hconfig);
		
		if(isNull(sf))
			throw new NullPointerException("Could not obtain SessionFactory for Config: " + hconfig);
		
		final Statistics stats = sf.getStatistics();

		String[] header = new String[]{ JVMInfoUtil.printSection("Entities for: " + hconfig.toString()) };
		return Arrays.stream(stats.getEntityNames()).reduce(header, (a, s) -> ArrayUtils.addAll(a, getEntityStatistics(sf, s)), ArrayUtils::addAll);
	}
	
	public static String[] getEntityStatistics(final SessionFactory sessionFactory, final String entityName){
		if(isNull(sessionFactory))
			throw new NullPointerException("Parameter sessionFactory is null");
		if(isNull(entityName))
			throw new NullPointerException("Parameter entityName is null");
		if(entityName.isEmpty())
			throw new IllegalArgumentException("Parameter entityName is a empty String");
		
		final Statistics sfstats = sessionFactory.getStatistics();
		
		if(!ArrayUtils.contains(sfstats.getEntityNames(), entityName))
			throw new IllegalArgumentException("Entity " + entityName + " is not managed by given SessionFactory");
		
		final EntityStatistics stats = sfstats.getEntityStatistics(entityName);
		
		return new String[]{
				 "+ Entity Statistics: " + entityName,
				 "+",
				 "+ Fetched: " + stats.getFetchCount(),
				 "+ Inserted: " + stats.getInsertCount(),
				 "+ Loaded: " + stats.getLoadCount(),
				 "+ Updated: " + stats.getUpdateCount(),
				 "+ Deleted: " + stats.getDeleteCount(),
				 "+ Optimistic Lock Failures: " + stats.getOptimisticFailureCount(),
				 "+",
				 "+"
			};
	}
	
	public static String[] getCollectionStatistics(final IHibernateConfig hconfig){
		if(isNull(hconfig))
			throw new NullPointerException("Parameter hconfig is null");
		
		SessionFactory sf = HibernateService.getInstance().getSessionFactory(hconfig);
		
		if(isNull(sf))
			throw new NullPointerException("Could not obtain SessionFactory for Config: " + hconfig);
		
		final Statistics stats = sf.getStatistics();

		String[] header = new String[]{ JVMInfoUtil.printSection("Collections for: " + hconfig.toString()) };
		return Arrays.stream(stats.getCollectionRoleNames()).reduce(header, (a, s) -> ArrayUtils.addAll(a, getCollectionStatistics(sf, s)), ArrayUtils::addAll);
	}
	
	public static String[] getCollectionStatistics(final SessionFactory sessionFactory, final String collectionRoleName){
		if(isNull(sessionFactory))
			throw new NullPointerException("Parameter sessionFactory is null");
		if(isNull(collectionRoleName))
			throw new NullPointerException("Parameter collectionRoleName is null");
		if(collectionRoleName.isEmpty())
			throw new IllegalArgumentException("Parameter collectionRoleName is a empty String");
		
		final Statistics sfstats = sessionFactory.getStatistics();
		
		if(!ArrayUtils.contains(sfstats.getCollectionRoleNames(), collectionRoleName))
			throw new IllegalArgumentException("Collection Role " + collectionRoleName + " is not managed by given SessionFactory");
		
		final CollectionStatistics stats = sfstats.getCollectionStatistics(collectionRoleName);
		
		return new String[]{
				 "+ Collection Statistics: " + collectionRoleName,
				 "+",
				 "+ Fetched: " + stats.getFetchCount(),
				 "+ Recreated: " + stats.getRecreateCount(),
				 "+ Loaded: " + stats.getLoadCount(),
				 "+ Updated: " + stats.getUpdateCount(),
				 "+ Removed: " + stats.getRemoveCount(),
				 "+",
				 "+"
			};
	}
	
	public static String[] getCacheStatistics(final IHibernateConfig hconfig){
		if(isNull(hconfig))
			throw new NullPointerException("Parameter hconfig is null");
		
		SessionFactory sf = HibernateService.getInstance().getSessionFactory(hconfig);
		
		if(isNull(sf))
			throw new NullPointerException("Could not obtain SessionFactory for Config: " + hconfig);
		
		final Statistics stats = sf.getStatistics();

		String[] header = new String[]{ JVMInfoUtil.printSection("Cache for: " + hconfig.toString()) };
		return Arrays.stream(stats.getSecondLevelCacheRegionNames()).reduce(header, (a, s) -> ArrayUtils.addAll(a, getCacheStatistics(sf, s)), ArrayUtils::addAll);
	}
	
	public static String[] getCacheStatistics(final SessionFactory sessionFactory, final String cacheRegion){
		if(isNull(sessionFactory))
			throw new NullPointerException("Parameter sessionFactory is null");
		if(isNull(cacheRegion))
			throw new NullPointerException("Parameter cacheRegion is null");
		if(cacheRegion.isEmpty())
			throw new IllegalArgumentException("Parameter cacheRegion is a empty String");
		
		final Statistics sfstats = sessionFactory.getStatistics();
		
		if(!ArrayUtils.contains(sfstats.getSecondLevelCacheRegionNames(), cacheRegion))
			throw new IllegalArgumentException("Cache Region" + cacheRegion + " is not managed by given SessionFactory");
		
		final SecondLevelCacheStatistics stats = sfstats.getSecondLevelCacheStatistics(cacheRegion);
		final NaturalIdCacheStatistics idstats = sfstats.getNaturalIdCacheStatistics(cacheRegion);
		
		return new String[]{
				 "+ 2nd Level Cache Statistics: " + cacheRegion,
				 "+",
				 "+ # Puts: " + stats.getPutCount(),
				 "+ # Hits: " + stats.getHitCount(),
				 "+ # Misses: " + stats.getMissCount(),
				 "+ Hit Ratio: " + ((double)stats.getHitCount())/(stats.getHitCount() + stats.getMissCount()),
				 "+ Size in Memory: " + stats.getSizeInMemory(),
				 "+ Elements in Memory: " + stats.getElementCountInMemory(),
				 "+ Elements on Disk: " + stats.getElementCountOnDisk(),
				 "+",
				 "+ ID # Puts: " + idstats.getPutCount(),
				 "+ ID # Hits: " + idstats.getHitCount(),
				 "+ ID # Misses: " + idstats.getMissCount(),
				 "+ ID Hit Ratio: " + ((double)idstats.getHitCount())/(idstats.getHitCount() + idstats.getMissCount()),
				 "+ ID Size in Memory: " + idstats.getSizeInMemory(),
				 "+ ID Elements in Memory: " + idstats.getElementCountInMemory(),
				 "+ ID Execution Count: " + idstats.getExecutionCount(),
				 "+ ID Avg Execution Time: " + idstats.getExecutionAvgTime() + "ms",
				 "+ ID Max Execution Time: " + idstats.getExecutionMaxTime() + "ms",
				 "+ ID Min Execution Time: " + idstats.getExecutionMinTime() + "ms",
				 "+",
				 "+"
			};
	}
	
	public static String[] getQueryStatistics(final IHibernateConfig hconfig){
		if(isNull(hconfig))
			throw new NullPointerException("Parameter hconfig is null");
		
		SessionFactory sf = HibernateService.getInstance().getSessionFactory(hconfig);
		
		if(isNull(sf))
			throw new NullPointerException("Could not obtain SessionFactory for Config: " + hconfig);
		
		final Statistics stats = sf.getStatistics();

		String[] header = new String[]{ JVMInfoUtil.printSection("Querries for: " + hconfig.toString()) };
		return Arrays.stream(stats.getQueries()).reduce(header, (a, s) -> ArrayUtils.addAll(a, getQueryStatistics(sf, s)), ArrayUtils::addAll);
	}
	
	public static String[] getQueryStatistics(final SessionFactory sessionFactory, final String queryString){
		if(isNull(sessionFactory))
			throw new NullPointerException("Parameter sessionFactory is null");
		if(isNull(queryString))
			throw new NullPointerException("Parameter queryString is null");
		if(queryString.isEmpty())
			throw new IllegalArgumentException("Parameter queryString is a empty String");
		
		final Statistics sfstats = sessionFactory.getStatistics();
		
		if(!ArrayUtils.contains(sfstats.getQueries(), queryString))
			throw new IllegalArgumentException("Query " + queryString + " was not executed by given SessionFactory");
		
		final QueryStatistics stats = sfstats.getQueryStatistics(queryString);
		
		return new String[]{
				 "+ Query Statistics: " + queryString,
				 "+",
				 "+ Exec Count: " + stats.getExecutionCount(),
				 "+ Avg Exec Time: " + stats.getExecutionAvgTime() + "ms",
				 "+ Max Exec Time: " + stats.getExecutionMaxTime() + "ms",
				 "+ Min Exec Time: " + stats.getExecutionMinTime() + "ms",
				 "+ Exec Row Count: " + stats.getExecutionRowCount(),
				 "+",
				 "+ Cache Puts: " + stats.getCachePutCount(),
				 "+ Cache Hits: " + stats.getCacheHitCount(),
				 "+ Cache Misses: " + stats.getCacheMissCount(),
				 "+ Hit Ratio: " + ((double)stats.getCacheHitCount())/(stats.getCacheHitCount() + stats.getCacheMissCount()),
				 "+",
				 "+"
			};
	}
	
	public static String[] getSessionStatistics(final Session session){
		if(isNull(session))
			throw new NullPointerException("Parameter session is null");
		final SessionStatistics stats = session.getStatistics();
		
		return new String[]{
				 "+ Session Statistics: " + session.toString(),
				 "+",
				 "+ Entity Count: " + stats.getEntityCount(),
				 "+ Collection Count: " + stats.getCollectionCount(),
				 "+",
				 "+"
			};
	}
	
	public static String[] getSessionFactoryStatistics(final Session session){
		if(isNull(session))
			throw new NullPointerException("Parameter session is null");
		return getSessionFactoryStatistics(session.getSessionFactory());
	}
	
	public static String[] getSessionFactoryStatistics(final SessionFactory sessionFactory){
		if(isNull(sessionFactory))
			throw new NullPointerException("Parameter sessionFactory is null");
		final Statistics stats = sessionFactory.getStatistics(); 

		return new String[]{
			 "+ General Statistics:",
			 "+",
			 "+ Running Time: " + (System.currentTimeMillis() - stats.getStartTime())/1_000 +"s",
			 "+",
			 "+ Sessions Opened: " + stats.getSessionOpenCount(),
			 "+ Sessions Closed: " + stats.getSessionCloseCount(),
			 "+",
			 "+ Successful Transactions: " + stats.getSuccessfulTransactionCount(),
			 "+ Completed Transactions: " + stats.getTransactionCount(),
			 "+",
			 "+ Connection Requests: " + stats.getConnectCount(),
			 "+ Flush Count: " + stats.getFlushCount(),
			 "+",
			 "+ Executed Queries: " + stats.getQueryExecutionCount(),
			 "+ Slowest Query Execution Time: " + stats.getQueryExecutionMaxTime() + "ms",
			 "+ Slowest Query: " + stats.getQueryExecutionMaxTimeQueryString(),
			 "+",
			 "+ Prepared Statements Acquired: " + stats.getPrepareStatementCount(),
			 "+ Prepared Statements Released: " + stats.getCloseStatementCount(),
			 "+ StaleObjectStateExceptions: " + stats.getOptimisticFailureCount(),
			 "+",
			 "+ N. Id Queries Executed: " + stats.getNaturalIdQueryExecutionCount(),
			 "+ N. Id Query Max Exec. Time: " + stats.getNaturalIdQueryExecutionMaxTime() + "ms",
			 "+ N. Id Query Max Exec. Time Region: " + stats.getNaturalIdQueryExecutionMaxTimeRegion(),
			 "+",
			 "+ Entities:",
			 "+",
			 "+ Entity Deletes: " + stats.getEntityDeleteCount(),
			 "+ Entity Inserts: " + stats.getEntityInsertCount(),
			 "+ Entity Loads: " + stats.getEntityLoadCount(),
			 "+ Entity Fetchs: " + stats.getEntityFetchCount(),
			 "+ Entity Updates: " + stats.getEntityUpdateCount(),
			 "+",
			 "+ Collections:",
			 "+",
			 "+ Collections Loaded: " + stats.getCollectionLoadCount(),
			 "+ Collections Fetched: " + stats.getCollectionFetchCount(),
			 "+ Collections Updated: " + stats.getCollectionUpdateCount(),
			 "+ Collections Removed: " + stats.getCollectionRemoveCount(),
			 "+ Collections Recreated: " + stats.getCollectionRecreateCount(),
			 "+",
			 "+",
			 "+ Caches:",
			 "+",
			 "+ Query Cache Put Count: " + stats.getQueryCachePutCount(),
			 "+ Query Cache Hit Count: " + stats.getQueryCacheHitCount(),
			 "+ Query Cache Miss Count: " + stats.getQueryCacheMissCount(),
			 "+ Query Cache Hit Ratio: " + ((double)stats.getQueryCacheHitCount())/(stats.getQueryCacheHitCount() + stats.getQueryCacheMissCount()),
			 "+",
			 "+ N. Id Cache Put Count: " + stats.getNaturalIdCachePutCount(),
			 "+ N. Id Cache Hit Count: " + stats.getNaturalIdCacheHitCount(),
			 "+ N. Id Cache Miss Count: " + stats.getNaturalIdCacheMissCount(),
			 "+ N. Id Cache Hit Ratio: " + ((double)stats.getNaturalIdCacheHitCount())/(stats.getNaturalIdCacheHitCount() + stats.getNaturalIdCacheMissCount()),
			 "+",
			 "+ Timestamp Cache Put Count: " + stats.getUpdateTimestampsCachePutCount(),
			 "+ Timestamp Cache Hit Count: " + stats.getUpdateTimestampsCacheHitCount(),
			 "+ Timestamp Cache Miss Count: " + stats.getUpdateTimestampsCacheMissCount(),
			 "+ Timestamp Cache Hit Ratio: " + ((double)stats.getUpdateTimestampsCacheHitCount())/(stats.getUpdateTimestampsCacheHitCount() + stats.getUpdateTimestampsCacheMissCount()),
			 "+",
			 "+ 2nd Level Cache Put Count: " + stats.getSecondLevelCachePutCount(),
			 "+ 2nd Level Cache Hit Count: " + stats.getSecondLevelCacheHitCount(),
			 "+ 2nd Level Cache Miss Count: " + stats.getSecondLevelCacheMissCount(),
			 "+ 2nd Level Cache Hit Ratio: " + ((double)stats.getSecondLevelCacheHitCount())/(stats.getSecondLevelCacheHitCount() + stats.getSecondLevelCacheMissCount()),
			 "+",
			 "+",
			 "+"
		};
	}
}
