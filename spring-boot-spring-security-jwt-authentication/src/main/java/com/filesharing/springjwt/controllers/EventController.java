package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.ArticleDTO;
import com.filesharing.springjwt.dto.EventDTO;
import com.filesharing.springjwt.models.Event;
import com.filesharing.springjwt.repository.EventRepository;
import com.filesharing.springjwt.services.EventService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getAllArticles() {
    try {
        List<Event> list = eventRepository.findAll();
        List<EventDTO> eventDTOS = new ArrayList<>();

        for (Event event: list){
            EventDTO eventDTO = new EventDTO(event.getId(), event.getTitle(), event.getStart(), event.getEnd(), event.getUser().getUsername(), event.getStartStr(), event.getEndStr(), event.getAllDay());
            EventDTO clone = new EventDTO(eventDTO);
            eventDTOS.add(clone);
        }
        return new ResponseEntity<>(eventDTOS, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(       Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
    }
    @PostMapping("/event")
    public ResponseEntity<EventDTO> getAllArticles(@RequestHeader(name = "Authorization", required = false) String token,
                                                   @CookieValue(name = "accessToken", required=false) String accessToken, @RequestParam("title") String title,
                                                      @RequestParam(name="start") Date start,
                                                      @RequestParam(name="end") Date end,
                                                      @RequestParam(name="startStr") String startStr,
                                                      @RequestParam(name="endStr") String endStr,
                                                      @RequestParam(name="allday") Boolean allDay) {
    try {
        if (token != null && token.length() > 15) {
            accessToken = token.substring(7);
        }
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        EventDTO event = eventService.addEvent(title, start, end, startStr, endStr, allDay, decryptedAccessToken);
        return new ResponseEntity<>(event, HttpStatus.OK);
    } catch (Exception e){
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    }

    @DeleteMapping("/event")
    public ResponseEntity<Event>deleteEvent(@RequestParam("id") long id,@RequestHeader(name = "Authorization", required = false) String token,
                                            @CookieValue(name = "accessToken", required = false) String accessToken) {
        try {
            if (token != null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
             eventService.deleteEvent(id, decryptedAccessToken);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
