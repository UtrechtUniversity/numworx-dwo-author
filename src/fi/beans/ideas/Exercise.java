package fi.beans.ideas;

public class Exercise {
	private String identifier, description, status;

	/**
	 * Als Exercise(domain.identitier, ...).
	 * @param domain
	 * @param identifier
	 * @param description
	 * @param status
	 * @deprecated gebruik zonder domain
	 */
	public Exercise(String domain, String identifier, String description,
			String status) {
		this(domain + "." + identifier, description, status);
	}
	
	/**
	 * 
	 * @param identifier
	 * @param description
	 * @param status2
	 */
	public Exercise(String identifier, String description, String status) {
		// TODO Auto-generated constructor stub
		this.identifier = identifier;
		this.description = description;
		this.status = status;
	}

	/**
	 * @deprecated NIET GEBRUIKEN!
	 * @return domain part of identifier
	 */
	public String getDomain() {
		int dot = identifier.indexOf('.');
		return identifier.substring(0, dot);
	}

	/**
	 * @return the identifier
	 * @deprecated NIET MEER GEBRUIKEN
	 */
	public String getIdentifier() {
		int dot = identifier.indexOf('.');
		return identifier.substring(dot+1);
	}

	public String getID() {
		return identifier;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
}
