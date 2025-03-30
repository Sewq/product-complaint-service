package com.mw.productcomplaintservice.service;

import com.mw.productcomplaintservice.configuration.RestTemplateConfiguration;
import jakarta.inject.Inject;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {IpCountryLocationServiceImpl.class, RestTemplateConfiguration.class, RestTemplate.class})
@ExtendWith(OutputCaptureExtension.class)
class IpCountryLocationServiceIntegrationTest {
    private static final String UNKNOWN_COUNTRY = "Unknown";
    private static final String US_IP = "8.8.8.8";
    private static final String PL_IP = "109.125.192.0";
    @Inject
    private IpCountryLocationService ipCountryLocationService;

    @ParameterizedTest
    @MethodSource("sourceForValidIpQueries")
    void shouldGetCountryLocation_basedOnIp(String ip, String country) {
        String countryFromIp = ipCountryLocationService.getCountryFromIp(ip);
        assertThat(countryFromIp).isEqualTo(country);
    }

    @ParameterizedTest
    @MethodSource("sourceForInvalidIpQueries")
    void shouldGetUnknownCountryLocation_whenInvalidIpProvided(String ip, String logMessage, CapturedOutput capturedOutput) {
        String countryFromIp = ipCountryLocationService.getCountryFromIp(ip);
        assertThat(countryFromIp).isEqualTo(UNKNOWN_COUNTRY);
        assertThat(capturedOutput).contains(logMessage);
    }

    static Stream<Arguments> sourceForValidIpQueries() {
        return Stream.of(
                Arguments.of(US_IP, "United States"),
                Arguments.of(PL_IP, "Poland")
        );
    }

    static Stream<Arguments> sourceForInvalidIpQueries() {
        return Stream.of(
                Arguments.of(null, "Failed to fetch country from IP address: null"),
                Arguments.of("invalid", "Failed to fetch country from IP address: invalid"),
                Arguments.of("123.453.2", "Failed to fetch country from IP address: 123.453.2")
        );
    }
}