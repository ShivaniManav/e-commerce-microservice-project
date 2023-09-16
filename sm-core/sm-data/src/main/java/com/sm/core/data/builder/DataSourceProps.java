package com.sm.core.data.builder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SuppressWarnings("unchecked")
public class DataSourceProps {

    private static final String dataSourceLabel = "%s-%s-datasource";

    //TODO: modify this class to load properties of by app and current profile only.
    private static final String resourceUrl = "http://localhost:8762/config-server/%s/dev/%s";

    private static final Map<String, Map<String, Object>> dataSourceProps = new HashMap<>();

    public static void loadDataSourceProps(String serviceName, String datasourceName, String vendor) {
        RestTemplate restTemplate = new RestTemplate();
        String label = String.format(dataSourceLabel, datasourceName, vendor);
        ResponseEntity<Object> response = restTemplate.getForEntity(String.format(resourceUrl, serviceName,
                label), Object.class);
        Map<String,String> responseBody = (Map<String,String>)response.getBody();
        if(Objects.isNull(responseBody)) {
            return;
        }
        List<Object> propertySources = Collections.singletonList(responseBody.get("propertySources"));
        propertySources = (List<Object>) propertySources.get(0);
        Map<String, Object> property = (Map<String, Object>) propertySources.get(0);
        dataSourceProps.put(getRootKey(serviceName,datasourceName,vendor),
                (Map<String, Object>) property.get("source"));
    }

    public static Boolean getBooleanProperty(String rootKey, String key) {
        if(Objects.isNull(key)) {
            throw new IllegalArgumentException("key argument cannot be null");
        }
        return Boolean.valueOf(dataSourceProps.get(rootKey).get(key).toString());
    }

    public static Long getNumericProperty(String rootKey, String key) {
        if(Objects.isNull(key)) {
            throw new IllegalArgumentException("key argument cannot be null");
        }
        return Long.parseLong(dataSourceProps.get(rootKey).get(key).toString());
    }

    public static String getStringProperty(String rootKey, String key) {
        if(Objects.isNull(key)) {
            throw new IllegalArgumentException("key argument cannot be null");
        }
        return dataSourceProps.get(rootKey).get(key).toString();
    }

    public static Double getDecimalProperty(String rootKey, String key) {
        if(Objects.isNull(key)) {
            throw new IllegalArgumentException("key argument cannot be null");
        }
        return Double.valueOf(dataSourceProps.get(rootKey).get(key).toString());
    }

    public static String getRootKey(String serviceName, String datasourceName, String vendor) {
        return serviceName + "-" + String.format(dataSourceLabel, datasourceName, vendor);
    }
}
