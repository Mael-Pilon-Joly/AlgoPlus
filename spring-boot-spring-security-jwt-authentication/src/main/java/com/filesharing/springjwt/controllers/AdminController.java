package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.services.AdminService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

  @Autowired
  AdminService adminService;

  @GetMapping("/auth")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RequestResponse> adminAccess(@CookieValue(name = "accessToken", required=false) String accessToken) {
    String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
    return adminService.validateAdminRole(decryptedAccessToken);
  }
}
