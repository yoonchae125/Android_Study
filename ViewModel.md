# ViewModel

[ViewModel, android 공식 문서](https://developer.android.com/topic/libraries/architecture/viewmodel)

ViewModel 클래스는 수명 주기를 고려하여 UI 관련 데이터를 저장하고 관리하도록 설계되었습니다.

Android 프레임워크는 특정 사용자 작업이나 완전히 통제할 수 없는 기기 이벤트에 대한 응답으로 UI 컨트롤러를 제거하거나 다시 만들도록 결정할 수 있다.

시스템에서 UI 컨트롤러를 제거하거나 다시 만들면 컨트롤러에 저장된 일시적인 모든 UI 관련 데이터가 손실된다. 데이터가 단순한 경우 `onSaveInstanceState()` 메서드를 사용하여 `onCreate()` 의 번들에서 데이터를 복원할 수 있다. 하지만 이 접근 방법은 직렬화했다가 다시 역직렬화할 수 있는 소량의 데이터에만 적합하다.

또 다른 문제는 UI 컨트롤러가 반환하는 데 시간이 걸릴 수 있는 비동기 호출을 자주 해야한다는 점이다. UI 컨트롤러는 이러한 비동기 호출을 관리해야 하며, 메모리 누출 가능성을 방지하기 위해 시스템에서 호출 폐기 후 호출을 정리하는지 확인해야 한다. 이러한 관리에는 많은 유지보수가 필요하며, 구성 변경 시 개체가 다시 생성되는 경우 개체가 이미 실행된 호출을 다시 해야 할 수 있으므로 리소스가 낭비된다.

UI 컨트롤러는 UI 데이터를 표시하거나, 사용자 작업에 반응하거나, 권한 요청과 같은 운영체제 커뮤니케이션을 처리하기 위한 것이다. 또한 UI 컨트롤러에 데이터베이스나 네트워크에서 데이터 로드를 담당하도록 요구하면 클래스가 팽창되며 테스트가 어려워진다.

UI 컨트롤러 로직에서 뷰 데이터 소유권을 분리하는 방법이 훨씬 더 쉽고 효율적이다.



## ViewModel 구현

AAC는 UI의 데이터 준비를 담당하는 UI 컨트롤러에 ViewModel 도우미 클래스를 제공한다. `ViewModel` 객체는 구성이 변경되는 동안 자동으로 보관되므로, 객체가 보유한 데이터는 액티비티 또는 프래그먼트 인스턴스에서 즉시 사용할 수 있다.

예를 들어 앱에서 사용자 목록을 표시해야 한다면, 사용자 목록을 액티비티나 프래그먼트가 아닌 `ViewModel` 에 보관하도록 책임을 할당해야 한다.

~~~kotlin
class MyViewModel: ViewModel() {
  private val users: MutableLiveData<List<User>> by lazy {
    MutableLiveData().also {
      loadUsers()
    }
  }
  fun getUsers(): LiveData<List<User>> {
    return users
  }
  private fun loadUsers(){
    // Do an asynchronous operation to fetch users.
  }
}
~~~

액티비티에서 데이터에 액세스

~~~kotlin
class MyActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceStated: Bundle?){
    val model: MyViewModel by viewModels()
    model.getUsers().observe(this, Observer<List<User>>{ users ->
      // update UI
    })
  }
}
~~~

액티비티가 다시 생성되면 첫 번째 활동에서 생성된 동일한 `MyViewModel` 인스턴스를 받는다. Owner activity가 완료되면 프레임워크는 리소스를 정리할 수 있도록 `ViewModel` 객체의 `onCleared()` 메서드를 호출한다.

`ViewModel` 객체는 뷰 또는 `LifecycleOwners` 의 특정 인스턴스화보다 오래 지속되도록 설계되었다. 이로 인해 뷰 및 `Lifecycle`  객체에 관해 알지 못할 때도 ViewModel을 다루는 테스트를 더 쉽게 작성할 수 있다. ViewModel 객체에는 `LiveData` 객체와 같은 `LifecycleObservers` 가 포함될 수 있다. 그러나 `ViewModel` 객체는 `LiveData` 객체와 같이 생명 주기를 인식하는 Observavle의 변경사항을 관찰해서는 안된다. 예를 들어 ViewModel은 시스템 서비스를 찾는 데 Application 컨텍스트가 필요하면 `AndroidViewModel` 클래스를 확장하고 생성자에 `Application` 을 받는 생성자를 포함할 수 있다.

## ViewModel의 생명 주기

ViewModel 객체의 범위는 ViewModel을 가져올 때 `ViewModelProvider`에 전달되는 `Lifecycle` 로 지정된다.

![](https://developer.android.com/images/topic/libraries/architecture/viewmodel-lifecycle.png)

## 프래그먼트 간 데이터 공유

액티비티에 속한 둘 이상의 프래그먼트가 서로 커뮤니케이션해야 하는 일은 매우 일반적이다. 사용자가 목록에서 항목을 선택하는 프래그먼트와 선택된 항목의 콘텐츠를 표시하는 또 다른 프래그먼트가 있다고 가정해보자.

~~~kotlin
class SharedViewModel: ViewModel() {
  val selected = MutableLiveData<Item>()
  fun select(item: Item){
    selected.value = item
  }
}
class MasterFragment: Fragment() {
  private lateinit var itemSelector: Selector
  private val model: SharedViewModel by activityViewModels()
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?){
    super.onViewCreated(view, savedInstanceState)
    itemSelector.setOnClickListener { item -> 
       // Update UI
    }
  }
}
class DetailFragment: Fragment(){
  private val model: SharedViewModel by activityViewModels()
  
  override fun onViewCreated(view:View, savedInstanceState:Bundle?){
    super.onViewCreated(view, savedInstanceState)
    model.selected.observe(viewLifecycleOwner, Observer<Iten> { item ->
    	 // Updtate UI                                                      	
    })
  }
}
~~~

두 프래그먼트는 모두 자신이 포함된 활동을 검색한다. 그러면 각 프래그먼트는 `ViewModelProvider` 를 가져올 때 이 활동으로 범위가 지정된 동일한 SharedViewModel 인스턴스를 받는다

- 액티비티는 아무것도 하지 않아도 되거나, 이 커뮤니케이션에 관해 어떤 것도 알 필요가 없다.
- 프래그먼트는 SharedViewModel 계약 외에 서로 알 필요가 없다. 프래그먼트 중 하나가 사라져도 다른 프래그먼트는 계속 평소대로 작동한다.
- 각 프래그먼트는 자체 수명 주기가 있으며, 다른 프래그먼트 수명 주기가 영향을 받지 않는다. 한 프래그먼트가 다른 프래그먼트를 대체해도, UI는 아무 문제 없이 계속 작동한다.

## ViewModel로 로더 대체하기

`CursorLoader` 와 같은 로더 클래스는 앱 UI의 데이터와 데이터베이스 간의 동기화를 유지하는데 자주 사용된다. ViewModel을 몇가지 클래스와 함께 사용하여 로더를 대체할 수 있다.

ViewModel은 Room 및 LiveData와 함께 작업하여 로더를 대체한다. ViewModel은 기기 구성이 변경되어도 데이터가 유지되도록 보장한다. 데이터베이스가 변경되면 Room에서 LiveData에 변경을 알리고, 알림을 받은 LiveData는 수정된 데이터로 UI를 업데이트한다.