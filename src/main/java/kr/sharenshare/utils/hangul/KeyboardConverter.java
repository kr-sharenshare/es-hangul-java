package kr.sharenshare.utils.hangul;

/**
 * QWERTY 키보드와 한글 자모 간 변환을 위한 유틸리티 클래스
 */
public final class KeyboardConverter {

    private KeyboardConverter() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    /**
     * QWERTY 키보드 입력을 한글 자모로 변환
     * 예: "abc" -> "ㅁㅠㅊ"
     */
    public static String convertQwertyToAlphabet(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            Character hangul = HangulConstants.QWERTY_TO_HANGUL_MAP.get(c);
            if (hangul != null) {
                result.append(hangul);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * QWERTY 키보드 입력을 한글로 조합하여 변환
     * 예: "abc" -> "뮻" (자모를 조합)
     */
    public static String convertQwertyToHangul(String str) {
        String jamo = convertQwertyToAlphabet(str);
        return Hangul.assembleString(jamo);
    }

    /**
     * 한글 자모를 QWERTY 키보드 입력으로 변환
     * 예: "ㅁㅠㅊ" -> "abc"
     */
    public static String convertAlphabetToQwerty(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            Character qwerty = HangulConstants.HANGUL_TO_QWERTY_MAP.get(c);
            if (qwerty != null) {
                result.append(qwerty);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * 한글을 QWERTY 키보드 입력으로 변환
     * 예: "뮻" -> "abc" (한글 분해 후 변환)
     * JavaScript와 동일하게 먼저 disassemble로 분해한 후 각 자모를 QWERTY로 변환
     */
    public static String convertHangulToQwerty(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // JavaScript와 동일하게 먼저 전체 문자열을 분해
        String disassembled = Hangul.disassemble(str);
        
        StringBuilder result = new StringBuilder();
        for (char c : disassembled.toCharArray()) {
            Character qwerty = HangulConstants.HANGUL_TO_QWERTY_MAP.get(c);
            if (qwerty != null) {
                result.append(qwerty);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * 혼합된 문자열에서 QWERTY를 한글로 변환
     * 영문자는 한글 자모로 변환하고, 기존 한글 자모는 유지한 후 조합
     * 예: "vm론트" -> "ㅍㅡ론트" (convertQwertyToAlphabet만 적용)
     * 예: "RㅏㄱEㅜrl" -> "ㄲㅏㄱㄸㅜㄱㅣ" (대문자는 쌍자음으로)
     */
    public static String convertMixedToAlphabet(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            Character hangul = HangulConstants.QWERTY_TO_HANGUL_MAP.get(c);
            if (hangul != null) {
                result.append(hangul);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }
}

