package com.filesharing.springjwt.services;

import com.filesharing.springjwt.models.Event;
import com.filesharing.springjwt.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public Event addEvent(String title, Date start, Date end, String startStr, String endStr, Boolean allDay){
        end = new Date(end.getTime() + (1000 * 60 * 60 * 24));
        Event event = new Event(title, start, end, startStr, endStr, allDay);
        return eventRepository.save(event);
    }
}
