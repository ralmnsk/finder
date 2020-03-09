package com.finder.service.parser;

import com.finder.service.site.checker.Checker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParserDefault implements LinksFinder{
    private String uri;
    private String infToFind;


    @Autowired
    private Checker checker;

    @Override
    public Properties getLinks(String uri, String infToFind) {
        this.uri = uri;
        this.infToFind = infToFind;
        Properties properties = new Properties();

        Map<String, String> map = new HashMap<>();
        if(!uri.isEmpty() && !infToFind.isEmpty()){
            checker.setUrl(uri);
            if (checker.checkInternetConnection()){
                Document doc = null;
                try {
                    doc = Jsoup.connect(uri).get();
                    Elements links = doc.select("a[href]");
                    links.stream()
                            .filter(e -> e.hasText())
                            .filter(e -> isContain(e, infToFind))
                            .forEach(element->properties.put(element.text(),
                                    removeUri(uri,element.attr("href"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    private boolean isContain(Element e, String info){
            if (e.hasText()){
                String text = e.text();
                info = info.toLowerCase();
                text = text.toLowerCase();
                String[] array = info.split("[\\s|,|.|!]+");
                String regex = ".*";
                for (String s:array){
                    regex = regex + s + ".*";
                }
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()){
                    return true;
                }
//                if (text.contains(info)){
//                    return true;
//                }
            }
        return false;
    }

    private String removeUri(String uri, String addr){
            if(!addr.contains(uri)){
                addr = uri+addr;
            }
        return addr;
    }
}
