package com.filesharing.springjwt.repository;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface FileDBRepository extends JpaRepository<FileDB, String> {
}