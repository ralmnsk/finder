package com.finder.service.bot;

import com.finder.service.file.FileManager;
import com.finder.service.parser.LinksFinder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.finder.service.file.Constants.*;
import static com.finder.service.file.Constants.SITES;

@Component
@NoArgsConstructor
public class NewsReceiver {

    @Autowired
    private FileManager fileManagerData;
    @Autowired
    private FileManager fileManagerSites;
    @Autowired
    private FileManager fileManagerNews;
    @Autowired
    private LinksFinder parser;

    public List<String> receive(){
        List<String> list = new ArrayList<String>();
        fileManagerData.setFileProperties(DATA);
        fileManagerSites.setFileProperties(SITES);
        fileManagerNews.setFileProperties(NEWS);
        Properties data = fileManagerData.getProperty();
        Properties sites = fileManagerSites.getProperty();
        Properties news = fileManagerNews.getProperty();
        if (sites != null && news != null && sites.size() > 0 && data.size()>0) {
            Set<Object> sitesKeys = sites.keySet();
            for(Object siteKey:sitesKeys){
                Set<Object> dataKeys = data.keySet();
                String site = (String)sites.get(siteKey);
                for(Object dataKey:dataKeys){
                   String dataObj = (String)data.get(dataKey);
                   Properties latestNews = parser.getLinks(site, dataObj);
                   Set<Object> latestNewsSet = latestNews.keySet();
                   if(latestNews != null && !latestNews.isEmpty()){
                       for(Object latestNewsKey:latestNewsSet){
                           int listCountBefore = fileManagerNews.getProperty().size();
                                fileManagerNews.write((String)latestNewsKey);
                           int listCountAfter = fileManagerNews.getProperty().size();
                           if(listCountAfter - listCountBefore == 1){
                               list.add((String)latestNewsKey+" "+latestNews.get(latestNewsKey));
                           }
                       }
                   }
                }
            }
        }

        return list;
    }
}
