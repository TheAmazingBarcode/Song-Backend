package org.barcode.cacheservice.scheduled;

import org.barcode.cacheservice.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class KSQLTask {

    private final CacheService cacheService;

    @Autowired
    public KSQLTask(CacheService cacheService) {
        this.cacheService = cacheService;
    }


    @Scheduled(fixedDelay = 60000)
    public void pullData(){
        System.out.println("Pulling data");
        this.cacheService.pollKsqlDb();
    }
}