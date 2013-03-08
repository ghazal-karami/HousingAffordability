package au.org.housing.service.impl;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.config.GeoServerConfig;
import au.org.housing.exception.HousingException;
import au.org.housing.exception.Messages;
import au.org.housing.service.GeoServerService;
import au.org.housing.utilities.Zip;

/**
 * Implementation for checking the existence of 
 * GeoServer or required workspace and styles.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

@Service
public class GeoServerServiceImpl  implements GeoServerService{

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoServerServiceImpl.class);

	@Autowired
	private GeoServerConfig geoServerConfig; 

	GeoServerRESTPublisher publisher;
	GeoServerRESTReader reader;	

	public boolean getGeoServer(String workspace) throws HousingException, MalformedURLException, URISyntaxException{
		geoServerExist();
		workSpaceExist(workspace);	
		stylesExist();
		return true;			
	}

	public boolean geoServerExist() throws HousingException, MalformedURLException{
		reader = new GeoServerRESTReader(geoServerConfig.getRESTURL(), geoServerConfig.getRESTUSER(),geoServerConfig.getRESTPW());
		if (reader == null){
			throw new HousingException(Messages._ERROR_CONNECT_GEOSERVER);
		}
		if (!reader.existGeoserver()){
			throw new HousingException(Messages._GEOSERVER_NOT_RUNNING);
		}
		return true;
	}

	public boolean workSpaceExist(String workspace) throws HousingException{
		List<String> workSpaces = reader.getWorkspaceNames();
		publisher = new GeoServerRESTPublisher(geoServerConfig.getRESTURL(), geoServerConfig.getRESTUSER(),geoServerConfig.getRESTPW());
		if (!workSpaces.contains(workspace)){
			publisher.createWorkspace(workspace);
			LOGGER.info(Messages._WORKSPACE_NOT_EXIST_BUT_CREATED);
		}
		return true;
	}
	
	public boolean stylesExist() throws HousingException, URISyntaxException{
		if (!reader.existsStyle(geoServerConfig.getGsPotentialStyle())){
			URL url = this.getClass().getResource("/geoserver_styles");
			File parentDirectory = new File(new URI(url.toString()));

			File housingPotentialStyle = new File(parentDirectory, "housingPotentialStyle.sld");
			if (!publisher.publishStyle(housingPotentialStyle, geoServerConfig.getGsPotentialStyle())){
				throw new HousingException(Messages._STYLE_PUBLISH_FAILED);	        	
			}
			File housingAssessmentStyle = new File(parentDirectory, "housingAssessmentStyle.sld");
			if (!publisher.publishStyle(housingAssessmentStyle, geoServerConfig.getGsAssessmentStyle())){
				throw new HousingException(Messages._STYLE_PUBLISH_FAILED);	        	
			}			
		}
		return true;
	}

	public boolean publishToGeoServer(String workspace , String dataStore , String layer, String style, File newFile) throws FileNotFoundException, IllegalArgumentException, MalformedURLException, HousingException, URISyntaxException{
		try{
			if (reader.getLayer(layer)!=null){	
				boolean ftRemoved = publisher.unpublishFeatureType(workspace, dataStore, layer);
				publisher.removeLayer(workspace, layer);
				//	boolean dsRemoved = publisher.removeDatastore(workspace, dataStore, true);			
			}
			String zipfileName = newFile.getAbsolutePath().substring(0, newFile.getAbsolutePath().lastIndexOf("."))+".zip";
			Zip zip = new Zip(zipfileName,newFile.getParentFile().getAbsolutePath());			
			zip.createZip();

			File zipFile = new File(zipfileName);
			boolean published = true;
			published = publisher.publishShp(workspace,dataStore, layer, zipFile, "EPSG:28355", style);
			if (!published){
				throw new HousingException(Messages._WPRKSPACE_PUBLISH_FAILED);
			}

			GSLayerEncoder layerEncoder = new GSLayerEncoder();
			layerEncoder.setDefaultStyle(style);
			publisher.configureLayer(workspace, layer, layerEncoder);
			LOGGER.info("Publishsed Success");
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._WPRKSPACE_PUBLISH_FAILED);
		}
		return true;
	}

}
