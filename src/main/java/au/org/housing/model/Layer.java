package au.org.housing.model;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.ektorp.docref.DocumentReferences;
import org.ektorp.docref.FetchType;
import org.ektorp.spring.*;
import org.ektorp.support.*;
import org.joda.time.*;


@JsonIgnoreProperties
public class Layer extends CouchDbDocument{
	private static final long serialVersionUID = 1L;

	@TypeDiscriminator
	private String name = "property";

	@DocumentReferences(fetch = FetchType.LAZY,backReference = "layer")
	private Set<Attribute> attributes;

	private String location;

	public Layer(String name , String location){
		this.name = name;
		this.location = location;
	}
	
	public Layer(){
		super();
	}

	public Set<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
