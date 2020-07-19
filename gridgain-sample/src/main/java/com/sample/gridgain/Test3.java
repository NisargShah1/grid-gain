package com.sample.gridgain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.ClientException;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;

public class Test3 {

	public static void main(String[] args) {
	
//		ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
		try ( Ignite ignite = Ignition.start("example-spring.xml")) {
					
//			 CacheConfiguration ccfg = new CacheConfiguration("EmployeeCache");
					  
					  // Setting SQL schema for the cache. 
//					 ccfg.setIndexedTypes(Integer.class,Employee.class);
					  
			IgniteCache<Integer, Employee> cache = ignite.getOrCreateCache("EmployeeCache");
	
			System.out.println("size:" + cache.size());
	
			List<Employee> employees = createData(3);
			for (Employee e : employees) {
				cache.put(e.getEmployeeId(), e);
			}
	
			/// working for all
			
			SqlQuery sql = new SqlQuery(Employee.class, "employeeId > ?");

			// Find only persons earning more than 1,000.
			try (QueryCursor<javax.cache.Cache.Entry<Integer, Employee>> cursor = cache.query(sql.setArgs(11))) {
			  for (javax.cache.Cache.Entry<Integer, Employee> e : cursor)
			    System.out.println(e.getValue().toString());
			}
	
			
			 ScanQuery<Integer, Employee> filter =  new ScanQuery<Integer, Employee>(new IgniteBiPredicate<Integer, Employee>() {
						@Override
						public boolean apply(Integer e1, Employee e2) {
							return e2.getEmployeeName().contains("aa1");
						}
					});
					 List results1 = cache.query(filter).getAll();
					 System.out.println(results1);
			
					
					 //=======================Another cache
					 IgniteCache<Integer, Employee> personCache = ignite.getOrCreateCache("PersonCache");
						
						
						List<Employee> persons = createData(5);
						for (Employee e : persons) {
							personCache.put(e.getEmployeeId(), e);
						}
						
						System.out.println("size:" + personCache.size());
						
					 
		} catch (ClientException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		
	
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
}
