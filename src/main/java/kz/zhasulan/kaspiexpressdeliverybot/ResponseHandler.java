package kz.zhasulan.kaspiexpressdeliverybot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

import static kz.zhasulan.kaspiexpressdeliverybot.Constants.START_TEXT;

public class ResponseHandler {


    private final UserRepository userRepository;
    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;

    public ResponseHandler(UserRepository userRepository, SilentSender sender, DBContext db) {
        this.userRepository = userRepository;
        this.sender = sender;
        chatStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyToStart(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(START_TEXT);
        sender.execute(message);
        chatStates.put(chatId, UserState.AWAITING_PASS);
    }
    public void replyToButtons(Long chatId, Message message) {

        switch (message.getText()) {
            case "/start" -> replyToStart(chatId);
            case "/on" -> startNotifications(chatId, message);
            case "/off" -> stopNotifications(chatId, message);
            default -> unexpectedMessage(chatId);
        }
    }
    private void unexpectedMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("I did not expect that.");
        sender.execute(sendMessage);
    }
    private void startNotifications(Long chatId, Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if(userRepository.findFirstByChatId(message.getChatId()) == null){
            UserEntity user= new UserEntity();
            user.setChatId(chatId);
            userRepository.save(user);

            sendMessage.setText("Оповещения включены");

        }
        else {
            sendMessage.setText("Оповещения уже включены");
        }
        sender.execute(sendMessage);


    }
    private void stopNotifications(Long chatId, Message message){

        userRepository.deleteByChatId(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Оповещения выключены");
        sender.execute(sendMessage);
    }
    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

}