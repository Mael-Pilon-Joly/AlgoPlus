package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.ArticleDTO;
import com.filesharing.springjwt.dto.CommentDTO;
import com.filesharing.springjwt.dto.PasswordDto;
import com.filesharing.springjwt.dto.UserDto;
import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.Comment;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.LoginRequest;
import com.filesharing.springjwt.payload.request.ResetPasswordRequest;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.registration.token.PasswordResetToken;
import com.filesharing.springjwt.repository.PasswordTokenRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.services.UserService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "http://143.198.169.178:4200", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordTokenRepository passwordTokenRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/ranking")
    public ResponseEntity<List<UserDto>> findAllByPointsDesc() {
        try {
            List<User> users = userRepository.findAllByOrderByPointsDesc();
            List<UserDto> userDtos = new ArrayList<>();
            for (User user: users){
                UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getPoints(), user.getAvatar(), user.getCV());

                List<Article> articles = user.getArticles();
                List<ArticleDTO> articlesDTOs = new ArrayList<>();

                for(Article article: articles){
                    ArticleDTO articleDTO = new ArticleDTO(article.getId(), article.getTitle(), article.getImage(), article.getPublished(), article.getLastEdited(), article.getContent(), article.getUser().getUsername(), article.getUser().getId(), article.getLanguage());
                    List<Comment> comments = article.getComments();
                    List<CommentDTO> commentDTOS = new ArrayList<>();
                    for(Comment comment: comments) {
                        CommentDTO commentDTO = new CommentDTO(comment.getId(), comment.getUser().getAvatar(), comment.getPublished(), comment.getContent(), comment.getUser().getUsername(), comment.getUser().getId());
                        CommentDTO clone = new CommentDTO(commentDTO);
                        commentDTOS.add(clone);
                    }
                    articleDTO.setCommentDTOList(commentDTOS);
                    ArticleDTO clone_article = new ArticleDTO(articleDTO);
                    articlesDTOs.add(clone_article);
                }
                userDto.setArticleDTOList(articlesDTOs);
                UserDto clone = new UserDto(userDto);
                clone.setEmail(user.getEmail());
                userDtos.add(clone);
            }
           return new ResponseEntity<>(userDtos, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<RequestResponse> resetPassword( @Valid @RequestBody ResetPasswordRequest resetRequest, BindingResult bindingResult) {
        RequestResponse requestResponse = new RequestResponse();
        List<HttpStatus> status = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (FieldError fe : bindingResult.getFieldErrors()) {
                switch (fe.getField()) {
                    case "email":
                        status.add(HttpStatus.NOT_ACCEPTABLE);
                        break;
                }
            }
            requestResponse.setUsername(null);
            requestResponse.setHttpsStatus(status);
            return new ResponseEntity<>(requestResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> user = userRepository.findByEmail(resetRequest.getEmail());
        if (user.isEmpty()) {
            status.add(HttpStatus.BAD_REQUEST);
            requestResponse.setHttpsStatus(status);
            return new ResponseEntity<>(requestResponse, HttpStatus.UNAUTHORIZED);
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user.get(), token);
        status.add(HttpStatus.OK);
        requestResponse.setUsername(user.get().getUsername());
        requestResponse.setHttpsStatus(status);
        userService.constructAndSendPasswordResetEmail( user.get(), user.get().getEmail(), token);
        return new ResponseEntity<>(requestResponse, HttpStatus.OK);
    }

    @GetMapping("/validatepassword")
    public void passwordValidation( @RequestParam("token") String token, HttpServletResponse response) throws IOException {

        boolean result = userService.validatePasswordResetToken( token);
        if (result == false) {
            response.sendRedirect("http://143.198.169.178:4200/failedpasswordresetvalidation");
        } else {
            response.sendRedirect("http://143.198.169.178:4200/updatepassword?token=" + token);
        }
    }

    @PostMapping("/savepassword")
    public ResponseEntity<RequestResponse> savePassword(@Valid @RequestBody PasswordDto passwordDto, BindingResult bindingResult) {

        boolean validatePasswordResetToken = userService.validatePasswordResetToken(passwordDto.getToken());
        RequestResponse requestResponse = new RequestResponse();
        List<HttpStatus> status = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (FieldError fe : bindingResult.getFieldErrors()) {
                switch (fe.getField()) {
                    case "password":
                        status.add(HttpStatus.BAD_REQUEST);
                        break;
                }
            }
            requestResponse.setUsername(null);
            requestResponse.setHttpsStatus(status);
            return new ResponseEntity<>(requestResponse, HttpStatus.BAD_REQUEST);
        }
        if (!validatePasswordResetToken) {
            status.add(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(requestResponse, HttpStatus.NOT_ACCEPTABLE);
        } else {
            Optional<PasswordResetToken> passwordResetToken = passwordTokenRepository.findByToken(passwordDto.getToken());
            if (passwordResetToken.isEmpty()){
                status.add(HttpStatus.INTERNAL_SERVER_ERROR);
                return new ResponseEntity<>(requestResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            try {
                User user = passwordResetToken.get().getUser();
                user.setPassword(encoder.encode(passwordDto.getPassword()));
                userRepository.save(user);
                status.add(HttpStatus.OK);
                return new ResponseEntity<>(requestResponse, HttpStatus.OK);
            } catch (Exception e) {
                status.add(HttpStatus.INTERNAL_SERVER_ERROR);
                return new ResponseEntity<>(requestResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
