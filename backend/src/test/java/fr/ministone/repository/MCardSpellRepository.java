package fr.ministone.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import fr.ministone.game.PlayerMock;
import fr.ministone.game.card.CardSpell;
import fr.ministone.game.effect.GlobalEffect;
import fr.ministone.game.effect.MultipleTargetEffect;
import fr.ministone.game.effect.SingleTargetEffect;

public class MCardSpellRepository implements CardSpellRepository {
	private Map<String, CardSpell> cards = new HashMap<String, CardSpell>();

	@Override
	public <S extends CardSpell> S save(S entity) {
		cards.put(entity.getName(), entity);
		return entity;
	}

	@Override
	public <S extends CardSpell> Iterable<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public Optional<CardSpell> findById(String id) {
		return Optional.ofNullable(cards.get(id));
		
		//return null;
	}

	@Override
	public boolean existsById(String id) {
		return false;
	}

	@Override
	public Iterable<CardSpell> findAll() {
		return cards.values();
	}

	@Override
	public Iterable<CardSpell> findAllById(Iterable<String> ids) {
		return null;
	}

	@Override
	public long count() {
		return cards.size();
	}

	@Override
	public void deleteById(String id) {
		cards.remove(id);
	}

	@Override
	public void delete(CardSpell entity) {
		cards.remove(entity.getName());
	}

	@Override
	public void deleteAll(Iterable<? extends CardSpell> entities) {

	}

	@Override
	public void deleteAll() {
		cards.clear();
	}

	@Override
	public CardSpell findByName(String name) {
		return cards.get(name);
	}

	@Override
	public Iterable<CardSpell> findAllByDeck(String deck) {
		Collection<CardSpell> result = new ArrayList<>();
		for(CardSpell s : cards.values())
			if(deck.equals(s.getDeck()))
				result.add(s);

		return result;
	}
}