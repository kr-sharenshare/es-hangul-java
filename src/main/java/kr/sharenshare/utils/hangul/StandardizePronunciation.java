package kr.sharenshare.utils.hangul;

import java.util.ArrayList;
import java.util.List;

/**
 * 표준 발음 변환 클래스
 * 한글 문자열을 표준 발음으로 변환합니다.
 */
public class StandardizePronunciation {

    /**
     * 음절 정보를 담는 클래스
     */
    public static class Syllable {
        public String choseong;
        public String jungseong;
        public String jongseong;

        public Syllable(String choseong, String jungseong, String jongseong) {
            this.choseong = choseong;
            this.jungseong = jungseong;
            this.jongseong = jongseong;
        }

        public Syllable copy() {
            return new Syllable(choseong, jungseong, jongseong);
        }
    }

    /**
     * 비한글 문자 정보
     */
    private static class NotHangul {
        int index;
        String syllable;

        NotHangul(int index, String syllable) {
            this.index = index;
            this.syllable = syllable;
        }
    }

    /**
     * 표준 발음으로 변환
     * @param hangul 한글 문자열
     * @return 표준 발음으로 변환된 문자열
     */
    public static String standardize(String hangul) {
        return standardize(hangul, true);
    }

    /**
     * 표준 발음으로 변환
     * @param hangul 한글 문자열
     * @param hardConversion 경음화 적용 여부
     * @return 표준 발음으로 변환된 문자열
     */
    public static String standardize(String hangul, boolean hardConversion) {
        if (hangul == null || hangul.isEmpty()) {
            return "";
        }

        // 예외 단어 확인
        String exceptionResult = findFirstException(hangul);
        if (exceptionResult != null) {
            return exceptionResult;
        }

        String[] phrases = hangul.split(" ", -1);
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < phrases.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(transformHangulPhrase(phrases[i], hardConversion));
        }
        
