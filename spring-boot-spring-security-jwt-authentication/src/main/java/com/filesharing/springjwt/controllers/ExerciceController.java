package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.CompileDTO;
import com.filesharing.springjwt.payload.response.CompileRequest;
import com.filesharing.springjwt.services.ExerciceService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/exercise")
public class ExerciceController {
    @Autowired
    ExerciceService exerciceService;

    @PostMapping("/compile")
    public ResponseEntity<Map<String, Object>> getResultFromCompilation(@RequestBody CompileRequest request) throws MalformedURLException {
         try {
            JSONObject json = exerciceService.getCompileResultFromInput(request);
             JSONArray jsArr = new JSONArray();
             jsArr.put(json.toMap());
            return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
        } catch ( IOException e) {
            return new ResponseEntity<>(new HashMap<String, Object>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
