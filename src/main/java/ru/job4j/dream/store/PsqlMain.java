package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

public class PsqlMain {

    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Junior Java Job", "100RUB"));
        store.save(new Post(0, "Middle Java Job", "200RUB"));
        store.save(new Post(0, "Senior Java Job", "300RUB"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        store.save(new Candidate(0, "A", "A", "A"));
        store.save(new Candidate(0, "B", "B", "B"));
        store.save(new Candidate(0, "C", "C", "C"));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        System.out.println("post: " + store.findPostById(1).toString());
        System.out.println("can:" + store.findCandidateById(1).toString());
    }
}