package com.filesharing.springjwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private FileDB image;
    private String title;
    @NotBlank
    @Lob
    @Column( length = 100000 )
    private String explanation;

    @ElementCollection
    @MapKeyColumn(name="input")
    @Column(name="solution")
    @CollectionTable(name="solutions", joinColumns=@JoinColumn(name="solution_id"))
    private Map<String,String> solutions;
    @ManyToOne(fetch = FetchType.EAGER , optional = false)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private Date published;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileDB getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(FileDB image) {
        this.image = image;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Map<String, String> getSolutions() {
        return solutions;
    }

    public void setSolutions(Map<String, String> solutions) {
        this.solutions = solutions;
    }

    public User getCreator() {
        return user;
    }

    public void setCreator(User creator) {
        this.user = creator;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }


}
