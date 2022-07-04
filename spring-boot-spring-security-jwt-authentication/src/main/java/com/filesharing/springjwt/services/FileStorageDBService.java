package com.filesharing.springjwt.services;

import java.io.IOException;
import java.util.stream.Stream;

import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageDBService {
    @Autowired
    private FileDBRepository fileDBRepository;

}