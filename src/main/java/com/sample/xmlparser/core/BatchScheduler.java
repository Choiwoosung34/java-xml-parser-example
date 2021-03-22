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

    private @Autowired JockeyRepository jockeyRepository;
    private @Value("${api.serviceKey}") String apiKey;
    private OpenDataApi openDataApi;

    /**
     * Class 생성 시 실행 되는 함수
     */
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

    /**
     * Class 소멸 시 실행 되는 함수
     */
    @PreDestroy
    public void preDestroy() {
        log.info("BatchScheduler Finished At : {}", LocalDateTime.now().toString());
    }

    ///////////////////////////////////
    // Main Logic
    ///////////////////////////////////
    @Scheduled(fixedDelay = 1000)
    public void scheduleTask() throws Exception {
        
        // 1 ~ 10 Page 가져와서 DB 넣는 예시
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
            final List<Item> items = jockeyInfos.getBody().getItems();
            for (Item item : items) {
                jockeyRepository.save(Jockey.builder().name(item.getJkName()).build());
            }
        }
    }
}
