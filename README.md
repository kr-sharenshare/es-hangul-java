# es-hangul-java

[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

`es-hangul-java`는 [es-hangul](https://github.com/toss/es-hangul) TypeScript 라이브러리를 Java로 포팅한 한글 처리 유틸리티 라이브러리입니다. 한글 문자열 처리, 조사 붙이기, 숫자 변환, 발음 표준화 등 다양한 한글 관련 기능을 제공합니다.

## 주요 기능

- **한글 문자 처리**: 분해, 조합, 초성/중성/종성 추출
- **조사 처리**: 받침에 따라 적절한 조사 자동 선택
- **숫자 변환**: 숫자를 한글로 변환 (일반, 혼용, 금액, 순우리말 등)
- **발음 표준화**: 표준 발음 규칙에 따른 발음 변환
- **로마자 변환**: 한글을 로마자로 변환
- **키보드 변환**: QWERTY 키보드와 한글 자모 간 변환

## 요구사항

- Java 8 이상
- Gradle 또는 Maven

## 설치

### Gradle

```gradle
dependencies {
    implementation 'kr.sharenshare.utils:es-hangul-java:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>kr.sharenshare.utils</groupId>
    <artifactId>es-hangul-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 사용 예시

### 초성 추출 및 검색

```java
import kr.sharenshare.utils.hangul.Hangul;

String searchWord = "라면";
String userInput = "ㄹㅁ";

String result = Hangul.getChoseong(searchWord); // "ㄹㅁ"

// 검색어의 초성과 사용자 입력 초성이 일치하는지 확인
if (result.equals(userInput)) {
    // 매칭됨
}
```

### 조사 붙이기

```java
import kr.sharenshare.utils.hangul.Josa;
import kr.sharenshare.utils.hangul.Josa.JosaType;

String word1 = "사과";
String sentence1 = Josa.attach(word1, JosaType.EUL_REUL) + " 먹었습니다.";
System.out.println(sentence1); // "사과를 먹었습니다."

String word2 = "바나나";
String sentence2 = Josa.attach(word2, JosaType.I_GA) + " 맛있습니다.";
System.out.println(sentence2); // "바나나가 맛있습니다."

// 패턴 문자열 사용
String word3 = "책";
String sentence3 = Josa.attach(word3, "을/를") + " 읽었습니다.";
System.out.println(sentence3); // "책을 읽었습니다."
```

### 한글 분해 및 조합

```java
import kr.sharenshare.utils.hangul.Hangul;

// 한글 분해
String disassembled = Hangul.disassemble("값"); // "ㄱㅏㅂㅅ"
String groups = Hangul.disassembleToGroups("사과"); 
// [[ㅅ, ㅏ], [ㄱ, ㅗ, ㅏ]]

// 한글 조합
String assembled = Hangul.assembleString("ㅇㅏㅂㅓㅈㅣ"); // "아버지"
char combined = Hangul.combineCharacter('ㄱ', 'ㅏ', "ㅂㅅ"); // '값'
```

### 숫자 변환

```java
import kr.sharenshare.utils.hangul.NumberToHangul;

// 숫자를 한글로 변환
String hangul = NumberToHangul.numberToHangul(12345); 
// "일만이천삼백사십오"

String hangulWithSpacing = NumberToHangul.numberToHangul(12345, true); 
// "일만 이천삼백사십오"

// 숫자를 한글 혼용으로 변환
String mixed = NumberToHangul.numberToHangulMixed(12345); 
// "1만2345"

// 금액 변환
String amount = NumberToHangul.amountToHangul("15,201,100"); 
// "일천오백이십만천백"

// 순우리말 수사
String susa = NumberToHangul.susa(21); // "스물하나"
String susaDeterminer = NumberToHangul.susa(1, true); // "한"

// 날짜 수
String days = NumberToHangul.days(3); // "사흘"

// 서수사
String seosusa = NumberToHangul.seosusa(1); // "첫째"
```

### 발음 표준화

```java
import kr.sharenshare.utils.hangul.StandardizePronunciation;

String standardized = StandardizePronunciation.standardize("값어치");
// 표준 발음 규칙에 따라 변환된 문자열

// 경음화 미적용
String withoutHardConversion = StandardizePronunciation.standardize("값어치", false);
```

### 로마자 변환

```java
import kr.sharenshare.utils.hangul.Romanize;

String romanized = Romanize.romanize("안녕하세요"); 
// "annyeonghaseyo"
```

### 키보드 변환

```java
import kr.sharenshare.utils.hangul.KeyboardConverter;

// QWERTY를 한글로 변환
String hangul = KeyboardConverter.convertQwertyToHangul("abc"); 
// "뮻"

// QWERTY를 한글 자모로 변환
String jamo = KeyboardConverter.convertQwertyToAlphabet("abc"); 
// "ㅁㅠㅊ"

// 한글을 QWERTY로 변환
String qwerty = KeyboardConverter.convertHangulToQwerty("겨노"); 
// "rush"
```

### 받침 확인

```java
import kr.sharenshare.utils.hangul.Hangul;
import kr.sharenshare.utils.hangul.Hangul.BatchimOption;

boolean hasBatchim = Hangul.hasBatchim('값'); // true
boolean hasSingleBatchim = Hangul.hasBatchim('값', BatchimOption.SINGLE); // false
boolean hasDoubleBatchim = Hangul.hasBatchim('값', BatchimOption.DOUBLE); // true
```

## API 문서

### Hangul 클래스

한글 문자 처리 관련 유틸리티 메서드를 제공합니다.

- `isHangul(char c)` - 한글 완성형 문자인지 확인
- `disassemble(String str)` - 한글 문자열을 자모로 분해
- `disassembleCompleteCharacter(char c)` - 한글 문자를 초성/중성/종성으로 분해
- `assembleString(String jamoString)` - 자모 문자열을 한글로 조합
- `combineCharacter(...)` - 초성/중성/종성을 조합하여 한글 문자 생성
- `getChoseong(String str)` - 문자열의 초성 추출
- `hasBatchim(char c)` - 받침 유무 확인
- `canBeChoseong/Jungseong/Jongseong(...)` - 자모가 초성/중성/종성으로 사용 가능한지 확인

### Josa 클래스

조사 처리를 위한 메서드를 제공합니다.

- `attach(String word, JosaType josaType)` - 단어에 적절한 조사 붙이기
- `attach(String word, String pattern)` - 패턴 문자열로 조사 붙이기
- `pick(String word, JosaType josaType)` - 조사만 반환
- `pick(String word, String pattern)` - 패턴 문자열로 조사만 반환

**지원하는 조사 타입:**
- `EUL_REUL` (을/를)
- `I_GA` (이/가)
- `EUN_NEUN` (은/는)
- `GWA_WA` (와/과)
- `A_YA` (아/야)
- `EURO_RO` (으로/로)
- `INA_NA` (이나/나)
- `IRAN_RAN` (이란/란)
- `IRANG_RANG` (이랑/랑)
- `IEYO_YEYO` (이에요/예요)
- `EUROSEO_ROSEO` (으로서/로서)
- `EUROSSEO_ROSSEO` (으로써/로써)
- `EUROBUTEU_ROBUTEU` (으로부터/로부터)
- `IRA_RA` (이라/라)

### NumberToHangul 클래스

숫자를 한글로 변환하는 메서드를 제공합니다.

- `numberToHangul(long/double number, boolean spacing)` - 숫자를 한글로 변환
- `numberToHangulMixed(long/double number, boolean spacing)` - 숫자를 한글 혼용으로 변환
- `amountToHangul(String/long/double amount)` - 금액을 한글로 변환
- `susa(int number, boolean determiner)` - 순우리말 수사 변환
- `days(int number)` - 날짜 수 변환
- `seosusa(int number)` - 서수사 변환

### StandardizePronunciation 클래스

표준 발음 규칙에 따라 한글 발음을 변환합니다.

- `standardize(String hangul)` - 표준 발음으로 변환 (경음화 적용)
- `standardize(String hangul, boolean hardConversion)` - 경음화 옵션 지정

### Romanize 클래스

한글을 로마자로 변환합니다.

- `romanize(String hangul)` - 한글 문자열을 로마자로 변환

### KeyboardConverter 클래스

QWERTY 키보드와 한글 자모 간 변환을 제공합니다.

- `convertQwertyToHangul(String str)` - QWERTY를 한글로 변환
- `convertQwertyToAlphabet(String str)` - QWERTY를 한글 자모로 변환
- `convertHangulToQwerty(String str)` - 한글을 QWERTY로 변환
- `convertAlphabetToQwerty(String str)` - 한글 자모를 QWERTY로 변환

## 테스트

프로젝트에는 모든 주요 기능에 대한 단위 테스트가 포함되어 있습니다.

```bash
# Gradle을 사용한 테스트 실행
./gradlew test
```

## 원본 프로젝트

이 프로젝트는 [toss/es-hangul](https://github.com/toss/es-hangul) TypeScript 라이브러리를 Java로 포팅한 것입니다. 원본 프로젝트의 모든 공식 기능이 Java로 구현되었습니다.

## 기여하기

버그 리포트, 기능 제안, Pull Request를 환영합니다. 이슈를 등록하거나 Pull Request를 제출해주세요.

## 라이선스

MIT License - 자세한 내용은 [LICENSE](LICENSE) 파일을 참고하세요.

원본 프로젝트: MIT © Viva Republica, Inc. ([toss/es-hangul](https://github.com/toss/es-hangul))

## 감사의 말

한글 관련 JavaScript 생태계에 많은 기여를 해주시고, 많은 개발자들에게 영감을 주신 아래 라이브러리 오너 분들의 기여에 특별히 감사의 말씀드립니다.

- [hangul-js](https://github.com/e-/Hangul.js): 조재민님

