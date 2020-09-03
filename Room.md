[ref]

https://developer.android.com/training/data-storage/room/

https://developer.android.com/topic/libraries/architecture/

https://blog.yena.io/studynote/2018/09/08/Android-Kotlin-Room.html

# Android Room

안드로이드 아키텍쳐는 앱을 견고하고, 실험 가능하고, 유지 보수성이 뛰어나도록 만들어주는 라이브러리 모음이다. 이 중 하나가 Room이다.

Room 은 SQLite 추상 레이어를 제공하여 SQLite 객체를 매핑하는 역할을 한다. 즉 **SQLite의 기능을 모두 사용할 수 있고, DB로의 접근을 편하게 도와주는 라이브러리**다.

**SQLite를 직접 쓰지 않고 Room을 사용하는 이유**

Room을 사용하면 컴파일 시간을 체크할 수 있으며, 무의미한 boilerplate 코드의 반복을 줄여줄 수 있다.

## Room Components 구성요소

1. `Entity` - DB 안에 있는 테이블을 Java 클래스로 나타낸 것, 데이터 모델 클래스
2. `DAO`- Database Access Object, DB에서 접근해서 실질적으로 insert, delete 등을 수행하는 메소드를 포함
3. `Database` - DB holder를 포함하여, 앱에 영구 저장되는 데이터와 기본 연결을 위한 주 액세스 지점, RoomDatabase를 extend하는 추상 클래스여야 하며, 테이블과 버전을 정의하는 곳이다.

## Room Components 작성하기

### Entity



