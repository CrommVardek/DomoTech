package alarmeIncendie;

public class PompierDefaut implements Pompier {

	@Override
	public void AlerteIncendie() 
	{
		System.out.println("Alerte ! au feu c'est barbecue ici !!! On va tous griller !! Help !");
	}

}
