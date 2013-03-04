package au.org.housing.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

public class TemporaryFileManager {
	private static Map<String, List<String>> _tempfilesInfo = new Hashtable<String, List<String>>();

	public static File getNew(HttpSession session, String username , String prefix, String suffix,boolean isDirectory)											
			throws IOException {
		synchronized (session) {
			File file =null;
			if (isDirectory){
				File tempDir = new File(System.getProperty("java.io.tmpdir"));
				//				file = Files.createTempDirectory(tempDir.toPath(),prefix + "_" + session.getId()+"_"+suffix
				file = Files.createTempDirectory(tempDir.toPath(), username+"_"+prefix+"_").toFile();				
			}else{
				file = File.createTempFile(prefix + "_" + session.getId()
						+ "_", suffix);				
			}			
			List<String> infos = _tempfilesInfo.get(session.getId());
			if (infos == null)
				infos = new ArrayList<String>();
			infos.add(file.getAbsolutePath());
			_tempfilesInfo.put(session.getId(), infos);
			return file;
		}
	}	

	//Ghazal Karami
	public static boolean deleteDir(HttpSession session) throws IOException{
		List<String> infos = _tempfilesInfo.get(session.getId());
		if (infos != null) {
			for (int i = 0; i < infos.size(); i++) {
				File dir = new File(infos.get(i));
				if (dir.isDirectory()) {
					FileUtils.deleteDirectory(dir);
				}
			}
		}
		return false;
	}

}
