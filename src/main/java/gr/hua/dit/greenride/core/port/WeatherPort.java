package gr.hua.dit.greenride.core.port;


public interface WeatherPort {

    WeatherInfo getCurrent(double lat, double lon);

    record WeatherInfo(String description, double tempC, int humidity) {}
}
