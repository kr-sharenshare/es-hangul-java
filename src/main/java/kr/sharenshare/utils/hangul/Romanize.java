package kr.sharenshare.utils.hangul;

/**
 * 한글을 로마자로 변환하는 클래스
 */
public class Romanize {

    /**
     * 한글 문자열을 로마자로 변환
     * @param hangul 한글 문자열
     * @return 로마자로 변환된 문자열
     */
    public static String romanize(String hangul) {
        if (hangul == null || hangul.isEmpty()) {
            return "";
        }

        // 표준 발음으로 변환 (경음화 미적용)
        String changedHangul = StandardizePronunciation.standardize(hangul, false);

        StringBuilder result = new StringBuilder();
        char[] chars = changedHangul.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            result.append(romanizeSyllable(chars, i));
        }

        return result.toString();
    }

    private static String romanizeSyllable(char[] chars, int index) {
        char syllable = chars[index];

        // 완성형 한글인 경우
        if (Hangul.isHangul(syllable)) {
            HangulChar disassemble = Hangul.disassembleCompleteCharacter(syllable);
            if (disassemble == null) {
                return String.valueOf(syllable);
            }

            char choseong = disassemble.getChoseong();
            char jungseong = disassemble.getJungseong();
            
            // 종성 처리 - 겹받침을 문자열로 변환
            String jongseong = Hangul.getJongseongAsString(syllable);

            // 초성 로마자
            String choseongRoman = PronunciationConstants.초성_알파벳_발음.get(choseong);
            if (choseongRoman == null) {
                choseongRoman = "";
            }

            // ㄹ 특수 처리: 'ㄹ'은 모음 앞에서는 'r'로, ㄹㄹ은 'll'로
            if (choseong == 'ㄹ' && index > 0 && Hangul.isHangul(chars[index - 1])) {
                HangulChar prevDisassemble = Hangul.disassembleCompleteCharacter(chars[index - 1]);
                if (prevDisassemble != null && prevDisassemble.getJongseong() != null && 
                    prevDisassemble.getJongseong() == 'ㄹ') {
                    choseongRoman = "l";
                }
            }

            // 중성 로마자
            String jungseongRoman = PronunciationConstants.중성_알파벳_발음.get(jungseong);
            if (jungseongRoman == null) {
                jungseongRoman = "";
            }

            // 종성 로마자
            String jongseongRoman = PronunciationConstants.종성_알파벳_발음.get(jongseong);
            if (jongseongRoman == null) {
                jongseongRoman = "";
            }

            return choseongRoman + jungseongRoman + jongseongRoman;
        }

        // 중성(모음)인 경우
        if (PronunciationConstants.중성_알파벳_발음.containsKey(syllable)) {
            return PronunciationConstants.중성_알파벳_발음.get(syllable);
        }

        // 초성(자음)인 경우
        if (Hangul.canBeChoseong(syllable)) {
            String roman = PronunciationConstants.초성_알파벳_발음.get(syllable);
            return roman != null ? roman : String.valueOf(syllable);
        }

        // 그 외 문자는 그대로 반환
        return String.valueOf(syllable);
    }
}

