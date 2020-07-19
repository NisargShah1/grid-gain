package com.sample.gridgain;

import java.util.List;
import java.util.UUID;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;

public class Test2 {

	public static void main(String[] args) {
		
		  IgniteConfiguration cfg = new IgniteConfiguration();
		  
		  // Setting some custom name for the node.
		  cfg.setIgniteInstanceName("springDataNode");
		  
		  // Enabling peer-class loading feature. 
		  cfg.setPeerClassLoadingEnabled(true);
		  
		  // Defining and creating a new cache to be used by Ignite Spring Data //
		 CacheConfiguration ccfg = new
		  CacheConfiguration("EmployeeCache");
		  
		  // Setting SQL schema for the cache. 
		 ccfg.setIndexedTypes(Integer.class,
		  Employee.class);
		  
		  cfg.setCacheConfiguration(ccfg);
		  
		  Ignition.start(cfg);
		 
        
        populate();
        
   	
	}
	
	private static void populate() {
		UUID id = UUID.fromString("0eba3c91");
		Ignite ignite = Ignition.ignite(id);
		IgniteCache<Integer, Employee> employees = ignite.getOrCreateCache("EmployeeCache");
//		employees.put(111, new Employee(111, "Nisarg", "abc@test.com"));
//		employees.put(112, new Employee(112, "XYZ", "xyz@test.com"));
	
		
		 ScanQuery<Integer, Employee> filter =  new ScanQuery<Integer, Employee>(new IgniteBiPredicate<Integer, Employee>() {
//		     	private static final long serialVersionUID = 8143055346199229315L;

					@Override
					public boolean apply(Integer e1, Employee e2) {
						// TODO Auto-generated method stub
						return e2.getEmployeeName().contains("XY");
					}
				});
				 List results1 = employees.query(filter).getAll();
				 System.out.println(results1);
				 
				 if(ignite.active())
					 ignite.close();
	}
}
