package au.org.housing.start;

import java.io.IOException;

import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.vividsolutions.jts.io.ParseException;


@Controller
public class StartController {
	
	@RequestMapping("/hello.html")	
	public String handleRequest(Model model) throws ParseException, IOException, FactoryException, InstantiationException, IllegalAccessException, MismatchedDimensionException, TransformException {

		model.addAttribute("message", "HELLO!!!");
		return "mainPage"; 
		
	}
}
