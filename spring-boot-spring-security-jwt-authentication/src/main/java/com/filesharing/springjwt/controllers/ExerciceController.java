package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.ExerciseDTO;
import com.filesharing.springjwt.dto.NewExerciseDTO;
import com.filesharing.springjwt.models.Exercise;
import com.filesharing.springjwt.payload.request.CompileRequest;
import com.filesharing.springjwt.repository.ExerciseRepository;
import com.filesharing.springjwt.services.ExerciceService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/exercise")
public class ExerciceController {
    @Autowired
    ExerciceService exerciseService;

    @Autowired
    ExerciseRepository exerciseRepository;

    @GetMapping("/exercices")
    public ResponseEntity<List<NewExerciseDTO>> findAllExerices() {
        List<NewExerciseDTO> newExerciseDTOList = new ArrayList<>();

        try {
           List<Exercise> exercises = exerciseRepository.findAll();

           for(int i=0; i<exercises.size() ; i++) {
           NewExerciseDTO newExerciseDTO = new NewExerciseDTO();
           newExerciseDTO.setId(exercises.get(i).getId());
           newExerciseDTO.setImage(exercises.get(i).getImage());
           newExerciseDTO.setTitle(exercises.get(i).getTitle());
           newExerciseDTO.setExplanation(exercises.get(i).getExplanation());
           newExerciseDTO.setCreator_username(exercises.get(i).getCreator().getUsername());
           newExerciseDTO.setPublished(exercises.get(i).getPublished());
           NewExerciseDTO clone = new NewExerciseDTO(newExerciseDTO);
           newExerciseDTOList.add(clone);
           }
            return new ResponseEntity<>(newExerciseDTOList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(newExerciseDTOList, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

    @PostMapping("/exercise")
    public ResponseEntity<NewExerciseDTO> createExercise (@RequestHeader(name = "Authorization", required = false) String token,
                                                       @CookieValue(name = "accessToken", required = false) String accessToken,
                                                       @RequestParam("title") String title, @RequestParam("explanation") String explanation,
                                                       @RequestPart("solutions") String solutions, @RequestPart("image") MultipartFile image)
    {
        NewExerciseDTO newExerciseDTO = new NewExerciseDTO();
        try {
            if (token != null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            solutions = java.net.URLDecoder.decode(solutions, StandardCharsets.UTF_8.name());
            newExerciseDTO = exerciseService.createExercise(decryptedAccessToken,title,explanation,image,solutions);
            return new ResponseEntity<NewExerciseDTO>(newExerciseDTO, HttpStatus.OK);
        }   catch(Exception e) {
            return new ResponseEntity<NewExerciseDTO>(newExerciseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/compile")
    public ResponseEntity<Map<String, Object>> getResultFromCompilation(@RequestBody CompileRequest request) throws MalformedURLException {
         try {
            JSONObject json = exerciseService.getCompileResultFromInput(request);
             JSONArray jsArr = new JSONArray();
             jsArr.put(json.toMap());
            return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
        } catch ( IOException e) {
            return new ResponseEntity<>(new HashMap<String, Object>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
