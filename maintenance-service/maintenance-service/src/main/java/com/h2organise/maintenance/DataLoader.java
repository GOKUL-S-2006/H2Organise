package com.h2organise.maintenance;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.h2organise.maintenance.model.Asset;
import com.h2organise.maintenance.repository.AssetRepository;

// adds a few sample assets the first time the app runs
@Component
public class DataLoader implements CommandLineRunner {

    private final AssetRepository assetRepo;

    public DataLoader(AssetRepository assetRepo) {
        this.assetRepo = assetRepo;
    }

    @Override
    public void run(String... args) {
        if (assetRepo.count() > 0) {
            return;
        }
        addAsset("Main Pump 1", "PUMP", "Green Lake Station");
        addAsset("North Pipeline", "PIPELINE", "North Ward");
        addAsset("Overhead Tank A", "TANK", "East Ward");
        addAsset("Inlet Valve 3", "VALVE", "Treatment Plant");
    }

    private void addAsset(String name, String type, String location) {
        Asset asset = new Asset();
        asset.setName(name);
        asset.setType(type);
        asset.setLocation(location);
        asset.setStatus("WORKING");
        assetRepo.save(asset);
    }
}
