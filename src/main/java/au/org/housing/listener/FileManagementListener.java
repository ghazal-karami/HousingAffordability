package au.org.housing.listener;

import java.io.IOException;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import au.org.housing.utilities.TemporaryFileManager;

/**
 * Responsible for session creation and destroy.
 * Need to be changed. 
 *
 * @author Amir.Nasr, Gh.Karami
 * @version 1.0
 *
 */  

public class FileManagementListener implements HttpSessionListener{
	
	private static int totalActiveSessions = 0;

	public static int getTotalActiveSession(){
		return totalActiveSessions;
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		
		System.out.println("sessionCreated");
		totalActiveSessions++;
		printCounter(se);		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String username = auth.getName(); //get logged in username
		
		System.out.println("sessionDestroyed - deduct one session from counter");	
		totalActiveSessions--;
		printCounter(se);
		try {
//			TemporaryFileManager.deleteAll(se.getSession());  			 // Amir
			
//			String w = "housingWS_" + username+se.getSession().getId();
//			
			TemporaryFileManager.deleteDir(se.getSession());   			 // Ghazal			
//			if (!TemporaryFileManager.deleteFromGeoServer(se.getSession())){     // Ghazal
////				throw new Exception(Messages.getMessage());
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		SecurityContextImpl scImpl = null;
//		scImpl = (SecurityContextImpl)se.getSession().getAttribute("ACEGI_SECURITY_CONTEXT");
//		Object obj = null;
//		if ((scImpl != null) && (scImpl.getAuthentication().getPrincipal() != null)) {
//			obj = scImpl.getAuthentication().getPrincipal();
//		}            
//		if ((obj == null) /*|| !(obj instanceof ATUserDetails)*/) {
//			System.err.println("ERROR: can't find ATUserDetails");
//			return;
//		}
	}

	private void printCounter(HttpSessionEvent sessionEvent){		
		System.out.println(totalActiveSessions);
		System.out.println(sessionEvent.getSession().getId());	
		/*AbstractApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "springapp-servlet.xml" });
System.out.println("Add a shutdown hook for the above context");
context.registerShutdownHook();*/
		
		/*ConfigurableListableBeanFactory factory = new XmlBeanFactory(
                new FileSystemResource(
                        "springapp-servlet.xml"));
		
        Runtime.getRuntime().addShutdownHook(
                new Thread(new ShutdownHook(factory)));
        DestructiveBeanWithInterface bean = (DestructiveBeanWithInterface) factory.getBean("destructiveBean");*/

//		HttpSession session = sessionEvent.getSession();
//		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
//		CounterService counterService = (CounterService) ctx.getBean("counterService");
//		counterService.printCounter(totalActiveSessions);
	}

}
