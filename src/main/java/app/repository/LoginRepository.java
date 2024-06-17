package app.repository;

import app.model.Login;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO log_table (level, message, username) VALUES (?, ?, ?)",nativeQuery = true )
    void LogToDatabase(String level, String message, String username);
}
