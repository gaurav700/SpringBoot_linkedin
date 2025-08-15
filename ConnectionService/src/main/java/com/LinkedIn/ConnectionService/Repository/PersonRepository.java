package com.LinkedIn.ConnectionService.Repository;

import com.LinkedIn.ConnectionService.Entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(String name);

    @Query("""
    MATCH (a:Person {userId: $userId})-[:CONNECTED_TO]-(b:Person)
    RETURN b
    """)
    List<Person> getFirstDegreeConnections(Long userId);
}
