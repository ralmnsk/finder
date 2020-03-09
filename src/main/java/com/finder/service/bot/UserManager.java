package com.finder.service.bot;

import com.finder.service.file.FileManager;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.finder.service.file.Constants.USERS;

@Component
@NoArgsConstructor
public class UserManager {
    @Autowired
    private FileManager fileManager;

    public void add(Long charId){
        fileManager.setFileProperties(USERS);
        fileManager.write(charId.toString());
    }
    public void remove(Long charId){
        fileManager.setFileProperties(USERS);
        fileManager.deleteByValue(charId.toString());
    }

}
