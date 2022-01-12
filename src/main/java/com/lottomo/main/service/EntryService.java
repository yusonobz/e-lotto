package com.lottomo.main.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lottomo.main.repo.EntryRepository;
import com.lottomo.main.intrfc.EntryNums;
import com.lottomo.main.model.Entry;

@Service
public class EntryService {

	@Autowired
	EntryRepository entryRepository;
	
	public int saveEntry(Entry entry) {
		try {
			entryRepository.save(entry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Entry> listAllEntries() {
		return entryRepository.findAll();
	}
	
	public Entry findById(Long entryid) {
		return entryRepository.findById(entryid).get();
	}
	
	public int deleteEntryById(Long entryid) {
		 entryRepository.deleteById(entryid);
		 return 0;
	}
	
	public List<Entry> searchWinner(List<Integer> entries, List<Integer> winingNumbers) {
		//return entryRepository.findAll();
		//return entryRepository.findEntriesAll();
		return entryRepository.findDailyWinners(1);
	}
	
	public int deleteAllEntries() {
		entryRepository.deleteAllEntries();
		return 0;
	}
	
	public Page<Entry> listAll(int pageNum, String sortField, String sortDir) {
		
		Pageable pageable = PageRequest.of(pageNum - 1, 10, 
				sortDir.equals("asc") ? Sort.by(sortField).ascending()
									  : Sort.by(sortField).descending()
		);
		
		return entryRepository.findAll(pageable);
	}

	public int updateEntry(Entry entry) {
		entryRepository.save(entry);
		return 0;
	}
	
	public List<List<Integer>> getListOfIntgrLists(){
		List<Entry> entryList = entryRepository.findAll();
        List<List<Integer>> listOfIntgrLists = new ArrayList<>();

        if(entryList.size() > 0) {
        	for(Entry entry: entryList ) {
    	        List<Integer> tempList = new ArrayList<>();
    	        tempList.add(entry.getNum1());
    	        tempList.add(entry.getNum2());
    	        tempList.add(entry.getNum3());
    	        Collections.sort(tempList);
    	        listOfIntgrLists.add(tempList);
    	        tempList=null;
    		}
        }
		return listOfIntgrLists;
	}
	
	public Integer getMaxLimit(Integer category) {
		return entryRepository.getMaxLimit(category);
	}
	
	public List<List<Integer>> getNumbersByName(String playername){
		List<EntryNums> entryList = entryRepository.searchNumsByName(playername);
        List<List<Integer>> listOfIntgrLists = new ArrayList<>();

        if(entryList.size() > 0) {
        	for(EntryNums entry: entryList ) {
    	        List<Integer> tempList = new ArrayList<>();
    	        tempList.add(entry.getNum1());
    	        tempList.add(entry.getNum2());
    	        tempList.add(entry.getNum3());
    	        Collections.sort(tempList);
    	        listOfIntgrLists.add(tempList);
    	        tempList=null;
    		}
        }
		return listOfIntgrLists;
	}
		
	public List<List<Integer>> getAllEntryNumbersAsc(){
		List<EntryNums> entryList = entryRepository.findAllNumEntries();
        List<List<Integer>> listOfIntgrLists = new ArrayList<>();

        if(entryList.size() > 0) {
        	for(EntryNums entry: entryList ) {
    	        List<Integer> tempList = new ArrayList<>();
    	        tempList.add(entry.getNum1());
    	        tempList.add(entry.getNum2());
    	        tempList.add(entry.getNum3());
    	        Collections.sort(tempList);
    	        listOfIntgrLists.add(tempList);
    	        tempList=null;
    		}
        }
		return listOfIntgrLists;
	}
	
}
