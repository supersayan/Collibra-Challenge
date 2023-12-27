package collibra.challenge;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {
    @Query(value = """
        WITH RECURSIVE rectree AS (
            SELECT *
            FROM assets
            WHERE id = ?1
        UNION ALL
            SELECT a.*
            FROM assets a
            JOIN rectree
            ON a.parent_id = rectree.id
        ) SELECT * FROM rectree
        """,
        nativeQuery = true)
    Collection<Asset> findAllChildrenByRootId(Integer id);
}
