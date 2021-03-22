package com.sample.xmlparser.domain.xml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="response")
public class JockeyChangeInfoResponse {

    private Header header;
    private Body body;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "body")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Body {

        @XmlElementWrapper(name="items")
        @XmlElement(name="item")
        private List<Item> items = new ArrayList<>();
        private String numOfRows;
        private String pageNo;
        private String totalCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name="item")
    public static class Item {
        private String befBudam;
        private String chulNo;
        private String hrName;
        private String hrNo;
        private String jkAft;
        private String jkAftName;
        private String jkBef;
        private String jkBefName;
        private String meet;
        private String rcDate;
        private String rcNo;
        private String reason;
    }
}
