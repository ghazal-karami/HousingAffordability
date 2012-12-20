package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import au.org.housing.service.BufferService;
import au.org.housing.service.ExportToShpService;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

@Service
public class BufferServiceImpl implements BufferService {

	@Autowired
	private ExportToShpService exportToShpService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BufferServiceImpl.class);
	
	public Collection<Geometry> createFeaturesBuffer(SimpleFeatureCollection features3, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException{
		Collection<Geometry> bufferCollection = new ArrayList<Geometry>();
		DefaultFeatureCollection newBufferFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
		SimpleFeatureBuilder builder = createFeatureBuilder();
		SimpleFeatureIterator it = features3.features();
		try {
			while (it.hasNext()) {
				SimpleFeature simpleFeature = it.next();
				Geometry featureGeometry = (Geometry) simpleFeature.getDefaultGeometryProperty().getValue();
				Geometry bufferGeometry = featureGeometry.buffer(0.1);
				newBufferFeatures = createNewFeatures(builder,newBufferFeatures, bufferGeometry);
				bufferCollection.add(bufferGeometry);
			}
		} finally {
			it.close(); // IMPORTANT
		}		
//        File newFile = new File("C:/Programming/Projects/Data/"+fileName+"/GDA94_MGA_zone_55/Housing_"+fileName+"_Buffer.shp");
		URL url = this.getClass().getResource("/shps-transport");
        File parentDirectory = new File(new URI(url.toString()));
        File newFile = new File(parentDirectory, "Housing_"+fileName+".shp");
       exportToShpService.featuresExportToShapeFile(builder.getFeatureType(), newBufferFeatures, newFile, true);
       return bufferCollection;
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
    private DefaultFeatureCollection createNewFeatures(
    		SimpleFeatureBuilder builder, DefaultFeatureCollection newFeatures, Geometry geomtery) {
		builder.add(geomtery);
	    newFeatures.add(builder.buildFeature(null));	
		return (DefaultFeatureCollection) newFeatures;
	}
    

}
