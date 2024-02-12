package kz.zhasulan.kaspiexpressdeliverybot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Integer> {


    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from USERS where CHAT_ID=:chatId")
    void deleteByChatId(@Param("chatId") Long chatId);
}
