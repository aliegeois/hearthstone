package fr.ministone.repository;

import java.util.Optional;

import fr.ministone.game.card.CardMinion;

public class MCardMinionRepository implements CardMinionRepository {

	@Override
	public <S extends CardMinion> S save(S entity) {
		return null;
	}

	@Override
	public <S extends CardMinion> Iterable<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public Optional<CardMinion> findById(String id) {
		return null;
	}

	@Override
	public boolean existsById(String id) {
		return false;
	}

	@Override
	public Iterable<CardMinion> findAll() {
		return null;
	}

	@Override
	public Iterable<CardMinion> findAllById(Iterable<String> ids) {
		return null;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(String id) {

	}

	@Override
	public void delete(CardMinion entity) {

	}

	@Override
	public void deleteAll(Iterable<? extends CardMinion> entities) {

	}

	@Override
	public void deleteAll() {

	}

	@Override
	public CardMinion findByName(String name) {
		return null;
	}

	@Override
	public Iterable<CardMinion> findAllByDeck(String deck) {
		return null;
	}

}