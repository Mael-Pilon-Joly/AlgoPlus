package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin("http://localhost:8081")
public class FilesController {

    @Autowired
    FileStorageService storageService;



}