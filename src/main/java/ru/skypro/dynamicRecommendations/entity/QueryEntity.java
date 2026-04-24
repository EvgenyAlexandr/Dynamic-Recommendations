package ru.skypro.dynamicRecommendations.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "queries")
public class QueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String query; // USER_OF, ACTIVE_USER_OF, etc.

    @ElementCollection
    @CollectionTable(name = "query_arguments", joinColumns = @JoinColumn(name = "query_id"))
    @Column(name = "argument")
    private List<String> arguments;

    private boolean negate;

    // constructors, getters, setters
}