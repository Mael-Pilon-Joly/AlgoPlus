package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.ArticleDTO;
import com.filesharing.springjwt.models.Event;
import com.filesharing.springjwt.repository.EventRepository;
import com.filesharing.springjwt.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
    public ResponseEntity<List<Event>> getAllArticles() {
    try {
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
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
    public ResponseEntity<Event> getAllArticles(@RequestParam("title") String title,
                                                      @RequestParam(name="start") Date start,
                                                      @RequestParam(name="end") Date end,
                                                      @RequestParam(name="startStr") String startStr,
                                                      @RequestParam(name="endStr") String endStr,
                                                      @RequestParam(name="allday") Boolean allDay) {
    try {
        Event event = eventService.addEvent(title, start, end, startStr, endStr, allDay);
        return new ResponseEntity<>(event, HttpStatus.OK);
    } catch (Exception e){
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    }
}
