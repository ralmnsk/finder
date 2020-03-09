package com.finder.service.parser;

import java.util.Properties;

public interface LinksFinder {
    Properties getLinks(String uri, String infToFind);
}
