package gr.hua.dit.greenride.core.service;


import gr.hua.dit.greenride.core.port.WeatherPort;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final WeatherPort weatherPort;

    public WeatherService(WeatherPort weatherPort) {
        this.weatherPort = weatherPort;
    }

    public WeatherPort.WeatherInfo current(double lat, double lon) {
        return weatherPort.getCurrent(lat, lon);
    }
}
