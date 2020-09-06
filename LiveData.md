# LiveData

[livedata, android 공식 문서](https://developer.android.com/topic/libraries/architecture/livedata)

`LiveData`는 식별 가능한 데이터 홀더 클래스다. 식별 가능한 일반 클래스와 달리, 수명 주기를 인식한다. 즉 액티비티, 프래그먼트 또는 서비스와 같은 다른 앱 구성요소의 수명 주기를 고려한다. 이러한 수명 주기 인식을 통해 LiveData는 활성 수명 주기 상태에 있는 앱 구성요소 관찰자만 업데이트한다.



`Observer` 클래스로 표현되는 관찰자의 생명 주기가 `STARTED` 또는 `RESUMED` 상태이면 LiveData는 관찰자를 활성 상태로 간주한다. LiveData는 활성 관찰자에게만 업테이트 정보를 알린다. `LiveData` 객체를 보기 위해 등록된 비활성 관찰자는 변경사항에 관한 알림을 받지 않는다.

`LifecycleOwner` 인터페이스를 구현하는 객체와 페어링된 관찰자를 등록할 수 있다. 이 관계를 사용해 관찰자에 대응되는 `Lifecycle` 객체의 상태가 `DESTROYED` 로 변경될 때 관찰자를 삭제할 수 있다. 특히, 액티비티과 프래그먼트가 LiveData 객체를 안전하게 관찰할 수 있고, 생명주기가 끝나는 즉시 수신 거부되어 누출을 걱정하지 않아도 되므로 유용하다.



### LiveData 사용의 장점

**UI와 데이터 상태의 일치 보장**

LiveData는 관찰자 패턴을 따른다. LiveData는 수명 주기 상태가 변경될 때 `Observer` 객체에 알린다. 앱 데이터가 변경될 때마다 UI를 업데이트하는 대신, 변경이 발생할 때마다 관찰자가 UI를 업데이트할 수 있다.

**메모리 누출 없음**

관찰자는 `Lifecycle` 객체에 결합되어 있으며 연결된 생명 주기가 끝나면 자동으로 삭제된다.

**중지된 할동으로 인한 비정상 종료 없음**

관찰자의 수명 주기가 비활성 상태이면 관찰자는 어떤 LiveData 이벤트도 수신하지 않는다

**수명 주기를 더 수동으로 처리하지 않음**

UI 구성요소는 관련 데이터를 관찰하기만 할 뿐 관찰을 중지하거나 다시 시작하지 않는다.

**최신 데이터 유지**

생명 주기가 다시 활성화될 때 최신 데이터를 수신한다.

**적절한 구성 변경**

구성 변견(예: 기기 회전)으로 인해 활동이나 프래그먼트가 다시 생성되면 사용할 수 있는 최신 정보를 즉시 수신한다.

**리소스 공유**

앱에서 시스템 서비스를 공유할 수 있도록 싱글톤 패턴을 사용하는 `LiveData` 객체를 확장하여 시스템 서비스를 래핑할 수 있다. `LiveData` 객체가 시스템 서비스에 한 번 연결되면 리소스가 필요한 모든 관찰자가 `LiveData` 객체를 볼 수 있다.



## LiveData 객체로 작업

**dependency 추가**

```
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
```

1. 특정 유형의 데이터를 보유할 `LiveData` 의 인스턴스를 생성한다. 이 작업은 일반적으로 `ViewModel` 클래스 내에서 이루어진다.
2. `onChanged()` 메서드를 정의하는 `Observer` 객체를 생성한다. 이 메서드는 `LiveData` 객체가 보유한 데이터 변경 시 발생하는 작업을 제어한다. 일반적으로 액티비티나 프래그먼트 같은 UI 컨트롤러에 `Observer` 객체를 생성한다.
3. `observe()` 메서드를 사용해 `LiveData` 객체에 `Observer` 객체를 연결한다.

*** 참고**: observeForever(Observer) 메서드를 사용하여 연결된 LifecycleOwner 객체가 없는 관찰자를 등록할 수 있다. 이 경우 관찰자가 항상 활성 상태로 간주된다. removeObserver(Observer) 메서드를 호출하여 관찰자를 삭제할 수 있다.



`LiveData` 객체에 저장된 값을 업데이트하면 연결된 `LifecycleOwner` 가 활성 상태에 이쓴ㄴ 한 등록된 모든 관찰자가 트리거된다.

LiveData를 사용하면 UI 컨트롤러 관찰자가 업데이트를 구독할 수 있다. `LiveData`객체에서 보유한 데이터가 변경되면 응답으로 UI가 자동 업데이트 된다.



#### LiveData 객체 만들기

LiveData는 `List` 와 같은 `Collections` 를 구현하는 객체를 비롯해 모든 데이터와 함께 사용할 수 있는 래퍼다. `LiveData` 객체는 일반적으로 `ViewModel` 객체 내에 저장되며 다음 예에서 보는 것과 같이 getter 메서드를 통해 액세스 된다

```kotlin
class NameViewModel: ViewModel(){
  // create LiveData
  val currentName: MutableLiveData<String> by lazy {
    MutableLibeData<String>()
  }
}
```

처음에는 `LiveData` 객체의 데이터가 설정되지 않는다

***참고:** 액티비티 또는 프래그먼트가 아닌 ViewModel 객체의 UI를 업데이트하는 LiveData 객체를 저장해야하는 이유

- 액티비티와 프래그먼트가 지나치게 커지지 않게 하기 위해서이다. 이러한 UI 컨트롤러가 데이터 표시를 담당하지만 데이터 상태를 보유하지 않는다.
- LiveData 인스턴스를 특정 액티비티나 인스턴스에서 분리하고 구성 변경에도 LiveData 객체가 유지되도록 하기 위해서다.

#### LiveData 객체 관찰

대부분의 경우 앱 구성요소의 `onCreate()` 메서드는 `LiveData` 객체 관찰을 시작하기 적합하며 그 이유는 다음과 같다.

- 시스템이 액티비티나 프래그먼트의 `onResume()` 메서드에서 중복 호출하지 않는다.
- 액티비티 또는 프래그먼트의 활성 상태가 되는 즉시 표시할 수 있는 데이터를 보유하도록 하기 위해서이다. 

일반적으로 LiveData는 데이터가 변경될 때만, 그리고 활성 관찰자에게만 업데이트를 전달한다. 예외로, 관찰자가 비활성 상태에서 활성 상태로 변경될 때에도 관찰자는 업데이트를 받는다.

**LiveData 객체 관찰을 시작하는 방법**

~~~kotlin
class NameActivity: AppCompatActivity() {
  // Use the 'by viewModels()' Kotlin property delegate
  // from the activity-ktx artifact
  private val model: NameViewModel by viewModels()
  
  override fun onCreate(savedInstanceState: Bundel?) {
    ...
    
    // create the observer which updates the UI
    val nameObserver = Observer<String> { newName -> 
       nameTextView.text = newName
    }
    // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer
    model.currentName.observe(this, nameObserver)
  }
}
~~~

`nameObserver` 를 매개변수로 전달하여 `observe()` 를 호출하면 `onChanged()` 가 즉시 호출되어 `mCurrentName` 에 저장된 가장 최신 값을 제공한다. `LiveData` 객체가 `mCurrentName` 에 값을 설정하지 않았다면 `onChanged()`  는 호출되지 않는다.

#### LiveData 객체 업데이트

LiveData에는 저장된 데이터를 업데이트하는 데 공개적으로 사용할 수 있는 메서드가 없다.`MutableLiveData` 클래스는 `setValue(T)`  및 `postValue(T)` 메서드를 공개 메서드로 노출하며 `LiveData` 객체에 저장된 값을 수정할 때 사용한다. 일반적으로는 `MutableLiveData` 는 ViewModel  에서 사용되며 ViewModel은 변경이 불가능한 LiveData 객체만 관찰자에게 노출한다.

관찰자 관계를 설정한 후에는 아래와 같이 사용자가 버튼을 탭할 때 모든 관찰자를 트리거하는 `LiveData` 객체의 값을 업데이트 할 수 있다.

~~~kotlin
button.setOnClickListener{
  val anotherName = "Evan Kim"
  model.currentName.setValue(anotherName)
}
~~~

`setValue()` 또는 `postValue()` 의 호출은 관찰자를 트리거하고 UI를 업데이트한다

#### Room 으로 LiveData 사용

Room 지속성 라이브러리는 관찰  가능한 쿼리를 지원하며 이 쿼리는 LiveData 객체를 반환한다.

데이터베이스가 업데이트될 때 Room에서는 `LiveData` 객체를 업데이트하는 데 필요한 모든 코드를 생성한다. 생성된 코드는 필요 시 백그라운드 스레드에서 비동기적으로 쿼리를 실행한다. 이 패턴은 UI에 표시된 데이터와 데이터베이스에 저장된 데이터의 동기화를 유지하는 데 유용하다.

#### LiveData와 함께 코루틴 사용

LiveData에는 Kotlin 코루틴 지원이 포함 된다. 

[AAC와 코루틴](https://developer.android.com/topic/libraries/architecture/coroutines)



## LiveData 확장

관찰자의 생명주기가 `STARTED` 또는 `RESUMED` 상태이면 LiveData는 관찰자를 활성 상태로 간주한다.

~~~Kotlin
class StockLiveData(symbol: String) : LiveData<BigDecimal>() {
  private val stockMangager = StockManager(symbol)
  private val listener = { price: BigDecimal ->
     value = price                   
  }
  override fun onActive() {
    stockManager.requestPriceUpdataes(listener)
  }
  override fun onInactive() {
    stockManager.removeUpdataes(listener)
  }
  
  companion object {
    //Singleton
    private lateinit var sInstance: StockLiveData

    @MainThread
    fun get(symbol: String): StockLiveData {
       sInstance = if (::sInstance.isInitialized) sInstance else StockLiveData(symbol)
       return sInstance
    }
  }
}
~~~

- `onActive()` 메서드는 LiveData 객체에 활성 상태의 관찰자가 있을 때 호출된다.
- `onInactive()` 메서드는 LiveData 객체에 활성 상태의 관찰자가 없을 때 호출된다.



## LiveData 변환

관찰자에게 `LiveData` 객체를 전달하기 전에 객체에 저장된 값을 변경하고 싶거나 다른 객체의 값에 따라 다른 `LiveData` 인스턴스를 반환해야 하는 경우가 있다. `Lifecycle` 패키지는 이러한 시나리오를 지원하는 도우미 메서드가 포함된 `Transformation` 클래스를 제공한다.

**Transformations.map()**

LiveData 객체에 저장된 값에 함수를 적용하여 결과를 다운스트림으로 전파한다

~~~kotlin
val userLiveData: LiveData<User> = UserLiveData()
val userName: LiveData<String> = Transformations.map(userLiveData) {
  user -> "${user.name} ${user.lastName}"
}
~~~

**Transformantions.switchMap**

map() 과 마찬가지로 LiveData 객체에 저장된 값에 함수를 적용하고 결과를 래핑 해제하여 다운스트림으로 전달한다. switchMap() 에 전달된 함수는 LiveData 객체를 반환해야 한다.

~~~kotlin
private fun getUser(id: String): LiveData<User> {
  ...
}
val userId: LiveData<String> = ...
var user = Transformations.switchMap(userid) { id -> getUser(id)
}
~~~

변환 메소드를 사용해 관찰자의 수명 주기 전반에 걸쳐 정보를 전달할 수 있다. 관찰자가 반환된 LiveData 객체를 관찰하고 있지 않다면 변환은 계산되지 않는다. 변환은 느리게 계산되기 때문에 생명 주기 관련 동작은 추가적인 명시적 호출이나 종속성 없이도 암시적으로 전달된다.

ViewModel 객체 내에 Lifecycle 객체가 필요한 경우라면 변환은 더 나은 해결 방법이 될 수 있다. 

**Example** :

주소를 받아서 우편번호를 반환하는 UI 구성요소가 있다

~~~kotlin
class MyViewModel(private repository: PostalCodeRepository) : ViewModel() {
  private fun getPostalCode(address: String): LiveData<String> {
    // Don'T DO THIS!!
    return repository.getPostCode(address)
  }
}
~~~

UI 구성요소는 `getPostalCode()` 를 호출할 때마다 이전 LiveData 객체에서 등록을 취소하고 새 인스턴스에 등록해야 한다. 또한 UI 구성요소가 다시 생성되면 이전 호출의 결과를 사용하지 않고 또 다른 `repository.getPoseCode()` 메서드 호출을 트리거한다.

대신 주소 입력의 변환으로 우편번호 조회를 구현할 수도 있다.

~~~kotlin
class MyViewModel(private val repository: PostalCodeRespository): ViewModel() {
  private val addressInput = MutableLiveData<String>()
  val postalCode: LiveData<String> = Transformations.switchMap(addressInput) {
    address -> 
    repository.getPostCode(address)
  }
  private fun setInput(address: String){
    addressInput.value = address
  }
}
~~~

