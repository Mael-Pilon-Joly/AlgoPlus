package com.filesharing.springjwt.registration.token;

import com.filesharing.springjwt.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
         name="token_sequence",
         sequenceName = "token_sequence",
         allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )
    @Column
    private Long id;

    @Column(nullable=false)
    private String token;

    @Column(nullable=false)
    private LocalDateTime localDateTime;

    @Column(nullable=false)
    private LocalDateTime expiresAt;

    @Column
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private User user;

    public ConfirmationToken(String token, LocalDateTime localDateTime, LocalDateTime expiresAt, User user) {
        this.token = token;
        this.localDateTime = localDateTime;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
