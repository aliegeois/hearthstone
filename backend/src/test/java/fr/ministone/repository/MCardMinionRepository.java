package fr.ministone.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import fr.ministone.game.PlayerMock;
import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;

public class MCardMinionRepository implements CardMinionRepository {
	private Map<String, CardMinion> cards = new HashMap<>();

	@Override
	public <S extends CardMinion> S save(S entity) {
		System.out.println("MCardMinionRepository.save(" + entity.getName() + ")");
		cards.put(entity.getName(), entity);
		System.out.println("new size: " + cards.size());
		return entity;
	}

	@Override
	public <S extends CardMinion> Iterable<S> saveAll(Iterable<S> entities) {
		System.out.println("MCardMinionRepository.saveAll()");
		return null;
	}

	@Override
	public Optional<CardMinion> findById(String id) {
		System.out.println("MCardMinionRepository.findById()");
		return Optional.ofNullable(cards.get(id));
	}

	@Override
	public boolean existsById(String id) {
		System.out.println("MCardMinionRepository.existsById()");
		return false;
	}

	@Override
	public Iterable<CardMinion> findAll() {
		System.out.println("MCardMinionRepository.findAll()");
		return cards.values();
	}

	@Override
	public Iterable<CardMinion> findAllById(Iterable<String> ids) {
		System.out.println("MCardMinionRepository.findAllById()");
		return null;
	}

	@Override
	public long count() {
		System.out.println("MCardMinionRepository.count()");
		return cards.size();
	}

	@Override
	public void deleteById(String id) {
		System.out.println("MCardMinionRepository.deleteById()");
		cards.remove(id);
	}

	@Override
	public void delete(CardMinion entity) {
		System.out.println("MCardMinionRepository.delete()");
		cards.remove(entity.getName());
	}

	@Override
	public void deleteAll(Iterable<? extends CardMinion> entities) {
		System.out.println("MCardMinionRepository.deleteAll()");
	}

	@Override
	public void deleteAll() {
		System.out.println("MCardMinionRepository.deleteAll()");
		cards.clear();
	}

	@Override
	public CardMinion findByName(String name) {
		System.out.println("MCardMinionRepository.findByName()");
		return cards.get(name);
	}

	@Override
	public Iterable<CardMinion> findAllByDeck(String deck) {
		System.out.println("MCardMinionRepository.findAllByDeck(\"" + deck + "\")");
		Collection<CardMinion> result = new ArrayList<>();
		for(CardMinion m : cards.values()) {
			System.out.println("Carte: name: " + m.getName() + " - deck: " + m.getDeck());
			if(deck.equals(m.getDeck())) {
				result.add(m);
			}
		}
			
		System.out.println("Result: " + result.size() + " cartes");

		return result;
	}
}