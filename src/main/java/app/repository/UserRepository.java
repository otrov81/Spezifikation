package app.repository;


import app.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "insert into Spezi.user(userName, password, berechtigung) values(?1, ?2, ?3)", nativeQuery = true)
    void insertDataUser(String userName, String password,String berechtigung); // sample


    @Transactional
    @Modifying
    @Query(value = "UPDATE Spezi.user SET userName=?1, password=?2, berechtigung=?3 WHERE userID=?4", nativeQuery = true)
    void updateDataUser(String userName, String password,String berechtigung, Integer userid);


    @Transactional
    @Modifying
    @Query(value = "UPDATE Spezi.user SET style=?1 WHERE userName=?2", nativeQuery = true)
    void updateDataStyle(String style, String userName);

}
