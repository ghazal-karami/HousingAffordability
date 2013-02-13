package au.org.housing.controller;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.security.annotation.Secured;

@Controller 
@RequestMapping("/housing-controller")
public class DownloadController {	
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
 
	 @RequestMapping(value = "downloadGeneratedOutput", method = RequestMethod.GET)
	  public void downloadGeneratedOutput(HttpServletRequest request,HttpServletResponse response) throws Exception {
		  synchronized (request.getSession()) {	
			  if (request.getSession().getAttribute("Generated_File_Location")==null)
				  throw new Exception("No output is generated") ;
			  File file = new File((String) request.getSession().getAttribute("Generated_File_Location")); 
			  response.setContentType("application/x-download");
			  response.setHeader("Content-disposition", "attachment; filename=" + "agent_walkability_output.geojson");
			  FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
		  }
	  }
	  
	
}