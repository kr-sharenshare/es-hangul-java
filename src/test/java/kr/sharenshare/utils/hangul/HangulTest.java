package kr.sharenshare.utils.hangul;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HangulTest {

    @Nested
    @DisplayName("isHangul 테스트")
    class IsHangulTest {

        @Test
        @DisplayName("isHangul은 완성된 한글 문자를 받으면 true를 반환한다")
        void isHangulCharacter() {
            assertTrue(Hangul.isHangul('가'));
            assertTrue(Hangul.isHangul('값'));
            assertFalse(Hangul.isHangul('ㄱ'));
            assertFalse(Hangul.isHangul('ㅏ'));
            assertFalse(Hangul.isHangul('a'));
        }

        @Test
        @DisplayName("isCompatJamo은 조합되지 않은 한글 문자를 받으면 true를 반환한다")
        void isHangulAlphabet() {
            assertFalse(Hangul.isCompatJamo('가'));
            assertFalse(Hangul.isCompatJamo('값'));
            assertTrue(Hangul.isCompatJamo('ㄱ'));
            assertTrue(Hangul.isCompatJamo('ㅏ'));
            assertFalse(Hangul.isCompatJamo('a'));
        }

        @Test
        @DisplayName("isHangulString은 한글 문자열을 받으면 true를 반환한다")
        void isHangulString() {
            assertTrue(Hangul.isHangulString("값"));
            assertTrue(Hangul.isHangulString("저는 고양이를 좋아합니다"));
            assertFalse(Hangul.isHangulString("a"));
            assertFalse(Hangul.isHangulString(""));
            assertFalse(Hangul.isHangulString(null));
        }
    }

    @Nested
    @DisplayName("disassemble 테스트")
    class DisassembleTest {

        @Test
        @DisplayName("한글 완성형을 자모로 분해한다")
        void disassembleHangul() {
            assertEquals("ㄱㅏㅂㅅ", Hangul.disassemble("값"));
            assertEquals("ㄱㅏㅂㅅㅇㅣ ㅂㅣㅆㅏㄷㅏ", Hangul.disassemble("값이 비싸다"));
            assertEquals("ㅅㅏㄱㅗㅏ ㅉㅏㅇ", Hangul.disassemble("사과 짱"));
        }

        @Test
        @DisplayName("겹모음을 분해한다")
        void disassembleDoubleVowel() {
            assertEquals("ㅗㅏ", Hangul.disassemble("ㅘ"));
        }

        @Test
        @DisplayName("겹자음을 분해한다")
        void disassembleDoubleConsonant() {
            assertEquals("ㄴㅈ", Hangul.disassemble("ㄵ"));
        }
    }

    @Nested
    @DisplayName("disassembleToGroups 테스트")
    class DisassembleToGroupsTest {

        @Test
        @DisplayName("한글을 그룹별로 분해한다")
        void disassembleToGroups() {
            List<List<String>> result = Hangul.disassembleToGroups("사과");
            assertEquals(2, result.size());
            assertEquals(Arrays.asList("ㅅ", "ㅏ"), result.get(0));
            assertEquals(Arrays.asList("ㄱ", "ㅗ", "ㅏ"), result.get(1));
        }

        @Test
        @DisplayName("'값'을 그룹별로 분해한다")
        void disassembleToGroupsGap() {
            List<List<String>> result = Hangul.disassembleToGroups("값");
            assertEquals(1, result.size());
            assertEquals(Arrays.asList("ㄱ", "ㅏ", "ㅂ", "ㅅ"), result.get(0));
        }

        @Test
        @DisplayName("'값이 비싸다'를 그룹별로 분해한다")
        void disassembleToGroupsSentence() {
            List<List<String>> result = Hangul.disassembleToGroups("값이 비싸다");
            assertEquals(6, result.size());
            assertEquals(Arrays.asList("ㄱ", "ㅏ", "ㅂ", "ㅅ"), result.get(0));
            assertEquals(Arrays.asList("ㅇ", "ㅣ"), result.get(1));
            assertEquals(Arrays.asList(" "), result.get(2));
            assertEquals(Arrays.asList("ㅂ", "ㅣ"), result.get(3));
            assertEquals(Arrays.asList("ㅆ", "ㅏ"), result.get(4));
            assertEquals(Arrays.asList("ㄷ", "ㅏ"), result.get(5));
        }

        @Test
        @DisplayName("'사과 짱'을 그룹별로 분해한다")
        void disassembleToGroupsSagwaJjang() {
            List<List<String>> result = Hangul.disassembleToGroups("사과 짱");
            assertEquals(4, result.size());
            assertEquals(Arrays.asList("ㅅ", "ㅏ"), result.get(0));
            assertEquals(Arrays.asList("ㄱ", "ㅗ", "ㅏ"), result.get(1));
            assertEquals(Arrays.asList(" "), result.get(2));
            assertEquals(Arrays.asList("ㅉ", "ㅏ", "ㅇ"), result.get(3));
        }

        @Test
        @DisplayName("겹모음을 그룹별로 분해한다")
        void disassembleDoubleVowelToGroups() {
            List<List<String>> result = Hangul.disassembleToGroups("ㅘ");
            assertEquals(1, result.size());
            assertEquals(Arrays.asList("ㅗ", "ㅏ"), result.get(0));
        }

        @Test
        @DisplayName("겹자음을 그룹별로 분해한다")
        void disassembleDoubleConsonantToGroups() {
            List<List<String>> result = Hangul.disassembleToGroups("ㄵ");
            assertEquals(1, result.size());
            assertEquals(Arrays.asList("ㄴ", "ㅈ"), result.get(0));
        }
    }

    @Nested
    @DisplayName("disassembleCompleteCharacter 테스트")
    class DisassembleCompleteCharacterTest {

        @Test
        @DisplayName("겹받침이 있는 문자를 분해한다")
        void disassembleWithDoubleJongseong() {
            HangulChar result = Hangul.disassembleCompleteCharacter('값');
            assertNotNull(result);
            assertEquals('ㄱ', result.getChoseong());
            assertEquals('ㅏ', result.getJungseong());
            assertEquals('ㅄ', result.getJongseong());
        }

        @Test
        @DisplayName("받침이 없는 문자를 분해한다")
        void disassembleWithoutJongseong() {
            HangulChar result = Hangul.disassembleCompleteCharacter('리');
            assertNotNull(result);
            assertEquals('ㄹ', result.getChoseong());
            assertEquals('ㅣ', result.getJungseong());
            assertNull(result.getJongseong());
        }

        @Test
        @DisplayName("홑받침이 있는 문자를 분해한다")
        void disassembleWithSingleJongseong() {
            HangulChar result = Hangul.disassembleCompleteCharacter('빚');
            assertNotNull(result);
            assertEquals('ㅂ', result.getChoseong());
            assertEquals('ㅣ', result.getJungseong());
            assertEquals('ㅈ', result.getJongseong());
        }

        @Test
        @DisplayName("홑받침 '박'을 분해한다")
        void disassembleBak() {
            HangulChar result = Hangul.disassembleCompleteCharacter('박');
            assertNotNull(result);
            assertEquals('ㅂ', result.getChoseong());
            assertEquals('ㅏ', result.getJungseong());
            assertEquals('ㄱ', result.getJongseong());
        }

        @Test
        @DisplayName("완성된 한글이 아니면 null을 반환한다")
        void disassembleNonCompleteHangul() {
            assertNull(Hangul.disassembleCompleteCharacter('ㄱ'));
            assertNull(Hangul.disassembleCompleteCharacter('ㅏ'));
        }
    }

    @Nested
    @DisplayName("getChoseong 테스트")
    class GetChoseongTest {

        @Test
        @DisplayName("초성을 추출한다")
        void getChoseong() {
            assertEquals("ㅅㄱ", Hangul.getChoseong("사과"));
            assertEquals("ㄸㅇ ㅆㄱ", Hangul.getChoseong("띄어 쓰기"));
            assertEquals("ㅍㄹㅌㅇㄷ", Hangul.getChoseong("프론트엔드"));
            assertEquals("ㄴㅈ", Hangul.getChoseong("ㄴㅈ"));
            assertEquals("ㄹㅇㅌ", Hangul.getChoseong("리액트"));
        }
    }

    @Nested
    @DisplayName("hasBatchim 테스트")
    class HasBatchimTest {

        @Test
        @DisplayName("받침이 있으면 true를 반환한다")
        void hasBatchimTrue() {
            assertTrue(Hangul.hasBatchim('값'));
            assertTrue(Hangul.hasBatchim('감'));
        }

        @Test
        @DisplayName("받침이 없으면 false를 반환한다")
        void hasBatchimFalse() {
            assertFalse(Hangul.hasBatchim('토'));
            assertFalse(Hangul.hasBatchim('가'));
            assertFalse(Hangul.hasBatchim('서'));
        }

        @Test
        @DisplayName("빈 문자열은 받침이 없으므로 false를 반환한다")
        void hasBatchimEmptyString() {
            // hasBatchim은 char를 받으므로 빈 문자열 테스트는 불가능
            // 대신 한글 외 문자 테스트
            assertFalse(Hangul.hasBatchim('c'));
            assertFalse(Hangul.hasBatchim('!'));
        }

        @Test
        @DisplayName("한글이 자음 또는 모음으로만 구성된 경우 false를 반환한다")
        void hasBatchimJamo() {
            assertFalse(Hangul.hasBatchim('ㄱ'));
            assertFalse(Hangul.hasBatchim('ㅏ'));
        }

        @Test
        @DisplayName("'읊' 문자에서 받침이 있으므로 true를 반환한다")
        void hasBatchimEulp() {
            assertTrue(Hangul.hasBatchim('읊'));
        }

        @Test
        @DisplayName("홑받침만 확인한다")
        void hasSingleBatchim() {
            assertTrue(Hangul.hasBatchim('갑', Hangul.BatchimOption.SINGLE));
            assertFalse(Hangul.hasBatchim('값', Hangul.BatchimOption.SINGLE));
        }

        @Test
        @DisplayName("겹받침만 확인한다")
        void hasDoubleBatchim() {
            assertTrue(Hangul.hasBatchim('값', Hangul.BatchimOption.DOUBLE));
            assertFalse(Hangul.hasBatchim('갑', Hangul.BatchimOption.DOUBLE));
            assertTrue(Hangul.hasBatchim('읊', Hangul.BatchimOption.DOUBLE));
            assertTrue(Hangul.hasBatchim('웱', Hangul.BatchimOption.DOUBLE));
        }

        @Test
        @DisplayName("홑받침을 받으면 true를 반환한다")
        void hasSingleBatchimTrue() {
            assertTrue(Hangul.hasBatchim('공', Hangul.BatchimOption.SINGLE));
            assertTrue(Hangul.hasBatchim('핫', Hangul.BatchimOption.SINGLE));
            assertTrue(Hangul.hasBatchim('양', Hangul.BatchimOption.SINGLE));
            assertTrue(Hangul.hasBatchim('신', Hangul.BatchimOption.SINGLE));
            assertTrue(Hangul.hasBatchim('확', Hangul.BatchimOption.SINGLE));
        }

        @Test
        @DisplayName("겹받침을 받으면 홑받침 옵션에서 false를 반환한다")
        void hasSingleBatchimFalse() {
            assertFalse(Hangul.hasBatchim('값', Hangul.BatchimOption.SINGLE));
            assertFalse(Hangul.hasBatchim('읊', Hangul.BatchimOption.SINGLE));
            assertFalse(Hangul.hasBatchim('웱', Hangul.BatchimOption.SINGLE));
        }

        @Test
        @DisplayName("홑받침을 받으면 겹받침 옵션에서 false를 반환한다")
        void hasDoubleBatchimFalse() {
            assertFalse(Hangul.hasBatchim('공', Hangul.BatchimOption.DOUBLE));
            assertFalse(Hangul.hasBatchim('핫', Hangul.BatchimOption.DOUBLE));
            assertFalse(Hangul.hasBatchim('양', Hangul.BatchimOption.DOUBLE));
            assertFalse(Hangul.hasBatchim('신', Hangul.BatchimOption.DOUBLE));
            assertFalse(Hangul.hasBatchim('확', Hangul.BatchimOption.DOUBLE));
        }

        @Test
        @DisplayName("받침이 없는 문자를 받으면 홑받침 옵션에서 false를 반환한다")
        void hasSingleBatchimWithoutBatchim() {
            assertFalse(Hangul.hasBatchim('토', Hangul.BatchimOption.SINGLE));
            assertFalse(Hangul.hasBatchim('서', Hangul.BatchimOption.SINGLE));
            assertFalse(Hangul.hasBatchim('와', Hangul.BatchimOption.SINGLE));
        }

        @Test
        @DisplayName("받침이 없는 문자를 받으면 겹받침 옵션에서 false를 반환한다")
        void hasDoubleBatchimWithoutBatchim() {
            assertFalse(Hangul.hasBatchim('토', Hangul.BatchimOption.DOUBLE));
            assertFalse(Hangul.hasBatchim('서', Hangul.BatchimOption.DOUBLE));
            assertFalse(Hangul.hasBatchim('와', Hangul.BatchimOption.DOUBLE));
        }

        @Test
        @DisplayName("'공' 문자에서 받침이 있으므로 true를 반환한다")
        void hasBatchimGong() {
            assertTrue(Hangul.hasBatchim('공'));
        }

        @Test
        @DisplayName("한글 외의 문자를 입력하면 홑받침 옵션에서 false를 반환한다")
        void hasSingleBatchimNonHangul() {
            assertFalse(Hangul.hasBatchim('c', Hangul.BatchimOption.SINGLE));
            assertFalse(Hangul.hasBatchim('!', Hangul.BatchimOption.SINGLE));
        }

        @Test
        @DisplayName("한글 외의 문자를 입력하면 겹받침 옵션에서 false를 반환한다")
        void hasDoubleBatchimNonHangul() {
            assertFalse(Hangul.hasBatchim('c', Hangul.BatchimOption.DOUBLE));
            assertFalse(Hangul.hasBatchim('!', Hangul.BatchimOption.DOUBLE));
        }
    }

    @Nested
    @DisplayName("canBeChoseong/canBeJungseong/canBeJongseong 테스트")
    class CanBeTest {

        @Test
        @DisplayName("초성 가능 여부를 확인한다")
        void canBeChoseong() {
            assertTrue(Hangul.canBeChoseong('ㄱ'));
            assertTrue(Hangul.canBeChoseong('ㅃ'));
            assertFalse(Hangul.canBeChoseong('ㅏ'));
            assertFalse(Hangul.canBeChoseong('가'));
        }

        @Test
        @DisplayName("중성 가능 여부를 확인한다")
        void canBeJungseong() {
            assertTrue(Hangul.canBeJungseong('ㅏ'));
            assertTrue(Hangul.canBeJungseong("ㅗㅏ"));
            assertFalse(Hangul.canBeJungseong('ㄱ'));
            assertFalse(Hangul.canBeJungseong('가'));
        }

        @Test
        @DisplayName("종성 가능 여부를 확인한다")
        void canBeJongseong() {
            assertTrue(Hangul.canBeJongseong('ㄱ'));
            assertTrue(Hangul.canBeJongseong("ㄱㅅ"));
            assertTrue(Hangul.canBeJongseong("ㅂㅅ"));
            assertFalse(Hangul.canBeJongseong("ㅎㄹ"));
            assertFalse(Hangul.canBeJongseong("ㅗㅏ"));
            assertFalse(Hangul.canBeJongseong('ㅏ'));
            assertFalse(Hangul.canBeJongseong('가'));
        }

        @Test
        @DisplayName("초성 가능 여부를 확인한다 - 문자열")
        void canBeChoseongString() {
            assertTrue(Hangul.canBeChoseong("ㄱ"));
            assertFalse(Hangul.canBeChoseong("ㅏ"));
            assertFalse(Hangul.canBeChoseong("ㅘ"));
            assertFalse(Hangul.canBeChoseong("ㄱㅅ"));
            assertFalse(Hangul.canBeChoseong("가"));
        }

        @Test
        @DisplayName("중성 가능 여부를 확인한다 - 문자열")
        void canBeJungseongString() {
            assertTrue(Hangul.canBeJungseong("ㅗㅏ"));
            assertTrue(Hangul.canBeJungseong("ㅘ"));
            assertTrue(Hangul.canBeJungseong("ㅏ"));
            assertFalse(Hangul.canBeJungseong("ㄱ"));
            assertFalse(Hangul.canBeJungseong("ㄱㅅ"));
            assertFalse(Hangul.canBeJungseong("가"));
        }
    }

    @Nested
    @DisplayName("combineCharacter 테스트")
    class CombineCharacterTest {

        @Test
        @DisplayName("종성으로 겹받침으로 구성될 수 있는 문자 두 개를 받으면 겹받침을 생성한다")
        void combineWithDoubleJongseong() {
            assertEquals("값", Hangul.combineCharacter("ㄱ", "ㅏ", "ㅂㅅ"));
        }

        @Test
        @DisplayName("종성이 입력되지 않았다면 받침이 없는 문자로 합성한다")
        void combineWithoutJongseong() {
            assertEquals("토", Hangul.combineCharacter("ㅌ", "ㅗ"));
        }

        @Test
        @DisplayName("종성이 입력되었다면 받침을 추가한다")
        void combineWithJongseong() {
            assertEquals("톳", Hangul.combineCharacter("ㅌ", "ㅗ", "ㅅ"));
        }

        @Test
        @DisplayName("초성이 될 수 없는 문자가 초성으로 입력되면 에러를 반환한다")
        void combineInvalidChoseong() {
            assertThrows(IllegalArgumentException.class, () -> Hangul.combineCharacter("ㅏ", "ㅏ", "ㄱ"));
        }

        @Test
        @DisplayName("중성이 될 수 없는 문자가 중성으로 입력되면 에러를 반환한다")
        void combineInvalidJungseong() {
            assertThrows(IllegalArgumentException.class, () -> Hangul.combineCharacter("ㄱ", "ㄴ", "ㅃ"));
        }

        @Test
        @DisplayName("종성이 될 수 없는 문자가 종성으로 입력되면 에러를 반환한다")
        void combineInvalidJongseong() {
            assertThrows(IllegalArgumentException.class, () -> Hangul.combineCharacter("ㄱ", "ㅏ", "ㅃ"));
        }

        @Test
        @DisplayName("온전한 한글 문자가 하나라도 입력되면 에러를 반환한다")
        void combineCompleteHangulThrowsError() {
            assertThrows(IllegalArgumentException.class, () -> Hangul.combineCharacter("가", "ㅏ", "ㄱ"));
        }
    }

    @Nested
    @DisplayName("combineVowels 테스트")
    class CombineVowelsTest {

        @Test
        @DisplayName("겹모음이 될 수 있는 모음이 순서대로 입력되면 겹모음으로 합성한다")
        void combineVowelsCorrectOrder() {
            assertEquals("ㅘ", Hangul.combineVowels('ㅗ', 'ㅏ'));
            assertEquals("ㅞ", Hangul.combineVowels('ㅜ', 'ㅔ'));
            assertEquals("ㅢ", Hangul.combineVowels('ㅡ', 'ㅣ'));
        }

        @Test
        @DisplayName("겹모음이 될 수 있는 모음이라고 해도 틀린 순서로 입력되면 Join한다")
        void combineVowelsWrongOrder() {
            assertEquals("ㅏㅗ", Hangul.combineVowels('ㅏ', 'ㅗ'));
            assertEquals("ㅣㅡ", Hangul.combineVowels('ㅣ', 'ㅡ'));
        }

        @Test
        @DisplayName("이미 겹모음인 문자와 모음을 합성하려고 시도하면 Join한다")
        void combineAlreadyDoubleVowel() {
            assertEquals("ㅘㅏ", Hangul.combineVowels('ㅘ', 'ㅏ'));
            assertEquals("ㅝㅣ", Hangul.combineVowels('ㅝ', 'ㅣ'));
        }
    }

    @Nested
    @DisplayName("assemble 테스트")
    class AssembleTest {

        @Test
        @DisplayName("자모 배열을 한글로 조합한다")
        void assembleFromArray() {
            String result = Hangul.assemble(new String[]{"ㅇ", "ㅏ", "ㅂ", "ㅓ", "ㅈ", "ㅣ"});
            assertEquals("아버지", result);
        }

        @Test
        @DisplayName("자모 리스트를 한글로 조합한다")
        void assembleFromList() {
            String result = Hangul.assemble(Arrays.asList("ㅇ", "ㅏ", "ㅂ", "ㅓ", "ㅈ", "ㅣ"));
            assertEquals("아버지", result);
        }

        @Test
        @DisplayName("온전한 한글과 한글 문자 조합")
        void assembleMixed() {
            String result = Hangul.assemble(new String[]{"아버지가", " ", "방ㅇ", "ㅔ ", "들ㅇ", "ㅓ갑니다"});
            assertEquals("아버지가 방에 들어갑니다", result);
        }

        @Test
        @DisplayName("온전한 한글만 조합")
        void assembleCompleteHangul() {
            String result = Hangul.assemble(new String[]{"아버지가", " ", "방에 ", "들어갑니다"});
            assertEquals("아버지가 방에 들어갑니다", result);
        }
    }

    @Nested
    @DisplayName("removeLastCharacter 테스트")
    class RemoveLastCharacterTest {

        @Test
        @DisplayName("겹받침을 가진 글자에서 마지막 자모를 제거한다")
        void removeFromDoubleJongseong() {
            // ㅄ(ㅂ+ㅅ)에서 ㅅ을 제거하면 ㅂ만 남아서 "갑"이 됨
            assertEquals("갑", Hangul.removeLastCharacter("값"));
        }

        @Test
        @DisplayName("홑받침을 가진 글자에서 마지막 자모를 제거한다")
        void removeFromSingleJongseong() {
            assertEquals("가", Hangul.removeLastCharacter("감"));
        }

        @Test
        @DisplayName("받침이 없는 글자에서 마지막 자모를 제거한다")
        void removeFromNoJongseong() {
            assertEquals("ㄱ", Hangul.removeLastCharacter("가"));
        }

        @Test
        @DisplayName("'안녕하세요 값'에서 마지막 문자를 제거한다")
        void removeFromSentence() {
            assertEquals("안녕하세요 갑", Hangul.removeLastCharacter("안녕하세요 값"));
            assertEquals("안녕하세요 값ㅇ", Hangul.removeLastCharacter("안녕하세요 값이"));
        }

        @Test
        @DisplayName("초성과 중성의 조합으로 끝날 경우 초성만 남긴다")
        void removeFromChoseongJungseong() {
            assertEquals("프론트엔ㄷ", Hangul.removeLastCharacter("프론트엔드"));
            assertEquals("끓ㄷ", Hangul.removeLastCharacter("끓다"));
            assertEquals("관ㅅ", Hangul.removeLastCharacter("관사"));
            assertEquals("괴ㅅ", Hangul.removeLastCharacter("괴사"));
        }

        @Test
        @DisplayName("초성과 중성과 종성의 조합으로 끝날 경우 초성과 중성이 조합된 문자만 남긴다")
        void removeFromChoseongJungseongJongseong() {
            assertEquals("일요이", Hangul.removeLastCharacter("일요일"));
            assertEquals("완저", Hangul.removeLastCharacter("완전"));
            assertEquals("왅저", Hangul.removeLastCharacter("왅전"));
            // '깎' = 초성 ㄲ + 중성 ㅏ + 종성 ㄱ → 종성 제거 → '까'
            assertEquals("까", Hangul.removeLastCharacter("깎"));
        }

        @Test
        @DisplayName("이중모음 처리 - ㅗ/ㅜ/ㅡ 계 이중모음")
        void removeFromDoubleVowel() {
            assertEquals("전호", Hangul.removeLastCharacter("전화"));
            assertEquals("예으", Hangul.removeLastCharacter("예의"));
            assertEquals("신세ㄱ", Hangul.removeLastCharacter("신세계")); // ㅖ는 단일키 처리 가능
        }

        @Test
        @DisplayName("이중모음과 종성 조합 처리")
        void removeFromDoubleVowelWithJongseong() {
            assertEquals("수화", Hangul.removeLastCharacter("수확"));
        }

        @Test
        @DisplayName("종성이 겹자음인 경우 - 끓")
        void removeFromDoubleJongseongEulp() {
            assertEquals("끌", Hangul.removeLastCharacter("끓"));
        }

        @Test
        @DisplayName("이중모음과 겹자음 조합 처리")
        void removeFromDoubleVowelAndDoubleJongseong() {
            assertEquals("완", Hangul.removeLastCharacter("왅"));
        }

        @Test
        @DisplayName("빈 문자열일 경우 빈 문자열을 반환한다")
        void removeFromEmptyString() {
            assertEquals("", Hangul.removeLastCharacter(""));
        }
    }
}

