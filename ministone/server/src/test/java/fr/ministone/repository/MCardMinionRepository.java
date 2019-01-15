package fr.ministone.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.ministone.game.card.CardMinion;

public class MCardMinionRepository implements CardMinionRepository {
	private Map<String, CardMinion> cards = new HashMap<>();

	@Override
	public <S extends CardMinion> S save(S entity) {
		cards.put(entity.getName(), entity);
		return entity;
	}

	@Override
	public <S extends CardMinion> Iterable<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public Optional<CardMinion> findById(String id) {
		return Optional.ofNullable(cards.get(id));
	}

	@Override
	public boolean existsById(String id) {
		return false;
	}

	@Override
	public Iterable<CardMinion> findAll() {
		return cards.values();
	}

	@Override
	public Iterable<CardMinion> findAllById(Iterable<String> ids) {
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
	public void delete(CardMinion entity) {
		cards.remove(entity.getName());
	}

	@Override
	public void deleteAll(Iterable<? extends CardMinion> entities) {

	}

	@Override
	public void deleteAll() {
		cards.clear();
	}

	@Override
	public CardMinion findByName(String name) {
		return cards.get(name);
	}

	@Override
	public Iterable<CardMinion> findAllByDeck(String deck) {
		return null;
	}

}