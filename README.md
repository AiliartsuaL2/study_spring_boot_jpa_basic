# study_spring_boot_jpa_basic
인프런 김영한 '자바 ORM 표준 JPA 프로그래밍 기본편' 라이브 코딩 및 필기

자바 ORM 표준 JPA 프로그래밍

SQL 중심적인 개발의 문제점
- 애플리케이션의 개발은 보통 객체지향의 언어로 개발 진행, 데이터베이스는 관계형 DB사용
    - CRUD를 계속 반복한다… >> 자바 객체를 SQL로, SQL을 자바 객체로 받아야됨
- 패러다임의 불일치 : 객체와 관계형 베이스의 차이
    - 객체의 상속은 DB의 슈퍼타입 서브타입 관계로 생각 해볼 수 있다.
        - 데이터 삽입시 하위타입의 객체를 넣어주려면 쿼리를 슈퍼타입과 서브타입 두개에 다 넣어줘야함,,
        -  데이터 조회시 조인해서 객체로 매핑하고 매핑하고.. 케이스마다 조인쿼리를 다 만들어야함,, 매우 복잡
    - 연관관계
        - 객체는 참조를 사용하고 테이블은 외래 키를 사용한다. >> 객체를 테이블에 맞추어 저장한다.
    - 객체 그래프 탐색
        - 객체는 자유롭게 객체 그래프를 탐색 할 수 있어야 하지만, 처음 실행하는 SQL에 따라 달라진다 >> 엔티티에 대한 신뢰도가 떨어짐
        - 계층형 아키텍쳐(컨트롤러 서비스 레포지토리)는 다른 계층에 대한 신뢰가 있어야 사용 할 수 있음..
    - 비교하기
        - SQL에서 꺼내온 객체 2개를 비교하면 두 객체는 서로 다름
        - 컬렉션에서 꺼내온 객체 2개를 비교하면 두 객체는 서로 같음,,
- 객체답게 모델링 할 수록 매핑 작업만 늘어남…. 
- >> 자바 컬렉션에 저장하듯 데이터를 저장하는 방법.. JPA!

JPA란 : JavaPersistenceAPI,, 자바 진영의 ORM 기술 표준
- ORM : Object-Relational(관계형DB) Mapping
    - 객체는 객체대로 설계하고 , 관계형 DB는 관계형 DB대로 설계.
    - ORM 프레임워크가 중간에서 매핑하여 패러다임의 불일치등을 해결해줌
    - 대중적인 언어에는 대부분 ORM 기술이 존재
- 애플리케이션과 JDBC 사이에서 동작함.
- JPA의 동작
    - 저장 : JPA에게 저장 호출시 JPA가 Entity 분석하여 InsertSQL 생성, JDBC API 사용하여 패러다임 불일치를 해결함
    - 조회 : JPA에게 조회 호출시 : select쿼리 만들어서 JDBC API생성, ResultSet 매핑, 패러다임 불일치(상속이나 문제점들) 해결
- EJB 중 엔티티 빈이라는 자바 표준 ORM이 있었는데, 실용성이 떨어져서 어떤 개발자가 하이버 네이트라는 오픈소스를 만듬 > > Java에서 영입해서 하이버네이트 개발자를 데려와서 하이버네이트랑 거의 똑같이 해서 만듬 
    - 오픈소스로부터 시작했기 때문에 실용적이다
    - JPA는 인터페이스의 모음, 대표적인 구현체 3가지
        - 구현체 1. 하이버네이트 80~90% 이상
        - 구현제 2. EclipseLink
        - 구현체 3. 
    - JPA를 사용해야하는 이유
        - SQL 중심적인 개발에서 객체 중심으로 개발 >> 패러다임 불일치 해결, 생산성 ,유지보수 향상,,
        - 동일한 트랜잭션에서 조회한 엔티티는 같음을 보장
        - 성능 최적화 기능 지원
            - 중간에 매개가 되는 기술이 추가가되면 캐싱(이미 조회했던 것은 매개에서 보내줌), 버퍼라이팅(모아서 한번에 보내줌)이 가능, 
            - 1차 캐시와 동일성 보장 : 같은 트랜잭션 안에서는 같은 엔티티 반환 > 약간의 조회 성능 향상
            - 트랜잭션을 지원하는 쓰기 지연 (버퍼 라이팅)
                - Insert : 트랜잭션을 커밋 할 때까지 Insert SQL을 모으고 한 번에 SQL 전송 > 네트워크 통신 비용 감소
                - Update ,, 있음
            - 지연 로딩과 즉시 로딩
                - 지연 로딩(Lazy) : 객체가 실제 사용 될 때 로딩
                - 즉시 로딩(Eager) : Join SQL로 한 번에 연관된 객체까지 미리 조회
