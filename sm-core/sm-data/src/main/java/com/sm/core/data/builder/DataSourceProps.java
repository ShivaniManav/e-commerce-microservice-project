package com.sm.core.data.builder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.*;

@SuppressWarnings("unchecked")
public class DataSourceProps {

    private static final String resourceUrl = "http://localhost:8888/config-server/%s/dev/%s-datasource";

    private static final Map<String, Map<String, Object>> dataSourceProps = new HashMap<>();

    public static void loadDataSourceProps(String datasourceName, String vendor) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response
                = restTemplate.getForEntity(String.format(resourceUrl, datasourceName, vendor), Object.class);
        Map<String,String> responseBody = (Map<String,String>)response.getBody();
        if(Objects.isNull(responseBody)) {
            return;
        }
        List<Object> propertySources = Collections.singletonList(responseBody.get("propertySources"));
        propertySources = (List<Object>) propertySources.get(0);
        Map<String, Object> property = (Map<String, Object>) propertySources.get(0);
        dataSourceProps.put(datasourceName, (Map<String, Object>) property.get("source"));
    }

    public static Boolean getBooleanProperty(String dataSourceName, String key) {
        if(Objects.isNull(key)) {
            throw new IllegalArgumentException("key argument cannot be null");
        }
        return (Boolean) dataSourceProps.get(dataSourceName).get(key);
    }

    public static Long getNumericProperty(String dataSourceName, String key) {
        if(Objects.isNull(key)) {
            throw new IllegalArgumentException("key argument cannot be null");
        }
        return (Long) dataSourceProps.get(dataSourceName).get(key);
    }

    public static String getStringProperty(String dataSourceName, String key) {
        if(Objects.isNull(key)) {
            throw new IllegalArgumentException("key argument cannot be null");
        }
        return (String) dataSourceProps.get(dataSourceName).get(key);
    }

    public static Double getDecimalProperty(String dataSourceName, String key) {
        if(Objects.isNull(key)) {
            throw new IllegalArgumentException("key argument cannot be null");
        }
        return (Double) dataSourceProps.get(dataSourceName).get(key);
    }

}
