# MVP 패턴

Model + View + Presenter를 합친 용어

## 구조

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FclZlsT%2FbtqBTLzeUCL%2FIDA8Ga6Yarndgr88g9Nkhk%2Fimg.png)

- Model : 어플리케이션에서 사용되는 데이터와 그 데이터를 처리하는 부분
- View : 사용자에게 보여지는 부분
- Preseneter : View에서 요청한 정보로 Model을 가공하여 View에 전달해 주는 부분

## 동작

1. 사용자의 Action들은 View를 통해 들어온다.
2. View는 데이터를 Presenter에게 요청한다.
3. Presenter는 Model에게 데이터를 요청한다.
4. Model은 Preseneter에게 요청받은 데이터를 응답한다.
5. Preseneter는 View에게 응답한다.
6. View는 Presenter가 응답한 데이터를 이용하여 화면을 나타낸다.

## 특징

- Preseneter와 View는 1:1 관계
- Presenter는 View와 Model의 인스턴스를 가지고 있어 둘을 연결하는 접착제 역할을 함

## 장점

View와 Model의 의존성이 없다.

## 단점 

View와 Preseneter 사이의 의존성이 높다

