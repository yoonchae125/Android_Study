# Data Binding

[data-binding, android 공식문서](https://developer.android.com/topic/libraries/data-binding)

data binding(데이터 결합 라이브러리) 은 프로그래매틱 방식이 아니라 선언적 형식으로 레이아웃의 UI 구성요소를 앱의 데이터 소스와 결합할 수 있는 지원 라이브러리이다.



**프로그래매틱 방식 (UI 프레임워크 호출)**

```kotlin
    findViewById<TextView>(R.id.sample_text).apply {
        text = viewModel.userName
    }

    
```

**data binding 사용**

```xml
<TextView
        android:text="@{viewmodel.userName}" />    
```

레이아웃 파일에서 구성요서를 결합하면 활동에서 많은 UI 프레임워크 호출을 삭제할 수 있어 파일이 더욱 단순화되고 유지관리 또한 쉬워진다. 앱 성능이 향상되면 메모리누수 및 null 포인터 예외를 방지할 수 있다.

## 시작하기

**빌드 환경**

~~~
android{
	...
	dataBinding {
		enabled = true
	}
}
~~~

## 레이아웃 및 결합 표현식

표현식 언어로 레이아웃의 뷰와 변수를 연결하는 표현식을 작성할 수 있다. 데이터 결합 라이브러리는 레이아웃의 뷰를 데이터 개체와 결합하는 데 필요한 클래스를 자동으로 생성한다. 라이브러리는 가져오기, 변수 및 포함과 같이 레이아웃에서 사용할 수 이쓴ㄴ 기능을 제공한다.

표현식에서 사용할 수 있는 결합 변수는 UI 레이아웃 루트 요소의 동위 요소인 `data` 요소 내에서 정의 된다. 아래 예처럼 두 요소는 모두 `layout` 태그로 래핑된다.

~~~xml
<?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android">
       <data>
           <variable name="user" type="com.example.User"/>
       </data>
       <LinearLayout
           android:orientation="vertical"
           android:layout_width="match_parent"
           android:layout_height="match_parent">
           <TextView android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:text="@{user.firstName}"/>
           <TextView android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:text="@{user.lastName}"/>
       </LinearLayout>
    </layout>     
~~~

`data` 내의 user 변수는 이 레이아웃 내에서 사용할 수 있는 속성을 설명한다.

~~~xml
<variable name="user" type="com.example.User" />
~~~

레이아웃 내의 표현식은 `@{}` 구문을 사용하여 특성 속성에 작성된다.

~~~xml
<TextView 
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:text="@{user.firstName}" />
~~~

***참고** : 레이아웃 표현식은 단위테스트가 불가능하고 IDE 지원이 제한적이므로 단순하게 유지해야한다. 맞춤 결합 어댑터를 사용하면 표현식을 단순화할 수 있다.

### 데이터 객체

`User` 항목을 설명하기 위해 간단한 기존 객체가 있다고 가정해 보자.

~~~kotlin
data class User(val firstName: String, val lastName: String)
~~~

`android:text` 속성에 사용된 `@{user.firstName}` 표현식은 `getFirstName()` 메서드에 액세스한다.

### 데이터 결합

https://developer.android.com/topic/libraries/data-binding/expressions#kotlin

각 레이아웃 파일의 겹합 클래스가 생성된다. 기본적으로 클래스 이름은 레이아웃 파일 이름을 기반으로 하여 파스칼 표기법으로 변환하고 *Binding* 접미사를 추가한다.

~~~kotlin
override fun onCreate(savedInstanceState: Bundel?) {
  super.onCreate(savedInstanceState)
  val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
  binding.user = User("Test", "User")
}
~~~

`LayoutInflater` 를 사용하여 뷰를 가져올 수도 있다

~~~kotlin
val binding: ActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater())
~~~

`Fragment`, `ListView` 또는 `RecyclerView` 어댑터 내에서 data binding을 사용하고 있다면 `DataBindingUtil` 의 `inflate()` 메서드를 사용할 수도 있다

~~~kotlin
val listItemBinding = ListItemBinding.inflate(layoutInflater, viewGroup, false)
val listItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false)
~~~

### 표현식 언어

