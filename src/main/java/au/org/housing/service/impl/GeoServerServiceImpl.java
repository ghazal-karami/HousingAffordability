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
 * Implementation for checking the existence of GeoServer or required workspace
 * and styles.
 * 
 * @author Gh.Karami
 * @version 1.0
 * 
 */

@Service
public class GeoServerServiceImpl implements GeoServerService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GeoServerServiceImpl.class);

	@Autowired
	private GeoServerConfig geoServerConfig;

	GeoServerRESTPublisher publisher;
	GeoServerRESTReader reader;

	static String potentialStyleStart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\" version=\"1.0.0\">"
			+ "<sld:NamedLayer>" + "<sld:Name>Default Styler</sld:Name>"
			+ "<sld:UserStyle>" + "<sld:Name>Default Styler</sld:Name>"
			+ "<sld:Title>SLD Potential Development</sld:Title>"
			+ "<sld:FeatureTypeStyle>";

	static String potentialStyleEnd = "</sld:FeatureTypeStyle>" + "</sld:UserStyle>"
			+ "</sld:NamedLayer>" + "</sld:StyledLayerDescriptor>";


	static String assessmentStyle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\" version=\"1.0.0\">"
			+ "<sld:NamedLayer><sld:Name>Default Styler</sld:Name>" 
			+ "<sld:UserStyle>"
			+ "<sld:Name>Default Styler</sld:Name>"
			+ "<sld:Title>SLD Assessment Development</sld:Title>"
			+ "<sld:FeatureTypeStyle>"
			+ "<sld:Name>name</sld:Name>"
			+ "<sld:Rule>"
			+ "<sld:Name>output</sld:Name>"
			+ "<sld:Title>Output</sld:Title>"
			+ "<sld:PolygonSymbolizer>"
			+ "<sld:Fill>"
			+ "<sld:CssParameter name=\"fill\">#FF0000</sld:CssParameter>"
			+ "</sld:Fill>"
			+ "</sld:PolygonSymbolizer>"
			+ "</sld:Rule>"
			+ "</sld:FeatureTypeStyle>"
			+ "</sld:UserStyle>"
			+ "</sld:NamedLayer>"
			+ "</sld:StyledLayerDescriptor>";

	public boolean getGeoServer(String workspace) throws HousingException,
	MalformedURLException, URISyntaxException {
		geoServerExist();
		workSpaceExist(workspace);
		return true;
	}

	public boolean geoServerExist() throws HousingException,
	MalformedURLException {
		reader = new GeoServerRESTReader(geoServerConfig.getRESTURL(),
				geoServerConfig.getRESTUSER(), geoServerConfig.getRESTPW());
		if (reader == null) {
			throw new HousingException(Messages._ERROR_CONNECT_GEOSERVER);
		}
		if (!reader.existGeoserver()) {
			throw new HousingException(Messages._GEOSERVER_NOT_RUNNING);
		}
		return true;
	}

	public boolean workSpaceExist(String workspace) throws HousingException {
		List<String> workSpaces = reader.getWorkspaceNames();
		publisher = new GeoServerRESTPublisher(geoServerConfig.getRESTURL(),
				geoServerConfig.getRESTUSER(), geoServerConfig.getRESTPW());
		if (!workSpaces.contains(workspace)) {
			publisher.createWorkspace(workspace);
			LOGGER.info(Messages._WORKSPACE_NOT_EXIST_BUT_CREATED);
		}
		return true;
	}

	public boolean publishToGeoServer(String workspace, String dataStore,
			String layer, String style, File newFile)
					throws FileNotFoundException, IllegalArgumentException,
					MalformedURLException, HousingException, URISyntaxException {
		try {
			if (reader.getLayer(layer) != null) {
				boolean ftRemoved = publisher.unpublishFeatureType(workspace,
						dataStore, layer);
				publisher.removeLayer(workspace, layer);
				// boolean dsRemoved = publisher.removeDatastore(workspace,
				// dataStore, true);
			}
			String zipfileName = newFile.getAbsolutePath().substring(0,
					newFile.getAbsolutePath().lastIndexOf("."))
					+ ".zip";
			Zip zip = new Zip(zipfileName, newFile.getParentFile()
					.getAbsolutePath());
			zip.createZip();

			File zipFile = new File(zipfileName);
			boolean published = true;
			published = publisher.publishShp(workspace, dataStore, layer,
					zipFile, "EPSG:28355", style);
			if (!published) {
				throw new HousingException(Messages._LAYER_PUBLISH_FAILED);
			}

			GSLayerEncoder layerEncoder = new GSLayerEncoder();
			layerEncoder.setDefaultStyle(style);
			publisher.configureLayer(workspace, layer, layerEncoder);
			LOGGER.info("Publishsed Success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new HousingException(Messages._LAYER_PUBLISH_FAILED);
		}
		return true;
	}

	public boolean publishAssessmentStyle(String styleName) throws HousingException, URISyntaxException {
		boolean published = true;
		if (reader.existsStyle(styleName)) {
			published = publisher.updateStyle(assessmentStyle, styleName);
		} else {
			published = publisher.publishStyle(assessmentStyle, styleName);
		}
		if (!published) {
			throw new HousingException(Messages._STYLE_PUBLISH_FAILED);
		}
		return published;

	}

	public boolean publishPotentialStyle(String sldBody, String styleName)
			throws HousingException, URISyntaxException {
		boolean published = true;
		if (reader.existsStyle(styleName)) {
			published = publisher.updateStyle(sldBody, styleName);
		} else {
			published = publisher.publishStyle(sldBody, styleName);
		}
		if (!published) {
			throw new HousingException(Messages._STYLE_PUBLISH_FAILED);
		}
		return published;
	}

	public String createPotentialRule(String oldRules, int propertyOverlaysNum) {
		String fillColor = "";
		String sldTitle = "";
		System.out.println("propertyOverlaysNum==" + propertyOverlaysNum);

		if (propertyOverlaysNum == -1){
			sldTitle = "No Overlay Selected";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#FF0000</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 0){

			sldTitle = "No Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#00FF00</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 1){
			sldTitle = "1 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#FFFF00</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 2){
			sldTitle = "2 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#557FFF</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 3){
			sldTitle = "3 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#FF7F55</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 4){
			sldTitle = "4 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#00FFFF</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 5){
			sldTitle = "5 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#FF55FF</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 6){
			sldTitle = "6 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#810606</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 7){
			sldTitle = "7 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#AA00FF</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 8){
			sldTitle = "8 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#3D8952</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 9){
			sldTitle = "9 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#FFAF02</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 10){
			sldTitle = "10 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#B7C910</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 11){
			sldTitle = "11 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#23D1A6</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 12){
			sldTitle = "12 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#BB9D94</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 13){
			sldTitle = "13 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#0031E9</sld:CssParameter>"
					+ "</sld:Fill>";
		}else if (propertyOverlaysNum == 14){
			sldTitle = "14 Overlay";
			fillColor = "<sld:Fill>"
					+ "<sld:CssParameter name=\"fill\">#00AAFF</sld:CssParameter>"
					+ "</sld:Fill>";
		}

		String ruleStart = "<sld:Rule>"

		+ "<sld:Title>" + sldTitle + "</sld:Title>" + "<ogc:Filter>"
		+ "<ogc:PropertyIsEqualTo>"
		+ "<ogc:PropertyName>OverlaysNu</ogc:PropertyName>"
		+ "<ogc:Literal>" + propertyOverlaysNum + "</ogc:Literal>"
		+ "</ogc:PropertyIsEqualTo>" + "</ogc:Filter>"
		+ "<sld:PolygonSymbolizer>";

		String strokeColor = "<sld:Stroke>"
				+ "<sld:CssParameter name=\"stroke\">#FF0000</sld:CssParameter>"
				+ "</sld:Stroke>";

		String ruleEnd = "</sld:PolygonSymbolizer>" + "</sld:Rule>";

		String rules = oldRules + ruleStart + fillColor + strokeColor + ruleEnd;

		System.out.println(rules);
		System.out.println("--------------------------------");
		System.out.println(potentialStyleStart + rules + potentialStyleEnd);

		return rules;
	}

}


//
//public boolean stylesExist() throws HousingException, URISyntaxException {
//	if (!reader.existsStyle(geoServerConfig.getGsPotentialStyle())) {
//		URL url = this.getClass().getResource("/geoserver_styles");
//		File parentDirectory = new File(new URI(url.toString()));
//
//		//			File housingPotentialStyle = new File(parentDirectory,
//		//					"housingPotentialStyle.sld");
//		//			if (!publisher.publishStyle(housingPotentialStyle,
//		//					geoServerConfig.getGsPotentialStyle())) {
//		//				throw new HousingException(Messages._STYLE_PUBLISH_FAILED);
//		//			}
//		File housingAssessmentStyle = new File(parentDirectory,
//				"housingAssessmentStyle.sld");
//		if (!publisher.publishStyle(housingAssessmentStyle,
//				geoServerConfig.getGsAssessmentStyle())) {
//			throw new HousingException(Messages._STYLE_PUBLISH_FAILED);
//		}
//	}
//	return true;
//}
