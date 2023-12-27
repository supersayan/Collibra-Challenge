package collibra.challenge;

import java.util.List;

import org.modelmapper.ModelMapper;
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

    private final ModelMapper mapper = new ModelMapper();

    @GetMapping("/asset")
    @ResponseBody
    public List<AssetDto> getAllAssets() {
        logger.info("Called get all assets");
        return assetService.getAllAssets().stream()
                .map((asset) -> mapper.map(asset, AssetDto.class))
                .toList();
    }

    @GetMapping("/asset/{id}")
    @ResponseBody
    public AssetDto getAsset(@PathVariable("id") Integer id) {
        logger.info("Called get asset " + id);
        return mapper.map(assetService.getAsset(id), AssetDto.class);
    }
    
    @PostMapping("/asset")
    @ResponseBody
    public AssetDto createAsset(@RequestBody AssetDto assetDto) {
        logger.info("Called create asset");

        // Converter<Integer, Asset> idToAsset = ctx -> {
        //     Asset a = new Asset();
        //     a.setId(ctx.getSource());
        //     return a;
        // };
        // TypeMap<AssetDto, Asset> propertyMapper = this.mapper.createTypeMap(AssetDto.class, Asset.class);
        // propertyMapper.addMappings(mapper -> {
        //     mapper.using(idToAsset).map(AssetDto::getParentId, Asset::setParent);
        // });

        // mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        Asset asset = mapper.map(assetDto, Asset.class);
        if (assetDto.getParentId() != null) {
            Asset parent = new Asset();
            parent.setId(assetDto.getParentId());
            asset.setParent(parent);
        }

        return mapper.map(assetService.createAsset(asset), AssetDto.class);
    }

    @PutMapping("/asset/{id}")
    @ResponseBody
    public AssetDto updateAsset(@PathVariable Integer id, @RequestBody AssetDto assetDto) {
        logger.info("Called update asset");

        Asset asset = mapper.map(assetDto, Asset.class);
        if (assetDto.getParentId() != null) {
            Asset parent = new Asset();
            parent.setId(assetDto.getParentId());
            asset.setParent(parent);
        }

        return mapper.map(assetService.updateAsset(id, asset), AssetDto.class);
    }

    @DeleteMapping("/asset/{id}")
    public Boolean deleteAsset(@PathVariable Integer id) throws Exception {
        logger.info("Called delete asset");
        assetService.deleteAsset(id);
        return true;
    }
}