[표현식 언어](https://developer.android.com/topic/libraries/data-binding/expressions#expression_language)

#### Null 병합 연산자

null 병합 연산자(`??`)는 왼쪽 피연산자가 `null` 이 아니면 왼쪽 피연산자를, `null` 이면 오른쪽 피연산자를 선택한다.

~~~xml
android:text="@{user.displayName ?? user.lastName}"
~~~

####  속성 참조

표현식은 다음 형식을 사용해 클래스의 속성을 참조할 수 있으며 이 형식은 fields, getters 및 `OvservableField` 객체에서도 동일하다.

~~~
android:text"@{user.lastName}"
~~~

#### null 포인터 예외 방지

생성된 데이터 결합 코드는 자동으로 `null` 값을 확인하고 null 포인터 예외를 방지한다. 

#### 뷰 참조

표현식은 다음 구문을 사용하여 ID로 레이아웃의 다른 뷰를 참조할 수 있다

~~~xml
<EditText
        android:id="@+id/example_text"
        android:layout_height="wrap_content"
        android:layout_width="match_parent"/>
    <TextView
        android:id="@+id/example_output"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@{exampleText.text}"/>
~~~

#### 컬랙션

`[]` 연산자를 사용하여 컬렉션에 액세스할 수 있다

~~~xml
<data>
        <import type="android.util.SparseArray"/>
        <import type="java.util.Map"/>
        <import type="java.util.List"/>
        <variable name="list" type="List&lt;String>"/>
        <variable name="sparse" type="SparseArray&lt;String>"/>
        <variable name="map" type="Map&lt;String, String>"/>
        <variable name="index" type="int"/>
        <variable name="key" type="String"/>
    </data>
    …
    android:text="@{list[index]}"
    …
    android:text="@{sparse[index]}"
    …
    android:text="@{map[key]}"
    
~~~

#### 문자열 리터럴

~~~
android:text='@{map["firstName"]}'
~~~

~~~
android:text="@{map[`firstName`]}"
~~~

#### 리소스

[리소스](https://developer.android.com/topic/libraries/data-binding/expressions#resources)

~~~
android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}"
~~~

### 이벤트 처리

데이터 결합을 사용하면 뷰에서 전달되는 표현식 처리 이벤트를 작성할 수 있다.

...



## 식별 가능한 데이터 객체 작업

식별 가능성은 객체가 데이터 변경에 관해 다른 객체에 알릴 수 있는 기능을 말한다. DataBinding을 통해 객체, 필드 또는 컬렉션을 식별 가능하게 만들 수 있다.

식별 가능한 클래스에는 세가지 유형- 객체, 필드 및 컬렉션이 있다. 식별 가능한 데티어 객체가 UI에 결합되고 데이터 객체 속성이 변경되면 UI가 자동으로 업데이트된다.

### 식별 가능한 필드

일부 작업은 `Observable` 인터페이스를 구현하는 클래스를 생성하는 작업과 관련이 있지만 클래스에 몇 가지 속성만 있다면 그다지 애쓸 필요가 없다.`Observable`  클래스 및 다음과 같은 Primitive 관련 클래스를 사용하여 필드를 식별 가능하게 만들 수 있다.

- `ObservableBoolean`
- `ObservableByte`
- `ObservableChar`
- `ObservableShort`
- `ObservableInt`
- `ObservableLong`
- `ObservableFloat`
- `ObservableDouble`
- `ObservableParcelable`

식별 가능한 필는 단일 필드가 있는 독립적인 식별 가능한 객체다. Primitive 버전은 액세스 작업 중에 박싱 및 언박싱을 방지한다. 이 메커니즘을 사용하려면 다음과 같이 자바 프로그래밍 언어로 `public final` 속성을 만들거나 Kotlin으로 읽기 전용 속성을 만들어야 한다.

~~~kotlin
class User{
  val firstName = ObservableField<String>()
  val lastName = ObservableField<String>()
  val age = ObservableInt()
}
~~~

***참고:** Android Studio 3.1 이상 사용시 식별 가능한 필드를 `LiveData` 객체로 바꿀 수 있다.

### 식별 가능한 컬렉션

**ObservableArrayMap**

~~~kotlin
ObservableArrayMap<String, Any>().apply{
  put("firstName", "Google")
  put("lastName", "Inc.")
  put("age", 17)
}
~~~

layout에서 문자열 키를 사용하여 map을 찾을 수 있다.

~~~xml
<data>
	<import type="android.databinding.ObservableMap"/>
  <variable name="user" type="ObservableMap"/>
</data>
...
<TextView
  ....
	android:text="@{user.lastName}"/>
<TextView
  ....
	android:text="@{String.valueOf(1+(Integer)user.age)}"/>
~~~

**ObservableArrayList**

~~~kotlin
    ObservableArrayList<Any>().apply {
        add("Google")
        add("Inc.")
        add(17)
    }
~~~

~~~xml
<data>
        <import type="android.databinding.ObservableList"/>
        <import type="com.example.my.app.Fields"/>
        <variable name="user" type="ObservableList<Object>"/>
    </data>
    …
    <TextView
        android:text='@{user[Fields.LAST_NAME]}'
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
    <TextView
        android:text='@{String.valueOf(1 + (Integer)user[Fields.AGE])}'
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
~~~

### 식별 가능한 객체

`Observable` 인터페이스를 구현하는 클래스를 사용하면 식별 가능한 객체의 속성 변경에 관해 알림을 받으려는 리스너를 등록할 수 있다.

`Observable` 인터페이스에 리스너를 추가하고 삭제하는 메커니즘이 있지만 알림이 전송되는 시점은 개발자가 직접 결정해야한다. 더 쉽게 개발할 수 있도록 데이터 결합 라이브러리는 리스너 등록 메커니즘을 구현하는 `BaseObservable` 클래스를 제공한다.

`BaseObservable` 을 구현한 데이터 클래스는 속성이 변경될 때 알리는 역할을 한다. `Bindable` 어노테이션을 getter에 할당하고 setter의 `notifyPropertyChanged()` 메서드를 호출하면 된다.

~~~kotlin
class User: BaseObservable() {
  @get:Bindable
  var firstName: String = ""
  	set(value){
      field = value
      notifyPropertyChanged(BR.firstName)
    }
  @get:Bindable
  var lastName: String = ""
  	set(value) {
      field = value
      notifyPropertyChanged(BR.lastName)
    }
}
~~~

데이터 결합은 데이터 결합에 사용된 리소스의 ID를 포함하는 모듈 패키지에 이름이 `BR`인 클래스를 생성합니다. [`Bindable`](https://developer.android.com/reference/android/databinding/Bindable) 주석은 컴파일 중에 `BR` 클래스 파일에 항목을 생성합니다. 데이터 클래스의 기본 클래스를 변경할 수 없으면 [`PropertyChangeRegistry`](https://developer.android.com/reference/android/databinding/PropertyChangeRegistry) 객체를 사용하여 [`Observable`](https://developer.android.com/reference/android/databinding/Observable) 인터페이스를 구현함으로써 효율적으로 리스너를 등록하고 리스너에 알림을 제공할 수 있습니다.