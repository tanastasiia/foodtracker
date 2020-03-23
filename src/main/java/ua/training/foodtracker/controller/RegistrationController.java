package ua.training.foodtracker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.training.foodtracker.dto.UserRegDTO;
import ua.training.foodtracker.entity.User;
import ua.training.foodtracker.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegDTO userRegDto() {
        return new UserRegDTO();
    }

    @GetMapping
    public String showRegistrationForm() {

        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegDTO userRegDto,
                                      BindingResult result){

        Optional<User> user = userService.findByUsername(userRegDto.getUsername());
        if (user.isPresent()){
            result.rejectValue("username", null, "There is already an account registered with that username");
        }

        if (result.hasErrors()){
            return "redirect:/registration?error";
        }

        userService.save(userRegDto);
        return "redirect:/registration?success";
    }

}
