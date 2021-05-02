# Easy way to read XML in java

사용한 라이브러리 : https://github.com/OpenFeign/feign

``` shell
공공데이터 포털 오픈 API 의 xml 을 쉽게 파싱하고 저장하는 예제~

본 프로젝트는 XML 파싱하는 Java 코드를 구글링하며 찾다가,

아직도 대부분의 자료가 HttpURLConnection 을 열고 BufferedReader 로 읽는

자료가 대부분이라 화나서 더 좋은 방법을 찾다가 openfeign 을 사용하여 개발한

공공데이터 포털의 아래 API 들을 호출하여 읽고 저장하는 예제입니당.
```

## 예제 API 리스트
한국마사회 기수 상세정보
 - https://data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15056828

한국마사회 기수변경 정보
- https://data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15057181

한국마사회 기수 성적 정보
- https://data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15056591

## 이런 분들 봤으면 좋겠어요..
- `공공 데이터 포털의 API 를 사용하려는 사람~`
- `API Response 가 JSON 이 아니라 xml 형식으로 내려와서 화나는 사람.`
- `xml 파싱하는거 검색 했더니 HttpURLConnection 열어서 BufferedReader로 읽어와서 어쩌구저쩌구 너무 복잡한 코드가 싫으신 분`

## 프로젝트 실행 시 체크해야 되는 것!
- ~~application.properties 파일의 `api.serviceKey`의 값을 공공 데이터 포털에서 받은 key 값으로 넣는다.~~
  - **그냥 아무나 내려받아서 실제로 돌려볼 수 있게 api key 업데이트 해놓음.**
- domain 패키지의 xml 안에 있는 객체를 참고하여, 내려받는 Response 에 맞춰 객체를 정의한다.

#
블로그에도 남겨놨어용..

https://blog.naver.com/PostView.nhn?blogId=myhouse34&Redirect=View&logNo=222284326241&categoryNo=22&isAfterWrite=true&isMrblogPost=false&isHappyBeanLeverage=true&contentLength=55523
