package com.finder.service.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

@Component
@PropertySource("application.properties")
public class Bot extends TelegramLongPollingBot {

    @Autowired
    private UserManager userManager;

    @Value("user.name")
    private String name;

    public void initBot() {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotToken() {
        return "1052752588:AAGBsI-dE6pcKe5sP3jVD2-kgzkl-ihHy1A";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message receive = update.getMessage();
        String sendText = "Send 'add_me' and you will get news. Send 'del_me' and you will be removed from list";
        if (receive != null && receive.hasText()){
            String receiveText = receive.getText();
            switch(receiveText){
                case "add_me":
                    userManager.add(receive.getChatId());
                    sendText = "you have been added to get news";
                    break;
                case "del_me":
                    userManager.remove(receive.getChatId());
                    sendText = "you have been deleted from list of receivers";
                    break;
            }
        }

        InetAddress[] localaddr;

        String systemipaddress = "";
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));

            // reads system IPAddress
            systemipaddress = sc.readLine().trim();
        }
        catch (Exception e)
        {
            systemipaddress = "Cannot Execute Properly";
        }
        System.out.println("Public IP Address: http://" + systemipaddress +"\n");

        SendMessage message=new SendMessage();
        message.setText(sendText+" http://" + systemipaddress);
        message.setChatId(update.getMessage().getChatId());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    public boolean send (Long chatId, String message){
            SendMessage messageToSend = new SendMessage();
            messageToSend.setText(message);
            messageToSend.setChatId(chatId);
        try {
            execute(messageToSend);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return true;
    }
}
