package fr.ministone.game;

import org.springframework.beans.factory.annotation.Autowired;

import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.CardSpellRepository;

public abstract class Constants {
	public static final int HEROHEALTHMAX = 30;
	public static final int NBPOSSIBLEHEROES = 3;

	@Autowired public final CardMinionRepository cardMinionRepository;
	@Autowired public final CardSpellRepository cardSpellRepository;
	
	public Constants(CardMinionRepository cardMinionRepository, CardSpellRepository cardSpellRepository) {
		this.cardMinionRepository = cardMinionRepository;
		this.cardSpellRepository = cardSpellRepository;
	}
}
