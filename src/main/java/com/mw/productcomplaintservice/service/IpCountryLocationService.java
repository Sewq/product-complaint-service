package com.mw.productcomplaintservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class IpCountryLocationService {

    private static final String API_URL = "http://ip-api.com/json/";
    private static final String COUNTRY_KEY = "country";
    private static final String DEFAULT_UNKNOWN_COUNTRY = "Unknown";
    private static final String STATUS = "status";
    private static final String FAIL = "fail";

    private final RestTemplate restTemplate;

    public String getCountryFromIp(String ip) {

        try {
            String response = restTemplate.getForObject(API_URL + ip, String.class);
            JSONObject jsonObject = new JSONObject(response);
            checkStatus(jsonObject, ip);
            return jsonObject.optString(COUNTRY_KEY, DEFAULT_UNKNOWN_COUNTRY);

        } catch (RestClientException ex) {

            log.error("Error calling IP lookup API: {}", ex.getMessage());
            return DEFAULT_UNKNOWN_COUNTRY;
        }

    }

    private void checkStatus(JSONObject jsonObject, String ip) {
        if (jsonObject.has(STATUS) && jsonObject.getString(STATUS).equals(FAIL)) {
            log.warn("Failed to fetch country from IP address: {}", ip);
        }
    }
}
