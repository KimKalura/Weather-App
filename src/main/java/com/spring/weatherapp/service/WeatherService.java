package com.spring.weatherapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.tomcat.websocket.server.UriTemplate;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.spring.weatherapp.dto.CurrentWeatherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;


import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}&units=metric";
    private static final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}&units=metric";
    @Value("${api.openweathermap.key}")
    private String apiKey;

    private RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CurrentWeatherDTO getCurrentWeather(String cityName) throws JsonProcessingException {
        JsonNode root = getResponseBodyJson(CURRENT_WEATHER_URL, cityName);
        return convertFromJsonToCurrentWeather(root);
    }

    public List<CurrentWeatherDTO> getForecastWeather (String cityName) throws  JsonProcessingException{
        JsonNode root = getResponseBodyJson(FORECAST_WEATHER_URL, cityName);
        ArrayNode forecastsListJson = (ArrayNode) root.path("list");
        List<CurrentWeatherDTO> forecastList = new ArrayList<>();
        for(JsonNode forecastJson : forecastsListJson) {
            forecastList.add(convertFromJsonToCurrentWeather(forecastJson));
        }
        return forecastList;
    }

    public JsonNode getResponseBodyJson (String requestBaseUrl, String cityName) throws JsonProcessingException {
        URI url = new UriTemplate(requestBaseUrl).expand(cityName, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.getBody());
    }

    public CurrentWeatherDTO convertFromJsonToCurrentWeather (JsonNode node){
        String dateString = node.path("dt_txt").asText();
        LocalDateTime date;
        if (dateString.equals("")){
            date = LocalDateTime.now();
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            date = LocalDateTime.parse(dateString, formatter);
        }
        String description = node.path("weather").get(0).path("description").asText();
        Double windSpeed = node.path("wind").path("speed").asDouble();
        Double temperature = node.path("main").path("temp").asDouble();
        Double feelsLikeTemperature = node.path("main").path("feels_like").asDouble();
        return new CurrentWeatherDTO(description, temperature, feelsLikeTemperature, windSpeed, date);
    }
}
