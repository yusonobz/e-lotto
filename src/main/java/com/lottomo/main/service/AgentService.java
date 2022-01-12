package com.lottomo.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lottomo.main.interfaces.IMainAgent;
import com.lottomo.main.interfaces.ISubAgent;
import com.lottomo.main.model.Entry;
import com.lottomo.main.repo.AgentRepository;

@Service
public class AgentService {
	@Autowired
	AgentRepository agentRepository;
	
//	public List<IMainAgent> findAllMainAgentByGroup() {
//		return agentRepository.findAllMainAgentByGroup();	
//	}
	
//	public Page<Entry> listAll(int pageNum, String sortField, String sortDir) {
//		
//		Pageable pageable = PageRequest.of(pageNum - 1, 10, 
//				sortDir.equals("asc") ? Sort.by(sortField).ascending()
//									  : Sort.by(sortField).descending()
//		);
//		
//		return entryRepository.findAll(pageable);
//	}
	public Page<IMainAgent> findAllMainAgentByGroup(int pageNum) {
		int pageSize = 2;
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
		Page<IMainAgent> page = agentRepository.findAllMainAgentByGroup(pageable);
		System.out.println("AgentService: page size "+ page.getTotalElements() );

		return page;
	}
//	public Page<IMainAgent> findAllMainAgentByGroup(int pageNum) {
//	
//		Pageable pageable = PageRequest.of(pageNum, 10);
//		
//		List<IMainAgent> users = agentRepository.findAllMainAgentByGroup(pageable);
//		System.out.println("AgentService: list size "+ users.size());
//		int start = Math.min((int)pageable.getOffset(), users.size());
//		int end = Math.min((start + pageable.getPageSize()), users.size());
//
//		Page<IMainAgent> page = new PageImpl<>(users.subList(start, end), pageable, users.size());
//	
//		return page;	
//	}
	
	public List<ISubAgent> findAllSubAgentByGroup() {
		return agentRepository.findAllSubAgentByGroup();
	}
	

}
