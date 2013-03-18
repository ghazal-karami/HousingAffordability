package au.org.housing.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import au.org.housing.config.GeoServerConfig;
import au.org.housing.config.InputLayersConfig;
import au.org.housing.exception.Messages;
import au.org.housing.service.GeoServerService;
import au.org.housing.service.PostGISService;
import au.org.housing.start.StartController;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(StartController.class);

	@Autowired InputLayersConfig layersConfig;	
	@Autowired GeoServerConfig geoServerConfig;
	@Autowired PostGISService postGISService;
	@Autowired GeoServerService geoServerService;


	@RequestMapping(value="/loginSuccess", method = RequestMethod.GET)
	public ModelAndView  printWelcome(Principal principal, HttpServletRequest request, HttpSession session ) {
		ModelAndView mav = new ModelAndView();		
		if (principal == null){
			mav.setViewName("loginPage");
			return mav;
		}		
		System.out.println("session.getId()="+session.getId());
		System.out.println("request.getSession()="+request.getSession().getId());		
		System.out.println("principal.getName()="+principal.getName());		
		
		String username = principal.getName();
		String workspace = geoServerConfig.getGsWorkspace() + "_" + username;			

		mav.setViewName("mainPage");
		mav.addObject("username", username);
		mav.addObject("successStatus", Messages._SUCCESS);
		
		try{
			postGISService.getPOSTGISDataStore();
			geoServerService.getGeoServer(workspace);
		}catch(Exception e){    
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			mav.addObject("successStatus", Messages._UNSUCCESS);			
			mav.addObject("message", e.getMessage() );	
		}
		return mav; 
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
	public String logout(ModelAndView mv, HttpSession session) {		
		System.out.println("logout");
//		mv.setViewName("loginPage");
//		return mv;
		return "loginPage";

	}
}