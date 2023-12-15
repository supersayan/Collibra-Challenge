package collibra.challenge;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetController {
    @Autowired
    private AssetService assetService;

    private Logger logger = LoggerFactory.getLogger(AssetController.class);

    @GetMapping("/asset")
    @ResponseBody
    public List<AssetDto> getAllAssets() {
        logger.info("Called get all assets");
        return assetService.getAllAssets().stream()
                .map(this::convertToDto)
                .toList();
    }

    @GetMapping("/asset/{id}")
    @ResponseBody
    public AssetDto getAsset(@PathVariable("id") Integer id) {
        logger.info("Called get asset " + id);
        return convertToDto(assetService.getAsset(id));
    }
    
    @PostMapping("/asset")
    @ResponseBody
    public AssetDto createAsset(@RequestBody AssetDto assetDto) {
        logger.info("Called create asset");
        return convertToDto(assetService.createAsset(convertToEntity(assetDto)));
    }

    @PutMapping("/asset/{id}")
    @ResponseBody
    public AssetDto updateAsset(@PathVariable Integer id, @RequestBody AssetDto assetDto) {
        logger.info("Called update asset");
        return convertToDto(assetService.updateAsset(id, convertToEntity(assetDto)));
    }

    @DeleteMapping("/asset/{id}")
    public Boolean deleteAsset(@PathVariable Integer id) throws Exception {
        logger.info("Called delete asset");
        assetService.deleteAsset(id);
        return true;
    }

    @PutMapping("/asset/{id}/promote")
    public AssetDto promoteAsset(@PathVariable Integer id) throws Exception {
        logger.info("Called promote asset");
        return convertToDto(assetService.promoteAsset(id));
    }

    // Helper methods

    private AssetDto convertToDto(Asset asset) {
        return AssetDto.builder()
                .id(asset.getId())
                .name(asset.getName())
                .parent(AssetDto.builder().id(asset.getParent() == null ? null : asset.getParent().getId()).build())
                .isPromoted(asset.getIsPromoted())
                .build();
    }

    private Asset convertToEntity(AssetDto assetDto) {
        return Asset.builder()
                .id(assetDto.getId())
                .name(assetDto.getName() != null ? assetDto.getName() : "New Asset")
                .parent(Asset.builder().id(assetDto.getParent() == null ? null : assetDto.getParent().getId()).build())
                .isPromoted(assetDto.getIsPromoted() != null ? assetDto.getIsPromoted() : false)
                .build();
    }
}

