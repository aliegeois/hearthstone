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
	public Optional<CardSpell> findById(Long id) {
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		return false;
	}

	@Override
	public Iterable<CardSpell> findAll() {
		return null;
	}

	@Override
	public Iterable<CardSpell> findAllById(Iterable<Long> ids) {
		return null;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(Long id) {

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