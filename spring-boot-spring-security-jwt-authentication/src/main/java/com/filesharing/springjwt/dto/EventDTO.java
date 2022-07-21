package com.filesharing.springjwt.dto;

import com.filesharing.springjwt.models.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

public class EventDTO {

    private Long id;

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;

    private String username_creator;

    private String startStr;

    private String endStr;

    private Boolean allDay;

    public EventDTO() {
    }

    public EventDTO(Long id, String title, Date start, Date end, String username_creator, String startStr, String endStr, Boolean allDay) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.username_creator = username_creator;
        this.startStr = startStr;
        this.endStr = endStr;
        this.allDay = allDay;
    }

    public EventDTO(EventDTO clone) {
        this.id = clone.id;
        this.title = clone.title;
        this.start = clone.start;
        this.end = clone.end;
        this.username_creator = clone.username_creator;
        this.startStr = clone.startStr;
        this.endStr = clone.endStr;
        this.allDay = clone.allDay;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getUsername_creator() {
        return username_creator;
    }

    public void setUsername_creator(String username_creator) {
        this.username_creator = username_creator;
    }

    public String getStartStr() {
        return startStr;
    }

    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public String getEndStr() {
        return endStr;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }
}
