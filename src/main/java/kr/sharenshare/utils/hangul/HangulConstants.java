package kr.sharenshare.utils.hangul;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 한글 유니코드 및 자모 상수 정의
 */
public final class HangulConstants {

    private HangulConstants() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    // 한글 유니코드 범위
    public static final char HANGUL_START = '\uAC00'; // 가
    public static final char HANGUL_END = '\uD7A3';   // 힣

    // 자모 유니코드 범위
    public static final char JAMO_START = '\u1100';
    public static final char COMPAT_JAMO_START = '\u3131'; // ㄱ
    public static final char COMPAT_JAMO_END = '\u3163';   // ㅣ

    // 초성 19개
    public static final char[] CHOSEONG = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
            'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    // 중성 21개
    public static final char[] JUNGSEONG = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
            'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };

    // 종성 28개 (첫 번째는 받침 없음)
    public static final char[] JONGSEONG = {
            '\0', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
            'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    public static final int CHOSEONG_COUNT = 19;
    public static final int JUNGSEONG_COUNT = 21;
    public static final int JONGSEONG_COUNT = 28;

    // 초성으로 사용 가능한 문자 Set
    public static final Set<Character> CHOSEONG_SET;
    // 중성으로 사용 가능한 문자 Set
    public static final Set<Character> JUNGSEONG_SET;
    // 종성으로 사용 가능한 문자 Set (받침 없음 제외)
    public static final Set<Character> JONGSEONG_SET;

    // 겹자음 분해 맵
    public static final Map<Character, String> DOUBLE_CONSONANT_MAP;
    // 겹모음 분해 맵
    public static final Map<Character, String> DOUBLE_VOWEL_MAP;

    // 겹자음 조합 맵 (분해된 문자열 -> 겹자음)
    public static final Map<String, Character> DOUBLE_CONSONANT_COMBINE_MAP;
    // 겹모음 조합 맵 (분해된 문자열 -> 겹모음)
    public static final Map<String, Character> DOUBLE_VOWEL_COMBINE_MAP;

    // QWERTY 키보드와 한글 자모 매핑
    public static final Map<Character, Character> QWERTY_TO_HANGUL_MAP;
    public static final Map<Character, Character> HANGUL_TO_QWERTY_MAP;

    static {
        // 초성 Set 초기화
        Set<Character> choseongSet = new HashSet<Character>();
        for (char c : CHOSEONG) {
            choseongSet.add(c);
        }
        CHOSEONG_SET = Collections.unmodifiableSet(choseongSet);

        // 중성 Set 초기화
        Set<Character> jungseongSet = new HashSet<Character>();
        for (char c : JUNGSEONG) {
            jungseongSet.add(c);
        }
        JUNGSEONG_SET = Collections.unmodifiableSet(jungseongSet);

        // 종성 Set 초기화 (받침 없음 '\0' 제외)
        Set<Character> jongseongSet = new HashSet<Character>();
        for (int i = 1; i < JONGSEONG.length; i++) {
            jongseongSet.add(JONGSEONG[i]);
        }
        JONGSEONG_SET = Collections.unmodifiableSet(jongseongSet);

        // 겹자음 분해 맵 초기화 (겹받침만 분해, 쌍자음은 분해하지 않음)
        // 쌍자음(ㄲ, ㄸ, ㅃ, ㅆ, ㅉ)은 분해하지 않음
        Map<Character, String> doubleConsonantMap = new HashMap<Character, String>();
        doubleConsonantMap.put('ㄳ', "ㄱㅅ");
        doubleConsonantMap.put('ㄵ', "ㄴㅈ");
        doubleConsonantMap.put('ㄶ', "ㄴㅎ");
        doubleConsonantMap.put('ㄺ', "ㄹㄱ");
        doubleConsonantMap.put('ㄻ', "ㄹㅁ");
        doubleConsonantMap.put('ㄼ', "ㄹㅂ");
        doubleConsonantMap.put('ㄽ', "ㄹㅅ");
        doubleConsonantMap.put('ㄾ', "ㄹㅌ");
        doubleConsonantMap.put('ㄿ', "ㄹㅍ");
        doubleConsonantMap.put('ㅀ', "ㄹㅎ");
        doubleConsonantMap.put('ㅄ', "ㅂㅅ");
        DOUBLE_CONSONANT_MAP = Collections.unmodifiableMap(doubleConsonantMap);

        // 겹모음 분해 맵 초기화
        Map<Character, String> doubleVowelMap = new HashMap<Character, String>();
        doubleVowelMap.put('ㅘ', "ㅗㅏ");
        doubleVowelMap.put('ㅙ', "ㅗㅐ");
        doubleVowelMap.put('ㅚ', "ㅗㅣ");
        doubleVowelMap.put('ㅝ', "ㅜㅓ");
        doubleVowelMap.put('ㅞ', "ㅜㅔ");
        doubleVowelMap.put('ㅟ', "ㅜㅣ");
        doubleVowelMap.put('ㅢ', "ㅡㅣ");
        DOUBLE_VOWEL_MAP = Collections.unmodifiableMap(doubleVowelMap);

        // 겹자음 조합 맵 초기화
        Map<String, Character> doubleConsonantCombineMap = new HashMap<String, Character>();
        for (Map.Entry<Character, String> entry : doubleConsonantMap.entrySet()) {
            doubleConsonantCombineMap.put(entry.getValue(), entry.getKey());
        }
        DOUBLE_CONSONANT_COMBINE_MAP = Collections.unmodifiableMap(doubleConsonantCombineMap);

        // 겹모음 조합 맵 초기화
        Map<String, Character> doubleVowelCombineMap = new HashMap<String, Character>();
        for (Map.Entry<Character, String> entry : doubleVowelMap.entrySet()) {
            doubleVowelCombineMap.put(entry.getValue(), entry.getKey());
        }
        DOUBLE_VOWEL_COMBINE_MAP = Collections.unmodifiableMap(doubleVowelCombineMap);

        // QWERTY to Hangul 매핑 초기화
        Map<Character, Character> qwertyToHangul = new HashMap<Character, Character>();
        qwertyToHangul.put('q', 'ㅂ'); qwertyToHangul.put('Q', 'ㅃ');
        qwertyToHangul.put('w', 'ㅈ'); qwertyToHangul.put('W', 'ㅉ');
        qwertyToHangul.put('e', 'ㄷ'); qwertyToHangul.put('E', 'ㄸ');
        qwertyToHangul.put('r', 'ㄱ'); qwertyToHangul.put('R', 'ㄲ');
        qwertyToHangul.put('t', 'ㅅ'); qwertyToHangul.put('T', 'ㅆ');
        qwertyToHangul.put('y', 'ㅛ'); qwertyToHangul.put('Y', 'ㅛ');
        qwertyToHangul.put('u', 'ㅕ'); qwertyToHangul.put('U', 'ㅕ');
        qwertyToHangul.put('i', 'ㅑ'); qwertyToHangul.put('I', 'ㅑ');
        qwertyToHangul.put('o', 'ㅐ'); qwertyToHangul.put('O', 'ㅒ');
        qwertyToHangul.put('p', 'ㅔ'); qwertyToHangul.put('P', 'ㅖ');
        qwertyToHangul.put('a', 'ㅁ'); qwertyToHangul.put('A', 'ㅁ');
        qwertyToHangul.put('s', 'ㄴ'); qwertyToHangul.put('S', 'ㄴ');
        qwertyToHangul.put('d', 'ㅇ'); qwertyToHangul.put('D', 'ㅇ');
        qwertyToHangul.put('f', 'ㄹ'); qwertyToHangul.put('F', 'ㄹ');
        qwertyToHangul.put('g', 'ㅎ'); qwertyToHangul.put('G', 'ㅎ');
        qwertyToHangul.put('h', 'ㅗ'); qwertyToHangul.put('H', 'ㅗ');
        qwertyToHangul.put('j', 'ㅓ'); qwertyToHangul.put('J', 'ㅓ');
        qwertyToHangul.put('k', 'ㅏ'); qwertyToHangul.put('K', 'ㅏ');
        qwertyToHangul.put('l', 'ㅣ'); qwertyToHangul.put('L', 'ㅣ');
        qwertyToHangul.put('z', 'ㅋ'); qwertyToHangul.put('Z', 'ㅋ');
        qwertyToHangul.put('x', 'ㅌ'); qwertyToHangul.put('X', 'ㅌ');
        qwertyToHangul.put('c', 'ㅊ'); qwertyToHangul.put('C', 'ㅊ');
        qwertyToHangul.put('v', 'ㅍ'); qwertyToHangul.put('V', 'ㅍ');
        qwertyToHangul.put('b', 'ㅠ'); qwertyToHangul.put('B', 'ㅠ');
        qwertyToHangul.put('n', 'ㅜ'); qwertyToHangul.put('N', 'ㅜ');
        qwertyToHangul.put('m', 'ㅡ'); qwertyToHangul.put('M', 'ㅡ');
        QWERTY_TO_HANGUL_MAP = Collections.unmodifiableMap(qwertyToHangul);

        // Hangul to QWERTY 매핑 초기화 (소문자 기준)
        Map<Character, Character> hangulToQwerty = new HashMap<Character, Character>();
        hangulToQwerty.put('ㅂ', 'q'); hangulToQwerty.put('ㅃ', 'Q');
        hangulToQwerty.put('ㅈ', 'w'); hangulToQwerty.put('ㅉ', 'W');
        hangulToQwerty.put('ㄷ', 'e'); hangulToQwerty.put('ㄸ', 'E');
        hangulToQwerty.put('ㄱ', 'r'); hangulToQwerty.put('ㄲ', 'R');
        hangulToQwerty.put('ㅅ', 't'); hangulToQwerty.put('ㅆ', 'T');
        hangulToQwerty.put('ㅛ', 'y');
        hangulToQwerty.put('ㅕ', 'u');
        hangulToQwerty.put('ㅑ', 'i');
        hangulToQwerty.put('ㅐ', 'o'); hangulToQwerty.put('ㅒ', 'O');
        hangulToQwerty.put('ㅔ', 'p'); hangulToQwerty.put('ㅖ', 'P');
        hangulToQwerty.put('ㅁ', 'a');
        hangulToQwerty.put('ㄴ', 's');
        hangulToQwerty.put('ㅇ', 'd');
        hangulToQwerty.put('ㄹ', 'f');
        hangulToQwerty.put('ㅎ', 'g');
        hangulToQwerty.put('ㅗ', 'h');
        hangulToQwerty.put('ㅓ', 'j');
        hangulToQwerty.put('ㅏ', 'k');
        hangulToQwerty.put('ㅣ', 'l');
        hangulToQwerty.put('ㅋ', 'z');
        hangulToQwerty.put('ㅌ', 'x');
        hangulToQwerty.put('ㅊ', 'c');
        hangulToQwerty.put('ㅍ', 'v');
        hangulToQwerty.put('ㅠ', 'b');
        hangulToQwerty.put('ㅜ', 'n');
        hangulToQwerty.put('ㅡ', 'm');
        HANGUL_TO_QWERTY_MAP = Collections.unmodifiableMap(hangulToQwerty);
    }

    /**
     * 초성 인덱스 반환
     */
    public static int getChoseongIndex(char c) {
        for (int i = 0; i < CHOSEONG.length; i++) {
            if (CHOSEONG[i] == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 중성 인덱스 반환
     */
    public static int getJungseongIndex(char c) {
        for (int i = 0; i < JUNGSEONG.length; i++) {
            if (JUNGSEONG[i] == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 종성 인덱스 반환
     */
    public static int getJongseongIndex(char c) {
        for (int i = 0; i < JONGSEONG.length; i++) {
            if (JONGSEONG[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
