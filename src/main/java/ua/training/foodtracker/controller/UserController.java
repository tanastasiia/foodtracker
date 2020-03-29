package ua.training.foodtracker.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ua.training.foodtracker.config.SecurityConfiguration;
import ua.training.foodtracker.dto.*;
import ua.training.foodtracker.entity.User;
import ua.training.foodtracker.entity.UserDetailsImpl;
import ua.training.foodtracker.exception.FoodExistsException;
import ua.training.foodtracker.exception.FoodNotExistsException;
import ua.training.foodtracker.exception.PasswordIncorrectException;
import ua.training.foodtracker.exception.UserNotExistsException;
import ua.training.foodtracker.service.FoodService;
import ua.training.foodtracker.service.UserFoodService;
import ua.training.foodtracker.service.UserService;

import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private UserFoodService userFoodService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityConfiguration securityConfiguration;

    @GetMapping("user")
    public UserDTO user() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOp = userService.findByUsername(principal.getUsername());
        //log.info("{}",LocaleContextHolder.getLocale());
        return new UserDTO(userOp.get());
    }

    @GetMapping("todays_food")
    public UsersMealStatDTO getTodaysFood() {
        return userFoodService.findAllTodaysPrincipalStat();
    }

    @GetMapping("all_food")
    public UsersMealStatDTO getAllFood() {
        return userFoodService.findAllPrincipalStat();
    }

    @GetMapping("user_statistics")
    public UserTodayStatisticsDTO getTodaysUserStatistics() throws UserNotExistsException {
        return userFoodService.getTodaysPrincipalStatistics();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("add_user_food")
    public int addUserFood(UserMealDTO userMealDTO) throws FoodNotExistsException, UserNotExistsException {

        foodService.findByName(userMealDTO.getFoodName()).orElseThrow(FoodNotExistsException::new);

        log.info("User food added: {}", userFoodService.save(userMealDTO));

        return userFoodService.todaysCalories();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("add_food")
    public void addFood(FoodDTO foodDTO) throws FoodExistsException {

        if (foodService.findByName(foodDTO.getName()).isPresent()) {
            throw new FoodExistsException();
        }
        log.info("Food added: {}", foodService.save(foodDTO));

    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("change_password")
    public void changePassword(PasswordChangeDTO passwordChangeDTO) throws PasswordIncorrectException {

        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("PasswordChangeDTO: {}", passwordChangeDTO);

        if (!securityConfiguration.getPasswordEncoder()
                .matches(passwordChangeDTO.getOldPassword(), principal.getPassword())) {
            throw new PasswordIncorrectException();
        }

        userService.updatePassword(passwordChangeDTO.getNewPassword(), principal.getUsername());
        log.info("Password changed");
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("change_account")
    public void changeAccount(UserDTO userDTO) {

        //TODO record update in repository and service
    }
}

