package fr.ministone.repository;

import org.springframework.data.repository.CrudRepository;

import fr.ministone.game.card.CardSpell;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface CardSpellRepository extends CrudRepository<CardSpell, String> {
    CardSpell findByName(String name);
    Iterable<CardSpell> findAllByDeck(String deck);
}
