package kr.sharenshare.utils.hangul;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JosaTest {

    @Test
    @DisplayName("받침이 없으면 '를'을 붙인다")
    void attachReul() {
        assertEquals("샴푸를", Josa.attach("샴푸", "을/를"));
        assertEquals("사과를", Josa.attach("사과", Josa.JosaType.EUL_REUL));
    }

    @Test
    @DisplayName("받침이 있으면 '을'을 붙인다")
    void attachEul() {
        assertEquals("칫솔을", Josa.attach("칫솔", "을/를"));
    }

    @Test
    @DisplayName("받침이 없으면 '가'를 붙인다")
    void attachGa() {
        assertEquals("샴푸가", Josa.attach("샴푸", "이/가"));
    }

    @Test
    @DisplayName("받침이 있으면 '이'를 붙인다")
    void attachI() {
        assertEquals("칫솔이", Josa.attach("칫솔", "이/가"));
    }

    @Test
    @DisplayName("받침이 없으면 '로'를 붙인다")
    void attachRo() {
        assertEquals("내부로", Josa.attach("내부", "으로/로"));
    }

    @Test
    @DisplayName("받침이 있으면 '으로'를 붙인다")
    void attachEuro() {
        assertEquals("바깥으로", Josa.attach("바깥", "으로/로"));
    }

    @Test
    @DisplayName("ㄹ 받침일 때는 '로'를 붙인다")
    void attachRoForRieul() {
        assertEquals("서울로", Josa.attach("서울", "으로/로"));
    }

    @Test
    @DisplayName("조사만 선택할 수 있다")
    void pickJosa() {
        assertEquals("가", Josa.pick("샴푸", "이/가"));
        assertEquals("이", Josa.pick("칫솔", "이/가"));
        assertEquals("으로", Josa.pick("바깥", "으로/로"));
        assertEquals("로", Josa.pick("내부", "으로/로"));
    }

    @Test
    @DisplayName("비교의 격조사 '와/과'를 붙인다")
    void attachGwaWa() {
        assertEquals("샴푸와", Josa.attach("샴푸", "와/과"));
        assertEquals("칫솔과", Josa.attach("칫솔", "와/과"));
    }

    @Test
    @DisplayName("선택의 보조사 '이나/나'를 붙인다")
    void attachInaNa() {
        assertEquals("샴푸나", Josa.attach("샴푸", "이나/나"));
        assertEquals("칫솔이나", Josa.attach("칫솔", "이나/나"));
    }

    @Test
    @DisplayName("화제의 보조사 '이란/란'을 붙인다")
    void attachIranRan() {
        assertEquals("샴푸란", Josa.attach("샴푸", "이란/란"));
        assertEquals("칫솔이란", Josa.attach("칫솔", "이란/란"));
    }

    @Test
    @DisplayName("접속조사 '이랑/랑'을 붙인다")
    void attachIrangRang() {
        assertEquals("고기랑", Josa.attach("고기", "이랑/랑"));
        assertEquals("과일이랑", Josa.attach("과일", "이랑/랑"));
    }

    @Test
    @DisplayName("서술격조사와 종결어미 '이에요/예요'를 붙인다")
    void attachIeyoYeyo() {
        assertEquals("사과예요", Josa.attach("사과", "이에요/예요"));
        assertEquals("책이에요", Josa.attach("책", "이에요/예요"));
        assertEquals("때밀이예요", Josa.attach("때밀이", "이에요/예요")); // "이"로 끝나는 단어 예외 처리
    }

    @Test
    @DisplayName("위격조사 '으로서/로서'를 붙인다")
    void attachEuroseoRoseo() {
        assertEquals("학생으로서", Josa.attach("학생", "으로서/로서"));
        assertEquals("부모로서", Josa.attach("부모", "으로서/로서"));
        assertEquals("라이벌로서", Josa.attach("라이벌", "으로서/로서")); // ㄹ 받침 예외 처리
    }

    @Test
    @DisplayName("도구격조사 '으로써/로써'를 붙인다")
    void attachEurosseoRosseo() {
        assertEquals("토큰으로써", Josa.attach("토큰", "으로써/로써"));
        assertEquals("함수로써", Josa.attach("함수", "으로써/로써"));
        assertEquals("건물로써", Josa.attach("건물", "으로써/로써")); // ㄹ 받침 예외 처리
    }

    @Test
    @DisplayName("출발점 격조사 '으로부터/로부터'를 붙인다")
    void attachEurobuteuRobuteu() {
        assertEquals("역삼동으로부터", Josa.attach("역삼동", "으로부터/로부터"));
        assertEquals("저기로부터", Josa.attach("저기", "으로부터/로부터"));
        assertEquals("동굴로부터", Josa.attach("동굴", "으로부터/로부터")); // ㄹ 받침 예외 처리
    }

    @Test
    @DisplayName("주제의 보조사 '이라/라'를 붙인다")
    void attachIraRa() {
        assertEquals("의사라", Josa.attach("의사", "이라/라"));
        assertEquals("선생님이라", Josa.attach("선생님", "이라/라"));
    }

    @Test
    @DisplayName("영어 약어에 조사를 붙인다")
    void attachEnglishAbbreviation() {
        assertEquals("URL을", Josa.attach("URL", "을/를"));
        assertEquals("CSS를", Josa.attach("CSS", "을/를"));
        assertEquals("URL이", Josa.attach("URL", "이/가"));
        assertEquals("CSS가", Josa.attach("CSS", "이/가"));
        assertEquals("URL은", Josa.attach("URL", "은/는"));
        assertEquals("CSS는", Josa.attach("CSS", "은/는"));
        assertEquals("URL과", Josa.attach("URL", "와/과"));
        assertEquals("CSS와", Josa.attach("CSS", "와/과"));
        assertEquals("URL로", Josa.attach("URL", "으로/로"));
        // 추가 테스트 케이스 (JavaScript 원본)
        assertEquals("URL이나", Josa.attach("URL", "이나/나"));
        assertEquals("CSS나", Josa.attach("CSS", "이나/나"));
        assertEquals("URL이란", Josa.attach("URL", "이란/란"));
        assertEquals("CSS란", Josa.attach("CSS", "이란/란"));
        assertEquals("URL아", Josa.attach("URL", "아/야"));
        assertEquals("CSS야", Josa.attach("CSS", "아/야"));
        assertEquals("URL이랑", Josa.attach("URL", "이랑/랑"));
        assertEquals("CSS랑", Josa.attach("CSS", "이랑/랑"));
        assertEquals("URL이에요", Josa.attach("URL", "이에요/예요"));
        assertEquals("CSS예요", Josa.attach("CSS", "이에요/예요"));
    }

    @Test
    @DisplayName("빈 문자열일 경우 빈 문자열을 반환한다")
    void attachEmptyString() {
        assertEquals("", Josa.attach("", "이/가"));
    }

    @Test
    @DisplayName("pick 메서드에서 빈 문자열일 경우 첫 번째 옵션을 반환한다")
    void pickEmptyString() {
        assertEquals("이", Josa.pick("", "이/가"));
        assertEquals("을", Josa.pick("", "을/를"));
    }

    @Test
    @DisplayName("pick - 주격조사")
    void pickSubjectCase() {
        assertEquals("가", Josa.pick("샴푸", "이/가"));
        assertEquals("이", Josa.pick("칫솔", "이/가"));
    }

    @Test
    @DisplayName("pick - 목적격조사")
    void pickObjectCase() {
        assertEquals("를", Josa.pick("샴푸", "을/를"));
        assertEquals("을", Josa.pick("칫솔", "을/를"));
    }

    @Test
    @DisplayName("pick - 대조의 보조사")
    void pickContrastParticle() {
        assertEquals("는", Josa.pick("샴푸", "은/는"));
        assertEquals("은", Josa.pick("칫솔", "은/는"));
    }

    @Test
    @DisplayName("pick - 방향의 격조사")
    void pickDirectionalCase() {
        assertEquals("으로", Josa.pick("바깥", "으로/로"));
        assertEquals("로", Josa.pick("내부", "으로/로"));
    }

    @Test
    @DisplayName("pick - 방향의 격조사 ㄹ 받침 예외 처리")
    void pickDirectionalCaseRieul() {
        assertEquals("로", Josa.pick("지름길", "으로/로"));
    }

    @Test
    @DisplayName("pick - 비교의 격조사")
    void pickComparativeCase() {
        assertEquals("와", Josa.pick("샴푸", "와/과"));
        assertEquals("과", Josa.pick("칫솔", "와/과"));
    }

    @Test
    @DisplayName("pick - 선택의 보조사")
    void pickSelectiveParticle() {
        assertEquals("나", Josa.pick("샴푸", "이나/나"));
        assertEquals("이나", Josa.pick("칫솔", "이나/나"));
    }

    @Test
    @DisplayName("pick - 화제의 보조사")
    void pickTopicParticle() {
        assertEquals("란", Josa.pick("샴푸", "이란/란"));
        assertEquals("이란", Josa.pick("칫솔", "이란/란"));
    }

    @Test
    @DisplayName("pick - 호격조사")
    void pickVocativeCase() {
        assertEquals("야", Josa.pick("철수", "아/야"));
        assertEquals("아", Josa.pick("길동", "아/야"));
    }

    @Test
    @DisplayName("pick - 접속조사")
    void pickConjunctiveParticle() {
        assertEquals("랑", Josa.pick("고기", "이랑/랑"));
        assertEquals("이랑", Josa.pick("과일", "이랑/랑"));
    }

    @Test
    @DisplayName("pick - 서술격조사와 종결어미")
    void pickPredicativeCase() {
        assertEquals("예요", Josa.pick("사과", "이에요/예요"));
        assertEquals("이에요", Josa.pick("책", "이에요/예요"));
    }

    @Test
    @DisplayName("pick - 서술격조사와 종결어미, '이'로 끝나는 단어 예외 처리")
    void pickPredicativeCaseException() {
        assertEquals("예요", Josa.pick("때밀이", "이에요/예요"));
    }

    @Test
    @DisplayName("pick - 위격조사")
    void pickStatusCase() {
        assertEquals("으로서", Josa.pick("학생", "으로서/로서"));
        assertEquals("로서", Josa.pick("부모", "으로서/로서"));
    }

    @Test
    @DisplayName("pick - 위격조사 ㄹ 받침 예외 처리")
    void pickStatusCaseRieul() {
        assertEquals("로서", Josa.pick("라이벌", "으로서/로서"));
    }

    @Test
    @DisplayName("pick - 도구격조사")
    void pickInstrumentalCase() {
        assertEquals("으로써", Josa.pick("토큰", "으로써/로써"));
        assertEquals("로써", Josa.pick("함수", "으로써/로써"));
    }

    @Test
    @DisplayName("pick - 도구격조사 ㄹ 받침 예외 처리")
    void pickInstrumentalCaseRieul() {
        assertEquals("로써", Josa.pick("건물", "으로써/로써"));
    }

    @Test
    @DisplayName("pick - 출발점 격조사")
    void pickOriginCase() {
        assertEquals("으로부터", Josa.pick("역삼동", "으로부터/로부터"));
        assertEquals("로부터", Josa.pick("저기", "으로부터/로부터"));
    }

    @Test
    @DisplayName("pick - 출발점 격조사 ㄹ 받침 예외 처리")
    void pickOriginCaseRieul() {
        assertEquals("로부터", Josa.pick("동굴", "으로부터/로부터"));
    }

    @Test
    @DisplayName("호격조사 '아/야'를 붙인다")
    void attachAYa() {
        assertEquals("철수야", Josa.attach("철수", "아/야"));
        assertEquals("길동아", Josa.attach("길동", "아/야"));
    }
}

