package au.org.housing.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ektorp.CouchDbConnector;
import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;



@Component
public class LayerRepository extends CouchDbRepositorySupport<Layer> {

	@Autowired
	public LayerRepository(@Qualifier("housing_database") CouchDbConnector db) {
		super(Layer.class, db);
		initStandardDesignDocument(); // create new database
	}
	
	@GenerateView
    public List<Layer> findByName(String name) {
            return queryView("by_name", name);
    }

	public void add(Layer layer) {
		super.add(layer);		
	}
	public void update(Layer layer) {
		super.update(layer);
	}
	public void remove(Layer layer) {
		super.remove(layer);
	}
	public Layer get(String id) {
		return super.get(id);
	}
	@SuppressWarnings("deprecation")
	public Layer get(String id, String rev) {
		return super.get(id, rev);
	}
	public List<Layer> getAllLayers() {
		return super.getAll();
	}
	public boolean contains(String layerId) {
		return super.contains(layerId);
	}		

	public void create() {
		
		
		Layer layer = new Layer("property", "PostGIS");
		layer.setId("property");
		Set<Attribute> atts =  new HashSet<Attribute>();
		Attribute attribute = new Attribute("sv_current_year", layer);
		attribute.setId("sv_current_year");
		atts.add(attribute);
		attribute = new Attribute("civ_current_year", layer);
		attribute.setId("civ_current_year");
		atts.add(attribute);
		layer.setAttributes(atts) ;
		super.add(layer);
//		db.create(layer);
//		for (Attribute att : atts ){
////			db.create(att);
//			super.add(att);
//		}
//		String id = layer.getId();
//		String revision = layer.getRevision();
	}

	@GenerateView @Override
	public List<Layer> getAll() {
		ViewQuery q = createQuery("all")
				.descending(true)
				.includeDocs(true);
		return db.queryView(q, Layer.class);
	}

	public Page<Layer> getAll(PageRequest pr) {
		ViewQuery q = createQuery("all")
				.descending(true)
				.includeDocs(true);
		return db.queryForPage(q, pr, Layer.class);
	}





}
