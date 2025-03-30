package com.mw.productcomplaintservice.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IpCountryLocationServiceTest {
    private static final String IP_SERVICE_URL = "http://ip-api.com/json/";
    private static final String UNITED_STATES_COUNTRY = "United States";
    public static final String IP = "8.8.8.8";
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IpCountryLocationServiceImpl ipLocationService;

    @Test
    public void shouldReturnCountryName_whenCorrectIpProvided() throws JSONException {
        String mockResponse = new JSONObject()
                .put("country", UNITED_STATES_COUNTRY)
                .put("status", "success")
                .toString();

        when(restTemplate.getForObject(IP_SERVICE_URL + IP, String.class))
                .thenReturn(mockResponse);

        String country = ipLocationService.getCountryFromIp(IP);
        assertThat(country).isEqualTo(UNITED_STATES_COUNTRY);
    }

    @Test
    public void shouldReturnDefaultCountryName_whenPrivateIpProvided() throws JSONException {
        String ip = null;

        when(restTemplate.getForObject(IP_SERVICE_URL + ip, String.class))
                .thenReturn(new JSONObject().put("status", "fail").toString());

        String country = ipLocationService.getCountryFromIp(ip);
        assertThat(country).isEqualTo("Unknown");
    }

    @Test
    public void shouldReturnDefaultCountryName_whenInternetOrConnectionFailureOccurs() {

        when(restTemplate.getForObject(IP_SERVICE_URL + IP, String.class))
                .thenThrow(new RestClientException("Failed to connect to the Internet"));

        String country = ipLocationService.getCountryFromIp(IP);
        assertThat(country).isEqualTo("Unknown");
    }
}