# Easy way to read XML in java

``` shell
공공데이터 포털 오픈 API 의 xml 을 쉽게 파싱하고 저장하는 예제~

본 프로젝트는 XML 파싱하는 Java 코드를 구글링하며 찾다가,

아직도 대부분의 자료가 HttpURLConnection 을 열고 BufferedReader 로 읽는

자료가 대부분이라 더 좋은 방법을 찾다가 openfeign 을 사용하여 개발한

공공데이터 포털의 아래 API 들을 호출하여 읽고 저장하는 예제입니당.
```
사용한 라이브러리 : https://github.com/OpenFeign/feign  
예제로 사용한 API : https://data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15056828 (한국마사회 기수 상세정보)

## Docker
내려받고 run 만 시키면 실제로 돌려볼 수 있게 해놓았습니다.  
docker 로 돌리려면, Dockerfile 이 있는 디렉토리에서 아래 명령어 순서대로 입력하면 됩니다.
### 1.1 Package
``` shell
mvn package
```
### 1.2 Image
``` shell
docker build -t com.sample.xmlparser:0.0.1-SNAPSHOT .
```

### 1.3 Run
``` shell
docker run -d -it --name com.sample.xmlparser -e SPRING_PROFILES_ACTIVE=local -p 9000:9000 com.sample.xmlparser:0.0.1-SNAPSHOT
```

## Projet 설명
### Basics
기본적인 전체 구조입니다.  
(예시코드로 실제 프로젝트와는 구조가 다릅니다.)
``` java
@SpringBootApplication
public class Application {

	interface OpenDataApi {
		@RequestLine("GET /API12/jockeyInfo")
		JockeyInfoResponse getJockeyInfo(@QueryMap Map<String, Object> params);
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name="response")
	public static class JockeyInfoResponse {

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
			private List<JockeyInfo> items = new ArrayList<>();
			private String numOfRows;
			private String pageNo;
			private String totalCount;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		@XmlRootElement(name="item")
		public static class JockeyInfo {
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


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
				.withMarshallerJAXBEncoding("UTF-8")
				.build();

		OpenDataApi client = Feign.builder()
				.encoder(new JAXBEncoder(jaxbFactory))
				.decoder(new JAXBDecoder(jaxbFactory))
				.target(OpenDataApi.class, "http://apis.data.go.kr/B551015");


		Map<String, Object> parameters = new LinkedHashMap<>();
		parameters.put("serviceKey", "5zfa35vkduvcCLxLYseIFgVCx2En3NQeUH29FixNIseuJRfYbjx0PHGdbuDHk%2B8n63vE29gzdrbib3EPQ36mGg%3D%3D");
		parameters.put("pageNo", "1");
		parameters.put("numOfRows", "10");

		final JockeyInfoResponse response = client.getJockeyInfo(parameters);
		System.out.println("RESULT CODE : ("+response.getHeader().getResultCode()+")");
		List<JockeyInfoResponse.JockeyInfo> jockeyInfos = response.getBody().getItems();
		for (JockeyInfoResponse.JockeyInfo jockeyInfo : jockeyInfos) {
			System.out.println("기수 정보 :"+jockeyInfo);
		}
	}
}
```

위 코드를 실행시키면 아래와 같은 로그를 볼 수 있습니다.  
![log](https://user-images.githubusercontent.com/48939262/117090297-f14b3380-ad92-11eb-8c3c-1dd55d6508dd.png)


### XML MAPPING
본 코드의 핵심은 XML 을 객체에 잘 매핑 시키는 것입니다.  
http://apis.data.go.kr/B551015/API12/jockeyInfo?serviceKey={serviceKey}&pageNo=1&numOfRows=3  
위 API 를 호출하면 아래와 같은 응답을 받아볼 수 있습니다.
``` xml
<response>
  <header>
    <resultCode>00</resultCode>
    <resultMsg>NORMAL SERVICE.</resultMsg>
  </header>
  <body>
    <items>
      <item>
        <age>29</age>
        <birthday>19911030</birthday>
        <debut>20130601</debut>
        <jkName>권석원</jkName>
        <jkNo>080511</jkNo>
        <meet>서울</meet>
        <ord1CntT>38</ord1CntT>
        <ord1CntY>0</ord1CntY>
        <ord2CntT>38</ord2CntT>
        <ord2CntY>0</ord2CntY>
        <ord3CntT>49</ord3CntT>
        <ord3CntY>1</ord3CntY>
        <part>4</part>
        <rcCntT>672</rcCntT>
        <rcCntY>62</rcCntY>
        <spDate>-</spDate>
        <wgOther>52</wgOther>
        <wgPart>52</wgPart>
      </item>
      <item>
        <age>58</age>
        <birthday>19621218</birthday>
        <debut>19790411</debut>
        <jkName>김귀배</jkName>
        <jkNo>080014</jkNo>
        <meet>서울</meet>
        <ord1CntT>322</ord1CntT>
        <ord1CntY>3</ord1CntY>
        <ord2CntT>322</ord2CntT>
        <ord2CntY>3</ord2CntY>
        <ord3CntT>378</ord3CntT>
        <ord3CntY>5</ord3CntY>
        <part>21</part>
        <rcCntT>4433</rcCntT>
        <rcCntY>70</rcCntY>
        <spDate>-</spDate>
        <wgOther>52</wgOther>
        <wgPart>52</wgPart>
      </item>
      <item>
        <age>28</age>
        <birthday>19921102</birthday>
        <debut>20170602</debut>
        <jkName>김덕현</jkName>
        <jkNo>080577</jkNo>
        <meet>서울</meet>
        <ord1CntT>51</ord1CntT>
        <ord1CntY>8</ord1CntY>
        <ord2CntT>41</ord2CntT>
        <ord2CntY>6</ord2CntY>
        <ord3CntT>32</ord3CntT>
        <ord3CntY>10</ord3CntY>
        <part>50</part>
        <rcCntT>388</rcCntT>
        <rcCntY>110</rcCntY>
        <spDate>-</spDate>
        <wgOther>52</wgOther>
        <wgPart>52</wgPart>
      </item>
    </items>
    <numOfRows>3</numOfRows>
    <pageNo>1</pageNo>
    <totalCount>583</totalCount>
  </body>
</response>
```
위 XML 응답을 아래처럼 마치 JPA 테이블 매핑 하듯이 객체로 정의하면 됩니다.  
그럼 feignClient 에서 응답 받아올 때 객체에 매핑돼서,
객체로 편하게 내부적으로 이용할 수 있습니다.
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="response")
public static class JockeyInfoResponse {

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
		private List<JockeyInfo> items = new ArrayList<>();
		private String numOfRows;
		private String pageNo;
		private String totalCount;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@XmlRootElement(name="item")
	public static class JockeyInfo {
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
```

