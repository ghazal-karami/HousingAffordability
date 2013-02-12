package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;

import au.org.housing.exception.LayerValidationException;

import com.vividsolutions.jts.geom.Geometry;

public interface FacilitiesBufferService {

	public Geometry generateFacilityBuffer()
			throws NoSuchAuthorityCodeException, IOException, FactoryException,
			URISyntaxException, LayerValidationException, PSQLException;
}
