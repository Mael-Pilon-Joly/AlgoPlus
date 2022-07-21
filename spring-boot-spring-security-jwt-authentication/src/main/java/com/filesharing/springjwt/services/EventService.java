package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.EventDTO;
import com.filesharing.springjwt.models.Event;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.repository.EventRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    public EventDTO addEvent(String title, Date start, Date end, String startStr, String endStr, Boolean allDay, String token){
        end = new Date(end.getTime() + (1000 * 60 * 60 * 24));
        Event event = new Event(title, start, end, startStr, endStr, allDay);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            return null;
        }
        event.setUser(user.get());
        event = eventRepository.save(event);
        EventDTO eventDTO = new EventDTO(event.getId(), event.getTitle(), event.getStart(), event.getEnd(), event.getUser().getUsername(), event.getStartStr(), event.getEndStr(), event.getAllDay());
        return eventDTO;
    }

    public void deleteEvent(Long id, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent() && user.isPresent() && (user.get().getUsername().equals(event.get().getUser().getUsername())|| user.get().getUsername().equals("admin")) ){
            eventRepository.delete(event.get());
        }
    }
}
