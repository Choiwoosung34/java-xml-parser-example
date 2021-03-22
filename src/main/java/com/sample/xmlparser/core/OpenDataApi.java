package com.sample.xmlparser.core;

import com.sample.xmlparser.domain.xml.JockeyChangeInfoResponse;
import com.sample.xmlparser.domain.xml.JockeyInfoResponse;
import com.sample.xmlparser.domain.xml.JockeyRecordsResponse;
import feign.Param;
import feign.RequestLine;

/**
 * API Interface
 * 호출 하고자 하는 API 를 선언한다.
 */
public interface OpenDataApi {

    /**
     * 한국마사회 기수 상세정보 조회하는 API
     */
    @RequestLine("GET /API12/jockeyInfo" +
            "?serviceKey={serviceKey}" +
            "&pageNo={pageNumber}" +
            "&numOfRows={rowsPerPage}"
    )
    JockeyInfoResponse getJockeyInfo(@Param("serviceKey") String serviceKey,
                                     @Param("pageNumber") String pageNumber,
                                     @Param("rowsPerPage") String rowsPerPage);

    /**
     * 한국마사회 기수 변경 정보 조회하는 API
     */
    @RequestLine("GET /API10/jockeyChangeInfo" +
            "?serviceKey={serviceKey}" +
            "&pageNo={pageNumber}" +
            "&numOfRows={rowsPerPage}"
    )
    JockeyChangeInfoResponse getJockeyChangeInfo(@Param("serviceKey") String serviceKey,
                                                 @Param("pageNumber") String pageNumber,
                                                 @Param("rowsPerPage") String rowsPerPage);


    /**
     * 한국마사회 기수 성적 정보 조회하는 API
     */
    @RequestLine("GET /API11/jockeyResult" +
            "?serviceKey={serviceKey}" +
            "&pageNo={pageNumber}" +
            "&numOfRows={rowsPerPage}"
    )
    JockeyRecordsResponse getJockeyRecordInfo(@Param("serviceKey") String serviceKey,
                                              @Param("pageNumber") String pageNumber,
                                              @Param("rowsPerPage") String rowsPerPage);

}
