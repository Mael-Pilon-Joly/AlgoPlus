package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.ExerciseDTO;
import com.filesharing.springjwt.payload.request.CompileRequest;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;
import org.json.JSONObject;

@Service
public class ExerciceService {
    @Value("${jdoodle.id.key}")
    String clientId;
    @Value("${jdoodle.secret.key}")
    String clientSecret;

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
        exerciseDTO.setStdIn(request.getStdIn());
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
}