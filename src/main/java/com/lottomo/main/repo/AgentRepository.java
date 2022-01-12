package com.lottomo.main.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lottomo.main.interfaces.IMainAgent;
import com.lottomo.main.interfaces.ISubAgent;
import com.lottomo.main.model.Entry;

@Repository
public interface AgentRepository extends JpaRepository<Entry,Long>{
	
	@Query( value = "SELECT mainagent as mainagent , SUM(amount) as amount, SUM(freebet) as freebet,"
			+ "SUM(returnbet) as returnbet FROM Entries  GROUP BY mainagent #{#pageable}",
	countQuery="SELECT COUNT(*) FROM (" + 
	"select mainagent,subagent, sum(amount),SUM(returnbet) as returnbet,SUM(freebet) as freebet"
	+ " from Entries group by mainagent,subagent) as e ",nativeQuery = true)
	@Transactional(readOnly = true)
	Page<IMainAgent> findAllMainAgentByGroup(Pageable pageable);
	
	@Query( value = "select mainagent,subagent, sum(amount) as amount,SUM(returnbet) as returnbet,SUM(freebet) as freebet"
			+ " from Entries group by mainagent,subagent",nativeQuery = true)
	@Transactional(readOnly = true)
	List<ISubAgent> findAllSubAgentByGroup();
}

//countQuery="SELECT COUNT(*) FROM (" + 
//"select mainagent,subagent, sum(amount),SUM(returnbet) as returnbet,SUM(freebet) as freebet"
//+ " from Entries group by mainagent,subagent) as e ",
