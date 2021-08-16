package ru.job4j.dream.model;

import java.util.Objects;

public class Candidate {

    private int id;
    private String name;
    private String skills;
    private String position;

    public Candidate(int id, String name, String skills, String position) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id
                && Objects.equals(name, candidate.name)
                && Objects.equals(skills, candidate.skills)
                && Objects.equals(position, candidate.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, skills);
    }
}
