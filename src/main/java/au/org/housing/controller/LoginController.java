package au.org.housing.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
/**
 * GeoServer Connection Parameters and configuration for 
 * publishing and visualization of the output layer
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Controller
public class LoginController {
 
	@RequestMapping(value="/loginSuccess", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, Principal principal ) {
		return "mainPage";
 
	}
 
	@RequestMapping(value="/loginForm", method = RequestMethod.GET)
	public String login(ModelMap model) { 
		return "loginPage";
 
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "loginPage";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
 
		return "loginPage";
 
	}
}