        return result.toString();
    }

    private static String findFirstException(String hangul) {
        String result = PronunciationConstants.사이시옷_예외사항_목록.get(hangul);
        if (result != null) {
            return result;
        }
        return PronunciationConstants.단일어_예외사항_단어모음.get(hangul);
    }

    private static String transformHangulPhrase(String phrase, boolean hardConversion) {
        List<NotHangul> notHangulList = new ArrayList<>();
        List<Syllable> syllables = new ArrayList<>();

        // 음절 분해
        for (int i = 0; i < phrase.length(); i++) {
            char c = phrase.charAt(i);
            // 한글 완성형 문자가 아니거나 한글 자모인 경우 제외
            if (!Hangul.isHangul(c) || Hangul.isCompatJamo(c)) {
                notHangulList.add(new NotHangul(i, String.valueOf(c)));
            } else {
                HangulChar hc = Hangul.disassembleCompleteCharacter(c);
                if (hc != null) {
                    // 종성을 문자열로 변환 (겹받침 포함)
                    String jongseong = Hangul.getJongseongAsString(c);
                    syllables.add(new Syllable(
                        String.valueOf(hc.getChoseong()),
                        String.valueOf(hc.getJungseong()),
                        jongseong
                    ));
                } else {
                    notHangulList.add(new NotHangul(i, String.valueOf(c)));
                }
            }
        }

        // 규칙 적용
        for (int i = 0; i < syllables.size(); i++) {
            Syllable current = syllables.get(i);
            Syllable next = (i < syllables.size() - 1) ? syllables.get(i + 1) : null;

            if (next != null && hardConversion) {
                transformHardConversion(current, next);
            }

            if (next != null) {
                transform16th(current, next, phrase, i);
                transform17th(current, next);
                transform19th(current, next);
                transformNLAssimilation(current, next);
                transform18th(current, next);
                transform20th(current, next);
            }

            transform12th(current, next);

            if (next != null) {
                transform13And14th(current, next);
            }

            transform9And10And11th(current, next);
        }

        // 결과 조합
        return assembleChangedHangul(syllables, notHangulList);
    }

    private static String assembleChangedHangul(List<Syllable> syllables, List<NotHangul> notHangulList) {
        List<String> result = new ArrayList<>();
        
        for (Syllable syllable : syllables) {
            String combined = Hangul.combineCharacter(syllable.choseong, syllable.jungseong, syllable.jongseong);
            result.add(combined);
        }

        // 비한글 문자 삽입
        for (NotHangul nh : notHangulList) {
            if (nh.index <= result.size()) {
                result.add(nh.index, nh.syllable);
            } else {
                result.add(nh.syllable);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String s : result) {
            sb.append(s);
        }
        return sb.toString();
    }

    // 제9, 10, 11항
    private static void transform9And10And11th(Syllable current, Syllable next) {
        if (current.jongseong == null || current.jongseong.isEmpty()) {
            return;
        }

        boolean is어말 = !current.jongseong.isEmpty() && next == null;
        boolean is음가있는자음앞 = !current.jongseong.isEmpty() && 
            (next == null || !next.choseong.equals(String.valueOf(PronunciationConstants.음가가_없는_자음)));

        if ((is어말 || is음가있는자음앞) && 
            PronunciationConstants.받침_대표음_발음.containsKey(current.jongseong)) {
            current.jongseong = PronunciationConstants.받침_대표음_발음.get(current.jongseong);
        }
    }

    // 제12항
    private static void transform12th(Syllable current, Syllable next) {
        if (current.jongseong == null || current.jongseong.isEmpty()) {
            return;
        }

        if (PronunciationConstants.발음변환_받침_ㅎ.contains(current.jongseong)) {
            if (next != null) {
                // ㄱ, ㄷ, ㅈ, ㅅ 처리
                char nextChoseong = next.choseong.charAt(0);
                if (nextChoseong == 'ㄱ' || nextChoseong == 'ㄷ' || nextChoseong == 'ㅈ' || nextChoseong == 'ㅅ') {
                    Character newChoseong = PronunciationConstants.발음변환_받침_ㅎ_발음.get(nextChoseong);
                    if (newChoseong != null) {
                        next.choseong = String.valueOf(newChoseong);
                        current.jongseong = replace받침ㅎ(current);
                    }
                }
                // ㄴ 처리
                if (next.choseong.equals("ㄴ") && 
                    (current.jongseong.equals("ㄴㅎ") || current.jongseong.equals("ㄹㅎ"))) {
                    current.jongseong = replace받침ㅎ(current);
                }
                // ㅇ 처리
                if (next.choseong.equals(String.valueOf(PronunciationConstants.음가가_없는_자음))) {
                    if (current.jongseong.equals("ㄴㅎ") || current.jongseong.equals("ㄹㅎ")) {
                        current.jongseong = replace받침ㅎ(current);
                    } else {
                        current.jongseong = "";
                    }
                } else {
                    // ㅇ이 아닌 경우에도 replace받침ㅎ 적용
                    current.jongseong = replace받침ㅎ(current);
                }
            }

            if (next == null) {
                current.jongseong = replace받침ㅎ(current);
            }
        }

        // 첫소리 ㅎ 처리
        if (next != null && next.choseong.equals("ㅎ") && 
            PronunciationConstants.발음변환_첫소리_ㅎ.contains(current.jongseong)) {
            Character newChoseong = PronunciationConstants.발음변환_첫소리_ㅎ_발음.get(current.jongseong);
            if (newChoseong != null) {
                next.choseong = String.valueOf(newChoseong);
                if (current.jongseong.length() == 1) {
                    current.jongseong = "";
                } else {
                    current.jongseong = String.valueOf(current.jongseong.charAt(0));
                }
            }
        }
    }

    // 받침 ㅎ 제거 헬퍼 함수
    private static String replace받침ㅎ(Syllable syllable) {
        return syllable.jongseong.replace("ㅎ", "");
    }

    // 제13, 14항
    private static void transform13And14th(Syllable current, Syllable next) {
        if (current.jongseong == null || current.jongseong.isEmpty()) {
            return;
        }
        if (!next.choseong.equals(String.valueOf(PronunciationConstants.음가가_없는_자음))) {
            return;
        }

        // 홑받침 또는 쌍받침 처리
        if (!current.jongseong.equals("ㅇ") && !current.jongseong.isEmpty()) {
            boolean is홑받침 = current.jongseong.length() == 1;
            boolean is쌍받침 = current.jongseong.length() == 2 && 
                current.jongseong.charAt(0) == current.jongseong.charAt(1);

            if (is홑받침 || is쌍받침) {
                next.choseong = current.jongseong;
                current.jongseong = "";
            }
        }

        // 겹받침 처리 (홑받침/쌍받침 처리 후에도 겹받침인 경우 처리)
        if (current.jongseong != null && current.jongseong.length() == 2 && 
            current.jongseong.charAt(0) != current.jongseong.charAt(1)) {
            char secondJong = current.jongseong.charAt(1);
            if (secondJong == 'ㅅ') {
                next.choseong = "ㅆ";
            } else {
                next.choseong = String.valueOf(secondJong);
            }
            // 두 번째 문자 제거
            current.jongseong = current.jongseong.replace(String.valueOf(secondJong), "");
        }
    }

    // 제16항
    private static void transform16th(Syllable current, Syllable next, String phrase, int index) {
        if (current.jongseong == null || current.jongseong.isEmpty()) {
            return;
        }
        if (!next.choseong.equals(String.valueOf(PronunciationConstants.음가가_없는_자음))) {
            return;
        }

        if (index > 0 && index < phrase.length()) {
            String combinedSyllables = String.valueOf(phrase.charAt(index - 1)) + phrase.charAt(index);
            
            // 특별한 한글 자모 처리
            if (PronunciationConstants.특별한_한글_자모.contains(combinedSyllables)) {
                char jongChar = current.jongseong.charAt(0);
                Character newChoseong = PronunciationConstants.특별한_한글_자모의_발음.get(jongChar);
                if (newChoseong != null) {
                    current.jongseong = "";
                    next.choseong = String.valueOf(newChoseong);
                }
            }

            // 일반 한글 자모 처리 (특별한 한글 자모 처리 후에도 확인)
            if (PronunciationConstants.한글_자모.contains(combinedSyllables)) {
                next.choseong = current.jongseong;
                if (!current.jongseong.equals("ㅇ")) {
                    current.jongseong = "";
                }
            }
        }
    }

    // 제17항
    private static void transform17th(Syllable current, Syllable next) {
        if (!next.jungseong.equals("ㅣ")) {
            return;
        }

        // ㅇ 초성 처리
        if (next.choseong.equals("ㅇ") && PronunciationConstants.음의_동화_받침.containsKey(current.jongseong)) {
            next.choseong = String.valueOf(PronunciationConstants.음의_동화_받침.get(current.jongseong));
            if (current.jongseong.equals("ㄹㅌ")) {
                current.jongseong = "ㄹ";
            } else {
                current.jongseong = "";
            }
        }

        // ㅎ 초성 + ㄷ 받침 처리
        if (next.choseong.equals("ㅎ") && current.jongseong.equals("ㄷ")) {
            next.choseong = "ㅊ";
            current.jongseong = "";
        }
    }

    // 제18항
    private static void transform18th(Syllable current, Syllable next) {
        if (current.jongseong == null || current.jongseong.isEmpty()) {
            return;
        }
        if (!next.choseong.equals("ㄴ") && !next.choseong.equals("ㅁ")) {
            return;
        }

        if (PronunciationConstants.비음화_받침_ㅇ_변환.contains(current.jongseong)) {
            current.jongseong = "ㅇ";
        }
        if (PronunciationConstants.비음화_받침_ㄴ_변환.contains(current.jongseong)) {
            current.jongseong = "ㄴ";
        }
        if (PronunciationConstants.비음화_받침_ㅁ_변환.contains(current.jongseong)) {
            current.jongseong = "ㅁ";
        }
    }

    // 제19항
    private static void transform19th(Syllable current, Syllable next) {
        if (PronunciationConstants.자음동화_받침_ㄴ_변환.contains(current.jongseong) && 
            next.choseong.equals("ㄹ")) {
            next.choseong = "ㄴ";
        }
    }

    // 제20항
    private static void transform20th(Syllable current, Syllable next) {
        // ㄴ이 ㄹ 앞에서 ㄹ로
        if (current.jongseong.equals("ㄴ") && next.choseong.equals("ㄹ")) {
            current.jongseong = "ㄹ";
        }

        // ㄴ이 ㄹ 뒤에서 ㄹ로
        if (next.choseong.equals("ㄴ") && 
            (current.jongseong.equals("ㄹ") || 
             current.jongseong.equals("ㄹㅎ") || 
             current.jongseong.equals("ㄹㅌ"))) {
            next.choseong = "ㄹ";
        }
    }

    // 경음화 (된소리)
    private static void transformHardConversion(Syllable current, Syllable next) {
        if (!PronunciationConstants.된소리.containsKey(next.choseong.charAt(0))) {
            return;
        }

        // 자음군 단순화 예외
        if (PronunciationConstants.자음군_단순화.contains(next.jongseong)) {
            return;
        }

        boolean 제23항조건 = PronunciationConstants.된소리_받침.contains(current.jongseong);
        boolean 제24_25항조건 = PronunciationConstants.어간_받침.contains(current.jongseong) && 
            !next.choseong.equals("ㅂ");

        if (제23항조건 || 제24_25항조건) {
            next.choseong = String.valueOf(PronunciationConstants.된소리.get(next.choseong.charAt(0)));
        }
    }

    // ㄴㄹ 동화
    private static void transformNLAssimilation(Syllable current, Syllable next) {
        if (current.jongseong == null || current.jongseong.isEmpty()) {
            return;
        }

        boolean ㄴㄹ이덧나는조건 = !current.jongseong.isEmpty() && 
            next.choseong.equals("ㅇ") && 
            PronunciationConstants.ㄴㄹ이_덧나는_후속음절_모음.contains(next.jungseong.charAt(0));

        boolean is이 = next.choseong.equals(String.valueOf(PronunciationConstants.음가가_없는_자음)) &&
            next.jungseong.equals("ㅣ") &&
            (next.jongseong == null || next.jongseong.isEmpty()) &&
            !PronunciationConstants.자음군_단순화.contains(current.jongseong);

        if (!ㄴㄹ이덧나는조건 || is이) {
            return;
        }

        if (PronunciationConstants.ㄴㄹ이_덧나는_모음.contains(current.jungseong.charAt(0))) {
            if (PronunciationConstants.ㄴㄹ이_덧나서_받침_ㄴ_변환.contains(current.jongseong)) {
                if (current.jongseong.equals("ㄱ")) {
                    current.jongseong = "ㅇ";
                }
                next.choseong = "ㄴ";
            }
            if (PronunciationConstants.ㄴㄹ이_덧나서_받침_ㄹ_변환.contains(current.jongseong)) {
                next.choseong = "ㄹ";
            }
        } else {
            if (PronunciationConstants.자음군_단순화.contains(current.jongseong)) {
                String simplified = PronunciationConstants.자음군_단순화_결과.get(current.jongseong);
                if (simplified != null) {
                    current.jongseong = simplified;
                }
            } else {
                next.choseong = current.jongseong;
            }
        }
    }
}