- 하이버네이트 설정
    - dialect : JPA는 특정 데이터베이스에 종속 X,,
        - 각각의 DB가 제공하는 SQL 문법과 함수는 조금씩 다름, (VARCHAR, VARCHAR2 등)
- JPA 구동 방식 : Persistence가 설정 정보(/META-INF/persistence.xml의 unitName)를 조회하여 EntityManagerFactory 클래스를 생성하고, EMF 클래스가 EntityManager를 생성
JPA 생성 
- DB 테이블에 맞는 엔티티 생성
    - 도메인을 만들고, 클래스레벨에 @Entity를 붙여줌.
- 엔티티 매니저 팩토리는 로딩 시점에 딱 1개만 만들어야함
    - 웹서버 올라갈 시점에 DB당 1개만 생성!
- 실제 DB에 저장하는 트랜잭션(DB커넥션 하나를 얻어서 저장하고 종료시키는) 단위 하나당 엔티티 매니저 하나를 꼭 생성해줘야함 
    - 엔티티 매니저는 트랜잭션 사이클 내에서 작동이 되기 때문에 트랜잭션을 실행시키고 꺼주는 프로세스가 필요함
    - 엔티티 매니저는 한 번만 사용해야하고 쓰레드간 공유를 하면 절대 안됨,, 사용하고 버려야함
    - JPA의 모든 데이터 변경은 트랜잭션 안에서 실행. (조회는 트랜잭션 없어도 괜찮다.)
- Update : JPA로 조회를 하게되면 JPA가 관리를하고, 트랜잭션이 끝나는 시점에 값을 비교 후 변경되었으면 자동으로 Update 쿼리를 날림 >> java에서 collection 처럼 다룸  
- Select : 조건 검색을 위해선 JPQL을 통해서 조회
- JPQL : JPA에서 코드를 짤 때 테이블을 대상으로 쿼리를 짜지않고, 엔티티를 기준으로 쿼리를 짬 (from Member << 테이블이 아닌 객체)  
    - ANSI에서 제공하는건 다 가능,
    - 모든 DB 데이터를 객체로 변환하는건 불가능,, 결국엔 검색조건의 SQL이 필요 (JPQL의 필요성)
    - SQL을 추상화시킨 객체 지향 쿼리 언어
JPA는  내부적으로 리플렉션을 쓰기 때문에 동적으로 객체를 생성함 >>도메인의 기본생성자가 필요함

