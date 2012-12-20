package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import au.org.housing.model.Parameter;
import au.org.housing.service.BufferService;
import au.org.housing.service.ExportToShpService;
import au.org.housing.service.TransportationBufferService;
import au.org.housing.service.UnionService;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

@Service
public class TransportationBufferServiceImpl implements TransportationBufferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransportationBufferServiceImpl.class);
	Collection<Geometry> trasportationbufferCollection ;
	
	@Autowired
	private Parameter parameter;
	
	@Autowired
	private BufferService bufferService;
	
	@Autowired
	private UnionService unionService;
	
	@Autowired
	private ExportToShpService exportToShpService;
	
	private DataStore dataStore;
	
	SimpleFeatureSource trainStationFc ;
	SimpleFeatureSource trainRouteFc ;
	SimpleFeatureSource tramRouteFc ;
	SimpleFeatureSource busRouteFc ;
	
	public Geometry generateTranportBuffer(DataStore datastore) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException{
		
		trasportationbufferCollection = new ArrayList<Geometry>();
		this.dataStore = datastore;
		
    	if (parameter.getTrain_St_BufferDistance() != 0){
    		trainStationFc = dataStore.getFeatureSource("train_station"); 
    		SimpleFeatureCollection features = trainStationFc.getFeatures();
    		System.out.println("features== " + features.size());
    		Collection<Geometry> geoms = bufferService.createFeaturesBuffer(features, parameter.getTrain_St_BufferDistance(), "Train-Station");
    		trasportationbufferCollection.addAll(geoms);
    	}
    	if (parameter.getTrain_Rt_BufferDistance() != 0){
    		trainRouteFc = dataStore.getFeatureSource("train_route");  
    		trasportationbufferCollection.addAll(bufferService.createFeaturesBuffer(trainRouteFc.getFeatures(), parameter.getTrain_Rt_BufferDistance(), "Train-Route"));
    	}
    	if (parameter.getTram_Rt_BufferDistance() != 0){
    		tramRouteFc = dataStore.getFeatureSource("tram_route");  
    		trasportationbufferCollection.addAll(bufferService.createFeaturesBuffer(tramRouteFc.getFeatures(), parameter.getTram_Rt_BufferDistance(), "Tram-Route"));
    	}    	
    	Geometry union = unionService.createUnion(trasportationbufferCollection);
    	exportToShp(union);	
    	return union;     	
	}
	
	
	//*************************   ***************************
    public void exportToShp(Geometry union) throws URISyntaxException, NoSuchAuthorityCodeException, IOException, FactoryException {
    	System.out.println("floodway union");
    	URL url = this.getClass().getResource("/shps-transport");
        File parentDirectory = new File(new URI(url.toString()));
        File newFile = new File(parentDirectory, "Housing_Floodway_Union.shp");
//    	File newFile = new File("C:/Programming/Projects/Data/TransportBuffer/GDA94_MGA_zone_55/Housing_Transport_Buffer_union.shp");
    	
        DefaultFeatureCollection unionFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
    	SimpleFeatureBuilder builder = createFeatureBuilder();
    	builder.add(union);
    	unionFeatures.add(builder.buildFeature(null));
    	exportToShpService.featuresExportToShapeFile(builder.getFeatureType(), unionFeatures, newFile, true);
	}
	    
	//*************************   ***************************
    private SimpleFeatureBuilder createFeatureBuilder() {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("a-type-name");
		typeBuilder.add("location", MultiPolygon.class);
		SimpleFeatureType type = typeBuilder.buildFeatureType();
		return new SimpleFeatureBuilder(type);
	}
	
    //*************************   ***************************
    
    public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

       
}
