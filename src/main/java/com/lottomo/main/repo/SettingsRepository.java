package com.lottomo.main.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lottomo.main.intrfc.EntryNums;
import com.lottomo.main.model.Entry;
import com.lottomo.main.model.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings,Long>{
	
	@Query( value = "SELECT * FROM Settings",  nativeQuery = true)
	List<Settings> findEntriesAll();
	
}
