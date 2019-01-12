package fr.ministone.game;

public abstract class Constants {
	public static final int HEROMAXHEALTH = 30;
	public static final int NBPOSSIBLEHEROES = 3;
	
	private Constants() {}
	
	public static String getAleatoireHero() {
		int min = 1;
		int max = Constants.NBPOSSIBLEHEROES;
		int nbHeroChoosed = min + (int)(Math.random() * ((max - min) + 1));
		String heroChoosed;
		
		switch(nbHeroChoosed) {
		case 1:
			heroChoosed = "paladin";
			break;
		case 2:
			heroChoosed = "warrior";
			break;
		case 3:
			heroChoosed = "mage";
			break;
		default:
			heroChoosed = "casParDefaut";
			break;
		}
		
		return heroChoosed;
	}
}
