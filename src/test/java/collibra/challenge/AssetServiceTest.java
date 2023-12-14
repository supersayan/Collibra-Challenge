package collibra.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AssetServiceTest {
    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private Asset testAsset;

    @BeforeEach
    public void setup() {
        testAsset = Asset.builder()
            .id(0)
            .name("A")
            .isPromoted(false)
            .build();
    }

    // @Test
    // void shouldGetAllAssets() {
    //     assetService.getAllAssets();
    //     verify(assetRepository).findAll();
    // }

    // @Test
    // void shouldGetAsset() {
    //     int id = 0;
    //     assetService.getAsset(id);
    //     verify(assetRepository).findById(id);
    // }

    // @Test
    // void shouldCreateAsset() {
    //     assertEquals(Optional.empty(), assetRepository.findById(0));
    //     assertEquals(testAsset, assetRepository.save(testAsset));
    // }
}
