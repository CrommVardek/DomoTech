package commonsObjects;

/*
 * Dorian Lecomte
 */

public class Spice implements Container {

/* VARIABLES */
	private static final long serialVersionUID = -3385426336579645324L;
	private String spiceId;
	private String name;
	private String description;
	private String barCode;
	
/* CONSTRUCTEURS */
	public Spice(String spiceId, String name, String description, String barCode) {
		this.spiceId = spiceId;
		this.name = name;
		this.description = description;
		this.barCode = barCode;
	}
	
	public Spice()
	{
		this.setSpiceId("0");
		this.name = "Empty_Name";
		this.description="Test";
		this.barCode = "Test";
		//this("0", "Empty_Name", "Empty_Description", "Empty_barCode");
	}

/* GETTERS ET SETTERS */
	
 // GETTERS	
	public String getSpiceId() {
		return spiceId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getBarCode() {
		return barCode;
	}

 // SETTERS	
	public void setSpiceId(String spiceId) {
		this.spiceId = spiceId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
// To STRING
	public String toString() {
		return "Spice [" + (spiceId != null ? "spiceId=" + spiceId + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (barCode != null ? "barCode=" + barCode : "") + "]";
	}
}
