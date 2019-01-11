package game;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game g = new Game();
		
		String np1 = "Nero",
		       np2 = "Claudius";
		Player p1 = new Player(np1),
		       p2 = new Player(np2);
		
		g.addPlayer(p1);
		g.addPlayer(p2);
		
	}
}
