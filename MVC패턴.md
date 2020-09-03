# MVC 패턴

Model + View + Controller를 합친 용어

## 구조

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F7IE8f%2FbtqBRvw9sFF%2FAGLRdsOLuvNZ9okmGOlkx1%2Fimg.png)

- Model : 어플리케이션에서 사용되는 데이터와 그 데이터를 처리하는 부분
- View : 사용자에게 보여지는 UI 부분
- Controller : 사용자의 입력(Action)을 받고 처리하는 부분

## 동작

1. 사용자의 Action이 Controller에 들어온다.
2. Controller는 사용자의 Action을 확인하고, Model을 업데이트한다. (Controller가 업데이트하는 건 아님)
3. Controller는 Model을 나타내줄 View를 선택한다.
4. View는 Model을 이용하여 화면에 나타낸다.

#### View가 업데이트되는 방법

- View가 Model을 이용하여 직접 업데이트
- Model이 View에게 Notify하여 업데이트
- View가 Polling으로 주기적으로 Model의 변경 감지하여 업데이트

## 특징

- Controller가 여러개의 View를 선택할 수 있는 1:n 구조
- Controller는 View를 선택할 뿐 직접 업데이트하지 않음
- View는 Controller를 알지 못함

## 장점

단순하다

## 단점

View와 Model 사이의 의존성이 높다