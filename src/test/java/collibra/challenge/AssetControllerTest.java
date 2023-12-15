package collibra.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AssetControllerTest {
    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetController assetController;

    @Test
    void shouldGetAllAssets() throws Exception {
        Asset asset1 = new Asset(0, "A", false, null, null);
        Asset asset2 = new Asset(1, "B", true, null, null);
        List<Asset> assetList = Arrays.asList(asset1, asset2);

        AssetDto assetDto1 = new AssetDto(asset1.getId(), asset1.getName(), asset1.getIsPromoted(), null);
        AssetDto assetDto2 = new AssetDto(asset2.getId(), asset2.getName(), asset2.getIsPromoted(), null);
        List<AssetDto> assetDtos = Arrays.asList(assetDto1, assetDto2);
        
        Mockito.when(assetService.getAllAssets()).thenReturn(assetList);

        List<AssetDto> actualAllAssets = assetController.getAllAssets();

        for (int i=0; i<assetDtos.size(); i++) {
            assertEquals(assetDtos.get(i).getId(), actualAllAssets.get(i).getId());
            assertEquals(assetDtos.get(i).getName(), actualAllAssets.get(i).getName());
        }
    }
}
