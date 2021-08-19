package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOGGER = LoggerFactory.getLogger(PsqlStore.class.getName());

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getDate("created")
                    ));
                }
            }
        } catch (Exception e) {
            LOGGER.error("SQLException", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection connection = pool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM candidate")
        ) {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                candidates.add(new Candidate(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("skills"),
                        resultSet.getString("position")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    @Override
    public void save(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(
                             "INSERT INTO user(name, email, password) VALUES (?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS
                     )
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        }
    }

    @Override
    public User getUserOnEmailAndPassword(String email, String pass) {
        User user = null;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM user WHERE email=? AND password=?")
        ) {
            ps.setString(1, email);
            ps.setString(2, pass);
            ps.execute();
            ResultSet rsl = ps.getResultSet();
            if (rsl.next()) {
                user = new User(
                        rsl.getInt("id"),
                        rsl.getString("name"),
                        rsl.getString("email"),
                        rsl. getString("password")
                );
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        }
        return user;
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                             "INSERT INTO post(name, description, created) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            Instant instant = post.getCreated().toInstant();
            ps.setTimestamp(
                    3, Timestamp.valueOf(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()))
            );
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error("SQLException", e);
        }
        return post;
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE candidate SET name=?, skills=?, position=? WHERE id=?"
             )
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getSkills());
            ps.setString(3, candidate.getPosition());
            ps.setInt(4, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("SQLException", e);
        }
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO candidate(name, skills, position) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getSkills());
            ps.setString(3, candidate.getPosition());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error("SQLException", e);
        }
        return candidate;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE post SET name=?, description=? WHERE id=?")
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("SQLException", e);
        }
    }

    @Override
    public Post findPostById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id=?")
        ) {
            ps.setInt(1, id);
            ps.execute();
            ResultSet rsl = ps.getResultSet();
            if (rsl.next()) {
                post = new Post(
                        rsl.getInt("id"),
                        rsl.getString("name"),
                        rsl.getString("description")
                );
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        }
        return post;
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id=?")
        ) {
            ps.setInt(1, id);
            ps.execute();
            ResultSet rsl = ps.getResultSet();
            if (rsl.next()) {
                candidate = new Candidate(
                        rsl.getInt("id"),
                        rsl.getString("name"),
                        rsl.getString("skills"),
                        rsl.getString("position")
                );
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        }
        return candidate;
    }

    @Override
    public Candidate deleteCandidate(int id) {
        Candidate candidate = findCandidateById(id);
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate WHERE id=?")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        }
        return candidate;
    }

    @Override
    public void deleteUser(int id) {
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("DELETE FROM user WHERE id=?")
        ) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        }
    }
}
