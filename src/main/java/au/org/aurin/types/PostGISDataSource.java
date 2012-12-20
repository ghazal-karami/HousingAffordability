package au.org.aurin.types;

public class PostGISDataSource {

	private String host;

	private String database;

	private String typeName;

	public PostGISDataSource() {
		super();
	}

	public PostGISDataSource(String host, String database, String datasetName) {
		super();
		this.host = host;
		this.database = database;
		this.typeName = datasetName;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String databaseName) {
		this.database = databaseName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String datasetName) {
		this.typeName = datasetName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((database == null) ? 0 : database.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostGISDataSource other = (PostGISDataSource) obj;
		if (database == null) {
			if (other.database != null)
				return false;
		} else if (!database.equals(other.database))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PostGISDataSource [host=").append(host)
				.append(", database=").append(database).append(", typeName=")
				.append(typeName).append("]");
		return builder.toString();
	}
}
