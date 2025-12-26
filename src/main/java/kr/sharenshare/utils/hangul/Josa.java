package kr.sharenshare.utils.hangul;

public final class Josa {

    public enum JosaType {
        EUL_REUL("을", "를"),           // 을/를
        I_GA("이", "가"),               // 이/가
        EUN_NEUN("은", "는"),           // 은/는
        GWA_WA("와", "과"),             // 와/과 (비교의 격조사 - 역순)
        A_YA("아", "야"),               // 아/야
        I_EMPTY("이", ""),              // 이/∅ (예: 철수이다/영희다)
        EURO_RO("으로", "로"),          // 으로/로
        INA_NA("이나", "나"),           // 이나/나 (선택의 보조사)
        IRAN_RAN("이란", "란"),         // 이란/란 (화제의 보조사)
        IRANG_RANG("이랑", "랑"),       // 이랑/랑 (접속조사)
        IEYO_YEYO("이에요", "예요"),    // 이에요/예요 (서술격조사와 종결어미)
        EUROSEO_ROSEO("으로서", "로서"), // 으로서/로서 (위격조사)
        EUROSSEO_ROSSEO("으로써", "로써"), // 으로써/로써 (도구격조사)
        EUROBUTEU_ROBUTEU("으로부터", "로부터"), // 으로부터/로부터 (출발점 격조사)
        IRA_RA("이라", "라");           // 이라/라 (주제의 보조사)

        private final String withBatchim;
        private final String withoutBatchim;

        JosaType(String withBatchim, String withoutBatchim) {
            this.withBatchim = withBatchim;
            this.withoutBatchim = withoutBatchim;
        }
    }

    // ㄹ 받침 예외 처리가 필요한 조사들
    private static final JosaType[] RO_JOSA_TYPES = {
            JosaType.EURO_RO,
            JosaType.EUROSEO_ROSEO,
            JosaType.EUROSSEO_ROSSEO,
            JosaType.EUROBUTEU_ROBUTEU
    };

    /**
     * 적절한 조사를 붙여서 반환
     */
    public static String attach(String word, JosaType josaType) {
        if (word == null || word.isEmpty()) {
            return word;
        }

        // 영어 약어 처리 (대문자만으로 구성된 경우)
        if (word.matches("^[A-Z]+$")) {
            char lastChar = word.charAt(word.length() - 1);
            String koreanPronunciation = getKoreanPronunciation(lastChar);
            return word + pick(koreanPronunciation, josaType);
        }

        char lastChar = word.charAt(word.length() - 1);
        boolean hasBatchim = Hangul.hasBatchim(lastChar);

        // "와/과"는 받침이 있으면 "과", 없으면 "와" 사용 (역순)
        // enum에서 withBatchim="와", withoutBatchim="과"로 정의되어 있음
        if (josaType == JosaType.GWA_WA) {
            return word + (hasBatchim ? josaType.withoutBatchim : josaType.withBatchim);
        }

        // ㄹ 받침 예외 처리
        if (isRoJosaType(josaType) && Hangul.isHangul(lastChar)) {
            HangulChar hangulChar = Hangul.disassembleCompleteCharacter(lastChar);
            if (hangulChar != null && hangulChar.getJongseong() != null && hangulChar.getJongseong() == 'ㄹ') {
                return word + josaType.withoutBatchim;
            }
        }

        // "이에요/예요" 특수 처리: "이"로 끝나는 단어 예외
        if (josaType == JosaType.IEYO_YEYO && word.endsWith("이")) {
            return word + josaType.withoutBatchim;
        }

        return word + (hasBatchim ? josaType.withBatchim : josaType.withoutBatchim);
    }

    /**
     * 문자열 패턴으로 조사 붙이기 (예: "을/를")
     */
    public static String attach(String word, String pattern) {
        JosaType josaType = parsePattern(pattern);
        return attach(word, josaType);
    }

