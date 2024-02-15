package kz.zhasulan.kaspiexpressdeliverybot;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.MonthDay;
import java.util.*;

@Service
public class OrderCheckService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private org.springframework.core.env.Environment Environment;
    @Autowired
    private AbilityBot bot;

    public void check() throws IOException, InterruptedException, URISyntaxException {

        StringBuilder stringBuilder = new StringBuilder();
        boolean needToSend = false;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://fo.sulpak.kz:8089/KaspiExpressOrders/OrdersForPrepare?shopId=4860"))
                .header("Content-Type", "text/html")
                .header("Cookie", Cookie.session)
                .header("Cookie", Cookie.info)
                .GET()
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        if (response1.body().contains("<button class=\"btn btn-danger btn-sm\" onclick=\"return openPrepareForm")) {
            stringBuilder.append("Каспи экспресс доставка\n");
            needToSend = true;
        }

        String dayFrom = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        String dayTo = new SimpleDateFormat(".MM.yyyy").format(Calendar.getInstance().getTime());
        StringBuilder duration = new StringBuilder();
        duration.append("ShopId=4860&From=");
        duration.append(dayFrom);
        duration.append("&To=");
        duration.append(MonthDay.now().getDayOfMonth()+1);
        duration.append(dayTo);

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://fo.sulpak.kz:8089/Reports/SalesFromNeighboringShop"))
                .header("Content-Type", "text/html")
                .header("Cookie", Cookie.session)
                .header("Cookie", Cookie.info)
                .POST(HttpRequest.BodyPublishers.ofString(duration.toString()))
                .build();

        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        if (response2.body().contains("Продажа с самовывозом")) {
            stringBuilder.append("Продажа с самовывозом\n");
            needToSend = true;
        }
        if (response2.body().contains("Продажа с доставкой")) {
            stringBuilder.append("Продажа с самовывозом\n");
            needToSend = true;
        }
        if (response2.body().contains("Продажа с ")) {
            stringBuilder.append("Продажа с самовывозом\n");
            needToSend = true;
        }
        if(needToSend){

            Iterator<UserEntity> iterator= userRepository.findAll().iterator();
              while (iterator.hasNext()) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(iterator.next().getChatId());
            sendMessage.setText(stringBuilder.toString());
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }}

    }
}
