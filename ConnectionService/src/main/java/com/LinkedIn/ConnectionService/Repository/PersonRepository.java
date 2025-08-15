package com.LinkedIn.ConnectionService.Repository;

import com.LinkedIn.ConnectionService.Entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (a:Person)-[:CONNECTED_TO]-(b:Person) WHERE a.userId = $userId RETURN b")
    List<Person> getFirstDegreeConnections(Long userId);

    // PersonRepository

    @Query("""
        MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person)
        WHERE p1.userId=$senderId AND p2.userId=$receiverId
        RETURN count(r) > 0
        """)
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("""
        MATCH (p1:Person)-[r:CONNECTED_TO]-(p2:Person)
        WHERE p1.userId=$senderId AND p2.userId=$receiverId
        RETURN count(r) > 0
        """)
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("""
        MERGE (p1:Person {userId: $senderId})
        MERGE (p2:Person {userId: $receiverId})
        MERGE (p1)-[:REQUESTED_TO]->(p2)
        """)
    void addConnectionRequest(Long senderId, Long receiverId);

    @Query("""
        MATCH (p1:Person {userId:$senderId})-[r:REQUESTED_TO]->(p2:Person {userId:$receiverId})
        DELETE r
        """)
    void deleteConnectionRequest(Long senderId, Long receiverId);

    @Query("""
        MATCH (p1:Person {userId:$senderId})-[r:REQUESTED_TO]->(p2:Person {userId:$receiverId})
        DELETE r
        MERGE (p1)-[:CONNECTED_TO]->(p2)
        """)
    void acceptConnectionRequest(Long senderId, Long receiverId);

}
