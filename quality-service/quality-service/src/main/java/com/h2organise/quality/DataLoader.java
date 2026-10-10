package com.h2organise.quality;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.h2organise.quality.model.QualityLimit;
import com.h2organise.quality.model.WaterUse;
import com.h2organise.quality.repository.QualityLimitRepository;

// adds default limits the first time the app runs
@Component
public class DataLoader implements CommandLineRunner {

    private final QualityLimitRepository limitRepo;

    public DataLoader(QualityLimitRepository limitRepo) {
        this.limitRepo = limitRepo;
    }

    @Override
    public void run(String... args) {
        if (limitRepo.count() > 0) {
            return;
        }
        addLimit(WaterUse.DRINKING, 6.5, 8.5, 5, 500);
        addLimit(WaterUse.IRRIGATION, 6.0, 8.5, 10, 2000);
        addLimit(WaterUse.INDUSTRIAL, 6.0, 9.0, 10, 1500);
    }

    private void addLimit(WaterUse use, double phMin, double phMax, double turbidityMax, double tdsMax) {
        QualityLimit limit = new QualityLimit();
        limit.setWaterUse(use);
        limit.setPhMin(phMin);
        limit.setPhMax(phMax);
        limit.setTurbidityMax(turbidityMax);
        limit.setTdsMax(tdsMax);
        limitRepo.save(limit);
    }
}
