package project.prototype.ipmedt4;

/**
 * De Class ItemHandle.
 * Deze Class behoudt de nummer van de knop/item die je indrukt in Categorie.
 * Het nummer wordt vervolgens in AsyncTaskPull.java opgeroepen via de getDBitem methode.
 * 
 * @author Jim Schoorl
 */
public final class ItemHandle {

	private static int dbItem;
	static Categorie1 catDBitem = new Categorie1();
	
	/**
	 * De main van ItemHandle
	 * Hier wordt dbItem geset via Categorie.
	 */
	public static void main(String[] args)
	{
		dbItem = catDBitem.getNummerTerug();
	}
	
	/**
	 * Sets the dbItem.
	 *
	 * @param nummer is de nieuwe dbItem
	 */
	public void setDBitem(int nummer) 
	{
		
		ItemHandle.dbItem = nummer;
	}
	
	/**
	 * Stuurt dbItem naar andere klassen.
	 * 
	 * @return de dbItem
	 */
	public int getDBitem()
	{
		return dbItem;
		
	}

}
