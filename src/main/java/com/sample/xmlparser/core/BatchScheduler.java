package com.sample.xmlparser.core;

import com.sample.xmlparser.domain.Jockey;
import com.sample.xmlparser.domain.xml.JockeyChangeInfoResponse;
import com.sample.xmlparser.domain.xml.JockeyInfoResponse;
import com.sample.xmlparser.domain.xml.JockeyInfoResponse.Item;
import com.sample.xmlparser.domain.xml.JockeyRecordsResponse;
import com.sample.xmlparser.jockeys.JockeyRepository;
import feign.Feign;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.jaxb.JAXBEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class BatchScheduler {

    private @Value("${api.serviceKey}") String apiKey;
    private @Autowired JockeyRepository jockeyRepository;
    private OpenDataApi openDataApi;

    @PostConstruct
    public void postConstruct() {
        log.info("BatchScheduler Started At : {}", LocalDateTime.now().toString());

        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding("UTF-8")
                .build();

        openDataApi = Feign.builder()
                .encoder(new JAXBEncoder(jaxbFactory))
                .decoder(new JAXBDecoder(jaxbFactory))
                .target(OpenDataApi.class, "http://apis.data.go.kr/B551015");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("BatchScheduler Finished At : {}", LocalDateTime.now().toString());
    }

    ///////////////////////////////////
    // Main Logic
    ///////////////////////////////////
     @Scheduled(fixedDelay = 1000)
    public void scheduleTask() {

        // 1 ~ 10 Page 가져오는 예시
        for (int i = 1; i <= 10; i++) {
            String rowsPerPage = "10";
            String pageNumber = String.valueOf(i);

            JockeyInfoResponse jockeyInfos = openDataApi.getJockeyInfo(apiKey, pageNumber, rowsPerPage);
            JockeyChangeInfoResponse jockeyChangeInfos = openDataApi.getJockeyChangeInfo(apiKey, pageNumber, rowsPerPage);
            JockeyRecordsResponse jockeyRecords = openDataApi.getJockeyRecordInfo(apiKey, pageNumber, rowsPerPage);

            log.info("=========================================================================================");
            log.info("한국마사회 기수 상세 정보 Page {} : {}", pageNumber, jockeyInfos.toString());
            log.info("한국마사회 기수 변경 정보 Page {} : {}", pageNumber, jockeyChangeInfos.toString());
            log.info("한국마사회 기수 성적 정보 Page {} : {}", pageNumber, jockeyRecords.toString());
            log.info("\n");

            // DB 저장 예시
            jockeyInfos.getBody().getItems().forEach( item -> {
                log.info("Current Item : {}", item.toString());
                log.info("Save : {}",
                        jockeyRepository.save(Jockey.builder().name(item.getJkName()).build()).toString()
                );
            });
        }

        System.exit(0);
    }
}
