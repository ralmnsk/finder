package com.finder.service.bot;

import com.finder.service.file.FileManager;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.finder.service.file.Constants.NEWS;
import static com.finder.service.file.Constants.USERS;

@NoArgsConstructor
@Component
public class NewsSender {

    @Autowired
    private Bot bot;
    @Autowired
    private FileManager fileManagerUser;
    @Autowired
    private FileManager fileManagerNews;
    @Autowired
    private NewsReceiver newsReceiver;


//    @Scheduled(fixedRate = 60000)
    public void sendNews(){
                fileManagerUser.setFileProperties(USERS);
                fileManagerNews.setFileProperties(NEWS);
                Properties users = fileManagerUser.getProperty();
                Set<Object> userSet = users.keySet();
                for (Object key:userSet){
                    long chatId = Long.parseLong(users.get(key).toString());
                    List<String> list = newsReceiver.receive();
                    if(list !=null && !list.isEmpty()){
                        list.stream().forEach(news->bot.send(chatId,news));
                    }
                }
    }
}
