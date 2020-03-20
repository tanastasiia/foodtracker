package ua.training.foodtracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.training.foodtracker.dto.FoodDTO;
import ua.training.foodtracker.dto.UserDTO;
import ua.training.foodtracker.dto.UserFoodDTO;
import ua.training.foodtracker.entity.Food;
import ua.training.foodtracker.entity.User;
import ua.training.foodtracker.entity.UserDetailsImpl;
import ua.training.foodtracker.entity.UserFood;
import ua.training.foodtracker.service.FoodService;
import ua.training.foodtracker.service.UserFoodService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class FoodController {

    @Autowired
    private UserFoodService userFoodService;

    @Autowired
    private FoodService foodService;

    @ModelAttribute("userFood")
    public UserFoodDTO userFoodDto() {
        return new UserFoodDTO();
    }

    @ModelAttribute("food")
    public FoodDTO FoodDto() {
        return new FoodDTO();
    }

    @GetMapping
    public String home(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTH OK");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            System.out.println(((UserDetailsImpl) principal).getUsername());
        }
        return "user/index.html";
    }

    @PostMapping("add_user_food")
    public String addUserFood(@ModelAttribute("userFood") @Valid UserFoodDTO userFoodDTO,
                                      BindingResult result) {

        Optional<Food> food = foodService.findByName(userFoodDTO.getFoodName());
        if (!food.isPresent()) {
            result.rejectValue("foodName", null, "There is no such food in db. Add new food to db via form below");
        }

        if (result.hasErrors()) {
            return "user/index.html";
        }

        userFoodService.save(userFoodDTO);
        return "redirect:/?success";
    }

    @PostMapping("add_food")
    public String addFood(@ModelAttribute("food") @Valid FoodDTO foodDTO,
                                      BindingResult result) {

        Optional<Food> food = foodService.findByName(foodDTO.getName());
        if (food.isPresent()) {
            result.rejectValue("name", null, "There is such food in db.");
        }

        if (result.hasErrors()) {
            return "user/index.html";
        }

        foodService.save(foodDTO);
        return "redirect:/?success";
    }
}