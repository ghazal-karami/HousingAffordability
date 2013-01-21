package au.org.housing.model;

import org.ektorp.support.CouchDbDocument;

public class Attribute extends CouchDbDocument implements Comparable<Attribute>{
	private static final long serialVersionUID = 1L;

	private String name;
	private String layerName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	@Override
	public int compareTo(Attribute o) {
		if (o == this) return 0;
		return -1;
	}

}
