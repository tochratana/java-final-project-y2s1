package repository;

import model.User;
import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    User save(User user) throws SQLException;
    Optional<User> findByUsername(String username) throws SQLException;
    Optional<User> findById(Long id) throws SQLException;
    boolean existsByUsername(String username) throws SQLException;
    boolean existsByEmail(String email) throws SQLException;

}