영속성 관리 - 내부 동작 방식
- 객체와 관계형 DB 매핑하기
- 영속성 컨텍스트: 엔티티를 영구 저장하는 환경
    - 엔티티매니저 팩토리가 엔티티 매니저를 생성해서 요청을 수행함, 엔티티 매니저는 DB 커넥션풀을 잡고 실행
    - 논리적인 개념이고 눈에 보이지 않는다. 엔티티 매니저를 통해 영속성 컨텍스트에 접근함.
    - 엔티티의 생명주기
        - 비영속 (new, transient)
            - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
            - 객체를 생성한 상태(JPA와 관계 없는 상태)
        - 영속 (managed)
            - 영속성 컨텍스트에 관리되는 상태
            - 영속성 콘텍스트(엔티티 매니저)안에 객체가 들어가있는 경우 (persist 쓴 상태)
        - 준영속 (detached)
            - 영속성 컨텍스트에 저장되었다가 분리된 상태
        - 삭제
            - 삭제된 상태
    - 영속성 컨텍스트의 이점 ,, 어플리케이션과 DB사이의 중간 매개체가 있어서 얻는 이점,
        - 1차 캐시
            - 맵으로 되어있고, 키가 @Id로 지정한 컬럼 , 값은 등록한 Entity
            - DB가 아닌 1차 캐시를 먼저 확인하고 해당 캐시에 해당하는 값을 준다. 없으면 DB에서 가져와서 1차캐시에 저장하고 반환해줌
            - DB 트랜잭션 안에서만 작용하기때문에 성능에 큰 영향을 주진 않음
        - 영속 엔티티의 동일성 보장
            - 영속성에서 조회해온 id값이 같은 엔티티의 경우 인스턴스가 동일함,(==)
        - 트랜잭션을 지원하는 쓰기 지연
            - 쿼리를 데이터 베이스에 보내지않고 있다가 commit하는 순간 트랜잭션을 처리함 (flush)
            - 쓰기지연 SQL 저장소에 저장해 뒀다가 한 번에 처리함
            - hibernate.jdbc.batch_size 를 설정하면 설정한 값만큼 쿼리를 모아서 한 번에 DB에 날림,, 버퍼링처럼
        - 엔티티 수정(변경감지, dirty Checking)
            - 영속상태에 들어간 이후, 값을 변경하고서 persist를 안해줘도 자바 컬렉션처럼 자동으로 업데이트 쿼리를 날림
            - JPA는 값을 최초에 읽어온 시점에 스냅샷으로 저장해두고 commit 시점에 엔티티와 스냅샷을 비교한다. 값이 다를경우 update 쿼리를 날림.
            
- 플러시
    - 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영하는 것 , 영속성 컨텍스트의 변경사항과 데이터베이스를 맞추는 작업
    - 플러시 발생 (DB 커밋)
        - 변경 감지 (더티 체킹)
        - 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
        - 쓰기지연 SQL 저장소의 쿼리를 DB에 전송
    - 영속성 컨텍스트 플러시 하는 방법
        - em.flush() 
            - 강제로 플러시 처리, 플러시 처리가 즉시 일어남, 1차 캐시는 유지가 된다
        - 트랜잭션 커밋
        - JPQL 쿼리 실행
            - JPQL 실행 전 쌓여있던 쿼리들을 무조건 flush함,,
    - 플러시란 영속성 컨텍스트를 비우는게 아니고, 변경내용을 DB에 동기화하는 것,
    - 트랜잭션이라는 작업 단위가 중요함,, 커밋 직전에만 동기화 시키면 된다,
- 준영속 상태
    - 영속 상태의 엔티티가 영속성 컨텍스트에서 분리됨(detached)
    - 영속성 컨텍스트가 제공하는 기능을 사용 못 함
    - 준영속 상태로 만드는 방법
        - em.detach(entity) : 특정 엔티티만 준영속 상태로 전환
        - em.clear() : 영속성 컨텍스트를 완전히 초기화
        - em.close() : 영속성 컨텍스트를 종료

엔티티 매핑
- 객체와 테이블 매핑
    - @Entity
        - 위 어노테이션이 붙은 클래스를 엔티티라고 하고, JPA가 관리한다.
        - JPA를 사용해서 테이블과 매핑할 클래스는 @Entity가 필수이다.
        - 주의점
            - 기본 생성자가 필수이다(파라미터가 없는 public 또는 protected 생성자)
            - final 클래스 ,enum,interface,inner 클래스는 못쓴다.
            - db에 저장할 필드에 final 사용 금지
        - 속성
            - name : 엔티티 이름을 지정하는데, 기본값은 클래스 이름을 그대로 사용,, 같은 클래스 이름이 없으면 가급적 기본값을 사용(아니면 \너무 헷갈림)
    - @Table
        - 엔티티와 매핑할 테이블 지정
        - 속성 
            - name : DB 테이블의 이름을 지정한다.
            - catalog : 데이터베이스 catalog 매핑
            - schema : 데이터베이스 스키마 매핑
            - uniqueConstraints : DDL 생성시 유니크 제약 조건 생성
