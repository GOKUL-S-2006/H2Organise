package com.h2organise.maintenance.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.h2organise.maintenance.model.Asset;
import com.h2organise.maintenance.repository.AssetRepository;
import com.h2organise.maintenance.repository.MaintenanceJobRepository;

@Service
public class AssetService {

    private final AssetRepository assetRepo;
    private final MaintenanceJobRepository jobRepo;

    public AssetService(AssetRepository assetRepo, MaintenanceJobRepository jobRepo) {
        this.assetRepo = assetRepo;
        this.jobRepo = jobRepo;
    }

    public List<Asset> getAllAssets() {
        return assetRepo.findAll();
    }

    public Asset getAsset(Long id) {
        return assetRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found"));
    }

    public Asset addAsset(Asset asset) {
        if (asset.getName() == null || asset.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset name is required");
        }
        asset.setStatus("WORKING");
        return assetRepo.save(asset);
    }

    public void deleteAsset(Long id) {
        Asset asset = getAsset(id);
        if (jobRepo.existsByAssetId(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset has maintenance jobs, cannot delete");
        }
        assetRepo.delete(asset);
    }
}
