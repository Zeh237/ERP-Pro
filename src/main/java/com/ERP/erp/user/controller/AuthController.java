package com.ERP.erp.user.controller;

import com.ERP.erp.user.dto.PasswordConfirmationDto;
import com.ERP.erp.user.dto.UserDto;
import com.ERP.erp.user.dto.UserUpdateDto;
import com.ERP.erp.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto, Model model) {
        try {
            userService.registerNewUser(userDto);
            return "redirect:/login";
        } catch (Exception ex) {
            logger.error("Error during user registration: {}", ex.getMessage(), ex);
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model){
        model.addAttribute("user", userDetails);

        return "home";
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserDto userDto = userService.getUserByName(userDetails.getUsername());
        model.addAttribute("user", userDto);
        return "profile";
    }

    @GetMapping("/change-password")
    public String showPasswordForm(@AuthenticationPrincipal UserDetails userDetails,
                                   Model model) {
        UserDto currentUser = userService.getUserByName(userDetails.getUsername());
        UserUpdateDto passwordForm = new UserUpdateDto();
        passwordForm.setId(currentUser.getId());
        model.addAttribute("passwordForm", passwordForm);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @Valid @ModelAttribute("passwordForm") UserUpdateDto passwordForm,
                                 BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            return "change-password";
        }

        try {
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setId(passwordForm.getId());
            updateDto.setPassword(passwordForm.getPassword());

            userService.updateUser(updateDto);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update password: " + e.getMessage());
            return "change-password";
        }
    }

    @GetMapping("/change-email")
    public String showEmailForm(@AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        UserDto currentUser = userService.getUserByName(userDetails.getUsername());
        UserUpdateDto emailForm = new UserUpdateDto();
        emailForm.setId(currentUser.getId());
        emailForm.setEmail(currentUser.getEmail());
        model.addAttribute("emailForm", emailForm);
        return "change-email";
    }

    @PostMapping("/change-email")
    public String changeEmail(@AuthenticationPrincipal UserDetails userDetails,
                              @Valid @ModelAttribute("emailForm") UserUpdateDto emailForm,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            return "change-email";
        }

        try {
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setId(emailForm.getId());
            updateDto.setEmail(emailForm.getEmail());

            userService.updateUser(updateDto);
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update email: " + e.getMessage());
            return "change-email";
        }
    }

    @GetMapping("/update-profile")
    public String showProfileForm(@AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        UserDto currentUser = userService.getUserByName(userDetails.getUsername());
        UserUpdateDto profileForm = new UserUpdateDto();
        profileForm.setId(currentUser.getId());
        profileForm.setFirstName(currentUser.getFirstName());
        profileForm.setLastName(currentUser.getLastName());
        profileForm.setEmail(currentUser.getEmail());
        profileForm.setDepartment(currentUser.getDepartment());
        profileForm.setJobTitle(currentUser.getJobTitle());

        model.addAttribute("profileForm", profileForm);
        return "update-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid @ModelAttribute("profileForm") UserUpdateDto profileForm,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            return "update-profile";
        }

        try {
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setId(profileForm.getId());
            updateDto.setFirstName(profileForm.getFirstName());
            updateDto.setLastName(profileForm.getLastName());
            updateDto.setEmail(profileForm.getEmail());
            updateDto.setDepartment(profileForm.getDepartment());
            updateDto.setJobTitle(profileForm.getJobTitle());

            userService.updateUser(updateDto);
            return "redirect:/profile?updateSuccess";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            return "update-profile";
        }
    }

    @GetMapping("/confirm-deactivate")
    public String showDeactivateForm(@AuthenticationPrincipal UserDetails userDetails,
                                     Model model) {
        model.addAttribute("passwordForm", new PasswordConfirmationDto());
        return "confirm-deactivate";
    }

    @PostMapping("/deactivate")
    public String deactivateAccount(@AuthenticationPrincipal UserDetails userDetails,
                                    @Valid @ModelAttribute("passwordForm") PasswordConfirmationDto passwordForm,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "confirm-deactivate";
        }

        try {
            if (!userService.verifyPassword(userDetails.getUsername(), passwordForm.getPassword())) {
                result.rejectValue("password", "error.passwordForm", "Incorrect password");
                return "confirm-deactivate";
            }
            userService.deactivateUser(userDetails.getUsername());
            SecurityContextHolder.clearContext();

            redirectAttributes.addFlashAttribute("message", "Your account has been deactivated");
            return "redirect:/login?deactivated";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to deactivate account: " + e.getMessage());
            return "redirect:/confirm-deactivate";
        }
    }

}
