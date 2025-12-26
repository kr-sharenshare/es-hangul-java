package kr.sharenshare.utils.hangul;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberToHangulTest {

    @Nested
    @DisplayName("numberToHangul 테스트")
    class NumberToHangulMethodTest {

        @Test
        @DisplayName("숫자를 한글로 변환한다")
        void convertNumberToHangul() {
            assertEquals("이십일만", NumberToHangul.numberToHangul(210_000));
            assertEquals("일만이천삼백사십오", NumberToHangul.numberToHangul(12_345));
            assertEquals("일억이천삼백사십오만육천칠백팔십", NumberToHangul.numberToHangul(123_456_780));
            assertEquals("일억", NumberToHangul.numberToHangul(100_000_000));
            assertEquals("일조", NumberToHangul.numberToHangul(1_000_000_000_000L));
        }

        @Test
        @DisplayName("띄어쓰기 옵션을 적용한다")
        void convertWithSpacing() {
            assertEquals("일억 이천삼백사십오만 육천칠백팔십", NumberToHangul.numberToHangul(123_456_780, true));
        }

        @Test
        @DisplayName("0을 변환한다")
        void convertZero() {
            assertEquals("영", NumberToHangul.numberToHangul(0));
        }

        @Test
        @DisplayName("0 이상 10,000 미만인 경우")
        void convertSmallNumbers() {
            assertEquals("일", NumberToHangul.numberToHangul(1));
            assertEquals("이", NumberToHangul.numberToHangul(2));
            assertEquals("삼", NumberToHangul.numberToHangul(3));
            assertEquals("사", NumberToHangul.numberToHangul(4));
            assertEquals("오", NumberToHangul.numberToHangul(5));
            assertEquals("육", NumberToHangul.numberToHangul(6));
            assertEquals("칠", NumberToHangul.numberToHangul(7));
            assertEquals("팔", NumberToHangul.numberToHangul(8));
            assertEquals("구", NumberToHangul.numberToHangul(9));
            assertEquals("십", NumberToHangul.numberToHangul(10));
            assertEquals("십일", NumberToHangul.numberToHangul(11));
            assertEquals("이십", NumberToHangul.numberToHangul(20));
            assertEquals("삼십", NumberToHangul.numberToHangul(30));
            assertEquals("백", NumberToHangul.numberToHangul(100));
            assertEquals("백일", NumberToHangul.numberToHangul(101));
            assertEquals("백십", NumberToHangul.numberToHangul(110));
            assertEquals("이백", NumberToHangul.numberToHangul(200));
            assertEquals("삼백", NumberToHangul.numberToHangul(300));
            assertEquals("천", NumberToHangul.numberToHangul(1_000));
            assertEquals("천일", NumberToHangul.numberToHangul(1_001));
            assertEquals("천백", NumberToHangul.numberToHangul(1_100));
            assertEquals("천이백", NumberToHangul.numberToHangul(1_200));
            assertEquals("천이백삼십사", NumberToHangul.numberToHangul(1_234));
            assertEquals("구천구백구십구", NumberToHangul.numberToHangul(9_999));
        }

        @Test
        @DisplayName("음수를 변환한다")
        void convertNegative() {
            assertEquals("마이너스일만이천삼백사십오", NumberToHangul.numberToHangul(-12_345));
            assertEquals("마이너스이십일만", NumberToHangul.numberToHangul(-210_000));
            assertEquals("마이너스일억이천삼백사십오만육천칠백팔십", NumberToHangul.numberToHangul(-123_456_780));
            assertEquals("마이너스 이십일만", NumberToHangul.numberToHangul(-210_000, true));
            assertEquals("마이너스 일만 이천삼백사십오", NumberToHangul.numberToHangul(-12_345, true));
            assertEquals("마이너스 일억 이천삼백사십오만 육천칠백팔십", NumberToHangul.numberToHangul(-123_456_780, true));
        }

        @Test
        @DisplayName("소수를 변환한다")
        void convertDecimal() {
            assertEquals("일만이천삼백사십오점육칠팔", NumberToHangul.numberToHangul(12_345.678));
            assertEquals("영점일", NumberToHangul.numberToHangul(0.1));
            assertEquals("영점영일영이", NumberToHangul.numberToHangul(0.0102));
            assertEquals("마이너스영점일", NumberToHangul.numberToHangul(-0.1));
            assertEquals("마이너스일만이천삼백사십오점육칠팔", NumberToHangul.numberToHangul(-12_345.678));
            assertEquals("영점 일", NumberToHangul.numberToHangul(0.1, true));
            assertEquals("일만 이천삼백사십오점 육칠팔", NumberToHangul.numberToHangul(12_345.678, true));
            assertEquals("마이너스 영점 일", NumberToHangul.numberToHangul(-0.1, true));
            assertEquals("마이너스 일만 이천삼백사십오점 육칠팔", NumberToHangul.numberToHangul(-12_345.678, true));
        }

        @Test
        @DisplayName("Infinity를 변환한다")
        void convertInfinity() {
            assertEquals("무한대", NumberToHangul.numberToHangul(Double.POSITIVE_INFINITY));
            assertEquals("마이너스무한대", NumberToHangul.numberToHangul(Double.NEGATIVE_INFINITY));
            assertEquals("무한대", NumberToHangul.numberToHangul(Double.POSITIVE_INFINITY, true));
            assertEquals("마이너스 무한대", NumberToHangul.numberToHangul(Double.NEGATIVE_INFINITY, true));
        }

        @Test
        @DisplayName("NaN은 에러를 발생시킨다")
        void convertNaN() {
            assertThrows(IllegalArgumentException.class, () -> {
                NumberToHangul.numberToHangul(Double.NaN);
            });
        }
    }

    @Nested
    @DisplayName("numberToHangulMixed 테스트")
    class NumberToHangulMixedTest {

        @Test
        @DisplayName("숫자를 한글 혼용으로 변환한다")
        void convertToMixed() {
            assertEquals("1만2,345", NumberToHangul.numberToHangulMixed(12_345));
            assertEquals("1억2,345만6,780", NumberToHangul.numberToHangulMixed(123_456_780));
            assertEquals("21만", NumberToHangul.numberToHangulMixed(210_000));
        }

        @Test
        @DisplayName("띄어쓰기 옵션을 적용한다")
        void convertMixedWithSpacing() {
            assertEquals("1억 2,345만 6,780", NumberToHangul.numberToHangulMixed(123_456_780, true));
            assertEquals("21만", NumberToHangul.numberToHangulMixed(210_000, true));
            assertEquals("1만 2,345", NumberToHangul.numberToHangulMixed(12_345, true));
        }

        @Test
        @DisplayName("0 이상 10,000 미만인 경우")
        void convertMixedSmallNumbers() {
            assertEquals("0", NumberToHangul.numberToHangulMixed(0));
            assertEquals("1", NumberToHangul.numberToHangulMixed(1));
            assertEquals("10", NumberToHangul.numberToHangulMixed(10));
            assertEquals("100", NumberToHangul.numberToHangulMixed(100));
            assertEquals("1,000", NumberToHangul.numberToHangulMixed(1_000));
            assertEquals("9,999", NumberToHangul.numberToHangulMixed(9_999));
        }

        @Test
        @DisplayName("음수를 한글 혼용으로 변환한다")
        void convertMixedNegative() {
            assertEquals("-21만", NumberToHangul.numberToHangulMixed(-210_000));
            assertEquals("-1만2,345", NumberToHangul.numberToHangulMixed(-12_345));
            assertEquals("-1억2,345만6,780", NumberToHangul.numberToHangulMixed(-123_456_780));
            assertEquals("-21만", NumberToHangul.numberToHangulMixed(-210_000, true));
            assertEquals("-1만 2,345", NumberToHangul.numberToHangulMixed(-12_345, true));
            assertEquals("-1억 2,345만 6,780", NumberToHangul.numberToHangulMixed(-123_456_780, true));
        }

        @Test
        @DisplayName("Infinity를 한글 혼용으로 변환한다")
        void convertMixedInfinity() {
            assertEquals("무한대", NumberToHangul.numberToHangulMixed(Double.POSITIVE_INFINITY));
            assertEquals("-무한대", NumberToHangul.numberToHangulMixed(Double.NEGATIVE_INFINITY));
            assertEquals("-무한대", NumberToHangul.numberToHangulMixed(Double.NEGATIVE_INFINITY, true));
        }

        @Test
        @DisplayName("소수를 한글 혼용으로 변환한다")
        void convertMixedDecimal() {
            assertEquals("0.1", NumberToHangul.numberToHangulMixed(0.1));
            assertEquals("1만2,345.678", NumberToHangul.numberToHangulMixed(12_345.678));
            assertEquals("-0.1", NumberToHangul.numberToHangulMixed(-0.1));
            assertEquals("-1만2,345.678", NumberToHangul.numberToHangulMixed(-12_345.678));
            assertEquals("0.1", NumberToHangul.numberToHangulMixed(0.1, true));
            assertEquals("1만 2,345.678", NumberToHangul.numberToHangulMixed(12_345.678, true));
            assertEquals("-0.1", NumberToHangul.numberToHangulMixed(-0.1, true));
            assertEquals("-1만 2,345.678", NumberToHangul.numberToHangulMixed(-12_345.678, true));
        }

        @Test
        @DisplayName("NaN은 에러를 발생시킨다")
        void convertMixedNaN() {
            assertThrows(IllegalArgumentException.class, () -> {
                NumberToHangul.numberToHangulMixed(Double.NaN);
            });
        }
    }

    @Nested
    @DisplayName("amountToHangul 테스트")
    class AmountToHangulTest {

        @Test
        @DisplayName("콤마가 포함된 금액을 변환한다")
        void convertAmountWithComma() {
            assertEquals("일천오백이십만천백", NumberToHangul.amountToHangul("15,201,100"));
            assertEquals("일억", NumberToHangul.amountToHangul("100000000"));
            assertEquals("일억백", NumberToHangul.amountToHangul("100000100"));
            assertEquals("영", NumberToHangul.amountToHangul("0"));
            assertEquals("", NumberToHangul.amountToHangul(""));
        }

        @Test
        @DisplayName("원 기호가 포함된 금액을 변환한다")
        void convertAmountWithCurrency() {
            assertEquals("일십이만삼십", NumberToHangul.amountToHangul("120,030원"));
        }

        @Test
        @DisplayName("소수점이 포함된 금액을 변환한다")
        void convertAmountWithDecimal() {
            assertEquals("일만이천삼백사십오점육칠팔구", NumberToHangul.amountToHangul("12345.6789"));
            assertEquals("영점영일영이", NumberToHangul.amountToHangul("0.01020"));
            assertEquals("영", NumberToHangul.amountToHangul("0.0000"));
            assertEquals("영", NumberToHangul.amountToHangul(".0000"));
            assertEquals("삼백구십이점이사", NumberToHangul.amountToHangul("392.24"));
        }

        @Test
        @DisplayName("금액 숫자를 한글로 변환한다")
        void convertAmountNumber() {
            assertEquals("일천오백이십만천백", NumberToHangul.amountToHangul(15_201_100));
            assertEquals("일억백", NumberToHangul.amountToHangul(100000100));
            assertEquals("삼백구십이점이사", NumberToHangul.amountToHangul(392.24));
        }

        @Test
        @DisplayName("선행 0이 있는 경우 처리")
        void convertAmountLeadingZeros() {
            assertEquals("천이십삼", NumberToHangul.amountToHangul("01023"));
            assertEquals("천이십삼", NumberToHangul.amountToHangul("001023"));
            assertEquals("천이십삼", NumberToHangul.amountToHangul("0001023"));
        }

        @Test
        @DisplayName("숫자로 된 금액이 80글자를 넘을 시 에러 발생")
        void convertAmountTooLong() {
            StringBuilder longNumber = new StringBuilder();
            for (int i = 0; i < 81; i++) {
                longNumber.append("1");
            }
            String longNumberString = longNumber.toString();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                NumberToHangul.amountToHangul(longNumberString);
            });
            assertEquals("convert range exceeded : " + longNumberString, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("susa 테스트")
    class SusaTest {

        @Test
        @DisplayName("순 우리말 수사로 변환한다")
        void convertToSusa() {
            assertEquals("하나", NumberToHangul.susa(1));
            assertEquals("둘", NumberToHangul.susa(2));
            assertEquals("셋", NumberToHangul.susa(3));
            assertEquals("넷", NumberToHangul.susa(4));
            assertEquals("다섯", NumberToHangul.susa(5));
            assertEquals("여섯", NumberToHangul.susa(6));
            assertEquals("일곱", NumberToHangul.susa(7));
            assertEquals("여덟", NumberToHangul.susa(8));
            assertEquals("아홉", NumberToHangul.susa(9));
            assertEquals("열", NumberToHangul.susa(10));
            assertEquals("열하나", NumberToHangul.susa(11));
            assertEquals("열둘", NumberToHangul.susa(12));
            assertEquals("스물", NumberToHangul.susa(20));
            assertEquals("스물하나", NumberToHangul.susa(21));
            assertEquals("서른", NumberToHangul.susa(30));
            assertEquals("아흔아홉", NumberToHangul.susa(99));
            assertEquals("백", NumberToHangul.susa(100));
        }

        @Test
        @DisplayName("관형사 형태로 변환한다")
        void convertToSusaDeterminer() {
            assertEquals("한", NumberToHangul.susa(1, true));
            assertEquals("두", NumberToHangul.susa(2, true));
            assertEquals("세", NumberToHangul.susa(3, true));
            assertEquals("네", NumberToHangul.susa(4, true));
            assertEquals("다섯", NumberToHangul.susa(5, true));
            assertEquals("여섯", NumberToHangul.susa(6, true));
            assertEquals("일곱", NumberToHangul.susa(7, true));
            assertEquals("여덟", NumberToHangul.susa(8, true));
            assertEquals("아홉", NumberToHangul.susa(9, true));
            assertEquals("열", NumberToHangul.susa(10, true));
            assertEquals("열한", NumberToHangul.susa(11, true));
            assertEquals("열두", NumberToHangul.susa(12, true));
            assertEquals("스무", NumberToHangul.susa(20, true));
            assertEquals("스물한", NumberToHangul.susa(21, true));
            assertEquals("서른", NumberToHangul.susa(30, true));
            assertEquals("아흔아홉", NumberToHangul.susa(99, true));
            assertEquals("백", NumberToHangul.susa(100, true));
        }

        @Test
        @DisplayName("유효하지 않은 숫자에 대해 에러를 발생시킨다")
        void susaInvalidNumbers() {
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.susa(0));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.susa(-1));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.susa(101));
        }
    }

    @Nested
    @DisplayName("days 테스트")
    class DaysTest {

        @Test
        @DisplayName("날짜 수를 순 우리말로 변환한다")
        void convertToDays() {
            assertEquals("하루", NumberToHangul.days(1));
            assertEquals("이틀", NumberToHangul.days(2));
            assertEquals("사흘", NumberToHangul.days(3));
            assertEquals("나흘", NumberToHangul.days(4));
            assertEquals("닷새", NumberToHangul.days(5));
            assertEquals("엿새", NumberToHangul.days(6));
            assertEquals("이레", NumberToHangul.days(7));
            assertEquals("여드레", NumberToHangul.days(8));
            assertEquals("아흐레", NumberToHangul.days(9));
            assertEquals("열흘", NumberToHangul.days(10));
            assertEquals("열하루", NumberToHangul.days(11));
            assertEquals("스무날", NumberToHangul.days(20));
            assertEquals("스무하루", NumberToHangul.days(21));
            assertEquals("서른날", NumberToHangul.days(30));
        }

        @Test
        @DisplayName("유효하지 않은 숫자에 대해 에러를 발생시킨다")
        void daysInvalidNumbers() {
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.days(0));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.days(-1));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.days(31));
        }
    }

    @Nested
    @DisplayName("susa 추가 테스트")
    class SusaAdditionalTest {

        @Test
        @DisplayName("유효하지 않은 소수에 대해 에러를 발생시킨다")
        void susaInvalidDecimal() {
            // Java에서는 int 타입만 받으므로 소수점 테스트는 컴파일 오류
            // 대신 범위 테스트
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.susa(0));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.susa(-1));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.susa(101));
        }
    }

    @Nested
    @DisplayName("days 추가 테스트")
    class DaysAdditionalTest {

        @Test
        @DisplayName("유효하지 않은 숫자에 대해 에러를 발생시킨다")
        void daysInvalidRange() {
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.days(0));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.days(-1));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.days(31));
        }
    }

    @Nested
    @DisplayName("seosusa 테스트")
    class SeosusaTest {

        @Test
        @DisplayName("서수사로 변환한다")
        void convertToSeosusa() {
            assertEquals("첫째", NumberToHangul.seosusa(1));
            assertEquals("둘째", NumberToHangul.seosusa(2));
            assertEquals("셋째", NumberToHangul.seosusa(3));
            assertEquals("넷째", NumberToHangul.seosusa(4));
            assertEquals("다섯째", NumberToHangul.seosusa(5));
            assertEquals("여섯째", NumberToHangul.seosusa(6));
            assertEquals("일곱째", NumberToHangul.seosusa(7));
            assertEquals("여덟째", NumberToHangul.seosusa(8));
            assertEquals("아홉째", NumberToHangul.seosusa(9));
            assertEquals("열째", NumberToHangul.seosusa(10));
            assertEquals("열한째", NumberToHangul.seosusa(11));
            assertEquals("열두째", NumberToHangul.seosusa(12));
            assertEquals("열셋째", NumberToHangul.seosusa(13));
            assertEquals("열넷째", NumberToHangul.seosusa(14));
            assertEquals("열다섯째", NumberToHangul.seosusa(15));
            assertEquals("스무째", NumberToHangul.seosusa(20));
            assertEquals("스물한째", NumberToHangul.seosusa(21));
            assertEquals("스물두째", NumberToHangul.seosusa(22));
            assertEquals("서른째", NumberToHangul.seosusa(30));
            assertEquals("마흔째", NumberToHangul.seosusa(40));
            assertEquals("아흔째", NumberToHangul.seosusa(90));
            assertEquals("아흔아홉째", NumberToHangul.seosusa(99));
            assertEquals("백째", NumberToHangul.seosusa(100));
            assertEquals("백일째", NumberToHangul.seosusa(101));
        }

        @Test
        @DisplayName("유효하지 않은 숫자에 대해 에러를 발생시킨다")
        void seosusaInvalidNumbers() {
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.seosusa(0));
            assertThrows(IllegalArgumentException.class, () -> NumberToHangul.seosusa(-1));
        }
    }
}