    private static JosaType parsePattern(String pattern) {
        switch (pattern) {
            case "을/를":
                return JosaType.EUL_REUL;
            case "이/가":
                return JosaType.I_GA;
            case "은/는":
                return JosaType.EUN_NEUN;
            case "과/와":
            case "와/과":
                return JosaType.GWA_WA;
            case "아/야":
                return JosaType.A_YA;
            case "으로/로":
                return JosaType.EURO_RO;
            case "이나/나":
                return JosaType.INA_NA;
            case "이란/란":
                return JosaType.IRAN_RAN;
            case "이랑/랑":
                return JosaType.IRANG_RANG;
            case "이에요/예요":
                return JosaType.IEYO_YEYO;
            case "으로서/로서":
                return JosaType.EUROSEO_ROSEO;
            case "으로써/로써":
                return JosaType.EUROSSEO_ROSSEO;
            case "으로부터/로부터":
                return JosaType.EUROBUTEU_ROBUTEU;
            case "이라/라":
                return JosaType.IRA_RA;
            default:
                throw new IllegalArgumentException("Unknown josa pattern: " + pattern);
        }
    }

    /**
     * 적절한 조사만 반환 (단어에 붙이지 않음)
     */
    public static String pick(String word, JosaType josaType) {
        if (word == null || word.isEmpty()) {
            // 원본: 빈 문자열일 경우 첫 번째 옵션 반환
            return josaType.withBatchim;
        }

        char lastChar = word.charAt(word.length() - 1);
        boolean hasBatchim = Hangul.hasBatchim(lastChar);

        // "와/과"는 받침이 있으면 "과", 없으면 "와" 사용 (역순)
        // enum에서 withBatchim="와", withoutBatchim="과"로 정의되어 있음
        if (josaType == JosaType.GWA_WA) {
            return hasBatchim ? josaType.withoutBatchim : josaType.withBatchim;
        }

        // ㄹ 받침 예외 처리
        if (isRoJosaType(josaType) && Hangul.isHangul(lastChar)) {
            HangulChar hangulChar = Hangul.disassembleCompleteCharacter(lastChar);
            if (hangulChar != null && hangulChar.getJongseong() != null && hangulChar.getJongseong() == 'ㄹ') {
                return josaType.withoutBatchim;
            }
        }

        // "이에요/예요" 특수 처리: "이"로 끝나는 단어 예외
        if (josaType == JosaType.IEYO_YEYO && word.endsWith("이")) {
            return josaType.withoutBatchim;
        }

        return hasBatchim ? josaType.withBatchim : josaType.withoutBatchim;
    }

    /**
     * ㄹ 받침 예외 처리가 필요한 조사 타입인지 확인
     */
    private static boolean isRoJosaType(JosaType josaType) {
        for (JosaType type : RO_JOSA_TYPES) {
            if (type == josaType) {
                return true;
            }
        }
        return false;
    }

    /**
     * 영어 알파벳을 한글 발음으로 변환
     */
    private static String getKoreanPronunciation(char alphabet) {
        switch (Character.toUpperCase(alphabet)) {
            case 'A': return "에이";
            case 'B': return "비";
            case 'C': return "씨";
            case 'D': return "디";
            case 'E': return "이";
            case 'F': return "에프";
            case 'G': return "지";
            case 'H': return "에이치";
            case 'I': return "아이";
            case 'J': return "제이";
            case 'K': return "케이";
            case 'L': return "엘";
            case 'M': return "엠";
            case 'N': return "엔";
            case 'O': return "오";
            case 'P': return "피";
            case 'Q': return "큐";
            case 'R': return "알";
            case 'S': return "에스";
            case 'T': return "티";
            case 'U': return "유";
            case 'V': return "브이";
            case 'W': return "더블유";
            case 'X': return "엑스";
            case 'Y': return "와이";
            case 'Z': return "지";
            default: return String.valueOf(alphabet);
        }
    }

    /**
     * 문자열 패턴으로 조사만 반환 (예: "을/를")
     */
    public static String pick(String word, String pattern) {
        JosaType josaType = parsePattern(pattern);
        return pick(word, josaType);
    }
}