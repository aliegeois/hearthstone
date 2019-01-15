package fr.ministone.repository;

import java.util.Optional;

import fr.ministone.game.card.CardSpell;

public class MCardSpellRepository implements CardSpellRepository {

	@Override
	public <S extends CardSpell> S save(S entity) {
		return null;
	}

	@Override
	public <S extends CardSpell> Iterable<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public Optional<CardSpell> findById(String id) {
		return null;
	}

	@Override
	public boolean existsById(String id) {
		return false;
	}

	@Override
	public Iterable<CardSpell> findAll() {
		return null;
	}

	@Override
	public Iterable<CardSpell> findAllById(Iterable<String> ids) {
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
	public void delete(CardSpell entity) {

	}

	@Override
	public void deleteAll(Iterable<? extends CardSpell> entities) {

	}

	@Override
	public void deleteAll() {

	}

	@Override
	public CardSpell findByName(String name) {
		return null;
	}

	@Override
	public Iterable<CardSpell> findAllByDeck(String deck) {
		return null;
	}

}