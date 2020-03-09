package com.finder.service.site.checker;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

@Component
@Getter
@Setter
@NoArgsConstructor
public class Checker {
    private String url;
    private InetAddress remoteAddress;

    public Checker(String url) {
        this.url = url;
    }

    public boolean checkInternetConnection() {
        Boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
