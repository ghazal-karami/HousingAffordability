package au.org.housing.model;

import org.ektorp.support.CouchDbDocument;

public class Attribute extends CouchDbDocument implements Comparable<Attribute>{
	private static final long serialVersionUID = 1L;

	private String name;
	private Layer layer ;
	
	public Attribute(String name, Layer layer  ){
		this.name = name;
		this.layer = layer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Layer getLayer() {
		return layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	@Override
	public int compareTo(Attribute o) {
		if (o == this) return 0;
		return -1;
	}

}
