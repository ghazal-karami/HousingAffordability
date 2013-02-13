package au.org.housing.listener;

import java.io.IOException;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import au.org.housing.exception.Messages;
import au.org.housing.utilities.TemporaryFileManager;

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
		System.out.println("sessionDestroyed - deduct one session from counter");	
		totalActiveSessions--;
		printCounter(se);
		try {
//			TemporaryFileManager.deleteAll(se.getSession());  			 // Amir			
			TemporaryFileManager.deleteDir(se.getSession());   			 // Ghazal			
			if (!TemporaryFileManager.deleteFromGeoServer(se.getSession())){     // Ghazal
				throw new Exception(Messages.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void printCounter(HttpSessionEvent sessionEvent){		
		System.out.println(totalActiveSessions);
		System.out.println(sessionEvent.getSession().getId());		
//		HttpSession session = sessionEvent.getSession();
//		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
//		CounterService counterService = (CounterService) ctx.getBean("counterService");
//		counterService.printCounter(totalActiveSessions);
	}

}
