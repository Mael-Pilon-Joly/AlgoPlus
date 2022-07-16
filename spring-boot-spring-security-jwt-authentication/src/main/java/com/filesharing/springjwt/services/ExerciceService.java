package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.CompileDTO;
import com.filesharing.springjwt.payload.response.CompileRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
        script = script.replaceAll("\"","\\\\\"");
        String language = request.getLanguage();
        String versionIndex = request.getVersionIndex();
        CompileDTO compileDTO = new CompileDTO();

        try {
            URL url = new URL("https://api.jdoodle.com/v1/execute");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String input = "{\"clientId\": \"" + clientId + "\",\"clientSecret\":\"" + clientSecret + "\",\"script\":\"" + script +
                    "\",\"language\":\"" + language + "\",\"versionIndex\":\"" + versionIndex + "\"} ";

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(input.getBytes());
            outputStream.flush();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
               compileDTO.setErrorMessage(connection.getResponseMessage());
               compileDTO.setStatus(connection.getResponseCode());
               compileDTO.setErrorStream(connection.getErrorStream());
               return new JSONObject();
            }

            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output = bufferedReader.lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(output);

            connection.disconnect();
            compileDTO.setOutput(output);

            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}