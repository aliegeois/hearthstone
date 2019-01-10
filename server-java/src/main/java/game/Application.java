package game;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game g = new Game();
		
		String np1 = "Nero",
		       np2 = "Claudius";
		Player p1 = new Player(np1, "warrior"),
		       p2 = new Player(np2, "mage");
		
		g.addPlayer(np1, p1);
		g.addPlayer(np2, p2);
		
	}
}
