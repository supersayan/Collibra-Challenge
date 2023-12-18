package collibra.challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    public Asset createAsset(Asset asset) {
        logger.debug("New asset: ", asset);
        Asset newAsset = assetRepository.save(asset);
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
        logger.debug("Update asset: ", newAsset);
        return assetRepository.save(asset);
    }

    public void deleteAsset(int id) throws Exception {
        assetRepository.deleteById(id);
        Optional<List<Asset>> descendents = getDescendents(id);
        if (descendents.isPresent()) {
            assetRepository.deleteAll(descendents.get());
        }
        // rabbitTemplate.send("asset.deleted", null);
    }

    public Asset promoteAsset(int id) throws Exception {
        Optional<Asset> assetFromRepo = assetRepository.findById(id);
        if (assetFromRepo.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Asset asset = assetFromRepo.get();
        asset.setIsPromoted(true);
        // rabbitTemplate.send("asset.promoted", null);
        Asset newAsset = assetRepository.save(asset);
        Optional<List<Asset>> descendents = getDescendents(id);
        if (descendents.isPresent()) {
            for (Asset childAsset : descendents.get()) {
                childAsset.setIsPromoted(true);
                assetRepository.save(childAsset);
            }
        }
        Optional<List<Asset>> ancestors = getAncestors(id);
        if (ancestors.isPresent()) {
            for (Asset parentAsset : ancestors.get()) {
                parentAsset.setIsPromoted(true);
                assetRepository.save(parentAsset);
            }
        }
        return newAsset;
    }
    
    public Optional<List<Asset>> getDescendents(int rootId) throws Exception {
        Optional<Asset> rootAsset = assetRepository.findById(rootId);
        if (rootAsset.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Asset asset = rootAsset.get();
        final int RECURSION_LIMIT = 2;
        List<Asset> descendents = asset.getChildren();
        List<Asset> children = asset.getChildren();
        List<Asset> newChildren = new ArrayList<Asset>();
        for (int i=0; i<RECURSION_LIMIT; i++) {
            for (Asset c : children) {
                newChildren.addAll(c.getChildren());
            }
            descendents.addAll(newChildren);
            children = newChildren;
            newChildren = new ArrayList<Asset>();
        }
        return descendents.size() > 0 ? Optional.of(descendents) : Optional.empty();
    }

    public Optional<List<Asset>> getAncestors(int childId) throws Exception {
        Optional<Asset> childAsset = assetRepository.findById(childId);
        if (childAsset.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Asset asset = childAsset.get();
        final int RECURSION_LIMIT = 3;
        List<Asset> ancestors = Arrays.asList();
        for (int i=0; i<RECURSION_LIMIT; i++) {
            Optional<Asset> parentAsset = assetRepository.findById(asset.getParent().getId());
            if (parentAsset.isEmpty())
                break;
            asset = parentAsset.get();
            ancestors.add(asset);
        }
        return ancestors.size() > 0 ? Optional.of(ancestors) : Optional.empty();
    }
}
