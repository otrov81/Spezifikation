package app.repository;

import app.model.SpeziSa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeziSaRepository extends JpaRepository<SpeziSa, Integer> {

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM `Spezi`.`tblspez_sa` ORDER BY `sa` ASC",nativeQuery = true)
    List<SpeziSa> selectDropdownSa();
}
