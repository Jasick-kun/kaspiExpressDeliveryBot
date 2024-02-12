package kz.zhasulan.kaspiexpressdeliverybot;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name="USERS")
@Entity
@Getter
@RequiredArgsConstructor
@Setter
public class UserEntity {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "CHAT_ID")
    private Long chatId;

}
