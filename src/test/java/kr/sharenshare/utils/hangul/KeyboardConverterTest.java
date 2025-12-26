package kr.sharenshare.utils.hangul;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyboardConverterTest {

    @Nested
    @DisplayName("convertQwertyToAlphabet 테스트")
    class ConvertQwertyToAlphabetTest {

        @Test
        @DisplayName("QWERTY 입력을 한글 자모로 변환한다")
        void convertQwertyToAlphabet() {
            assertEquals("ㅁㅠㅊ", KeyboardConverter.convertQwertyToAlphabet("abc"));
            assertEquals("ㅁㅠㅊ", KeyboardConverter.convertQwertyToAlphabet("ABC"));
        }

        @Test
        @DisplayName("영어 알파벳은 한글 음소로 바꾸고, 한글 음절은 유지한다")
        void convertMixedInput() {
            assertEquals("ㅍㅡ론트", KeyboardConverter.convertQwertyToAlphabet("vm론트"));
        }

        @Test
        @DisplayName("혼합 입력을 변환한다")
        void convertMixed() {
            assertEquals("ㅍㅡ론트", KeyboardConverter.convertMixedToAlphabet("vm론트"));
        }

        @Test
        @DisplayName("대문자는 쌍자음으로 변환한다")
        void convertUppercase() {
            assertEquals("ㄲㅏㄱㄸㅜㄱㅣ", KeyboardConverter.convertQwertyToAlphabet("RkrEnrl"));
            assertEquals("ㅇㅖㅇㅡㅣ", KeyboardConverter.convertQwertyToAlphabet("ㅇPdml"));
        }

        @Test
        @DisplayName("분해된 한글 음소는 유지한다")
        void convertPreservesJamo() {
            assertEquals("ㅍㅡㄹㅗㄴㅌㅡ", KeyboardConverter.convertQwertyToAlphabet("ㅍㅡㄹㅗㄴㅌㅡ"));
        }

        @Test
        @DisplayName("영어 알파벳이 아닌 입력은 유지한다")
        void convertNonAlphabet() {
            assertEquals("4월/20ㅇㅣㄹ!", KeyboardConverter.convertQwertyToAlphabet("4월/20dlf!"));
        }

        @Test
        @DisplayName("빈 문자열은 빈 문자열을 반환한다")
        void convertEmptyString() {
            assertEquals("", KeyboardConverter.convertQwertyToAlphabet(""));
        }
    }

    @Nested
    @DisplayName("convertHangulToQwerty 테스트")
    class ConvertHangulToQwertyTest {

        @Test
        @DisplayName("한글을 QWERTY로 변환한다")
        void convertHangulToQwerty() {
            assertEquals("dkssud", KeyboardConverter.convertHangulToQwerty("안녕"));
            assertEquals("gksrmf", KeyboardConverter.convertHangulToQwerty("한글"));
            assertEquals("abc", KeyboardConverter.convertHangulToQwerty("뮻"));
            assertEquals("rush", KeyboardConverter.convertHangulToQwerty("겨노"));
            assertEquals("slash", KeyboardConverter.convertHangulToQwerty("님노"));
            // JavaScript 원본 테스트 케이스 추가 - 겹모음 처리
            assertEquals("hong gildong", KeyboardConverter.convertHangulToQwerty("ㅙㅜㅎ 햐ㅣ애ㅜㅎ"));
        }

        @Test
        @DisplayName("쌍자음 및 이중모음도 변환한다")
        void convertDoubleConsonantVowel() {
            assertEquals("WOW", KeyboardConverter.convertHangulToQwerty("쨰ㅉ"));
            assertEquals("QkWkEkRkTk", KeyboardConverter.convertHangulToQwerty("빠짜따까싸"));
            assertEquals("rhkrnjrhlrnlrml", KeyboardConverter.convertHangulToQwerty("과궈괴귀긔"));
        }

        @Test
        @DisplayName("영문, 숫자 등 한글이 아닌 글자는 그대로 유지한다")
        void convertNonHangul() {
            assertEquals("FEroqkf!", KeyboardConverter.convertHangulToQwerty("FE개발!"));
            assertEquals("seoul, korea", KeyboardConverter.convertHangulToQwerty("ㄴ대ㅕㅣ, ㅏㅐㄱㄷㅁ"));
        }

        @Test
        @DisplayName("한글 음소 또한 알파벳으로 변환한다")
        void convertJamo() {
            assertEquals("vmfhsxm", KeyboardConverter.convertHangulToQwerty("ㅍㅡㄹㅗㄴㅌㅡ"));
            assertEquals("RkrEnrl", KeyboardConverter.convertHangulToQwerty("RㅏㄱEㅜrl"));
            assertEquals("dPdml", KeyboardConverter.convertHangulToQwerty("ㅇPdml"));
        }

        @Test
        @DisplayName("빈 문자열은 빈 문자열을 반환한다")
        void convertHangulToQwertyEmpty() {
            assertEquals("", KeyboardConverter.convertHangulToQwerty(""));
        }
    }

    @Nested
    @DisplayName("convertQwertyToHangul 테스트")
    class ConvertQwertyToHangulTest {

        @Test
        @DisplayName("QWERTY 입력을 한글로 조합한다")
        void convertQwertyToHangul() {
            assertEquals("안녕", KeyboardConverter.convertQwertyToHangul("dkssud"));
            assertEquals("한글", KeyboardConverter.convertQwertyToHangul("gksrmf"));
            assertEquals("뮻", KeyboardConverter.convertQwertyToHangul("abc"));
            assertEquals("프론트엔드", KeyboardConverter.convertQwertyToHangul("vmfhsxmdpsem"));
        }

        @Test
        @DisplayName("대문자도 한글로 합성한다")
        void convertQwertyToHangulUppercase() {
            assertEquals("뮻", KeyboardConverter.convertQwertyToHangul("ABC"));
            assertEquals("프론트", KeyboardConverter.convertQwertyToHangul("VMFHSXM"));
        }

        @Test
        @DisplayName("혼합 입력을 한글로 합성한다")
        void convertQwertyToHangulMixed() {
            assertEquals("프론트", KeyboardConverter.convertQwertyToHangul("vm론트"));
        }

        @Test
        @DisplayName("한글 음소를 한글로 합성한다")
        void convertQwertyToHangulJamo() {
            assertEquals("프론트", KeyboardConverter.convertQwertyToHangul("ㅍㅡㄹㅗㄴㅌㅡ"));
        }

        @Test
        @DisplayName("대문자와 한글 음소 혼합")
        void convertQwertyToHangulMixedCase() {
            assertEquals("깍뚜기", KeyboardConverter.convertQwertyToHangul("RㅏㄱEㅜrl"));
            assertEquals("예의", KeyboardConverter.convertQwertyToHangul("ㅇPdml"));
        }

        @Test
        @DisplayName("빈 문자열은 빈 문자열을 반환한다")
        void convertQwertyToHangulEmpty() {
            assertEquals("", KeyboardConverter.convertQwertyToHangul(""));
        }
    }

    @Nested
    @DisplayName("convertQwertyToAlphabet 추가 테스트")
    class ConvertQwertyToAlphabetAdditionalTest {

        @Test
        @DisplayName("숫자와 특수문자는 유지한다")
        void convertWithNumbersAndSpecialChars() {
            assertEquals("4월/20ㅇㅣㄹ!", KeyboardConverter.convertQwertyToAlphabet("4월/20dlf!"));
        }
    }

    @Nested
    @DisplayName("convertHangulToQwerty 추가 테스트")
    class ConvertHangulToQwertyAdditionalTest {

        @Test
        @DisplayName("쌍자음 및 이중모음을 변환한다")
        void convertDoubleConsonantsAndVowels() {
            assertEquals("WOW", KeyboardConverter.convertHangulToQwerty("쨰ㅉ"));
            assertEquals("QkWkEkRkTk", KeyboardConverter.convertHangulToQwerty("빠짜따까싸"));
            assertEquals("rhkrnjrhlrnlrml", KeyboardConverter.convertHangulToQwerty("과궈괴귀긔"));
        }

        @Test
        @DisplayName("한글 음소를 알파벳으로 변환한다")
        void convertJamoToAlphabet() {
            assertEquals("vmfhsxm", KeyboardConverter.convertHangulToQwerty("ㅍㅡㄹㅗㄴㅌㅡ"));
            assertEquals("RkrEnrl", KeyboardConverter.convertHangulToQwerty("RㅏㄱEㅜrl"));
            assertEquals("dPdml", KeyboardConverter.convertHangulToQwerty("ㅇPdml"));
        }
    }
}

