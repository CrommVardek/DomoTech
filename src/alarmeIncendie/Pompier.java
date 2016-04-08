package alarmeIncendie;

public interface Pompier {
	/*
	 * Définit l'interface utilisée par l'alarme incendie pour pouvoir invoquer l'appelant 
	 * lorsqu'un incendie est détecté. Une seule méthode doit être implémentée
	 * ( la seule disponible...).
	 */

	public void AlerteIncendie();
}
