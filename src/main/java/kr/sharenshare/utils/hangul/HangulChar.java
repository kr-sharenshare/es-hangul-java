package kr.sharenshare.utils.hangul;

/**
 * 한글 완성형 문자를 초성, 중성, 종성으로 분해한 결과를 담는 클래스
 */
public final class HangulChar {
    private final Character choseong;   // 초성
    private final Character jungseong;  // 중성
    private final Character jongseong;  // 종성 (nullable)

    public HangulChar(Character choseong, Character jungseong, Character jongseong) {
        this.choseong = choseong;
        this.jungseong = jungseong;
        this.jongseong = jongseong;
    }

    public Character getChoseong() {
        return choseong;
    }

    public Character getJungseong() {
        return jungseong;
    }

    public Character getJongseong() {
        return jongseong;
    }

    public boolean hasBatchim() {
        return jongseong != null;
    }

    /**
     * 분해된 자모를 문자열로 반환
     */
    public String toArray() {
        if (jongseong != null) {
            return String.valueOf(choseong) + jungseong + jongseong;
        }
        return String.valueOf(choseong) + jungseong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HangulChar that = (HangulChar) o;
        return (choseong != null ? choseong.equals(that.choseong) : that.choseong == null) &&
               (jungseong != null ? jungseong.equals(that.jungseong) : that.jungseong == null) &&
               (jongseong != null ? jongseong.equals(that.jongseong) : that.jongseong == null);
    }

    @Override
    public int hashCode() {
        int result = choseong != null ? choseong.hashCode() : 0;
        result = 31 * result + (jungseong != null ? jungseong.hashCode() : 0);
        result = 31 * result + (jongseong != null ? jongseong.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HangulChar{" +
                "choseong=" + choseong +
                ", jungseong=" + jungseong +
                ", jongseong=" + jongseong +
                '}';
    }
}