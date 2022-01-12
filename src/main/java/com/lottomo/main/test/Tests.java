package com.lottomo.main.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.lottomo.main.interfaces.IMainAgent;
import com.lottomo.main.model.Entry;
import com.lottomo.main.repo.AgentRepository;
import com.lottomo.main.repo.EntryRepository;
import com.lottomo.main.service.AgentService;
import com.lottomo.main.service.EntryService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Tests {
	@Autowired
	EntryRepository entryRepo;

	@Test
	public void testGetNumsByPlayername() {
	//	List<Integer> numList = entryRepo.searchNumsByName("@Evil Oreo");
	//	assertNotNull(numList);
		
	}
	
	@Test
	public void testFindAllEntries() {
		List<Entry> entryList = entryRepo.findEntriesAll();
		assertNotNull(entryList);
	}
	
	  public static <T> Set<T> findDuplicateBySetAdd(List<T> list) {

	        Set<T> items = new HashSet<>();
	        return list.stream()
	                .filter(n -> !items.add(n)) // Set.add() returns false if the element was already in the set.
	                .collect(Collectors.toSet());

	  }
}
	
