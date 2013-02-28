package au.org.housing.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class MainController extends MultiActionController{
	 
	@RequestMapping(value="/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request,
	   HttpServletResponse response) throws Exception {
	  return new ModelAndView("ui-jsp/mainPage.jsp");
	 }
	
}