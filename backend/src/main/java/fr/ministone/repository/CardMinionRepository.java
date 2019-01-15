package fr.ministone.repository;

import org.springframework.data.repository.CrudRepository;

import fr.ministone.game.card.CardMinion;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface CardMinionRepository extends CrudRepository<CardMinion, String> {
    CardMinion findByName(String name);
    Iterable<CardMinion> findAllByDeck(String deck);
}
