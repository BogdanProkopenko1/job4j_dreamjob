package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job",
                "Maven, Intellij IDEA, JUnit, Travis CI, JaCoCo.\nSalary 80000-120000 RUB."));
        posts.put(2, new Post(2, "Middle Java Job",
                "Maven, Intellij IDEA, JUnit, Travis CI, JaCoCo, Rest API, SQL, Spring*, Hibernate.\nSalary 120000-200000 RUB"));
        posts.put(3, new Post(3, "Senior Java Job",
                "Maven, Intellij IDEA, JUnit, Travis CI, JaCoCo, Rest API, SQL, Spring*, Hibernate, Architecture\nSalary 200000-300000 RUB"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}