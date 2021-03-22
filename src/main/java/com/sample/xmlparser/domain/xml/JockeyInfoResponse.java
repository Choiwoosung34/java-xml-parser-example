package com.sample.xmlparser.domain.xml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 아래와 같은 XML Dom 구조를 객체로 표현한다.
 * 해당 모델만 잘 정의 해 놓으면 BatchScheduler 를 참고하여
 * XML Response 를 쉽게 Java Class 로 변환 할 수 있다.
 *
 *
 <response>
     <header>
         <resultCode>00</resultCode>
         <resultMsg>NORMAL SERVICE.</resultMsg>
     </header>
     <body>
     <items>
         <item>
         ...
         </item>
         <item>
         ...
         </item>
     </items>
     <numOfRows>2</numOfRows>
     <pageNo>1</pageNo>
     <totalCount>583</totalCount>
     </body>
 </response>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="response")
public class JockeyInfoResponse {

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
        private String age;
        private String birthday;
        private String debut;
        private String jkName;
        private String jkNo;
        private String meet;
        private String ord1CntT;
        private String ord1CntY;
        private String ord2CntT;
        private String ord2CntY;
        private String ord3CntT;
        private String ord3CntY;
        private String part;
        private String rcCntT;
        private String rcCntY;
        private String spDate;
        private String wgOther;
        private String wgPart;
    }
}
