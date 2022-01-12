package com.lottomo.main.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lottomo.main.intrfc.EntryNums;
import com.lottomo.main.model.Entry;

@Repository
public interface EntryRepository extends JpaRepository<Entry,Long>{
	
	@Query( value = "SELECT * FROM Entries",  nativeQuery = true)
	List<Entry> findEntriesAll();
	
	@Query( value = "SELECT e.num1 as num1, e.num2 as num2,e.num3 as num3 FROM Entries e ",  nativeQuery = true)
	List<EntryNums> findAllNumEntries();
	
	@Query( value = "SELECT * FROM Entries e WHERE e.entryid = ?1",  nativeQuery = true)
	List<Entry> findDailyWinners(Integer entryid);
	
	@Modifying
	@Query( value = "TRUNCATE Entries",  nativeQuery = true)
	public void deleteAllEntries();
	
	@Query( value = "SELECT s.voidmaxlimit FROM Settings s WHERE s.category=?1",  nativeQuery = true)
	public Integer getMaxLimit(Integer category);

	@Query(value="SELECT e.num1 as num1, e.num2 as num2,e.num3 as num3 FROM Entries e WHERE e.playername LIKE CONCAT('%',?1, '%')",nativeQuery = true)
	List<EntryNums> searchNumsByName(String playername);

}
