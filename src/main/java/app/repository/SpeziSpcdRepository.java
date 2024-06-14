package app.repository;

import app.model.SpeziSpcd;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeziSpcdRepository extends JpaRepository<SpeziSpcd, Integer> {
    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM `Spezi`.`tblspez_spcd` ORDER BY `spcd` ASC",nativeQuery = true)
    List<SpeziSpcd> selectDropdownSpcd();
}
