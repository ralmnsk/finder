package com.finder.service.file;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

@Component
@Scope("prototype")
public class FileManager {

    private String fileProperties;

    public FileManager() {
    }

    public String getFileProperties() {
        return fileProperties;
    }

    public void setFileProperties(String fileProperties) {
        this.fileProperties = fileProperties;
    }

    public Properties getProperty() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(new File(fileProperties)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public void write(String siteName){
        Properties properties = getProperty();
        if (!properties.containsValue(siteName)){
            int size = 0;
            while(properties.containsKey(Integer.toString(size))){
                ++size;
            }
            properties.put(Integer.toString(size),siteName);
            try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileProperties), Charset.forName("UTF-8"))){
                properties.store(writer,"");
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void delete(String delId){
        Properties properties = getProperty();

        if (properties.containsKey(delId)){
            properties.remove(delId);
            try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileProperties), Charset.forName("UTF-8"))){
                properties.store(writer,"");
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void deleteByValue(String value){
        Properties properties = getProperty();

        if (properties.containsValue(value)){
            Set<Object> keySet = properties.keySet();
            for (Object key:keySet){
                Object val = properties.get(key);
                String valString = (String)val;
                if (value.equals(valString)){
                    properties.remove(key);
                }
            }
            try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileProperties), Charset.forName("UTF-8"))){
                properties.store(writer,"");
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
