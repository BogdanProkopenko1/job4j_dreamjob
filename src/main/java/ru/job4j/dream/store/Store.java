package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;

public interface Store {

    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    void save(Candidate candidate);

    void save(User user);

    User getUserOnEmail(String email);

    Post findPostById(int id);

    Candidate findCandidateById(int id);

    Candidate deleteCandidate(int id);

    void deleteUser(int id);
}
