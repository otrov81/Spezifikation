package app.repository;

import app.model.Docu;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocuRepository extends JpaRepository<Docu, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE `Spezi`.`tblspez_docu` SET `docu`=?1 WHERE  `id`=1;",nativeQuery = true )
    void saveAllDocu(String docu);

    @Query(value = "SELECT docu FROM tblspez_docu WHERE id = 1", nativeQuery = true)
    List<String> selectDocuById();


}
