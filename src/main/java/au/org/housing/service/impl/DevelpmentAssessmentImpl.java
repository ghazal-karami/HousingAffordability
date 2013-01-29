package au.org.housing.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Geometry;

import au.org.housing.model.AppCategoryOutcome;
import au.org.housing.model.LayerMapping;
import au.org.housing.model.ParameterDevelopAssessment;
import au.org.housing.service.Config;
import au.org.housing.service.DevelpmentAssessment;

@Service
public class DevelpmentAssessmentImpl implements DevelpmentAssessment {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnionServiceImpl.class);

	@Autowired
	private ParameterDevelopAssessment parameter;

	@Autowired
	private LayerMapping layerMapping;

	SimpleFeatureSource propertyFc;
	

	public void analyse() throws IOException {
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());		
		
		List<Filter> pparsFilters = new ArrayList<Filter>();
		List<Filter> lgaFilters = new ArrayList<Filter>();
		List<Filter> propertyFilters = new ArrayList<Filter>();
		
		SimpleFeatureSource pparsFc = Config.getDefaultFactory().getFeatureSource(layerMapping.getPpars());
		SimpleFeatureSource propertyFc = Config.getDefaultFactory().getFeatureSource(layerMapping.getProperty());
				
		for (String lgaCode : parameter.getSelectedLGAs2()){
			Filter lgaFilter = ff.equals(ff.property("lga_code"), ff.literal(lgaCode));
			lgaFilters.add(lgaFilter);
		}
		Filter lgaFilter = ff.or(lgaFilters);
		System.out.println("lgaFilter="+lgaFilter);
				
		if (parameter.getDurationAssessment() != 0) {
			String operator = parameter.getDurationAssessmentOperateor();
			Filter filter = null;
			switch (operator.charAt(0)) {
			case '>':
				filter = ff.greater(ff.property("duration"), ff.literal(parameter.getDurationAssessment()));
				break;
			case '=':
				filter = ff.equals(ff.property("duration"), ff.literal(parameter.getDurationAssessment()));
				break;
			case '<':
				filter = ff.less(ff.property("duration"), ff.literal(parameter.getDurationAssessment()));
				break;
			}
			pparsFilters.add(filter);
		}
		
		if (parameter.getNumOfObjection() != 0) {
			String operator = parameter.getNumOfObjectionOperateor();
			Filter filter = null;
			switch (operator.charAt(0)) {
			case '>':
				filter = ff.greater(ff.property("objections"), ff.literal(parameter.getNumOfObjection()));
				break;
			case '=':
				filter = ff.equals(ff.property("objections"), ff.literal(parameter.getNumOfObjection()));
				break;
			case '<':
				filter = ff.less(ff.property("objections"), ff.literal(parameter.getNumOfObjection()));
				break;
			}
			pparsFilters.add(filter);
		}
		
		
		if (parameter.getFurtherInfo() == 2) {
			Filter filter = ff.equals(ff.property("furtherinf"), ff.literal(parameter.getFurtherInfo()));	
			pparsFilters.add(filter);  
		}
		
		if (parameter.getPublicNotice() == 2) {
			Filter filter = ff.equals(ff.property("publicnoti"), ff.literal(parameter.getPublicNotice()));	
			pparsFilters.add(filter);
		}
		
		if (parameter.getReferralIssues() == 2) {
			Filter filter = ff.equals(ff.property("referralis"), ff.literal(parameter.getReferralIssues()));	
			pparsFilters.add(filter);
		}
		
		if (!parameter.getSelectedCategories().isEmpty()) {
			Filter filter = null;
			StringBuilder sb = new StringBuilder();
		    int index = 0 ; 
			for (Integer categoryCode : parameter.getSelectedCategories()){
				sb.append(categoryCode);				
				if (++index != parameter.getSelectedCategories().size()){
					sb.append(",");
				}
				filter = ff.equals(ff.property("category"), ff.literal(sb.toString()));				
			}
			pparsFilters.add(filter);
		}					
		
		if (parameter.getNumOfDwelling() != 0) {
			String operator = parameter.getNumOfDwellingOperateor();
			Filter filter = null;
			switch (operator.charAt(0)) {
			case '>':
				filter = ff.greater(ff.property("numberofne"), ff.literal(parameter.getNumOfDwelling()));
				break;
			case '=':
				filter = ff.equals(ff.property("numberofne"), ff.literal(parameter.getNumOfDwelling()));
				break;
			case '<':
				filter = ff.less(ff.property("numberofne"), ff.literal(parameter.getNumOfDwelling()));
				break;
			}
			pparsFilters.add(filter);
		}
		
		if (parameter.getCurrentUse() == 7) {
			Filter filter = ff.equals(ff.property("currentuse"), ff.literal(parameter.getCurrentUse()));	
			pparsFilters.add(filter);
		}else if (parameter.getReferralIssues() == -7){
			Filter filter = ff.notEqual(ff.property("currentuse"), ff.literal(parameter.getCurrentUse()));	
			pparsFilters.add(filter);
		}
		
		if (parameter.getProposedUse() == 7) {
			Filter filter = ff.equals(ff.property("proposedus"), ff.literal(parameter.getProposedUse()));	
			pparsFilters.add(filter);
		}else if (parameter.getProposedUse() == -7){
			Filter filter = ff.notEqual(ff.property("proposedus"), ff.literal(parameter.getProposedUse()));	
			pparsFilters.add(filter);
		}
		
		if (parameter.getEstimatedCostOfWork() != 0) {
			String operator = parameter.getEstimatedCostOfWorkOperateor();
			Filter filter = null;
			switch (operator.charAt(0)) {
			case '>':
				filter = ff.greater(ff.property("estimatedc"), ff.literal(parameter.getEstimatedCostOfWork()));
				break;
			case '=':
				filter = ff.equals(ff.property("estimatedc"), ff.literal(parameter.getEstimatedCostOfWork()));
				break;
			case '<':
				filter = ff.less(ff.property("estimatedc"), ff.literal(parameter.getEstimatedCostOfWork()));
				break;
			}
			pparsFilters.add(filter);
		}
		
		if (parameter.getPreMeeting() == 2) {
			Filter filter = ff.equals(ff.property("premeeting"), ff.literal(parameter.getPreMeeting()));	
			pparsFilters.add(filter);  
		}
		
		if (parameter.getSelectedOutcome() != -1) {			
			Filter filter = ff.less(ff.property("outcome"), ff.literal(parameter.getSelectedOutcome()));
			pparsFilters.add(filter);
		}
		
		String geomName = propertyFc.getSchema().getGeometryDescriptor().getLocalName();
		Filter pparsFilter = ff.and(pparsFilters);	
		SimpleFeatureCollection pparsCollection = pparsFc.getFeatures(pparsFilter);
		System.out.println("collection.size()"+pparsCollection.size());
		SimpleFeatureIterator pparsIterator = pparsCollection.features();
		while(pparsIterator.hasNext()){
			SimpleFeature  ppars = pparsIterator.next();
			Geometry pparsGeom = (Geometry) ppars.getDefaultGeometry();
			Filter filter = ff.intersects(ff.property(geomName), ff.literal(pparsGeom));
//			Filter propertyFilter = ff.and(filter, lgaFilter);
			propertyFilters.add(filter);
		}
		Filter propertyFilter = ff.or(propertyFilters);	
		SimpleFeatureCollection propertyCollection = propertyFc.getFeatures(propertyFilter);
		System.out.println("propertyCollection.size= "+propertyCollection.size());
	}

}













/*Collection<PropertyDescriptor> pds = pparsFc.getSchema().getDescriptors();
Iterator<PropertyDescriptor> it = pds.iterator();
while (it.hasNext()){
	PropertyDescriptor pd = it.next();
	System.out.println(pd.getName().toString());
	System.out.println(pd.getName().getLocalPart());
	System.out.println("      ");
	
}*/
