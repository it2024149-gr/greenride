package gr.hua.dit.greenride.adapters.weather;


import gr.hua.dit.greenride.core.port.WeatherPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OpenWeatherHttpAdapter implements WeatherPort {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;
    private final String units;
    private final String lang;

    public OpenWeatherHttpAdapter(
            RestTemplate restTemplate,
            @Value("${weather.base-url}") String baseUrl,
            @Value("${weather.api-key}") String apiKey,
            @Value("${weather.units:metric}") String units,
            @Value("${weather.lang:el}") String lang
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.units = units;
        this.lang = lang;
    }

    @Override
    public WeatherInfo getCurrent(double lat, double lon) {

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/weather")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .queryParam("units", units)
                .queryParam("lang", lang)
                .toUriString();

        try {
            OpenWeatherResponse res = restTemplate.getForObject(url, OpenWeatherResponse.class);

            if (res == null || res.main == null || res.weather == null || res.weather.length == 0) {
                return new WeatherInfo("N/A", 0.0, 0);
            }

            String desc = res.weather[0].description;
            return new WeatherInfo(desc, res.main.temp, res.main.humidity);

        } catch (Exception ex) {
            // external black-box: αν πέσει, δεν “σπάμε” την εφαρμογή
            return new WeatherInfo("Weather service unavailable", 0.0, 0);
        }
    }

    // --- DTO classes (minimal) ---
    public static class OpenWeatherResponse {
        public Weather[] weather;
        public Main main;
    }

    public static class Weather {
        public String description;
    }

    public static class Main {
        public double temp;
        public int humidity;
    }
}