- 데이터베이스 스키마 자동 생성
    - DDL을 애플리케이션 실행 시점에 자동 생성한다. (운영 x 개발단계에서 사용)
    - DB 방언을 활용하여 DB에 맞는 적절한 DDL 생성해줌, (꼭 개발에서 사용)
    - 속성 (persistence.xml에서 hibernate.hbm2ddl.auto value=“????”)
        - create : 기존 테이블 삭제 후 다시 생성, (DROP-CREATE)
        - create-drop : create와 같으나 애플리케이션이 끝날때 drop 시킴
        - update : 변경 부분만 반영,,alter table등 ..(운영 DB에서 사용 금지)
        - validate : 엔티티와 테이블이 정상 매핑이 되었을때만 사용
        - none : 안쓸경우에,, (이런 속성 없는데 관례상 none이라고 함)
    - 운영 장비에는 절대 create, create-drop, update 사용하면 안된다..
        - 개발 초기에는 create 또는 update 사용
        - 테스트 서버는 update 또는 validate
        - 스테이징과 운영 서버는 validate 또는 none
    - DDL 생성 기능
        - unique, nullable, length 등,, 속성 추가,, DDL을 자동 생성 할 때만 사용되고 JPA 실행 로직에는 영향을 주지 않음
- 필드와 컬럼 매핑
    - @Column : 해당 엔티티와 매핑할 DB의 컬럼명
        - 속성 
            - name : 필드와 매핑 할 테이블의 컬럼 이름
            - insertable, updatable : 등록, 변경 가능 여부, 기본값 True
            - nullable(DDL) : 널 여부 체크,, 기본값 true,, false 시 NotNull
            - unique(DDL) : 유니크 제약조건 만들어줌,, 잘 안쓴다. (제약조건 이름이 랜덤이라 이름을 못 알아보기 때문에 @Table에서 씀)
            - columnDefinition : 데이터베이스 컬럼 정보를 직접 줄 수 있다. “varchar(100) default ‘EMPTY’”등
            - length(DDL) : 문자 길이 제약조건,, String 타입에만 사용
            - precision, scale(DDL) : BigInteger이나BicDecimal 타입에서 사용,, precision은 소수점 포함 전체 자리수, scale은 소수 자리수   
    - @Enumerated : enum타입을 매핑하기위한 ,, enumType : STRING 써야함
        - 주의사항 : enumType은 ORDINAL이 기본인데, 이렇게되면 enum 순서를(숫자) DB에 저장하고, STRING을 쓰면 이름(문자)을 순서로 DB에 저장 
            - ORDINAL이라면 요구사항이 변경되면 해당 데이터가 뭘 했던건지 모름
    - @Temporal : 날짜를 매핑하기위한 어노테이션
        - DATE :  DB의 날짜 타입
        - TIME : DB의 시간 타입
        - TIMESTAMP : DB의 날짜+시간타입
        - 자바 8부터는 LocalDate, LocalDateTime을 통해 생략 가능
    - @Lob : BLOB, CLOB,, 
        - 지정하는 속성이 없음, 필드 타입이 문자면 CLOB, 나머지는 BLOB 매핑
    - @Transient : 매핑 하고싶지 않은 컬럼 
- 기본 키 매핑
    - @Id
