package com.filesharing.springjwt.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public void init(String user);

    public void save(MultipartFile file, String user);

    public Resource load(String filename, String user);

    public void deleteAll(String user);

    public void deleteFile(String user, String file);

    public Stream<Path> loadAll(String user);
}