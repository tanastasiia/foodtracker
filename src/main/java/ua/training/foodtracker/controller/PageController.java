package ua.training.foodtracker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.training.foodtracker.config.LocaleConfiguration;
import ua.training.foodtracker.entity.User;
import ua.training.foodtracker.entity.UserDetailsImpl;
import ua.training.foodtracker.entity.UserFood;
import ua.training.foodtracker.service.FoodService;
import ua.training.foodtracker.service.UserFoodService;

import java.util.List;
import java.util.Locale;

@Controller
public class PageController {

    @Autowired
    private UserFoodService userFoodService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private LocaleConfiguration localeConfiguration;


    @GetMapping("/login")
    public String login(Model model) {
        return "login.html";
    }

    @RequestMapping("/account")
    public String account(){
        return "user/account.html";
    }

    @GetMapping(value = { "/statistics" })
    public String personList(Model model) {

        List<UserFood> todaysFood = userFoodService.findAllTodays();

        model.addAttribute("todaysFood", todaysFood);

        return "user/statistics.html";
    }

    /*@RequestMapping("/registration")
    public String registration(){
        System.out.println("REG");
        return "registration.html";
    }*/

    @RequestMapping("/user")
    public String user() {
        return "user/index.html";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin/index.html";
    }
}
