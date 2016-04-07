package distributeurEpices;

public class Epice {
	
	private String nom;
	private String description;
	private String RFID;
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setRFID(String RFID)
	{
		this.RFID = RFID;
	}
	public String getRFID()
	{
		return RFID;
	}
}
