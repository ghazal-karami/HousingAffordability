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
import org.ektorp.support.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;



@Component
@View( name="all", map = "function(doc) { if (doc.layer) { emit(null, doc) } }")
public class AttributeRepository extends CouchDbRepositorySupport<Attribute> {

	@Autowired
    public AttributeRepository(@Qualifier("housing_database") CouchDbConnector db) {
            super(Attribute.class, db);
            initStandardDesignDocument();
    }
    
    @GenerateView
    public List<Attribute> findByLayer(String layer) {
            return queryView("by_layer", layer);
    }


}
