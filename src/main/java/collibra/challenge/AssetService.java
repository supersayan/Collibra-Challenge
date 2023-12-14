package collibra.challenge;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    private MongoTemplate mongoTemplate;

    private RabbitTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(AssetController.class);

    public AssetService(MongoTemplate mongoTemplate, RabbitTemplate rabbitTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

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
        rabbitTemplate.convertAndSend("asset.created", newAsset);
        return newAsset;
    }

    public Asset updateAsset(int id, Asset newAsset) throws EntityNotFoundException {
        Optional<Asset> assetFromRepo = assetRepository.findById(id);
        if (assetFromRepo.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Asset asset = assetFromRepo.get();
        asset.setName(newAsset.getName());
        logger.debug("Update asset: ", newAsset);
        return assetRepository.save(asset);
    }

    public void deleteAsset(int id) throws Exception {
        assetRepository.deleteById(id);
        Optional<List<Asset>> children = getAllChildren(id);
        if (children.isPresent()) {
            assetRepository.deleteAll(children.get());
        }
        rabbitTemplate.send("asset.deleted", null);
    }

    public Asset promoteAsset(int id) throws Exception {
        Optional<Asset> assetFromRepo = assetRepository.findById(id);
        if (assetFromRepo.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Asset asset = assetFromRepo.get();
        asset.setPromoted(true);
        rabbitTemplate.send("asset.promoted", null);
        Asset newAsset = assetRepository.save(asset);
        Optional<List<Asset>> children = getAllChildren(id);
        if (children.isPresent()) {
            for (Asset childAsset : children.get()) {
                childAsset.setPromoted(true);
                assetRepository.save(childAsset);
            }
        }
        return newAsset;
    }
    
    public Optional<List<Asset>> getAllChildren(int rootId) throws Exception {
        final Criteria byAssetId = new Criteria("id").is(rootId);
        
        GraphLookupOperation graphLookup = GraphLookupOperation.builder()
                .from("assets")
                .startWith("$id")
                .connectFrom("id")
                .connectTo("parentId")
                .as("children");
        
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(byAssetId), graphLookup);
        List<Asset> results = mongoTemplate.aggregate(aggregation, "assets", Asset.class).getMappedResults();
        return CollectionUtils.isEmpty(results) ? Optional.empty() : Optional.of(results);
    }

    public Optional<List<Asset>> getAllAncestors(int childId) throws Exception {
        Optional<Asset> childAsset = assetRepository.findById(childId);
        if (childAsset.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Asset asset = childAsset.get();
        final int RECURSION_LIMIT = 3;
        List<Asset> ancestors = Arrays.asList();
        for (int i=0; i<RECURSION_LIMIT; i++) {
            Optional<Asset> parentAsset = assetRepository.findById(asset.getParentId());
            if (parentAsset.isEmpty())
                break;
            asset = parentAsset.get();
            ancestors.add(asset);
        }
        return ancestors.size() > 0 ? Optional.of(ancestors) : Optional.empty();
    }
}