- 연관관계 매핑
    - 객체지향적인 ORM 사용을 위해 엔티티들간 연관관계를 맺는 방법
    - 객체의 참조와 테이블의 외래 키를 매핑
        - 단방향 다대일
            - @ManyToOne (해당 엔티티 입장에서 연결하는 엔티티와의 관계)
            - @JoinColumn(조인하는 컬럼명 입력)
        - 양방향 연관관계와 연관관계의 주인
            - 양쪽으로 참조 할 수 있는 관계
            - 테이블 연관관계는 방향이란 의미가 없고 폴인키로 양쪽 다 참조가 가능함
            - 객체의 경우, 양방향을 참조하기 위해선 두 쪽에 다 세팅을 해주어야 함. 
                - 연관관계의 주인쪽에 List를 두고, 관례상 ArrayList로 초기화시킴
                - OneToMany로 설정해주고 속성에 mappedBy = 변수명으로 선언,,
                - mappedBy : 객체와 테이블간 연관관계를 맺는 차이를 이해,,
                    - 테이블 양방향 연관관계 : 폴인키를 통해 양방향 연관관계가 생김
                    - 객체의 양방향 연관관계 : 사실 양방향 관계가 아닌, 단방향 연관관계가 2개가 있는것,(서로 보고있음,, 양방향이라고 억지로 참조시킴)
                    - 테이블은 폴인키 하나로 연관관계를 설정하고, 객체는 두개로 설정하기 때문에 DB의 폴인키를 객체의 뭘로 업데이트 할 지를 하나로 결정해서 외래키 관리를 해야함, 연관관계의 주인을 정해서 설정.
                    - 연관관계의 주인만이 외래 키를 관리(등록, 수정) 하고, 주인이 아닌 쪽은 읽기만 가능. 주인이 아닌쪽이 mappedBy 사용하여 주인을 지정한다.
                    - 외래키가 있는 엔티티(일대 다 중 다쪽)를 연관관계의 주인으로 설정한다.
                    - 값을 변경할때에는 주인쪽에만 설정, 조회는 양 쪽 다 가능,
            - 양방향 매핑시 주의점
                - 주인이 아닌쪽에만 데이터를 입력하고 연관관계의 주인에 값을 입력하지 않은경우 DB에 값이 들어가지 않음
                    - 주인이 아닌쪽은 읽기 전용이고, 주인인쪽이 실질적으로 외래키를 담당하기 때문에 수정,등록하는데에는 주인에 값을 넣어줘야함
                    - 객체지향적으로 생각해보면 주인쪽에만 데이터를 넣는게 아닌 양쪽에 다 데이터를 넣어주는게 맞음 
                        - 영속성 컨텍스트에 등록을 시켜줘야 select 없이 나오기 때문에
                        - 테스트코드 작성시에도 DB없이 순수 자바코드로만(순수 객체만 조회) 테스트하기 때문에, 양쪽에 세팅을 해줘야함,  
                        - 이렇게하면 빼먹을 수 있기 때문에 연관관계 편의 메서드를 생성한다.
                        - >> changeTeam같이, 메서드를 작성해서 해당 메서드로 주인을 가져올 때 주인 아닌 매핑된 엔티티에도 add로 추가를 해준다.
                    - 무한루프를 조심해야함. toString(), lombok, Json 생성 라이브러리에서 ,,
                        - 컨트롤러에서 Entity를 반환하면 JSON에서 무한루프 걸림, API ,, DTO로 반환하기,
            - 양방향 매핑 정리
                - 단방향 매핑만으로도 이미 연관관계 매핑은 완료됨, 처음 설계시에는 양방향으로 설계하지 말 것
                - 양방향 매핑은 반대방향으로 조회(객체 그래프 탐색) 기능이 추가 된 것 뿐,,
                - JPQL에서 역방향으로 탐색 할 일이 많기 때문에, 단방향 매핑을 잘 해두고, 필요 할 때 양방향을 추가시킨다.(테이블에 영향을 주지않음)
        - 객체는 가급적이면 단방향이 편하고 좋음,,
    - 연관관계의 주인은 비즈니스 로직이 아닌 외래 키의 위치를 기준으로 정해야 한다.
    
    다양한 연관관계 매핑
- 연관관계 매핑시 고려사항
    - 다중성
    - 단방향, 양방향
        - 테이블은 방향이라는 개념이 없음, 외래키만 있으면 양쪽 조인이 가능하기 때문에
        - 객체는 참조용 필드가 있는 쪽으로만 참조가 가능, 한쪽만 참조하면 단방향, 양쪽이 서로 참조하면 양방향
    - 연관관계의 주인
        - 양방향인경우 테이블은 외래키 하나로 두 테이블이 연관관계를 맺고, 객체는 참조가 2군데이기 때문에, 객체에서 외래키 관리 할 곳을 지정해주어야함.
        - 외래키를 관리하는 참조가 연관관계의 주인. 주인의 반대편은 외래키에 영향을 주지 않고, 단순 조회만 가능 
- 다대일 [N:1] : @ManyToOne, @JoinColumn // 양방향시 반대쪽에 @OneToMany(mappedBy=“상대쪽에서 조인컬럼한 필드명”)
    - 다쪽에 외래키가 있는 DB 특성상 객체는 외래키를 참조로 두고 단방향으로 연관관계 매핑을 해준다.
    - 양방향 할 경우, 주인 반대쪽 객체에 필드만 추가만 해주면 된다. (단순 조회만 하기 때문에 테이블에 영향 없음, OneToMany(mappedBy) 추가)
