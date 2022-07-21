package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.*;
import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.Exercise;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.LoginRequest;
import com.filesharing.springjwt.payload.response.ArticleResponse;
import com.filesharing.springjwt.payload.response.LoginResponse;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.registration.email.EmailSender;
import com.filesharing.springjwt.registration.token.PasswordResetToken;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.FileDBRepository;
import com.filesharing.springjwt.repository.PasswordTokenRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.utils.CookieUtil;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    UserDetailsImpl userDetails;

    @Autowired
    PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    FileStorageDBService fileStorageDBService;

    @Autowired
    FileDBRepository fileDBRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Enabled user verification
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (!user.get().isEnabled()) {
            return new ResponseEntity<>(
                    new LoginResponse(null, null, null, null, null, null, LoginResponse.SuccessFailure.FAILURE, "Email account not verified yet", null),
                    HttpStatus.PRECONDITION_FAILED);
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());


        Boolean accessTokenValid = tokenProvider.validateJwtToken(accessToken);
        Boolean refreshTokenValid = tokenProvider.validateJwtToken(refreshToken);

        HttpHeaders responseHeaders = new HttpHeaders();
        Token newAccessToken;
        Token newRefreshToken;

        if (!loginRequest.isRememberme()) {
            newAccessToken = tokenProvider.generateJwtToken(userDetails.getUsername());
            newRefreshToken = tokenProvider.generateRefreshJwtToken(userDetails.getUsername());
            return ResponseEntity.ok().headers(responseHeaders).body(new LoginResponse(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles, SecurityCipher.encrypt(newAccessToken.getTokenValue()),
                    SecurityCipher.encrypt(newRefreshToken.getTokenValue()),
                    LoginResponse.SuccessFailure.SUCCESS, "Successful Auth. Tokens are created in cookie.",
                    articleService.findArticlesByUser(newAccessToken.getTokenValue())));
        }

            newAccessToken = tokenProvider.generateJwtToken(userDetails.getUsername());
            newRefreshToken = tokenProvider.generateRefreshJwtToken(userDetails.getUsername());
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);

        return ResponseEntity.ok().headers(responseHeaders).body(new LoginResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles, null, null, LoginResponse.SuccessFailure.SUCCESS, "Successful Auth. Tokens are created in cookie.",
                articleService.findArticlesByUser(newAccessToken.getTokenValue())));
    }

    @Override
    public ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken) {
        Boolean refreshTokenValid = tokenProvider.validateJwtToken(refreshToken);
        if (!refreshTokenValid) {
            throw new IllegalArgumentException("Refresh Token is invalid!");
        }

        String currentUserUsername = tokenProvider.getUserNameFromJwtToken(accessToken);

        Token newAccessToken = tokenProvider.generateJwtToken(currentUserUsername);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration()).toString());

        LoginResponse loginResponse = new LoginResponse(LoginResponse.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }


    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    @Override
    public ResponseEntity<RequestResponse> getUserProfil(String accessToken) {
        Boolean tokenValid = tokenProvider.validateJwtToken(accessToken);
        if (!tokenValid) {
            return new ResponseEntity<>(new RequestResponse(), HttpStatus.BAD_REQUEST);
        }

        String currentUserUsername = tokenProvider.getUserNameFromJwtToken(accessToken);
        RequestResponse requestResponse = new RequestResponse();
        List<HttpStatus> httpStatus = new ArrayList<>();
        try {
            Optional<User> user = userRepository.findByUsername(currentUserUsername);
            if (user.isPresent()) {
                requestResponse.setId(user.get().getId());
                requestResponse.setUsername(user.get().getUsername());
                requestResponse.setAvatar(user.get().getAvatar());
                requestResponse.setCV(user.get().getCV());
                requestResponse.setEmail(user.get().getEmail());
                requestResponse.setEnabled(user.get().isEnabled());
                requestResponse.setRoles(user.get().getRoles());
                requestResponse.setPoints(user.get().getPoints());
                // Convert articles to articleDTOS to prevent json infinite loop

                List<ArticleDTO> articleDTOS = new ArrayList<>();
                List<Article> articles = user.get().getArticles();

                for (int i=0; i<articles.size() ; i++) {
                    ArticleDTO articleDTO = new ArticleDTO();
                    articleDTO.setId(articles.get(i).getId());
                    articleDTO.setUsername(articles.get(i).getUser().getUsername());
                    articleDTO.setContent(articles.get(i).getContent());
                    articleDTO.setImage(articles.get(i).getImage());
                    articleDTO.setTitle(articles.get(i).getTitle());
                    articleDTO.setPublished(articles.get(i).getPublished());
                    articleDTO.setLastEdited(articles.get(i).getLastEdited());
                    ArticleDTO clone = new ArticleDTO(articleDTO);
                    articleDTOS.add(articleDTO);
                }

                requestResponse.setArticlesDTOs(articleDTOS);

                // Convert exercises to exerciseDTOS to prevent json infinite loop

                List<NewExerciseDTO> exerciseDTOS = new ArrayList<>();
                List<Exercise> exercises = user.get().getExercises();

                for (int i=0; i<exercises.size() ; i++) {
                    NewExerciseDTO exerciseDTO = new NewExerciseDTO();
                    exerciseDTO.setId(exercises.get(i).getId());
                    exerciseDTO.setPublished(exercises.get(i).getPublished());
                    exerciseDTO.setImage(exercises.get(i).getImage());
                    exerciseDTO.setCreator_username(exercises.get(i).getCreator().getUsername());
                    exerciseDTO.setExplanation(exercises.get(i).getExplanation());
                    NewExerciseDTO clone = new NewExerciseDTO(exerciseDTO);
                    exerciseDTOS.add(clone);
                }

                requestResponse.setExercises(exerciseDTOS);
                httpStatus.add(HttpStatus.OK);
                requestResponse.setHttpsStatus(httpStatus);
                return new ResponseEntity<>(requestResponse, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<ArticleResponse> getUserArticles(String accessToken) {
        Boolean tokenValid = tokenProvider.validateJwtToken(accessToken);
        ArticleResponse articleResponse = new ArticleResponse();

        if (!tokenValid) {
            return new ResponseEntity<>(articleResponse, HttpStatus.BAD_REQUEST);
        }

        String currentUserUsername = tokenProvider.getUserNameFromJwtToken(accessToken);
        Optional<User> user = userRepository.findByUsername(currentUserUsername);
        try {
            if (user.isPresent()){
                articleResponse.setArticle(user.get().getArticles());
                return new ResponseEntity<>(articleResponse, HttpStatus.OK);
           }
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        Date now = Date.from(LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()).toInstant());
        myToken.setExpiryDate(now);
        passwordTokenRepository.save(myToken);
    }

    public ResponseEntity<RequestResponse> constructAndSendPasswordResetEmail(User user, String dest, String token) {
        RequestResponse requestResponse = new RequestResponse();
        List<HttpStatus> status = new ArrayList<>();
        requestResponse.setUsername(user.getUsername());
        try {
            String email = "<div> <p1> Hi " + user.getUsername() + ", you have requested a password reset from AlgoPlus.</p1> <p2> Please click this link to reset your password, or ignore this message if you haven't made this request: </p2> <a href='http://localhost:8080/api/user/validatepassword?token=" + token + "'>Reset your password</a></div>";
            emailSender.send(dest, email, "Reset your password");
            status.add(HttpStatus.OK);
            requestResponse.setHttpsStatus(status);
            return new ResponseEntity<>(requestResponse, HttpStatus.OK);
        } catch (Exception e) {
            status.add(HttpStatus.INTERNAL_SERVER_ERROR);
            requestResponse.setHttpsStatus(status);
            return new ResponseEntity<>(requestResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        final Calendar calendar = Calendar.getInstance();

            Optional<PasswordResetToken> passwordToken = passwordTokenRepository.findByToken(token);
            if (passwordToken.isEmpty()) {
                return false;
            } else if (passwordToken.get().getExpiryDate().before(calendar.getTime())) {
                return false;
            } else {
                return true;
            }


    }

    public User updateAvatar(MultipartFile file, User user) throws IOException {
        FileDB temp = fileStorageDBService.store(file);
        FileDB avatar = fileDBRepository.getById(temp.getId());
        user.setAvatar(avatar);

        return userRepository.save(user);
    }

    public User updateCV(MultipartFile file, User user) throws IOException {
        FileDB temp = fileStorageDBService.store(file);
        FileDB cv = fileDBRepository.getById(temp.getId());
        user.setCV(cv);
        return userRepository.save(user);
    }

}
