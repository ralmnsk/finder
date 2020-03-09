package com.finder.web.controller;

import com.finder.model.Data;
import com.finder.model.Information;
import com.finder.service.file.FileManager;
import com.finder.service.parser.LinksFinder;
import com.finder.service.site.checker.Checker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import static com.finder.service.file.Constants.*;

@Controller("/site")
public class MainController {

    @Autowired
    private LinksFinder parser;
    @Autowired
    private Checker checker;
    @Autowired
    private FileManager fileManager;


    @GetMapping("/")
    public String index(Model model){
            Information information = new Information();
            model.addAttribute("information", information);
        return "index";
    }

    @GetMapping("/sites")
    public String sites(Model model){
            Information information = new Information();
            model.addAttribute("information", information);
            fileManager.setFileProperties(SITES);
            Properties properties = fileManager.getProperty();
            if (properties.size()>0){
                model.addAttribute("properties",properties);
            }
        return "sites";
    }

    @PostMapping("/add")
    public String add(Model model,
                      @ModelAttribute("information") Information information){
        String page = addition(model,information.getUri());
        if (page != null && page.equals("index")){
            return "index";
        }
        return "redirect:/sites";
    }

    private String addition(Model model, String uri) {
        checker.setUrl(uri);
        if (checker.checkInternetConnection()){
            fileManager.write(uri);
        } else {
            model.addAttribute("exc","page addition error");
            return "sites";
        }
        return null;
    }

    @PostMapping("/find")
    public String find(Model model,
                       @ModelAttribute("information") Information information){
            fileManager.setFileProperties(DATA);
            Properties dataProperties= fileManager.getProperty();
            Properties properties = new Properties();
            fileManager.setFileProperties(SITES);

            for(Object p:dataProperties.values()) {
                String infToFind = (String)p;

                Properties sites = fileManager.getProperty();
                if (sites.size() > 0) {
                    Collection<Object> stringsOfSites = sites.values();
                    for (Object object : stringsOfSites) {
                        String site = (String) object;
                        Properties siteProperties = parser.getLinks(site, infToFind);
                        Set<Object> keys = siteProperties.keySet();
                        for (Object key : keys) {
                            Object value = siteProperties.get(key);
                            properties.put(key, value);
                            fileManager.setFileProperties(NEWS);
                            fileManager.write((String)key);
                            fileManager.setFileProperties(SITES);
                        }
                    }
                }
            }
            model.addAttribute("properties", properties);

        return "information";
    }

    @PostMapping("/del")
    public String del(Model model,
                      @ModelAttribute("delId") String delId){
        if(!delId.isEmpty()){
            fileManager.setFileProperties(SITES);
            fileManager.delete(delId);
        }
        return "redirect:/sites";
    }

    @GetMapping("/data")
    public String data(Model model,
                       @ModelAttribute("dataInput") Data dataInput){
        fileManager.setFileProperties(DATA);

        String data = dataInput.getData();
        model.addAttribute("data", data);

        Properties properties = fileManager.getProperty();
        if (properties.size()>0){
            model.addAttribute("properties",properties);
        }

        return "data";
    }

    @PostMapping("/addData")
    public String addData(Model model,
                      @ModelAttribute("dataInput") Data dataInput){
        fileManager.setFileProperties(DATA);
        fileManager.write(dataInput.getData());
        return "redirect:/data";
    }

    @PostMapping("/delData")
    public String delData(Model model,
                      @ModelAttribute("delId") String delId){
        if(!delId.isEmpty()){
            fileManager.setFileProperties(DATA);
            fileManager.delete(delId);
        }
        return "redirect:/data";
    }



//    @PostMapping("/login")
//    public String login(){
//
//        return "redirect:/";
//    }

    @RequestMapping(value="/home")
    public String home() {
        return "home";
    }

    @RequestMapping(value="/about")
    public String about() {
        return "about";
    }
}
