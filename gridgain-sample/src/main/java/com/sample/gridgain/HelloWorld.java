package com.sample.gridgain;

import java.util.ArrayList;
import java.util.List;

import javax.cache.Cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.Query;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.ClientException;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;

public class HelloWorld {
	public static void main(String[] args) throws ClientException, Exception {
		// Preparing IgniteConfiguration using Java APIs
		/*
		 * IgniteConfiguration cfg = new IgniteConfiguration();
		 * 
		 * // The node will be started as a client node. cfg.setClientMode(true);
		 * 
		 * // Classes of custom Java logic will be transferred over the wire from this
		 * app. cfg.setPeerClassLoadingEnabled(true);
		 * 
		 * // Setting up an IP Finder to ensure the client can locate the servers.
		 * TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		 * ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
		 * cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
		 */
		
		

		ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
		try (IgniteClient client = Ignition.startClient(cfg)) {
            
			ClientCacheConfiguration cacheCfg = new ClientCacheConfiguration().setName("EmployeeCache")
			        .setCacheMode(CacheMode.REPLICATED)
			        .setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
					
			ClientCache<Integer, Employee> cache = client.cache("EmployeeCache");

//			cache.clear();
			System.out.println("size:" + cache.size());

			/*
			 * List<Employee> employees = createData(3); for (Employee e : employees) {
			 * cache.put(e.getEmployeeId(), e); }
			 */

//			  cache.put(1, "Hello"); cache.put(2, "World!");

			/// working for all
			
			 ScanQuery<Integer, Employee> filter =  new ScanQuery<Integer, Employee>(new IgniteBiPredicate<Integer, Employee>() {
//            	private static final long serialVersionUID = 8143055346199229315L;

				@Override
				public boolean apply(Integer e1, Employee e2) {
					// TODO Auto-generated method stub
					return e2.getEmployeeId()>1;
				}
			});
			 List results1 = cache.query(filter).getAll();
			 System.out.println(results1);

			/*
			 * IgniteBiPredicate<Integer, Employee> filter = new IgniteBiPredicate<Integer,
			 * Employee>() {
			 *//**
				* 
				*//*
					 * private static final long serialVersionUID = 1L;
					 * 
					 * @Override public boolean apply(Integer key, Employee p) { return
					 * p.getEmployeeName().contains("abc"); } };
					 */

//			try (QueryCursor<Cache.Entry<Integer, Employee>> cur = cache
//					.query(qry)) {
//				System.out.println(cur.iterator().hasNext());
//				for (Cache.Entry<Integer, Employee> entry : cur) {
//					// Process the entry ...
//					System.out.println(entry.getKey() + ":" + entry.getValue());
//				}
//			}

			FieldsQueryCursor<List<?>> cursor = client.query(
					new SqlFieldsQuery("SELECT name from employee WHERE id=?").setArgs(222230).setSchema("PUBLIC"));

			// Get the results; the `getAll()` methods closes the cursor; you do not have to
			// call cursor.close();
			List<List<?>> results = cursor.getAll();

			results.stream().findFirst().ifPresent(columns -> {
				System.out.println("name = " + columns.get(0));
			});

			System.out.println(">> " + cache.size() + ">> " + cache.get(2));
			// Get data from the cache
		}
		// Starting the node
//        Ignite ignite = Ignition.start(cfg);
//        IgniteCluster cluster = ignite.cluster();

		// Create an IgniteCache and put some values in it.
//        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");

		System.out.println(">> Created the cache and add the values.");

		// Executing custom Java compute task on server nodes.
//        ignite.compute(ignite.cluster().forServers()).broadcast(new RemoteTask());

		System.out.println(">> Compute task is executed, check for output on the server nodes.");

		// Disconnect from the cluster.
//        ignite.close();
	}

	private static List<Employee> createData(int n) {
		List<Employee> employees = new ArrayList();
		for (int i = 0; i < n; i++) {
			Employee e = new Employee();
			e.setEmployeeId(Integer.valueOf("22223" + i));
			e.setEmployeeName("abc aa" + i);
			e.setEmail("afaf" + i + "aa@test.com");
			employees.add(e);
		}
		return employees;
	}

	/**
	 * A compute tasks that prints out a node ID and some details about its OS and
	 * JRE. Plus, the code shows how to access data stored in a cache from the
	 * compute task.
	 */
	private static class RemoteTask implements IgniteRunnable {
		@IgniteInstanceResource
		Ignite ignite;

//        @Override 
		public void run() {
			System.out.println(">> Executing the compute task");

			System.out.println("   Node ID: " + ignite.cluster().localNode().id() + "\n" + "   OS: "
					+ System.getProperty("os.name") + "   JRE: " + System.getProperty("java.runtime.name"));

			IgniteCache<Integer, String> cache = ignite.cache("myCache");

			System.out.println(">> " + cache.get(1) + " " + cache.get(2));
		}
	}

}
