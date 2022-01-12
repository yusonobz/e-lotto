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
import com.lottomo.main.repo.SettingsRepository;
import com.lottomo.main.intrfc.EntryNums;
import com.lottomo.main.model.Entry;
import com.lottomo.main.model.Settings;

@Service
public class SettingsService {

	@Autowired
	SettingsRepository settingsRepository;
	
	public int saveSettings(Settings settings) {
		try {
			settingsRepository.save(settings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Settings> listAllSettings() {
		return settingsRepository.findAll();
	}
	
	public Settings findById(Long settingsid) {
		return settingsRepository.findById(settingsid).get();
	}
	
	public int updateEntry(Settings settings) {
		settingsRepository.save(settings);
		return 0;
	}
	
	
}
