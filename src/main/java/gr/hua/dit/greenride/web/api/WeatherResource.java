package gr.hua.dit.greenride.web.api;


import gr.hua.dit.greenride.core.port.WeatherPort;
import gr.hua.dit.greenride.core.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherResource {

    private final WeatherService weatherService;

    public WeatherResource(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherPort.WeatherInfo current(@RequestParam double lat,
                                           @RequestParam double lon) {
        return weatherService.current(lat, lon);
    }
}