- 일대다 [1:N] : @OneToMany, @JoinColumn // 양방향시 반대쪽에 @ManyToOne(mappedBy=“상대쪽에서 조인컬럼한 필드명”)
    - 일대 다 단방향은 DB 설계상 권장하지않음, 실무에서 거의 안씀
    - 일쪽에 값을 둬도, 어차피 DB 설계 상 다쪽에 외래키가 있기 때문에 다쪽을 업데이트 쳐줘야함,, 굳이 쿼리를 한 번 더 나가야함
    - 유지보수 할 때 추적을 하기 힘듬
    - @JoinColumn을 꼭 사용해야 함, 그렇지 않은경우 조인 테이블 방식을 사용함
    - 따라서 일대다 단방향 매핑보다는 참조를 하여  객체적인 손해보더라도 다대일 양방향 매핑을 사용하자.
    - 일대다 양방향을 하고싶으면, 다쪽에 @JoinColumn을 하고 insertable = false, updatable = false로 두어 읽기 전용으로 씀,, 굳이..??
- 일대일 [1:1] : @OneToOne, @JoinCoulmn // 양방향인경우 @OneToOne(mappedBy=“상대쪽에서 조인컬럼한 필드명”)
    - 주 테이블이나 대상 테이블 중 외래 키 선택 가능,(상관없음)
        - 주 테이블에 외래키가 들어가있는 단방향
            - 다대일 단방향 매핑과 유사, 어노테이션만 달라짐
        - 주 테이블에 외래키가 있는 양방향
            - 다대일처럼 연관관계 주인을 주테이블에 하고 , 반대 쪽에 mappedBy
        - 대상 테이블에 외래키가 있는 단방향
            - 안된다. JPA 지원 x,
        - 대상 테이블에 외래키가 있는 양방향
            - 대상 테이블에 외래키를 두면 됨;; 어차피 1:1이니까 상관없음
            - DBA 관점에서 1:1은 DB 모델링 할 때, 비즈니스 로직이 변경 될 가능성을 잡고 모델링을 해주는게 좋음,,
                - 프록시 기능의 한계로 지연 로딩으로 설정시켜도 항상 즉시 로딩됨,
            - JPA 개발자 관점에서 셀렉트를 많이하는쪽이 주인이 되는게 좋음.
                - 주 테이블에 외래키를 둠,, 값이 없으면 외래키에 null 허용
    - 외래키에 데이터베이스 유니크 제약조건을 추가해야 가능하다.
- 다대다 [N:M] : @ManyToMany, @JoinTable
    - 실무에서 사용하면 안됨
        - 편리해 보이지만 실무에서 사용이 불가능, 쿼리가 뭐가 나가는지 모름,, 중간 테이블이 숨겨져있기 때문에 
        - 따라서 DB와 마찬가지로 중간 테이블을 만들어서 @ManyToOne, @OneToMany로 풀어나가야함,
        - A - AB - B 인경우 , A,B에 AB 컬렉션두고, @OneToMany(mappedBy=“필드명”) 설정, AB에는 A,B 필드 잡고 @ManyToOne, @JoinColumn(name=“컬럼명”)
    - 관계형 DB는 정규화 테이블 2개로 다대다를 표현 불가, 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야 한다.
    - 객체는 컬렉션을 사용하여 다대다가 가능함

