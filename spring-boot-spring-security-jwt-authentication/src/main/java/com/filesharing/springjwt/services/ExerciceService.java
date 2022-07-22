package com.filesharing.springjwt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filesharing.springjwt.dto.ExerciseDTO;
import com.filesharing.springjwt.dto.NewExerciseDTO;
import com.filesharing.springjwt.models.Exercise;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.CompileRequest;
import com.filesharing.springjwt.repository.ExerciseRepository;
import com.filesharing.springjwt.repository.FileDBRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExerciceService {
    @Value("${jdoodle.id.key}")
    String clientId;
    @Value("${jdoodle.secret.key}")
    String clientSecret;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileStorageDBService fileStorageDBService;

    @Autowired
    FileDBRepository fileDBRepository;

    @Autowired
    ExerciseRepository exerciseRepository;

    public JSONObject getCompileResultFromInput(CompileRequest request) throws MalformedURLException {
        String script = request.getScript();
        String language = request.getLanguage();
        String versionIndex = request.getVersionIndex();
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setClientId(clientId);
        exerciseDTO.setClientSecret(clientSecret);
        exerciseDTO.setScript(script);
        exerciseDTO.setLanguage(language);
        exerciseDTO.setVersionIndex(versionIndex);
        exerciseDTO.setStdIn(escapeMetaCharacters(request.getStdIn()));
        String jsonInString = new Gson().toJson(exerciseDTO);

        try {
            URL url = new URL("https://api.jdoodle.com/v1/execute");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String input = jsonInString;

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(input.getBytes());
            outputStream.flush();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
               return new JSONObject();
            }

            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output = bufferedReader.lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(output);

            connection.disconnect();

            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%"};

        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                inputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
            }
        }
        return inputString;
    }

    public NewExerciseDTO createExercise (String token, String title, String explanation, MultipartFile image, String solutions) throws IOException {
        Exercise exercise = new Exercise();
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        exercise.setCreator(user.get());
        exercise.setTitle(title);
        if (image != null) {
            FileDB temp = fileStorageDBService.store(image);
            FileDB exercise_image = fileDBRepository.getById(temp.getId());
            exercise.setImage(exercise_image);
        }
        exercise.setExplanation(explanation);
        JsonObject jsonObjectAlt = JsonParser.parseString(solutions).getAsJsonObject();
        Map<String, String> solutionsHashMap = new Gson().fromJson(jsonObjectAlt, HashMap.class);

        exercise.setSolutions(solutionsHashMap);
        exercise.setPublished(new Date());
        Exercise savedExercise = exerciseRepository.save(exercise);
        user.get().setPoints(user.get().getPoints()+10);
        userRepository.save(user.get());
        return new NewExerciseDTO(savedExercise.getId(), savedExercise.getTitle(), savedExercise.getImage(), savedExercise.getCreator().getUsername(), savedExercise.getExplanation(), savedExercise.getPublished() );
    }
}