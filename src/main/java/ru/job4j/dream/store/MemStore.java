package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {

    private static final MemStore INST = new MemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private static AtomicInteger POST_ID = new AtomicInteger(4);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(4);


    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job",
                "Maven, Intellij IDEA, JUnit, Travis CI, JaCoCo.\nSalary 80000-120000 RUB."));
        posts.put(2, new Post(2, "Middle Java Job",
                "Maven, Intellij IDEA, JUnit, Travis CI, JaCoCo, Rest API, SQL, Spring*, Hibernate.\nSalary 120000-200000 RUB"));
        posts.put(3, new Post(3, "Senior Java Job",
                "Maven, Intellij IDEA, JUnit, Travis CI, JaCoCo, Rest API, SQL, Spring*, Hibernate, Architecture\nSalary 200000-300000 RUB"));
        candidates.put(1, new Candidate(1, "Peter", "Spring, Hibernate", "Junior developer"));
        candidates.put(2, new Candidate(2, "Bogdan", "Design patterns, Microservices", "Senior developer"));
        candidates.put(3, new Candidate(3, "Stas", "SQL, Tomcat", "Middle developer"));
    }

    public static MemStore instOf() {
        return INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    @Override
    public Post findPostById(int id) {
        return posts.get(id);
    }

    @Override
    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    @Override
    public Candidate deleteCandidate(int id) {
        return candidates.remove(id);
    }
}
