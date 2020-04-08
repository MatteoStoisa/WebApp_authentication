package it.polito.ai.laboratorio1;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

@Controller
@Log(topic = "HomeController")
public class HomeController {

    @Autowired
    public RegistrationManager registrationManager;

    @GetMapping("/")
    public String home() {
        return "/homepage.html";
    }

    @GetMapping("/register")
    public String registrationPage(@ModelAttribute("command") RegistrationCommand registrationCommand,
                                   HttpSession httpSession,
                                   RedirectAttributes redirectAttributes) {
        if(httpSession.getAttribute("username") != null) {
            redirectAttributes.addFlashAttribute("logSessionLogged", "Already Logged In");
            return "redirect:/";
        }
        return "/register.html";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("command") RegistrationCommand registrationCommand,
                           BindingResult bindingResult,
                           HttpSession httpSession,
                           RedirectAttributes redirectAttributes) {
        if(httpSession.getAttribute("username") != null) {
            return "/";
        }
        if(!registrationCommand.registrationPassword.equals(registrationCommand.registrationPassword2)) {
            bindingResult.addError(new FieldError("command", "registrationPassword2","Passwords must match"));
        }
        if(!registrationCommand.registrationConditions) {
            bindingResult.addError(new FieldError("command", "registrationConditions","Conditions must be accepted"));
        }
        RegistrationDetails registrationDetails = new RegistrationDetails(registrationCommand.registrationName,
                registrationCommand.registrationSurname,
                registrationCommand.registrationEmail,
                registrationCommand.registrationPassword,
                new Date(System.currentTimeMillis()));
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.warning(objectError.toString()));
            return "/register";
        }
        if (registrationManager.putIfAbsent(registrationCommand.registrationEmail, registrationDetails) != null) {
            bindingResult.addError(new FieldError("command", "registrationEmail","Email already registered"));
        }
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.warning(objectError.toString()));
            return "/register";
        }
        else {
            log.info("correctly registered: " + registrationCommand.toString());
            redirectAttributes.addFlashAttribute("logCorrectlyRegistered", "Correctly Registered");
            return "redirect:/";
        }
    }

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("command") LoginCommand loginCommand,
                            HttpSession httpSession,
                            RedirectAttributes redirectAttributes) {
        if(httpSession.getAttribute("username") != null) {
            redirectAttributes.addFlashAttribute("logSessionLogged", "Already Logged In");
            return "redirect:/";
        }
        return "/login.html";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("command") LoginCommand loginCommand,
                        HttpSession httpSession,
                        RedirectAttributes redirectAttributes) {
        if(httpSession.getAttribute("username") != null) {
            return "/login";
        }
        if(loginCommand.loginEmail.equals("") || loginCommand.loginPassword.equals("")) {
            return "/login";
        }
        if(registrationManager.containsKey(loginCommand.loginEmail)) {
            if(registrationManager.get(loginCommand.loginEmail).userPassword.equals(loginCommand.loginPassword)) {
                httpSession.setAttribute("username", loginCommand.loginEmail);
                redirectAttributes.addFlashAttribute("LogSuccessLogin", "Login Successfully");
                return "redirect:/private";
            }
            else {
                log.warning("wrong password");
                return "/login";
            }
        }
        else {
            log.warning("wrong email");
            return "/login";
        }
    }

    @GetMapping("/private")
    public String privatePage(HttpSession httpSession,
                              RedirectAttributes redirectAttributes) {
        if(httpSession.getAttribute("username") != null) {
            return "/private.html";
        }
        else {
            log.warning("no logged session");
            redirectAttributes.addFlashAttribute("logNoLogged", "Login To Go Private");
            return "redirect:/";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("username");
        return "redirect:/";
    }

}
