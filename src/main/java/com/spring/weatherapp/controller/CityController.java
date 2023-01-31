package com.spring.weatherapp.controller;

import com.spring.weatherapp.model.City;
import com.spring.weatherapp.model.User;
import com.spring.weatherapp.service.CityService;
import com.spring.weatherapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    private CityService cityService;
    private UserService userService;



    @Autowired
    public CityController(CityService cityService, UserService userService) {
        this.cityService = cityService;
        this.userService = userService;
    }


    @PostMapping("/add/{userId}")
    public User addFavoriteCity(@RequestBody City city, @PathVariable Long userId){
        return cityService.addFavoriteCity(city, userId);
    }

    @DeleteMapping("/delete/{cityId}/{userId}")
    public User deleteCity (@PathVariable Long cityId, @PathVariable Long userId) {
        return cityService.deleteCity(cityId, userId);
    }

    @GetMapping("/{userId}")
    public List<City> getUserFavoriteCities(@PathVariable Long userId) {
        return cityService.getUserFavoriteCities(userId);
    }

}
