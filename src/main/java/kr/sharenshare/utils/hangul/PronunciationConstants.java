package kr.sharenshare.utils.hangul;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 발음 관련 상수 클래스
 */
public final class PronunciationConstants {
    
    private PronunciationConstants() {}

    // 음가가 없는 자음
    public static final char 음가가_없는_자음 = 'ㅇ';

    // 한글 자모 이름
    public static final Set<String> 한글_자모 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("기역", "니은", "리을", "미음", "비읍", "시옷", "이응")));
    
    public static final Set<String> 특별한_한글_자모 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("디귿", "지읒", "치읓", "키읔", "티읕", "피읖", "히읗")));
    
    public static final Map<Character, Character> 특별한_한글_자모의_발음;
    static {
        Map<Character, Character> map = new HashMap<>();
        map.put('ㄷ', 'ㅅ');
        map.put('ㅈ', 'ㅅ');
        map.put('ㅊ', 'ㅅ');
        map.put('ㅌ', 'ㅅ');
        map.put('ㅎ', 'ㅅ');
        map.put('ㅋ', 'ㄱ');
        map.put('ㅍ', 'ㅂ');
        특별한_한글_자모의_발음 = Collections.unmodifiableMap(map);
    }

    // 17항 - 구개음화
    public static final Map<String, Character> 음의_동화_받침;
    static {
        Map<String, Character> map = new HashMap<>();
        map.put("ㄷ", 'ㅈ');
        map.put("ㅌ", 'ㅊ');
        map.put("ㄹㅌ", 'ㅊ');
        음의_동화_받침 = Collections.unmodifiableMap(map);
    }

    // ㄴㄹ이 덧나는 동화작용
    public static final Set<Character> ㄴㄹ이_덧나는_모음 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList('ㅏ', 'ㅐ', 'ㅓ', 'ㅔ', 'ㅗ', 'ㅜ', 'ㅟ')));
    
    public static final Set<Character> ㄴㄹ이_덧나는_후속음절_모음 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList('ㅑ', 'ㅕ', 'ㅛ', 'ㅠ', 'ㅣ', 'ㅒ', 'ㅖ')));
    
    public static final Set<String> ㄴㄹ이_덧나서_받침_ㄴ_변환 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㄱ", "ㄴ", "ㄷ", "ㅁ", "ㅂ", "ㅇ")));
    
    public static final Set<String> ㄴㄹ이_덧나서_받침_ㄹ_변환 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㄹ")));

    // 19항
    public static final Set<String> 자음동화_받침_ㄴ_변환 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㅁ", "ㅇ", "ㄱ", "ㅂ")));

    // 18항 - 비음화
    public static final Set<String> 비음화_받침_ㅇ_변환 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㄱ", "ㄲ", "ㅋ", "ㄱㅅ", "ㄹㄱ")));
    
    public static final Set<String> 비음화_받침_ㄴ_변환 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㄷ", "ㅅ", "ㅆ", "ㅈ", "ㅊ", "ㅌ", "ㅎ")));
    
    public static final Set<String> 비음화_받침_ㅁ_변환 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㅂ", "ㅍ", "ㄹㅂ", "ㄹㅍ", "ㅂㅅ")));

    // 12항 - 받침 ㅎ 발음
    public static final Set<String> 발음변환_받침_ㅎ = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㅎ", "ㄴㅎ", "ㄹㅎ")));
    
    public static final Map<Character, Character> 발음변환_받침_ㅎ_발음;
    static {
        Map<Character, Character> map = new HashMap<>();
        map.put('ㄱ', 'ㅋ');
        map.put('ㄷ', 'ㅌ');
        map.put('ㅈ', 'ㅊ');
        map.put('ㅅ', 'ㅆ');
        발음변환_받침_ㅎ_발음 = Collections.unmodifiableMap(map);
    }
    
    public static final Set<String> 발음변환_첫소리_ㅎ = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㄱ", "ㄹㄱ", "ㄷ", "ㅂ", "ㄹㅂ", "ㅈ", "ㄴㅈ")));
    
    public static final Map<String, Character> 발음변환_첫소리_ㅎ_발음;
    static {
        Map<String, Character> map = new HashMap<>();
        map.put("ㄱ", 'ㅋ');
        map.put("ㄹㄱ", 'ㅋ');
        map.put("ㄷ", 'ㅌ');
        map.put("ㅂ", 'ㅍ');
        map.put("ㄹㅂ", 'ㅍ');
        map.put("ㅈ", 'ㅊ');
        map.put("ㄴㅈ", 'ㅊ');
        발음변환_첫소리_ㅎ_발음 = Collections.unmodifiableMap(map);
    }

    // 9항, 10항, 11항 - 대표음 발음
    public static final Map<String, String> 받침_대표음_발음;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("ㄲ", "ㄱ");
        map.put("ㅋ", "ㄱ");
        map.put("ㄱㅅ", "ㄱ");
        map.put("ㄹㄱ", "ㄱ");
        map.put("ㅅ", "ㄷ");
        map.put("ㅆ", "ㄷ");
        map.put("ㅈ", "ㄷ");
        map.put("ㅊ", "ㄷ");
        map.put("ㅌ", "ㄷ");
        map.put("ㅍ", "ㅂ");
        map.put("ㅂㅅ", "ㅂ");
        map.put("ㄹㅍ", "ㅂ");
        map.put("ㄴㅈ", "ㄴ");
        map.put("ㄹㅂ", "ㄹ");
        map.put("ㄹㅅ", "ㄹ");
        map.put("ㄹㅌ", "ㄹ");
        map.put("ㄹㅁ", "ㅁ");
        받침_대표음_발음 = Collections.unmodifiableMap(map);
    }

    // 된소리
    public static final Map<Character, Character> 된소리;
    static {
        Map<Character, Character> map = new HashMap<>();
        map.put('ㄱ', 'ㄲ');
        map.put('ㄷ', 'ㄸ');
        map.put('ㅂ', 'ㅃ');
        map.put('ㅅ', 'ㅆ');
        map.put('ㅈ', 'ㅉ');
        된소리 = Collections.unmodifiableMap(map);
    }

    // 23항 - 된소리 받침
    public static final Set<String> 된소리_받침 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList(
            "ㄱ", "ㄲ", "ㅋ", "ㄱㅅ", "ㄹㄱ",
            "ㄷ", "ㅅ", "ㅆ", "ㅈ", "ㅊ", "ㅌ",
            "ㅂ", "ㅍ", "ㄹㅂ", "ㄹㅍ", "ㅂㅅ")));

    // 24항, 25항 - 어간 받침
    public static final Set<String> 어간_받침 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("ㄴ", "ㄴㅈ", "ㅁ", "ㄹㅁ", "ㄹㅂ", "ㄹㅌ")));

    // 자음군 단순화
    public static final Set<String> 자음군_단순화 = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList(
            "ㄹㅁ", "ㄱㅅ", "ㄹㄱ", "ㄹㅂ", "ㄹㅅ", "ㅂㅅ",
            "ㄴㅈ", "ㄴㅎ", "ㄹㅌ", "ㄹㅍ", "ㄹㅎ")));
    
    public static final Map<String, String> 자음군_단순화_결과;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("ㄱㅅ", "ㄱ");
        map.put("ㄴㅈ", "ㄴ");
        map.put("ㄴㅎ", "ㄴ");
        map.put("ㄹㄱ", "ㄱ");
        map.put("ㄹㅁ", "ㅁ");
        map.put("ㄹㅂ", "ㄹ");
        map.put("ㄹㅅ", "ㄹ");
        map.put("ㄹㅌ", "ㄹ");
        map.put("ㄹㅍ", "ㄹ");
        map.put("ㄹㅎ", "ㄹ");
        map.put("ㅂㅅ", "ㅂ");
        자음군_단순화_결과 = Collections.unmodifiableMap(map);
    }

    // 사이시옷 예외 목록
    public static final Map<String, String> 사이시옷_예외사항_목록;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("베갯잇", "베갠닏");
        map.put("깻잎", "깬닙");
        map.put("나뭇잎", "나문닙");
        map.put("도리깻열", "도리깬녈");
        map.put("뒷윷", "뒨뉻");
        사이시옷_예외사항_목록 = Collections.unmodifiableMap(map);
    }

    // 단일어 예외사항
    public static final Map<String, String> 단일어_예외사항_단어모음;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("전역", "저녁");
        단일어_예외사항_단어모음 = Collections.unmodifiableMap(map);
    }

    // 로마자 표기 상수
    public static final Map<Character, String> 초성_알파벳_발음;
    static {
        Map<Character, String> map = new HashMap<>();
        map.put('ㄱ', "g");
        map.put('ㄲ', "kk");
        map.put('ㅋ', "k");
        map.put('ㄷ', "d");
        map.put('ㄸ', "tt");
        map.put('ㅌ', "t");
        map.put('ㅂ', "b");
        map.put('ㅃ', "pp");
        map.put('ㅍ', "p");
        map.put('ㅈ', "j");
        map.put('ㅉ', "jj");
        map.put('ㅊ', "ch");
        map.put('ㅅ', "s");
        map.put('ㅆ', "ss");
        map.put('ㅎ', "h");
        map.put('ㄴ', "n");
        map.put('ㅁ', "m");
        map.put('ㅇ', "");
        map.put('ㄹ', "r");
        초성_알파벳_발음 = Collections.unmodifiableMap(map);
    }

    public static final Map<Character, String> 중성_알파벳_발음;
    static {
        Map<Character, String> map = new HashMap<>();
        // 단모음
        map.put('ㅏ', "a");
        map.put('ㅓ', "eo");
        map.put('ㅗ', "o");
        map.put('ㅜ', "u");
        map.put('ㅡ', "eu");
        map.put('ㅣ', "i");
        map.put('ㅐ', "ae");
        map.put('ㅔ', "e");
        map.put('ㅚ', "oe");
        map.put('ㅟ', "wi");
        // 이중모음
        map.put('ㅑ', "ya");
        map.put('ㅕ', "yeo");
        map.put('ㅛ', "yo");
        map.put('ㅠ', "yu");
        map.put('ㅒ', "yae");
        map.put('ㅖ', "ye");
        map.put('ㅘ', "wa");
        map.put('ㅙ', "wae");
        map.put('ㅝ', "wo");
        map.put('ㅞ', "we");
        map.put('ㅢ', "ui");
        중성_알파벳_발음 = Collections.unmodifiableMap(map);
    }

    public static final Map<String, String> 종성_알파벳_발음;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("ㄱ", "k");
        map.put("ㄴ", "n");
        map.put("ㄷ", "t");
        map.put("ㄹ", "l");
        map.put("ㅁ", "m");
        map.put("ㅂ", "p");
        map.put("ㅇ", "ng");
        map.put("", "");
        종성_알파벳_발음 = Collections.unmodifiableMap(map);
    }
}

