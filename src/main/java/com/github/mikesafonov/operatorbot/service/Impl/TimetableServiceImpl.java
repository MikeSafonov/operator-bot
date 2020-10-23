package com.github.mikesafonov.operatorbot.service.Impl;

import org.springframework.stereotype.Service;

import com.github.mikesafonov.operatorbot.repository.TimetableRepository;
import com.github.mikesafonov.operatorbot.service.TimetableService;

@Service
public class TimetableServiceImpl implements TimetableService {
	
    private final TimetableRepository timetableRepository;
    
    public TimetableServiceImpl(TimetableRepository timetableRepository){
		this.timetableRepository = timetableRepository;
	}

}
