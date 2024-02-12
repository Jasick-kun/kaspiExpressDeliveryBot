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
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderCheckService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private org.springframework.core.env.Environment Environment;
    @Autowired
    private AbilityBot bot;

    public void check() throws IOException, InterruptedException, URISyntaxException {
        Map<String, String> formData = new HashMap<>();
        formData.put("Login", "ZSharipov_3");
        formData.put("Password", "Whatthefuck1");
        formData.put("IsRemember", "false");
        formData.put("x", "48");
        formData.put("y", "6");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://fo.sulpak.kz:8089/KaspiExpressOrders/OrdersForPrepare?shopId=4860"))
                .header("Content-Type", "text/html")
                .header("Cookie",Cookie.session)
                .header("Cookie",Cookie.info)
                .GET()
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        if (response1.body().contains("<button class=\"btn btn-danger btn-sm\" onclick=\"return openPrepareForm")) {
            if (userRepository.findAll().iterator().hasNext()) {

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(userRepository.findAll().iterator().next().getChatId());
                sendMessage.setText("новый заказ");
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }

    public static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }

    private static String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return formBodyBuilder.toString();
    }
}
