package collibra.challenge;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    // private RabbitTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(AssetController.class);

    // public AssetService(RabbitTemplate rabbitTemplate) {
    //     this.rabbitTemplate = rabbitTemplate;
    // }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset getAsset(int id) throws EntityNotFoundException {
        Optional<Asset> asset = assetRepository.findById(id);
        if (asset.isEmpty()) {
            logger.error("Asset id " + id + " was not found");
            throw new EntityNotFoundException();
        }
        return asset.get();
    }

    public Asset createAsset(Asset asset) throws IllegalArgumentException {
        if (doesAssetCreateLoop(asset))
            throw new IllegalArgumentException();
        Asset newAsset = assetRepository.save(asset);
        logger.info("New asset: {}", newAsset);
        // rabbitTemplate.send("asset.created", null);
        return newAsset;
    }

    public Asset updateAsset(int id, Asset newAsset) throws EntityNotFoundException {
        Optional<Asset> assetFromRepo = assetRepository.findById(id);
        if (assetFromRepo.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Asset asset = assetFromRepo.get();
        asset.setName(newAsset.getName());
        if (newAsset.getParent() != null) {
            Optional<Asset> parentAsset = assetRepository.findById(null);
            if (parentAsset.isPresent()) {
                asset.setParent(parentAsset.get());
            }
        }
        asset.setIsPromoted(newAsset.getIsPromoted());
        logger.debug("Update asset: ", newAsset);
        return assetRepository.save(asset);
    }

    public void deleteAsset(int id) throws Exception {
        assetRepository.deleteById(id);
        // rabbitTemplate.send("asset.deleted", null);
    }

    private boolean doesAssetCreateLoop(Asset asset) {
        Asset subAsset = asset;
        Asset superAsset;
        while (subAsset.getParent() != null) {
            superAsset = subAsset.getParent();
            if (superAsset.getId() == subAsset.getId())
                return true;
            subAsset = superAsset;
        }
        return false;
    }
}
