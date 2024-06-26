package app.repository;

import app.model.RestApiModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RestApiRepository extends JpaRepository<RestApiModel, Long> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE Spezi.tblspez_restapi", nativeQuery = true)
    void truncateRestApi();

}