고급 매핑
- 상속 관계 매핑 
    - 관계형 DB에는 상속관계가 없으나, 슈퍼타입 서브타입 관계가 객체의 상속과 유사하여 서로 매핑 할 수 있다.
    - DB에 상속관계 구현 방법(슈퍼타입, 서브타입)
        - 부모 클래스에서 @DiscriminatorColumn 을설정 (name=“원하는 컬럼명”) DTYPE이 default고, 관례상 DTYPE 많이 씀
        - 각각 테이블로 변환 : 조인전략 (JOINED)
            - 데이터 입력시 각각 테이블에 insert를 해주고, 조회시 join해서 같이 조회하는것 
            - 해당 DTYPE 값을 변경하고싶으면, 자식 클래스에서 DiscriminatorValue(“값 설정 이름”)을 통해 변경 할 수 있음
        - 통합 테이블로 변환 : 단일 테이블 전략(SINGLE_TABLE) -> JPA 기본 전략
            - 논리 모델을 상위 클래스인 한테이블에 합치고 DTYPE 컬럼을 생성해서 구분하는것,
            - 상위 테이블에 하위 클래스의 컬럼들이 전부 들어가고, DTYPE을 통해 구분을 지어짐, 하위 클래스와 상관없는 값들은 null로 설정이 됨,
            - 테이블 하나이기 때문에 성능이 제일 좋음, insert 한 번만, join도 안함
            - DiscriminatorColumn이 없어도 DTYPE이 자동으로 생성됨, 있는게 운영상 항상 좋음,,!!
        - 서브 타입 테이블로 변환 : 구현 클래스마다 테이블 전략(TABLE_PER_CLASS)
            - 상위테이블이 안만들어지고, 각각 테이블을 상위 테이블의 컬럼을 중복해서 구현하기
            - 테이블 자체가 다르기 때문에 DiscriminatorColumn을 안씀,
            - 데이터 조회시 상위 클래스로 조회하면 , union으로 상위 클래스를 구현하는 클래스들을 전부 확인해본다.. >> 굉장히 비효율적
    - 상위 클래스 레벨에 @Inheritance(strategy = InheritanceType.XXX)로 선언해서 전략 선택가능
        - 조인 전략 // 정석,, 깔끔한데 조회가 좀 복잡함 
            - 장점
                - 테이블이 정규화가 잘 되어있음
                - 외래키 참조 무결성 제약조건 활용 가능,,
                - 저장공간 효율화(정규화 이점)
            - 단점
                - 조회시 조인을 많이 사용 >> 애플리케이션 성능 저하 (실질적으로 큰 저하는 없음 )
                - 조회 쿼리가 복잡함!!
                - 데이터 저장시 InsertQuery 2번 호출 >> 생각보다 성능 저하가 별로 없음
        - 단일 테이블 전략
            - 장점
                - 조인이 필요 없어 조회 성능이 빠름
                - 조회 쿼리가 단순
            - 단점
                - 자식 엔티티가 매핑한 컬럼은 모두 null을 허용
                - 단일 테이블에 모든것을 저장하므로 테이블이 커질 수 있어 상황에 따라 조회 성능이 오히려 느려질 수 있음 (거의 이럴 일 없음)
        - 구현 클래스마다 테이블 전략 >> 쓰지말아라 !
            - 장점
                - 서브 타입을 명확하게 정의가능,,
                - not null 제약조건 사용 가능
            - 단점
                - 여러 자식 테이블을 함께 조회시 성능이 느림 (Union)
                - 자식 테이블을 통합해서 쿼리하기 어렵다
    - 비즈니스 전략으로 어렵고 복잡하면 조인전략을 선택하고, 정말 단순하고 데이터가 얼마 없고 확장 할 일이 없으면 단일테이블로 선택
- @MappedSuperclass
    - 목적 : 공통 매핑 정보가 필요할 때 사용,,
        - DB에서는 따로 사용하되, 객체에서는 속성이 너무 중복되어 공통 매핑정보가 필요한 경우 사용
    - ex) 모든 엔티티에서 누가 언제 수정했고, 누가 언제 등록했다 라는 정보가 필요한 상황이면
        - 클래스를 하나 만들어서 공통 속성을 넣어주고, 만든 클래스에 @MappedSuperclass 어노테이션 추가 후 해당 클래스를 상속시킨다
    - 엔티티가 아니라서 테이블과 매핑이 안되고, 상속받는 클래스에 매핑 정보만 제공
    - 직접 생성하여 사용 할 일이 없으므로 추상 클래스 권장
    - @Entity 클래스는 같은 Entity나 @MappedSuperclass로 지정한 클래스만 상속이 가능하다.
- 실무에서 상속관계로 쓸 수도 있지만, 애플리케이션이 커지면 테이블을 단순하게 유지하는게 편하기 때문에,, 적절한 트레이드 오프를 해야함

