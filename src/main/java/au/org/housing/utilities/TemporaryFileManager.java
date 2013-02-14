package au.org.housing.utilities;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

import au.org.housing.config.GeoServerConfig;
import au.org.housing.exception.Messages;

public class TemporaryFileManager {
	private static Map<String, List<String>> _tempfilesInfo = new Hashtable<String, List<String>>();

	private static GeoServerConfig geoServerConfig;
	private static GeoServerRESTPublisher publisher;
	private static GeoServerRESTReader reader;

	public static File getNew(HttpSession session, String prefix, String suffix,boolean isDirectory)
			throws IOException {
		synchronized (session) {
			File file =null;
			if (isDirectory){
				File tempDir = new File(System.getProperty("java.io.tmpdir"));
				file = Files.createTempDirectory(tempDir.toPath(),prefix + "_" + session.getId()+"_"+suffix
						).toFile();				
			}else{
				file = File.createTempFile(prefix + "_" + session.getId()
						+ "_", suffix);				
			}
			//			file.deleteOnExit();
			List<String> infos = _tempfilesInfo.get(session.getId());
			if (infos == null)
				infos = new ArrayList<String>();
			infos.add(file.getAbsolutePath());
			_tempfilesInfo.put(session.getId(), infos);
			return file;
		}
	}	

	public static void deleteAll(HttpSession session) throws IOException {
		synchronized (session) {
			List<String> infos = _tempfilesInfo.get(session.getId());
			if (infos != null) {
				for (int i = 0; i < infos.size(); i++) {
					File file = new File(infos.get(i));
					if (file.isDirectory()) {
						File[] listfiles = file.listFiles();
						for (int j = 0; j < listfiles.length; j++) {
							listfiles[i].delete();
						}
					} else
						file.delete();
				}
			}
			_tempfilesInfo.remove(session.getId());
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

	//Ghazal Karami
	public static boolean deleteFromGeoServer(HttpSession session) throws IOException{
		if (!reader.existGeoserver()){
			Messages.setMessage(Messages._GEOSERVER_NOT_EXIST);
			return false;
		}
		if (reader.getLayer(geoServerConfig.getGsPotentialLayer()+"_"+session.getId())!=null){			
			boolean ftRemoved = publisher.unpublishFeatureType(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentDatastore()+"_"+session.getId(),geoServerConfig.getGsAssessmentLayer()+"_"+session.getId());
			publisher.removeLayer(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentLayer()+"_"+session.getId());
			boolean dsRemoved = publisher.removeDatastore(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentDatastore()+"_"+session.getId(), true);
		}
		if (reader.getLayer(geoServerConfig.getGsAssessmentLayer()+"_"+session.getId())!=null){			
			boolean ftRemoved = publisher.unpublishFeatureType(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentDatastore()+"_"+session.getId(),geoServerConfig.getGsAssessmentLayer()+"_"+session.getId());
			publisher.removeLayer(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentLayer()+"_"+session.getId());
			boolean dsRemoved = publisher.removeDatastore(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentDatastore()+"_"+session.getId(), true);
		}
		return true;
	}
	
	public static void registerTempFile(HttpSession session, String absolutePath){
		synchronized (session) {
			List<String> infos = _tempfilesInfo.get(session.getId());
			if (infos == null)
				infos = new ArrayList<String>();
			infos.add(absolutePath);
			_tempfilesInfo.put(session.getId(), infos);
		}
	}


	public static GeoServerConfig getGeoServerConfig() {
		return geoServerConfig;
	}

	public static void setGeoServerConfig(GeoServerConfig geoServerConfig) {
		TemporaryFileManager.geoServerConfig = geoServerConfig;
	}

	public static GeoServerRESTPublisher getPublisher() {
		return publisher;
	}

	public static void setPublisher(GeoServerRESTPublisher publisher) {
		TemporaryFileManager.publisher = publisher;
	}

	public static GeoServerRESTReader getReader() {
		return reader;
	}

	public static void setReader(GeoServerRESTReader reader) {
		TemporaryFileManager.reader = reader;
	}











}
