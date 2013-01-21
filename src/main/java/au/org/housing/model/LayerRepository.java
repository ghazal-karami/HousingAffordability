package au.org.housing.model;

import java.util.*;

import org.ektorp.*;
import org.ektorp.support.*;
import org.ektorp.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class LayerRepository extends CouchDbRepositorySupport<Layer> {

	@Autowired
	public LayerRepository(@Qualifier("housing_database") CouchDbConnector db) {
		super(Layer.class, db);
		initStandardDesignDocument();
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
	
	
	
	public void addAttribute(Attribute att) {
		Assert.hasText(att.getLayerName(), "Attribute must have a Layer Name");
		db.create(att);
	}

}
