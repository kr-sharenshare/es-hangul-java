package kr.sharenshare.utils.hangul;

import java.util.ArrayList;
import java.util.List;

/**
 * 한글 처리를 위한 유틸리티 클래스
 * es-hangul 라이브러리의 Java 구현
 */
public final class Hangul {

    private Hangul() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    /**
     * 한글 완성형 문자인지 확인
     */
    public static boolean isHangul(char c) {
        return c >= HangulConstants.HANGUL_START && c <= HangulConstants.HANGUL_END;
    }

    /**
     * 한글 완성형 문자열인지 확인 (모든 문자가 한글이거나 공백인 경우)
     */
    public static boolean isHangulString(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!isHangul(c) && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 호환 자모인지 확인 (ㄱ~ㅣ)
     */
    public static boolean isCompatJamo(char c) {
        return c >= HangulConstants.COMPAT_JAMO_START && c <= HangulConstants.COMPAT_JAMO_END;
    }

    /**
     * 한글 완성형 문자 분해 (초성, 중성, 종성)
     */
    public static HangulChar disassembleCompleteCharacter(char c) {
        if (!isHangul(c)) {
            return null;
        }

        int code = c - HangulConstants.HANGUL_START;
        int choseongIndex = code / (HangulConstants.JUNGSEONG_COUNT * HangulConstants.JONGSEONG_COUNT);
        int jungseongIndex = (code % (HangulConstants.JUNGSEONG_COUNT * HangulConstants.JONGSEONG_COUNT)) / HangulConstants.JONGSEONG_COUNT;
        int jongseongIndex = code % HangulConstants.JONGSEONG_COUNT;

        return new HangulChar(
                HangulConstants.CHOSEONG[choseongIndex],
                HangulConstants.JUNGSEONG[jungseongIndex],
                jongseongIndex > 0 ? HangulConstants.JONGSEONG[jongseongIndex] : null
        );
    }

    /**
     * 한글 완성형 문자의 종성을 문자열로 반환 (겹받침 포함)
     * 예: '값' -> "ㅂㅅ", '박' -> "ㄱ", '가' -> ""
     */
    public static String getJongseongAsString(char c) {
        HangulChar hc = disassembleCompleteCharacter(c);
        if (hc == null || hc.getJongseong() == null) {
            return "";
        }
        Character jongseongChar = hc.getJongseong();
        // 겹받침인 경우 분해
        String decomposed = HangulConstants.DOUBLE_CONSONANT_MAP.get(jongseongChar);
        if (decomposed != null) {
            return decomposed;
        }
        return String.valueOf(jongseongChar);
    }

    /**
     * 한글 완성형 문자 분해 (deprecated, disassembleCompleteCharacter 사용 권장)
     */
    @Deprecated
    public static HangulChar disassemble(char c) {
        return disassembleCompleteCharacter(c);
    }

    /**
     * 한글 문자열을 자모로 분해
     * 예: "값" -> "ㄱㅏㅂㅅ"
     */
    public static String disassemble(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (isHangul(c)) {
                HangulChar hangulChar = disassembleCompleteCharacter(c);
                if (hangulChar != null) {
                    // 초성은 쌍자음도 분해하지 않음
                    sb.append(hangulChar.getChoseong());
                    // 중성은 겹모음 분해
                    sb.append(disassembleVowel(hangulChar.getJungseong()));
                    // 종성은 겹자음 분해 (쌍자음 포함)
                    if (hangulChar.getJongseong() != null) {
                        sb.append(disassembleConsonant(hangulChar.getJongseong()));
                    }
                }
            } else if (isCompatJamo(c)) {
                // 호환 자모는 겹자음/겹모음 분해
                sb.append(disassembleJamo(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 자음을 분해 (겹자음만, 쌍자음은 분해하지 않음)
     */
    private static String disassembleConsonant(char c) {
        String doubleConsonant = HangulConstants.DOUBLE_CONSONANT_MAP.get(c);
        if (doubleConsonant != null) {
            return doubleConsonant;
        }
        return String.valueOf(c);
    }

    /**
     * 모음을 분해 (겹모음)
     */
    private static String disassembleVowel(char c) {
        String doubleVowel = HangulConstants.DOUBLE_VOWEL_MAP.get(c);
        if (doubleVowel != null) {
            return doubleVowel;
        }
        return String.valueOf(c);
    }

    /**
     * 단일 자모를 분해 (겹자음, 겹모음 분해)
     */
    private static String disassembleJamo(char c) {
        String doubleConsonant = HangulConstants.DOUBLE_CONSONANT_MAP.get(c);
        if (doubleConsonant != null) {
            return doubleConsonant;
        }
        String doubleVowel = HangulConstants.DOUBLE_VOWEL_MAP.get(c);
        if (doubleVowel != null) {
            return doubleVowel;
        }
        return String.valueOf(c);
    }

    /**
     * 한글 문자열을 그룹별로 분해
     * 예: "사과" -> [["ㅅ", "ㅏ"], ["ㄱ", "ㅗ", "ㅏ"]]
     */
    public static List<List<String>> disassembleToGroups(String str) {
        if (str == null || str.isEmpty()) {
            return new ArrayList<List<String>>();
        }

        List<List<String>> result = new ArrayList<List<String>>();
        for (char c : str.toCharArray()) {
            List<String> group = new ArrayList<String>();
            if (isHangul(c)) {
                HangulChar hangulChar = disassembleCompleteCharacter(c);
                if (hangulChar != null) {
                    // 초성
                    addDisassembledJamo(group, hangulChar.getChoseong());
                    // 중성
                    addDisassembledJamo(group, hangulChar.getJungseong());
                    // 종성
                    if (hangulChar.getJongseong() != null) {
                        addDisassembledJamo(group, hangulChar.getJongseong());
                    }
                }
            } else if (isCompatJamo(c)) {
                addDisassembledJamo(group, c);
            } else {
                group.add(String.valueOf(c));
            }
            result.add(group);
        }
        return result;
    }

    /**
     * 자모를 분해하여 그룹에 추가
     */
    private static void addDisassembledJamo(List<String> group, char c) {
        String doubleConsonant = HangulConstants.DOUBLE_CONSONANT_MAP.get(c);
        if (doubleConsonant != null) {
            for (char ch : doubleConsonant.toCharArray()) {
                group.add(String.valueOf(ch));
            }
            return;
        }
        String doubleVowel = HangulConstants.DOUBLE_VOWEL_MAP.get(c);
        if (doubleVowel != null) {
            for (char ch : doubleVowel.toCharArray()) {
                group.add(String.valueOf(ch));
            }
            return;
        }
        group.add(String.valueOf(c));
    }

    /**
     * 한글 조합
     */
    public static char assemble(int choseongIndex, int jungseongIndex, int jongseongIndex) {
        return (char) (HangulConstants.HANGUL_START
                + (choseongIndex * HangulConstants.JUNGSEONG_COUNT * HangulConstants.JONGSEONG_COUNT)
                + (jungseongIndex * HangulConstants.JONGSEONG_COUNT)
                + jongseongIndex);
    }

    /**
     * 자모 배열을 한글 문자열로 조합
     * 예: ["ㅇ", "ㅏ", "ㅂ", "ㅓ", "ㅈ", "ㅣ"] -> "아버지"
     */
    public static String assemble(String[] jamos) {
        if (jamos == null || jamos.length == 0) {
            return "";
        }
        StringBuilder combined = new StringBuilder();
        for (String jamo : jamos) {
            combined.append(jamo);
        }
        return assembleString(combined.toString());
    }

    /**
     * 자모 리스트를 한글 문자열로 조합
     */
    public static String assemble(List<String> jamos) {
        if (jamos == null || jamos.isEmpty()) {
            return "";
        }
        StringBuilder combined = new StringBuilder();
        for (String jamo : jamos) {
            combined.append(jamo);
        }
        return assembleString(combined.toString());
    }

    /**
     * 자모 문자열을 한글로 조합
     */
    public static String assembleString(String jamoString) {
        if (jamoString == null || jamoString.isEmpty()) {
            return jamoString;
        }

        StringBuilder result = new StringBuilder();
        int i = 0;
        int len = jamoString.length();

        while (i < len) {
            char current = jamoString.charAt(i);

            // 한글 완성형이면 그대로 추가
            if (isHangul(current)) {
                result.append(current);
                i++;
                continue;
            }

            // 초성이 아니면 그대로 추가
            if (!canBeChoseong(current)) {
                result.append(current);
                i++;
                continue;
            }

            // 다음 문자가 없거나 중성이 아니면 그대로 추가
            if (i + 1 >= len) {
                result.append(current);
                i++;
                continue;
            }

            char next = jamoString.charAt(i + 1);
            
            // 겹모음 확인
            Character jungseong = null;
            int jungseongLen = 1;
            if (i + 2 < len) {
                String doubleVowel = String.valueOf(next) + jamoString.charAt(i + 2);
                Character combined = HangulConstants.DOUBLE_VOWEL_COMBINE_MAP.get(doubleVowel);
                if (combined != null && canBeJungseong(combined)) {
                    jungseong = combined;
                    jungseongLen = 2;
                }
            }
            if (jungseong == null && canBeJungseong(next)) {
                jungseong = next;
            }

            if (jungseong == null) {
                result.append(current);
                i++;
                continue;
            }

            int choseongIndex = HangulConstants.getChoseongIndex(current);
            int jungseongIndex = HangulConstants.getJungseongIndex(jungseong);

            if (choseongIndex < 0 || jungseongIndex < 0) {
                result.append(current);
                i++;
                continue;
            }

            i += 1 + jungseongLen;

            // 종성 확인
            int jongseongIndex = 0;
            if (i < len) {
                char possibleJongseong = jamoString.charAt(i);
                
                // 겹받침 확인
                Character jongseong = null;
                int jongseongLen = 1;
                if (i + 1 < len) {
                    String doubleConsonant = String.valueOf(possibleJongseong) + jamoString.charAt(i + 1);
                    Character combined = HangulConstants.DOUBLE_CONSONANT_COMBINE_MAP.get(doubleConsonant);
                    if (combined != null && canBeJongseong(combined)) {
                        // 다음 문자가 중성이면 겹받침 사용 안함
                        if (i + 2 < len && canBeJungseong(jamoString.charAt(i + 2))) {
                            // 겹받침 중 뒤 자음만 다음 글자 초성으로
                        } else {
                            jongseong = combined;
                            jongseongLen = 2;
                        }
                    }
                }
                if (jongseong == null && canBeJongseong(possibleJongseong)) {
                    jongseong = possibleJongseong;
                }

                if (jongseong != null) {
                    // 다음에 중성이 오면 종성을 다음 글자 초성으로
                    int nextIdx = i + jongseongLen;
                    if (nextIdx < len && canBeJungseong(jamoString.charAt(nextIdx))) {
                        if (jongseongLen == 2) {
                            // 겹받침이면 앞 자음만 종성으로, 뒤 자음은 다음 초성으로
                            jongseongIndex = HangulConstants.getJongseongIndex(possibleJongseong);
                            if (jongseongIndex > 0) {
                                i++;
                            }
                        }
                        // jongseongLen == 1이면 종성 없이 진행
                    } else {
                        jongseongIndex = HangulConstants.getJongseongIndex(jongseong);
                        if (jongseongIndex > 0) {
                            i += jongseongLen;
                        }
                    }
                }
            }

            char assembled = assemble(choseongIndex, jungseongIndex, jongseongIndex);
            result.append(assembled);
        }

        return result.toString();
    }

    /**
     * 초성 추출
     */
    public static String getChoseong(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (isHangul(c)) {
                int code = c - HangulConstants.HANGUL_START;
                int choseongIndex = code / (HangulConstants.JUNGSEONG_COUNT * HangulConstants.JONGSEONG_COUNT);
                sb.append(HangulConstants.CHOSEONG[choseongIndex]);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 받침 유무 확인
     */
    public static boolean hasBatchim(char c) {
        if (!isHangul(c)) {
            return false;
        }
        int code = c - HangulConstants.HANGUL_START;
        return (code % HangulConstants.JONGSEONG_COUNT) != 0;
    }

    /**
     * 받침 유무 확인 (옵션: 홑받침/겹받침 구분)
     */
    public static boolean hasBatchim(char c, BatchimOption option) {
        if (!isHangul(c)) {
            return false;
        }
        int code = c - HangulConstants.HANGUL_START;
        int jongseongIndex = code % HangulConstants.JONGSEONG_COUNT;
        
        if (jongseongIndex == 0) {
            return false;
        }

        if (option == null) {
            return true;
        }

        char jongseong = HangulConstants.JONGSEONG[jongseongIndex];
        boolean isDouble = HangulConstants.DOUBLE_CONSONANT_MAP.containsKey(jongseong);

        switch (option) {
            case SINGLE:
                return !isDouble;
            case DOUBLE:
                return isDouble;
            default:
                return true;
        }
    }

    /**
     * 받침 옵션
     */
    public enum BatchimOption {
        SINGLE,  // 홑받침만
        DOUBLE   // 겹받침만
    }

    /**
     * 문자가 초성으로 사용 가능한지 확인
     */
    public static boolean canBeChoseong(char c) {
        return HangulConstants.CHOSEONG_SET.contains(c);
    }

    /**
     * 문자열이 초성으로 사용 가능한지 확인
     */
    public static boolean canBeChoseong(String str) {
        if (str == null || str.length() != 1) {
            return false;
        }
        return canBeChoseong(str.charAt(0));
    }

    /**
     * 문자가 중성으로 사용 가능한지 확인
     */
    public static boolean canBeJungseong(char c) {
        return HangulConstants.JUNGSEONG_SET.contains(c);
    }

    /**
     * 문자열이 중성으로 사용 가능한지 확인 (겹모음 포함)
     */
    public static boolean canBeJungseong(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if (str.length() == 1) {
            return canBeJungseong(str.charAt(0));
        }
        if (str.length() == 2) {
            return HangulConstants.DOUBLE_VOWEL_COMBINE_MAP.containsKey(str);
        }
        return false;
    }

    /**
     * 문자가 종성으로 사용 가능한지 확인
     */
    public static boolean canBeJongseong(char c) {
        return HangulConstants.JONGSEONG_SET.contains(c);
    }

    /**
     * 문자열이 종성으로 사용 가능한지 확인 (겹받침 포함)
     */
    public static boolean canBeJongseong(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if (str.length() == 1) {
            return canBeJongseong(str.charAt(0));
        }
        if (str.length() == 2) {
            return HangulConstants.DOUBLE_CONSONANT_COMBINE_MAP.containsKey(str);
        }
        return false;
    }

    /**
     * 초성 검색 (초성이 포함되어 있는지)
     */
    public static boolean choseongIncludes(String str, String choseong) {
        return getChoseong(str).contains(choseong);
    }

    /**
     * 초성, 중성, 종성을 조합하여 한글 문자 생성
     * 예: combineCharacter('ㄱ', 'ㅏ', 'ㅂㅅ') -> '값'
     */
    public static char combineCharacter(char choseong, char jungseong, String jongseong) {
        if (!canBeChoseong(choseong)) {
            throw new IllegalArgumentException("Invalid hangul Characters: " + choseong + ", " + jungseong + ", " + jongseong);
        }
        if (!canBeJungseong(jungseong)) {
            throw new IllegalArgumentException("Invalid hangul Characters: " + choseong + ", " + jungseong + ", " + jongseong);
        }
        if (jongseong != null && !jongseong.isEmpty() && !canBeJongseong(jongseong)) {
            throw new IllegalArgumentException("Invalid hangul Characters: " + choseong + ", " + jungseong + ", " + jongseong);
        }

        int choseongIndex = HangulConstants.getChoseongIndex(choseong);
        int jungseongIndex = HangulConstants.getJungseongIndex(jungseong);
        int jongseongIndex = 0;
        
        if (jongseong != null && !jongseong.isEmpty()) {
            if (jongseong.length() == 1) {
                jongseongIndex = HangulConstants.getJongseongIndex(jongseong.charAt(0));
            } else {
                // 겹받침 조합 (예: "ㅂㅅ" -> 'ㅄ')
                Character combined = HangulConstants.DOUBLE_CONSONANT_COMBINE_MAP.get(jongseong);
                if (combined != null) {
                    jongseongIndex = HangulConstants.getJongseongIndex(combined);
                }
            }
        }

        return assemble(choseongIndex, jungseongIndex, jongseongIndex);
    }

    /**
     * 초성, 중성으로 한글 문자 생성 (종성 없음)
     */
    public static char combineCharacter(char choseong, char jungseong) {
        return combineCharacter(choseong, jungseong, null);
    }

    /**
     * 문자열로 초성, 중성, 종성을 받아 한글 문자 생성
     */
    public static String combineCharacter(String choseong, String jungseong, String jongseong) {
        if (choseong == null || choseong.length() != 1) {
            throw new IllegalArgumentException("Invalid hangul Characters: " + choseong + ", " + jungseong + ", " + (jongseong != null ? jongseong : ""));
        }
        if (jungseong == null || jungseong.isEmpty()) {
            throw new IllegalArgumentException("Invalid hangul Characters: " + choseong + ", " + jungseong + ", " + (jongseong != null ? jongseong : ""));
        }
        
        // 겹모음 처리 (예: "ㅗㅏ" -> 'ㅘ')
        char jungseongChar;
        if (jungseong.length() == 1) {
            jungseongChar = jungseong.charAt(0);
        } else {
            Character combined = HangulConstants.DOUBLE_VOWEL_COMBINE_MAP.get(jungseong);
            if (combined != null) {
                jungseongChar = combined;
            } else {
                throw new IllegalArgumentException("Invalid hangul Characters: " + choseong + ", " + jungseong + ", " + (jongseong != null ? jongseong : ""));
            }
        }
        
        return String.valueOf(combineCharacter(choseong.charAt(0), jungseongChar, jongseong));
    }

    /**
     * 문자열로 초성, 중성을 받아 한글 문자 생성 (종성 없음)
     */
    public static String combineCharacter(String choseong, String jungseong) {
        return combineCharacter(choseong, jungseong, null);
    }

    /**
     * 두 개의 모음을 합성하여 겹모음 생성
     * 합성할 수 없으면 단순 Join
     */
    public static String combineVowels(char vowel1, char vowel2) {
        String combined = String.valueOf(vowel1) + vowel2;
        Character result = HangulConstants.DOUBLE_VOWEL_COMBINE_MAP.get(combined);
        if (result != null) {
            return String.valueOf(result);
        }
        return combined;
    }

    /**
     * 마지막 문자 제거 (한글 자모 단위)
     * 예: "감" -> "가", "가" -> "ㄱ"
     */
    public static String removeLastCharacter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        char lastChar = str.charAt(str.length() - 1);
        String prefix = str.substring(0, str.length() - 1);

        if (!isHangul(lastChar)) {
            // 호환 자모인 경우 겹자음/겹모음 분해
            if (isCompatJamo(lastChar)) {
                String decomposed = HangulConstants.DOUBLE_CONSONANT_MAP.get(lastChar);
                if (decomposed != null) {
                    return prefix + decomposed.charAt(0);
                }
                decomposed = HangulConstants.DOUBLE_VOWEL_MAP.get(lastChar);
                if (decomposed != null) {
                    return prefix + decomposed.charAt(0);
                }
            }
            return prefix;
        }

        HangulChar hangulChar = disassembleCompleteCharacter(lastChar);
        if (hangulChar == null) {
            return prefix;
        }

        // 종성이 있으면 종성 제거
        if (hangulChar.getJongseong() != null) {
            char jongseong = hangulChar.getJongseong();
            String decomposed = HangulConstants.DOUBLE_CONSONANT_MAP.get(jongseong);
            
            int choseongIndex = HangulConstants.getChoseongIndex(hangulChar.getChoseong());
            int jungseongIndex = HangulConstants.getJungseongIndex(hangulChar.getJungseong());
            
            if (decomposed != null) {
                // 겹받침이면 첫 번째 자음만 종성으로
                int newJongseongIndex = HangulConstants.getJongseongIndex(decomposed.charAt(0));
                char assembled = assemble(choseongIndex, jungseongIndex, newJongseongIndex);
                return prefix + assembled;
            } else {
                // 홑받침이면 종성 제거
                char assembled = assemble(choseongIndex, jungseongIndex, 0);
                return prefix + assembled;
            }
        }

        // 중성만 남으면 초성만 반환
        char jungseong = hangulChar.getJungseong();
        String decomposedVowel = HangulConstants.DOUBLE_VOWEL_MAP.get(jungseong);
        
        if (decomposedVowel != null) {
            // 겹모음이면 첫 번째 모음만 남기고 조합
            int choseongIndex = HangulConstants.getChoseongIndex(hangulChar.getChoseong());
            int newJungseongIndex = HangulConstants.getJungseongIndex(decomposedVowel.charAt(0));
            char assembled = assemble(choseongIndex, newJungseongIndex, 0);
            return prefix + assembled;
        }

        // 중성이 홑모음이면 초성만 반환
        return prefix + hangulChar.getChoseong();
    }
}
