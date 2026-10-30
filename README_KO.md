# IceToSnowLOD (실험용 Fabric 애드온 소스)

## 기능 목표
Distant Horizons 3.3.3의 공개 API 이벤트를 이용해 다음 얼음 계열 블록의 LOD용 재질을 `SNOW`로 지정하고, 색상 오버라이드를 눈 색상으로 설정합니다.

- `minecraft:ice`
- `minecraft:packed_ice`
- `minecraft:blue_ice`
- `minecraft:frosted_ice`

일반 월드의 실제 블록 상태(BlockState)를 변경하는 코드는 없습니다.

## 확인한 API
제공된 `DistantHorizons-3.3.3-26.2-fabric-neoforge.jar` 내부에서 아래 API와 메서드가 실제로 존재하는 것을 바이트코드 시그니처로 확인했습니다.

- `DhApiEventRegister.on(...)`
- `DhApiBlockStateWrapperCreatedEvent`
- `DhApiBlockStateWrapperCreatedEvent.EventParam#setBlockMaterial(...)`
- `DhApiBlockStateWrapperCreatedEvent.EventParam#setOpacity(...)`
- `DhApiBlockStateWrapperCreatedEvent.EventParam#setAllowApiColorOverride(...)`
- `DhApiBlockColorOverrideEvent.EventParam#setColor(...)`
- `EDhApiBlockMaterial.SNOW`

이는 API 시그니처 확인이며, 애드온이 실제 게임에서 의도대로 동작한다는 검증은 아닙니다.

## 중요한 한계
- 이 애드온은 DH의 LOD 재질 분류와 색상/불투명도 관련 입력값을 변경하도록 시도합니다. 눈 블록의 실제 텍스처나 지오메트리를 복제하는 코드는 아닙니다.
- 벽 뒤 얼음이 보이던 현상이 해결되는지, 얼음의 높이와 형태가 기대대로 유지되는지는 게임 테스트가 필요합니다.
- SeedGen 0.9.1과의 호환성은 아직 검증되지 않았습니다.
- 기존에 생성된 LOD 캐시는 재생성해야 변경이 반영될 수 있습니다.

## 빌드 환경
- JDK 25
- Gradle 및 인터넷 연결(최초 빌드 시 의존성 다운로드)
- Minecraft 26.2.0 / Fabric Loader 0.19.5
- Distant Horizons 3.3.3 (포함된 `libs/` JAR)

프로젝트 루트에서 `gradle build`를 실행하면 `build/libs/IceToSnowLOD-0.1.0.jar`가 생성되도록 구성했습니다.

## 현재 검증 상태
- DH 3.3.3 JAR 내 API 클래스/메서드 시그니처: 확인함
- Java 소스의 실제 컴파일: 미실행
- Minecraft 실행 및 렌더링 테스트: 미실행
- SeedGen 호환성 테스트: 미실행

따라서 이 ZIP은 **수정된 소스 프로젝트**이며, 설치 가능한 완성 JAR이 아닙니다. 이 환경에는 JDK 25와 Gradle이 없고 외부 다운로드도 불가능하여 여기서 빌드할 수 없었습니다.

## Gradle을 PC에 설치하지 않고 빌드하는 방법 (GitHub Actions)
1. GitHub에 로그인하고 새 빈 저장소(repository)를 만듭니다.
2. 이 프로젝트 폴더 안의 파일과 폴더 전체를 저장소에 업로드합니다. `.github/workflows/build.yml`도 포함되어야 합니다.
3. 저장소의 **Actions** 탭에서 **Build IceToSnowLOD** 워크플로를 선택합니다.
4. **Run workflow**를 눌러 실행합니다.
5. 실행이 끝나면 해당 실행 기록 아래 **Artifacts**에서 `IceToSnowLOD-jar`를 다운로드합니다.

주의: 온라인 빌드가 성공하더라도 실제 게임에서의 렌더링 동작이나 SeedGen 호환성까지 검증된 것은 아닙니다. 실패하면 Actions의 빌드 로그가 필요합니다.
