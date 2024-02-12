package kz.zhasulan.kaspiexpressdeliverybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableAutoConfiguration
public class KaspiExpressDeliveryBotApplication {

	public static void main(String[] args) throws IOException, InterruptedException {

		Map<String, String> formData = new HashMap<>();
		formData.put("Login", "ZSharipov_3");
		formData.put("Password", "Whatthefuck1");
		formData.put("IsRemember", "false");
		formData.put("x", "48");
		formData.put("y", "6");

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://fo.sulpak.kz:8089/Account/LogOn"))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.statusCode());
		Cookie.session= response.headers().allValues("set-cookie").get(0);
		Cookie.info= response.headers().allValues("set-cookie").get(1);
		System.out.println(Cookie.session);
		System.out.println(Cookie.info);
		ConfigurableApplicationContext ctx = SpringApplication.run(KaspiExpressDeliveryBotApplication.class, args);
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(ctx.getBean("bot", AbilityBot.class));
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
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

