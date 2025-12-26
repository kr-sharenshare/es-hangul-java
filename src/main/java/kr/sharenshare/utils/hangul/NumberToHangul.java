package kr.sharenshare.utils.hangul;

/**
 * 숫자를 한글로 변환하는 유틸리티 클래스
 */
public final class NumberToHangul {

    private NumberToHangul() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    // 한글 숫자
    private static final String[] HANGUL_DIGITS = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
    private static final String[] HANGUL_DIGITS_PURE = {"영", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
    
    // 자릿수 단위
    private static final String[] SMALL_UNITS = {"", "십", "백", "천"};
    private static final String[] LARGE_UNITS = {"", "만", "억", "조", "경", "해"};

    // 순 우리말 숫자 (1~99)
    private static final String[] SUSA_ONES = {
            "", "하나", "둘", "셋", "넷", "다섯", "여섯", "일곱", "여덟", "아홉"
    };
    private static final String[] SUSA_ONES_DETERMINER = {
            "", "한", "두", "세", "네", "다섯", "여섯", "일곱", "여덟", "아홉"
    };
    private static final String[] SUSA_TENS = {
            "", "열", "스물", "서른", "마흔", "쉰", "예순", "일흔", "여든", "아흔"
    };
    private static final String[] SUSA_TENS_DETERMINER = {
            "", "열", "스무", "서른", "마흔", "쉰", "예순", "일흔", "여든", "아흔"
    };

    // 날짜 (하루, 이틀, ...)
    private static final String[] DAYS = {
            "", "하루", "이틀", "사흘", "나흘", "닷새", "엿새", "이레", "여드레", "아흐레", "열흘"
    };

    // 서수사 (첫째, 둘째, ...)
    private static final String[] SEOSUSA_SPECIAL = {"", "첫째", "둘째", "셋째"};

    /**
     * 숫자를 한글로 변환
     * 예: 12345 -> "일만이천삼백사십오"
     */
    public static String numberToHangul(long number) {
        return numberToHangul(number, false);
    }

    /**
     * 숫자를 한글로 변환 (띄어쓰기 옵션)
     * 예: 12345, true -> "일만 이천삼백사십오"
     */
    public static String numberToHangul(long number, boolean spacing) {
        if (number == 0) {
            return "영";
        }

        StringBuilder result = new StringBuilder();
        boolean isNegative = number < 0;
        number = Math.abs(number);

        if (isNegative) {
            result.append("마이너스");
            if (spacing) {
                result.append(" ");
            }
        }

        // 큰 단위별로 분리 (만, 억, 조, ...)
        int unitIndex = 0;
        StringBuilder[] parts = new StringBuilder[LARGE_UNITS.length];
        for (int i = 0; i < parts.length; i++) {
            parts[i] = new StringBuilder();
        }

        while (number > 0) {
            int chunk = (int) (number % 10000);
            if (chunk > 0) {
                // 만 단위 이상에서는 "일"을 포함, 만 단위 미만(unitIndex==0)에서는 생략
                parts[unitIndex] = convertChunk(chunk, unitIndex == 0);
                parts[unitIndex].append(LARGE_UNITS[unitIndex]);
            }
            number /= 10000;
            unitIndex++;
        }

        // 역순으로 조합
        for (int i = parts.length - 1; i >= 0; i--) {
            if (parts[i].length() > 0) {
                if (result.length() > 0 && spacing && !result.toString().endsWith(" ")) {
                    result.append(" ");
                }
                result.append(parts[i]);
            }
        }

        return result.toString();
    }

    /**
     * 소수점을 포함한 숫자를 한글로 변환
     * @throws IllegalArgumentException NaN이 입력된 경우
     */
    public static String numberToHangul(double number) {
        return numberToHangul(number, false);
    }

    /**
     * 소수점을 포함한 숫자를 한글로 변환 (띄어쓰기 옵션)
     */
    public static String numberToHangul(double number, boolean spacing) {
        if (Double.isNaN(number)) {
            throw new IllegalArgumentException("유효한 숫자를 입력해주세요.");
        }
        if (Double.isInfinite(number)) {
            if (number > 0) {
                return spacing ? "무한대" : "무한대";
            } else {
                return spacing ? "마이너스 무한대" : "마이너스무한대";
            }
        }

        StringBuilder result = new StringBuilder();
        boolean isNegative = number < 0;
        number = Math.abs(number);

        if (isNegative) {
            result.append("마이너스");
            if (spacing) {
                result.append(" ");
            }
        }

        long intPart = (long) number;
        double decimalPart = number - intPart;

        // 정수 부분
        if (intPart == 0 && decimalPart > 0) {
            result.append("영");
        } else {
            String intHangul = numberToHangul(intPart, spacing);
            if (isNegative && intHangul.startsWith("마이너스")) {
                intHangul = intHangul.substring(spacing ? 5 : 4);
            }
            result.append(intHangul);
        }

        // 소수 부분
        if (decimalPart > 0) {
            result.append("점");
            if (spacing) {
                result.append(" ");
            }
            String decimalStr = String.format("%.10f", decimalPart).substring(2);
            // 뒤의 0 제거
            decimalStr = decimalStr.replaceAll("0+$", "");
            for (char c : decimalStr.toCharArray()) {
                int digit = c - '0';
                result.append(HANGUL_DIGITS_PURE[digit]);
            }
        }

        return result.toString();
    }

    /**
     * 4자리 이하 숫자를 한글로 변환
     * @param chunk 변환할 숫자
     * @param omitOne true면 일십, 일백, 일천에서 "일" 생략
     */
    private static StringBuilder convertChunk(int chunk, boolean omitOne) {
        StringBuilder result = new StringBuilder();
        int unitIndex = 0;

        while (chunk > 0) {
            int digit = chunk % 10;
            if (digit > 0) {
                // 일십, 일백, 일천은 조건에 따라 "일" 생략
                if (digit == 1 && unitIndex > 0 && omitOne) {
                    result.insert(0, SMALL_UNITS[unitIndex]);
                } else {
                    result.insert(0, HANGUL_DIGITS[digit] + SMALL_UNITS[unitIndex]);
                }
            }
            chunk /= 10;
            unitIndex++;
        }

        return result;
    }

    /**
     * 숫자를 한글 혼용으로 변환
     * 예: 12345 -> "1만2345"
     */
    public static String numberToHangulMixed(long number) {
        return numberToHangulMixed(number, false);
    }

    /**
     * 숫자를 한글 혼용으로 변환 (double)
     * 예: 12345.678 -> "1만2,345.678"
     */
    public static String numberToHangulMixed(double number) {
        return numberToHangulMixed(number, false);
    }

    /**
     * 숫자를 한글 혼용으로 변환 (띄어쓰기 옵션)
     * 예: 12345, true -> "1만 2345"
     */
    public static String numberToHangulMixed(long number, boolean spacing) {
        if (number == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();
        boolean isNegative = number < 0;
        number = Math.abs(number);

        if (isNegative) {
            result.append("-");
        }

        // 큰 단위별로 분리
        int unitIndex = 0;
        StringBuilder[] parts = new StringBuilder[LARGE_UNITS.length];
        for (int i = 0; i < parts.length; i++) {
            parts[i] = new StringBuilder();
        }

        while (number > 0) {
            int chunk = (int) (number % 10000);
            if (chunk > 0) {
                // 콤마 포맷팅 추가 (모든 청크에 적용)
                String chunkStr = String.format("%,d", chunk);
                parts[unitIndex] = new StringBuilder(chunkStr);
                if (unitIndex > 0) {
                    parts[unitIndex].append(LARGE_UNITS[unitIndex]);
                }
            }
            number /= 10000;
            unitIndex++;
        }

        // 역순으로 조합
        for (int i = parts.length - 1; i >= 0; i--) {
            if (parts[i].length() > 0) {
                if (result.length() > (isNegative ? 1 : 0) && spacing) {
                    result.append(" ");
                }
                result.append(parts[i]);
            }
        }

        return result.toString();
    }

    /**
     * 숫자를 한글 혼용으로 변환 (double, 띄어쓰기 옵션)
     * 예: 12345.678, true -> "1만 2,345.678"
     */
    public static String numberToHangulMixed(double number, boolean spacing) {
        if (Double.isNaN(number)) {
            throw new IllegalArgumentException("유효한 숫자를 입력해주세요.");
        }

        if (Double.isInfinite(number)) {
            return number > 0 ? "무한대" : "-무한대";
        }

        if (number == 0.0) {
            return "0";
        }

        boolean isNegative = number < 0;
        double absoluteNumber = Math.abs(number);

        String numberStr = String.valueOf(absoluteNumber);
        String[] parts = numberStr.split("\\.");
        String integerPart = parts[0];
        String decimalPart = parts.length > 1 ? parts[1] : null;

        // 정수 부분 처리
        StringBuilder result = new StringBuilder();
        if (isNegative) {
            result.append("-");
        }

        if (integerPart.equals("0")) {
            result.append("0");
        } else {
            long integerValue = Long.parseLong(integerPart);
            String integerResult = numberToHangulMixed(integerValue, spacing);
            // spacing이 true일 때는 이미 공백이 포함되어 있으므로 그대로 사용
            result.append(integerResult);
        }

        // 소수 부분 처리
        if (decimalPart != null) {
            result.append(".").append(decimalPart);
        }

        return result.toString();
    }

    /**
     * 금액 문자열을 한글로 변환
     * 예: "15,201,100" -> "일천오백이십만천백"
     */
    public static String amountToHangul(String amount) {
        if (amount == null || amount.isEmpty()) {
            return "";
        }

        // 숫자와 소수점만 추출
        StringBuilder cleaned = new StringBuilder();
        boolean hasDecimal = false;
        for (char c : amount.toCharArray()) {
            if (Character.isDigit(c)) {
                cleaned.append(c);
            } else if (c == '.' && !hasDecimal) {
                cleaned.append(c);
                hasDecimal = true;
            } else if (c == '-' && cleaned.length() == 0) {
                cleaned.append(c);
            }
        }

        if (cleaned.length() == 0) {
            return "";
        }

        // 80글자를 넘으면 에러 발생 (JavaScript와 동일)
        String cleanedStr = cleaned.toString();
        String[] parts = cleanedStr.split("\\.");
        String integerPart = parts[0];
        if (integerPart.length() > 80) {
            throw new IllegalArgumentException("convert range exceeded : " + amount);
        }
        String decimalPart = parts.length > 1 ? parts[1] : null;
        
        // 선행 0 제거 (단, "0"은 유지)
        if (integerPart.length() > 1) {
            integerPart = integerPart.replaceFirst("^0+", "");
            if (integerPart.isEmpty()) {
                integerPart = "0";
            }
        }
        
        String finalStr = integerPart;
        if (decimalPart != null) {
            finalStr += "." + decimalPart;
        }

        try {
            if (hasDecimal) {
                double value = Double.parseDouble(finalStr);
                return numberToHangul(value);
            } else {
                long value = Long.parseLong(finalStr);
                return numberToHangul(value);
            }
        } catch (NumberFormatException e) {
            return "";
        }
    }

    /**
     * 금액(long)을 한글로 변환
     */
    public static String amountToHangul(long amount) {
        return numberToHangul(amount);
    }

    /**
     * 금액(double)을 한글로 변환
     */
    public static String amountToHangul(double amount) {
        return amountToHangul(String.valueOf(amount));
    }

    /**
     * 순 우리말 수사 변환 (1~99)
     * 예: 1 -> "하나", 21 -> "스물하나"
     */
    public static String susa(int number) {
        return susa(number, false);
    }

    /**
     * 순 우리말 수사 변환 (관형사 옵션)
     * 예: susa(1, true) -> "한", susa(20, true) -> "스무"
     */
    public static String susa(int number, boolean determiner) {
        if (number <= 0 || number > 100) {
            throw new IllegalArgumentException("지원하지 않는 숫자입니다.");
        }

        // 100은 한자어
        if (number == 100) {
            return numberToHangul(number);
        }

        int tens = number / 10;
        int ones = number % 10;

        StringBuilder result = new StringBuilder();

        if (determiner) {
            // 관형사 형태 (한, 두, 세, ...)
            if (tens > 0) {
                // 20은 "스무", 21+ 는 "스물"
                if (tens == 2 && ones == 0) {
                    return "스무";
                }
                // 다른 십의 자리는 기본 형태 사용 (스물, 서른, ...)
                result.append(SUSA_TENS[tens]);
            }
            if (ones > 0) {
                result.append(SUSA_ONES_DETERMINER[ones]);
            }
        } else {
            // 기본 형태 (하나, 둘, 셋, ...)
            if (tens > 0) {
                result.append(SUSA_TENS[tens]);
            }
            if (ones > 0) {
                result.append(SUSA_ONES[ones]);
            }
        }

        return result.toString();
    }

    /**
     * 날짜 수 변환 (1~30)
     * 예: 1 -> "하루", 10 -> "열흘"
     */
    public static String days(int number) {
        if (number <= 0 || number > 30) {
            throw new IllegalArgumentException("지원하지 않는 숫자입니다.");
        }

        // 1~10은 고유 명칭
        if (number <= 10) {
            return DAYS[number];
        }

        int tens = number / 10;
        int ones = number % 10;

        StringBuilder result = new StringBuilder();

        // 10, 20, 30은 "열흘", "스무날", "서른날"
        if (ones == 0) {
            if (tens == 1) {
                return "열흘";
            } else if (tens == 2) {
                return "스무날";
            } else if (tens == 3) {
                return "서른날";
            }
        }

        // 11~19, 21~29
        if (tens == 1) {
            result.append("열");
        } else if (tens == 2) {
            result.append("스무");
        }

        // 하루~아흐레
        if (ones > 0) {
            result.append(DAYS[ones]);
        }

        return result.toString();
    }

    /**
     * 서수사 변환
     * 예: 1 -> "첫째", 2 -> "둘째", 10 -> "열째"
     */
    public static String seosusa(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("유효하지 않은 입력입니다. 1이상의 정수만 지원합니다.");
        }

        // 1~3은 특별 형태
        if (number <= 3) {
            return SEOSUSA_SPECIAL[number];
        }

        // 100 이상은 한자어 + 째
        if (number >= 100) {
            return numberToHangul(number) + "째";
        }

        // 4~99는 서수사 형태 + 째
        return susaForSeosusa(number) + "째";
    }

    /**
     * 서수사를 위한 수사 변환
     * 십의 자리는 기본 형태, 일의 자리는 1,2만 관형사 형태
     * 예: 11 -> "열한", 12 -> "열두", 13 -> "열셋", 21 -> "스물한"
     */
    private static String susaForSeosusa(int number) {
        int tens = number / 10;
        int ones = number % 10;

        StringBuilder result = new StringBuilder();

        if (tens > 0) {
            // 20은 "스무", 21+ 는 "스물"
            if (tens == 2 && ones == 0) {
                return "스무";
            }
            result.append(SUSA_TENS[tens]);
        }

        if (ones > 0) {
            // 1, 2만 관형사 형태 (한, 두), 3 이상은 기본 형태 (셋, 넷, ...)
            if (ones <= 2) {
                result.append(SUSA_ONES_DETERMINER[ones]);
            } else {
                result.append(SUSA_ONES[ones]);
            }
        }

        return result.toString();
    }
}

