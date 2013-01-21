package au.org.housing.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.model.Parameter;
import au.org.housing.service.BufferService;
import au.org.housing.service.Config;
import au.org.housing.service.TransportationBufferService;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;

import com.vividsolutions.jts.geom.Geometry;

@Service
public class TransportationBufferServiceImpl implements TransportationBufferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransportationBufferServiceImpl.class);

	@Autowired
	private Parameter parameter;

	@Autowired
	private BufferService bufferService;

	@Autowired
	private UnionService unionService;

	@Autowired
	private ValidationService validationService;

	SimpleFeatureSource trainStationFc;
	SimpleFeatureSource trainRouteFc;
	SimpleFeatureSource tramRouteFc;

	Collection<Geometry> trasportationbufferCollection;

	public Geometry generateTranportBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException {

		trasportationbufferCollection = new ArrayList<Geometry>();

		if (parameter.getTrain_St_BufferDistance() != 0) {
//			trainStationFc = Config.getDefaultFactory().getFeatureSource(MapAttImpl.trainStation);
			trainStationFc = Config.getGeoJSONFileFactory().getFeatureSource(MapAttImpl.trainStation);
			if (validationService.isPoint(trainStationFc,MapAttImpl.trainStation) && validationService.isMetric(trainStationFc,MapAttImpl.trainStation)) {
				Collection<Geometry> bufferCollection = bufferService.createFeaturesBuffer(trainStationFc.getFeatures(),
						parameter.getTrain_St_BufferDistance(), MapAttImpl.trainStation);
				if (bufferCollection != null){
					trasportationbufferCollection.addAll(bufferCollection);
				}
			}
		}
		if (parameter.getTrain_Rt_BufferDistance() != 0) {
			trainRouteFc = Config.getGeoJSONFileFactory().getFeatureSource(MapAttImpl.trainRoute);
			if (validationService.isLine(trainRouteFc, MapAttImpl.trainRoute) && validationService.isMetric(trainRouteFc,MapAttImpl.trainRoute)) {
				Collection<Geometry> bufferCollection = bufferService.createFeaturesBuffer(trainRouteFc.getFeatures(),
						parameter.getTrain_Rt_BufferDistance(),MapAttImpl.trainRoute);
				if (bufferCollection != null){
					trasportationbufferCollection.addAll(bufferCollection);
				}
			}
		}
		if (parameter.getTram_Rt_BufferDistance() != 0) {
			tramRouteFc = Config.getGeoJSONFileFactory().getFeatureSource(MapAttImpl.tramRoute);
			if (validationService.isLine(tramRouteFc, MapAttImpl.tramRoute) && validationService.isMetric(tramRouteFc,MapAttImpl.tramRoute)) {
				Collection<Geometry> bufferCollection = bufferService.createFeaturesBuffer(tramRouteFc.getFeatures(),
						parameter.getTram_Rt_BufferDistance(),MapAttImpl.tramRoute); 
				if (bufferCollection != null){
					trasportationbufferCollection.addAll(bufferCollection);
				}
			}
		}
		Geometry union = unionService.createUnion(trasportationbufferCollection);
		return union;
	}	

}