프록시와 연관관계 정리
- 프록시
    - em.find() : 데이터베이스를 통해 실제 엔티티 객체를 조회
    - em.getReference() : 데이터 베이스 조회를 미루고 가짜(프록시) 객체를 조회
        - 객체의 사용이 없을때까지는 쿼리를 날리지 않고, 실제 사용되는 시점에 쿼리를 날려 DB에서 조회를 해옴
    - 실제 클래스를 상속받아서 만들어지고, 실제 객체의 참조(target)를 보관한다.
    - 사용자 입장에서 진짜 객체인지 프록시 객체인지 구분하지않고 사용하면 된다.(이론상)
    - 프록시 로직
        - 1. get 메서드를 통해 프록시 필드 접근시, 영속성 컨텍스트를 통해 데이터 요청
        - 2. 데이터가 없으면 DB조회(쿼리)하여 실제 엔티티를 생성
        - 3. 프록시 필드에 데이터를 넣음
    - 프록시 특징
        - 처음 사용시 딱 한 번만 초기화된다.
        - 프록시 객체를 초기화 할 때 프록시 객체가 실제 엔티티로 바뀌는게 아니라 초기화되면 프록시 객체를 통해 실제 엔티티에 접근이 가능 한 것.
            - 프록시는 유지되고 내부 타겟에 데이터를 넣는 것.
        - 프록시는 원본 엔티티를 상속받아서 타입 체크시 주의해야함, (== 비교는 안되고, instanceof를 사용해야함)
            - 타입 비교시, ==는 상속관계는 안되고 정확하게 같은 타입으로 보기 때문에, 상속을 허용해주는 instanceof를 사용해서 비교, 
        - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면, em.getReference()를 호출해도 실제 엔티티를 반환한다.
            - JPA에서는 한 트랜잭션 내에서 같은 id값으로 조회했을 경우, 객체가 동일함을 보장하기 때문에, 프록시로 조회하면 ==이 true가 안되기에 reference로 조회해도 이미 해당 엔티티가 영속성 컨텍스트에 있다면 프록시가 아닌 엔티티로 반환해줌
        - 같은 이유로 프록시로 조회를 했던 엔티티의 경우, find로 조회해도 프록시로 나오게끔 처리함.
        - 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태에서 프록시를 초기화(영속성 콘텍스트로 조회)하면 LazyInitializationException 발생    
    - 프록시 확인 메서드
        - 프록시 인스턴스 초기화 여부 : emf.PersistenceUnitUtil.isLoaded().isLoaded(프록시객체) 넣으면 초기화 여부를 boolean으로 리턴
        - 프록시 클래스 확인 방법 : 프록시객체.getClass().getName() 하면 확인 가능
        - 프록시 강제 초기화 - Hibernate로 ,,JPA 표준은 강제 초기화가 없음,, : Hibernate.initialize(프록시 객체);
- 즉시 로딩과 지연 로딩
    - 연관되어있는 엔티티를 같이 가져오고 싶은 경우와, 필요없는경우가 비즈니스 로직적으로 생길 수 있는데, 만약 필요 없는경우에 연관된 엔티티를 같이 가져오면 손해가 생김,, 이런경우 지연 로딩을 통해 해결 가능
    - 지연로딩 (FetchType.LAZY)
        - 연관관계 등록된 엔티티에 @ManyToOne(fetch=FetchType.LAZY) 를 처리하면, 본인 엔티티를 로딩해서 가져올 때 해당 엔티티는 조회를 안하고 프록시 객체로 등록한다.
        - 프록시 객체의 실제 값을 사용 할 때 쿼리가 나감(프록시 초기화). 
    - 즉시 로딩(FetchType.EAGER),, default로 설정되어있는 옵션이다.
        - 한 번에 연관되어있는 엔티티까지 join 해서 전부 가져옴
    - 가급적 지연 로딩만 사용,, 즉시 로딩 적용시 예상하지 못한 SQL이 발생한다.
    - 즉시 로딩은 JPQL에서 N+1 문제가 발생
- 지연로딩 활용
- 영속성 전이 : CASCADE
- 고아 객체
- 영속성 전이 + 고아 객체, 생명주기
