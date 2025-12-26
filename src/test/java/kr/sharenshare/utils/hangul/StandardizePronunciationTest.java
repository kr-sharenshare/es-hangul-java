package kr.sharenshare.utils.hangul;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardizePronunciationTest {

    @Nested
    @DisplayName("음절이 완성된 한글을 제외한 문자는 변경하지 않는다")
    class NonHangulCharactersTest {

        @Test
        @DisplayName("단일 자모는 그대로 반환한다")
        void singleJamo() {
            assertEquals("ㄱㄴㄷㄹㅏㅓㅑㅙ", StandardizePronunciation.standardize("ㄱㄴㄷㄹㅏㅓㅑㅙ"));
        }

        @Test
        @DisplayName("특수문자는 그대로 반환한다")
        void specialCharacters() {
            assertEquals("!?", StandardizePronunciation.standardize("!?"));
        }

        @Test
        @DisplayName("영어는 그대로 반환한다")
        void english() {
            assertEquals("hello", StandardizePronunciation.standardize("hello"));
        }

        @Test
        @DisplayName("숫자는 그대로 반환한다")
        void numbers() {
            assertEquals("1234567890", StandardizePronunciation.standardize("1234567890"));
        }

        @Test
        @DisplayName("빈 문자열은 그대로 반환한다")
        void emptyString() {
            assertEquals("", StandardizePronunciation.standardize(""));
        }
    }

    @Nested
    @DisplayName("한글은 음성 표기법으로 변경한다")
    class HangulTransformationTest {

        @Nested
        @DisplayName("16항")
        class Transform16thTest {

            @Test
            @DisplayName("한글 자모의 이름은 그 받침소리를 연음하되, \"ㄷ, ㅈ, ㅊ, ㅋ, ㅌ, ㅍ, ㅎ\"의 경우에는 특별히 다음과 같이 발음한다")
            void specialHangulJamo() {
                assertEquals("디그시", StandardizePronunciation.standardize("디귿이"));
                assertEquals("지으시", StandardizePronunciation.standardize("지읒이"));
                assertEquals("치으시", StandardizePronunciation.standardize("치읓이"));
                assertEquals("키으기", StandardizePronunciation.standardize("키읔이"));
                assertEquals("티으시", StandardizePronunciation.standardize("티읕이"));
                assertEquals("피으비", StandardizePronunciation.standardize("피읖이"));
                assertEquals("히으시", StandardizePronunciation.standardize("히읗이"));
            }

            @Test
            @DisplayName("자모의 이름이 \"ㄱ, ㄴ, ㄹ, ㅁ, ㅂ, ㅅ, ㅇ\"일 경우")
            void normalHangulJamo() {
                assertEquals("기여기", StandardizePronunciation.standardize("기역이"));
                assertEquals("니으니", StandardizePronunciation.standardize("니은이"));
                assertEquals("리으리", StandardizePronunciation.standardize("리을이"));
                assertEquals("미으미", StandardizePronunciation.standardize("미음이"));
                assertEquals("비으비", StandardizePronunciation.standardize("비읍이"));
                assertEquals("이응이", StandardizePronunciation.standardize("이응이"));
            }
        }

        @Nested
        @DisplayName("17항")
        class Transform17thTest {

            @Test
            @DisplayName("받침 \"ㄷ\", \"ㅌ(ㄾ)\"이 조사나 접미사의 모음 \"ㅣ\"와 결합되는 경우에는, \"ㅈ\", \"ㅊ\"으로 바꾸어서 뒤 음절 첫소리로 옮겨 발음한다")
            void palatalization() {
                assertEquals("고지듣따", StandardizePronunciation.standardize("곧이듣다"));
                assertEquals("구지", StandardizePronunciation.standardize("굳이"));
                assertEquals("미다지", StandardizePronunciation.standardize("미닫이"));
                assertEquals("땀바지", StandardizePronunciation.standardize("땀받이"));
                assertEquals("바치", StandardizePronunciation.standardize("밭이"));
                assertEquals("벼훌치", StandardizePronunciation.standardize("벼훑이"));
            }

        @Test
            @DisplayName("\"ㄷ\" 뒤에 접미사 \"히\"가 결합되어 \"티\"를 이루는 것은 \"치\"로 발음한다")
            void hiSuffix() {
                assertEquals("구치다", StandardizePronunciation.standardize("굳히다"));
                assertEquals("다치다", StandardizePronunciation.standardize("닫히다"));
                assertEquals("무치다", StandardizePronunciation.standardize("묻히다"));
            }
        }

        @Nested
        @DisplayName("ㄴ/ㄹ이 덧나는 경우")
        class TransformNLAssimilationTest {

            @Test
            @DisplayName("받침이 \"ㄱ, ㄴ, ㄷ, ㅁ, ㅂ, ㅇ\"이고 다음 음절이 \"야, 여, 요, 유, 이, 얘, 예\"로 이어지는 경우")
            void nlAddition() {
                assertEquals("항녀울", StandardizePronunciation.standardize("학여울"));
                assertEquals("맨닙", StandardizePronunciation.standardize("맨입"));
                assertEquals("담뇨", StandardizePronunciation.standardize("담요"));
                assertEquals("영엄뇽", StandardizePronunciation.standardize("영업용"));
                assertEquals("콩녇", StandardizePronunciation.standardize("콩엿"));
                assertEquals("알략", StandardizePronunciation.standardize("알약"));
            }

            @Test
            @DisplayName("받침이 \"ㄹ\"이고 다음 음절이 \"야, 여, 요, 유, 이, 얘, 예\"로 이어지는 경우")
            void rieulBatchim() {
                assertEquals("알략", StandardizePronunciation.standardize("알약"));
                assertEquals("서울력", StandardizePronunciation.standardize("서울역"));
                assertEquals("불려우", StandardizePronunciation.standardize("불여우"));
            }

            @Test
            @DisplayName("ㄴ/ㄹ이 되기 위한 조건이지만 다음 음절이 받침이 없는 \"이\"로 이어지는 경우")
            void iWithoutBatchim() {
                assertEquals("호랑이", StandardizePronunciation.standardize("호랑이"));
                assertEquals("개구쟁이", StandardizePronunciation.standardize("개구쟁이"));
                assertEquals("공이", StandardizePronunciation.standardize("공이"));
                assertEquals("손자비", StandardizePronunciation.standardize("손잡이", false));
            }

            @Test
            @DisplayName("ㄴ/ㄹ이 되기 위한 조건이지만 현재 음절의 중성의 ∙(아래아)가 하나가 아닐 경우에는 덧나지 않고 연음규칙이 적용된다")
            void complexVowel() {
                assertEquals("고양이", StandardizePronunciation.standardize("고양이"));
                assertEquals("윤녀정", StandardizePronunciation.standardize("윤여정"));
            }

            @Test
            @DisplayName("ㄴ/ㄹ이 되기 위한 조건이면서 현재 음절의 중성의 ∙(아래아)가 하나가 아닐 경우지만, 현재 종성이 \"자음군 단순화\"의 대상이라면 연음규칙이 적용되지 않고 둘 중 하나의 자음만 남고 나머지 자음은 탈락한다")
            void consonantClusterSimplification() {
                assertEquals("힘드미 읻따", StandardizePronunciation.standardize("힘듦이 있다"));
        }
    }

    @Nested
        @DisplayName("19항")
    class Transform19thTest {

        @Test
            @DisplayName("받침 \"ㅁ, ㅇ\" 뒤에 연결되는 \"ㄹ\"은 \"ㄴ\"으로 발음한다")
            void mAndNgBeforeRieul() {
                assertEquals("담녁", StandardizePronunciation.standardize("담력"));
                assertEquals("침냑", StandardizePronunciation.standardize("침략"));
                assertEquals("강능", StandardizePronunciation.standardize("강릉"));
                assertEquals("항노", StandardizePronunciation.standardize("항로"));
                assertEquals("대통녕", StandardizePronunciation.standardize("대통령"));
            }

            @Test
            @DisplayName("받침 \"ㄱ, ㅂ\" 뒤에 연결되는 \"ㄹ\"도 \"ㄴ\"으로 발음한다")
            void gAndBBeforeRieul() {
                assertEquals("망논", StandardizePronunciation.standardize("막론"));
                assertEquals("성뉴", StandardizePronunciation.standardize("석류"));
                assertEquals("혐녁", StandardizePronunciation.standardize("협력"));
                assertEquals("범니", StandardizePronunciation.standardize("법리"));
            }
        }

        @Nested
        @DisplayName("18항: 받침 \"ㄱ, ㄲ, ㅋ, ㄳ, ㄺ\" / \"ㄷ, ㅅ, ㅆ, ㅈ, ㅊ, ㅌ, ㅎ\" / \"ㅂ, ㅍ, ㄼ, ㄿ, ㅄ\"은 \"ㄴ, ㅁ\" 앞에서 \"ㅇ, ㄴ, ㅁ\"으로 발음한다")
        class Transform18thTest {

            @Test
            @DisplayName("받침 \"ㄱ, ㄲ, ㅋ, ㄳ, ㄺ\"일 경우")
            void gBatchim() {
                assertEquals("멍는", StandardizePronunciation.standardize("먹는"));
                assertEquals("깡는", StandardizePronunciation.standardize("깎는"));
                assertEquals("키응만", StandardizePronunciation.standardize("키읔만"));
                assertEquals("몽목씨", StandardizePronunciation.standardize("몫몫이"));
                assertEquals("긍는", StandardizePronunciation.standardize("긁는"));
            }

            @Test
            @DisplayName("받침 \"ㄷ, ㅅ, ㅆ, ㅈ, ㅊ, ㅌ, ㅎ\"일 경우")
            void dBatchim() {
                assertEquals("단는", StandardizePronunciation.standardize("닫는"));
                assertEquals("진는", StandardizePronunciation.standardize("짓는"));
                assertEquals("인는", StandardizePronunciation.standardize("있는"));
                assertEquals("만는", StandardizePronunciation.standardize("맞는"));
                assertEquals("쫀는", StandardizePronunciation.standardize("쫓는"));
                assertEquals("분는", StandardizePronunciation.standardize("붙는"));
                assertEquals("논는", StandardizePronunciation.standardize("놓는"));
            }

            @Test
            @DisplayName("받침 \"ㅂ, ㅍ, ㄼ, ㄿ, ㅄ\"일 경우")
            void bBatchim() {
                assertEquals("잠는", StandardizePronunciation.standardize("잡는"));
                assertEquals("암마당", StandardizePronunciation.standardize("앞마당"));
                assertEquals("밤는", StandardizePronunciation.standardize("밟는"));
                assertEquals("음는", StandardizePronunciation.standardize("읊는"));
                assertEquals("엄는", StandardizePronunciation.standardize("없는"));
            }
        }

        @Nested
        @DisplayName("20항")
        class Transform20thTest {

            @Test
            @DisplayName("\"ㄴ\"은 \"ㄹ\"의 앞이나 뒤에서 \"ㄹ\"로 발음한다")
            void nToL() {
                assertEquals("날로", StandardizePronunciation.standardize("난로"));
                assertEquals("실라", StandardizePronunciation.standardize("신라"));
                assertEquals("철리", StandardizePronunciation.standardize("천리"));
                assertEquals("대괄령", StandardizePronunciation.standardize("대관령"));
                assertEquals("칼랄", StandardizePronunciation.standardize("칼날"));
            }

            @Test
            @DisplayName("첫소리 \"ㄴ\"이 \"ㅀ, ㄾ\" 뒤에 연결되는 경우에도 \"ㄹ\"로 발음한다")
            void nAfterLhAndLt() {
                assertEquals("달른", StandardizePronunciation.standardize("닳는"));
                assertEquals("할레", StandardizePronunciation.standardize("핥네"));
            }
        }

        @Nested
        @DisplayName("12항")
        class Transform12thTest {

            @Test
            @DisplayName("\"ㅎ, ㄶ, ㅀ\" 뒤에 \"ㄱ, ㄷ, ㅈ\"이 결합되는 경우에는, 뒤 음절 첫소리와 합쳐서 \"ㅋ, ㅌ, ㅊ\"으로 발음한다")
            void hBeforeGdj() {
                assertEquals("노코", StandardizePronunciation.standardize("놓고"));
                assertEquals("조턴", StandardizePronunciation.standardize("좋던"));
                assertEquals("싸치", StandardizePronunciation.standardize("쌓지"));
                assertEquals("만코", StandardizePronunciation.standardize("많고"));
                assertEquals("안턴", StandardizePronunciation.standardize("않던"));
                assertEquals("달치", StandardizePronunciation.standardize("닳지"));
            }

            @Test
            @DisplayName("받침 \"ㄱ, ㄺ, ㄷ, ㅂ, ㄼ, ㅈ, ㄵ\"이 뒤 음절 첫소리 \"ㅎ\"과 결합되는 경우에도, 역시 두 음을 합쳐서 \"ㅋ, ㅌ, ㅍ, ㅊ\"으로 발음한다")
            void batchimBeforeH() {
                assertEquals("가카", StandardizePronunciation.standardize("각하"));
                assertEquals("머키다", StandardizePronunciation.standardize("먹히다"));
                assertEquals("발키다", StandardizePronunciation.standardize("밝히다"));
                assertEquals("마텽", StandardizePronunciation.standardize("맏형"));
                assertEquals("조피다", StandardizePronunciation.standardize("좁히다"));
                assertEquals("널피다", StandardizePronunciation.standardize("넓히다"));
                assertEquals("꼬치다", StandardizePronunciation.standardize("꽂히다"));
                assertEquals("안치다", StandardizePronunciation.standardize("앉히다"));
            }

            @Test
            @DisplayName("\"ㅎ, ㄶ, ㅀ\" 뒤에 \"ㅅ\"이 결합되는 경우에는, \"ㅅ\"을 \"ㅆ\"으로 발음한다")
            void hBeforeS() {
                assertEquals("다쏘", StandardizePronunciation.standardize("닿소"));
                assertEquals("만쏘", StandardizePronunciation.standardize("많소"));
                assertEquals("실쏘", StandardizePronunciation.standardize("싫소"));
            }

            @Test
            @DisplayName("\"ㅎ\" 뒤에 \"ㄴ\"이 결합되는 경우에는 \"ㄴ\"으로 발음한다")
            void hBeforeN() {
                assertEquals("논는", StandardizePronunciation.standardize("놓는"));
                assertEquals("싼네", StandardizePronunciation.standardize("쌓네"));
            }

            @Test
            @DisplayName("\"ㄶ, ㅀ\" 뒤에 \"ㄴ\"이 결합되는 경우에는, \"ㅎ\"을 발음하지 않는다")
            void nhAndLhBeforeN() {
                assertEquals("안네", StandardizePronunciation.standardize("않네"));
                assertEquals("안는", StandardizePronunciation.standardize("않는"));
                assertEquals("뚤레", StandardizePronunciation.standardize("뚫네"));
                assertEquals("뚤른", StandardizePronunciation.standardize("뚫는"));
            }

            @Test
            @DisplayName("\"ㅎ, ㄶ, ㅀ\" 뒤에 모음으로 시작된 어미나 접미사가 결합되는 경우에는 \"ㅎ\"을 발음하지 않는다")
            void hBeforeVowel() {
                assertEquals("나은", StandardizePronunciation.standardize("낳은"));
                assertEquals("노아", StandardizePronunciation.standardize("놓아"));
                assertEquals("싸이다", StandardizePronunciation.standardize("쌓이다"));
                assertEquals("마나", StandardizePronunciation.standardize("많아"));
                assertEquals("아는", StandardizePronunciation.standardize("않은"));
                assertEquals("다라", StandardizePronunciation.standardize("닳아"));
                assertEquals("시러도", StandardizePronunciation.standardize("싫어도"));
            }

            @Test
            @DisplayName("\"다음 음절이 없는 경우\"")
            void noNextSyllable() {
                assertEquals("만", StandardizePronunciation.standardize("많"));
                assertEquals("실", StandardizePronunciation.standardize("싫"));
            }
        }

        @Nested
        @DisplayName("13항")
        class Transform13thTest {

            @Test
            @DisplayName("홑받침이나 쌍받침이 모음으로 시작된 조사나 어미, 접미사와 결합되는 경우에는, 제 음가대로 뒤 음절 첫소리로 옮겨 발음한다")
            void singleOrDoubleBatchim() {
                assertEquals("까까", StandardizePronunciation.standardize("깎아"));
                assertEquals("오시", StandardizePronunciation.standardize("옷이"));
                assertEquals("이써", StandardizePronunciation.standardize("있어"));
                assertEquals("나지", StandardizePronunciation.standardize("낮이"));
                assertEquals("아프로", StandardizePronunciation.standardize("앞으로"));
            }
        }

        @Nested
        @DisplayName("14항")
        class Transform14thTest {

            @Test
            @DisplayName("겹받침이 모음으로 시작된 조사나 어미, 접미사와 결합되는 경우에는, 뒤엣것만을 뒤 음절 첫소리로 옮겨 발음한다")
            void doubleBatchim() {
                assertEquals("달글", StandardizePronunciation.standardize("닭을"));
                assertEquals("절머", StandardizePronunciation.standardize("젊어"));
                assertEquals("골씨", StandardizePronunciation.standardize("곬이"));
                assertEquals("할타", StandardizePronunciation.standardize("핥아"));
            }
        }

        @Nested
        @DisplayName("9항")
        class Transform9thTest {

            @Test
            @DisplayName("받침 \"ㄲ, ㅋ\" / \"ㅅ, ㅆ, ㅈ, ㅊ, ㅌ\" / \"ㅍ\"은 어말 또는 자음 앞에서 각각 대표음 \"ㄱ, ㄷ, ㅂ\"으로 발음한다")
            void representativeSound9() {
                assertEquals("닥따", StandardizePronunciation.standardize("닦다"));
                assertEquals("키윽", StandardizePronunciation.standardize("키읔"));

                assertEquals("옫", StandardizePronunciation.standardize("옷"));
                assertEquals("읻따", StandardizePronunciation.standardize("있다"));
                assertEquals("젇", StandardizePronunciation.standardize("젖"));
                assertEquals("빋따", StandardizePronunciation.standardize("빚다"));
                assertEquals("꼳", StandardizePronunciation.standardize("꽃"));
                assertEquals("솓", StandardizePronunciation.standardize("솥"));

                assertEquals("압", StandardizePronunciation.standardize("앞"));
                assertEquals("덥따", StandardizePronunciation.standardize("덮다"));
            }
        }

        @Nested
        @DisplayName("10항")
        class Transform10thTest {

            @Test
            @DisplayName("겹받침 \"ㄳ\" / \"ㄵ\" / \"ㄼ, ㄽ, ㄾ\" / \"ㅄ\"은 어말 또는 자음 앞에서 각각 \"ㄱ, ㄴ, ㄹ, ㅂ\"으로 발음한다")
            void representativeSound10() {
                assertEquals("넉", StandardizePronunciation.standardize("넋"));

                assertEquals("안따", StandardizePronunciation.standardize("앉다"));

                assertEquals("여덜", StandardizePronunciation.standardize("여덟"));
                assertEquals("외골", StandardizePronunciation.standardize("외곬"));
                assertEquals("할따", StandardizePronunciation.standardize("핥다"));

                assertEquals("갑", StandardizePronunciation.standardize("값"));
            }
        }

        @Nested
        @DisplayName("11항")
        class Transform11thTest {

            @Test
            @DisplayName("겹받침 \"ㄺ\" / \"ㄻ\" / \"ㄿ\"은 어말 또는 자음 앞에서 각각 \"ㄱ, ㅁ, ㅂ\"으로 발음한다")
            void representativeSound11() {
                assertEquals("닥", StandardizePronunciation.standardize("닭"));
                assertEquals("막따", StandardizePronunciation.standardize("맑다"));

                assertEquals("삼", StandardizePronunciation.standardize("삶"));
                assertEquals("점따", StandardizePronunciation.standardize("젊다"));

                assertEquals("읍꼬", StandardizePronunciation.standardize("읊고"));
                assertEquals("읍따", StandardizePronunciation.standardize("읊다"));
            }
        }

        @Nested
        @DisplayName("23항")
        class Transform23rdTest {

            @Test
            @DisplayName("받침 \"ㄱ(ㄲ, ㅋ, ㄳ, ㄺ), ㄷ(ㅅ, ㅆ, ㅈ, ㅊ, ㅌ), ㅂ(ㅍ, ㄼ, ㄿ, ㅄ)\" 뒤에 연결되는 \"ㄱ, ㄷ, ㅂ, ㅅ, ㅈ\"은 된소리로 발음한다")
            void tensification23() {
                assertEquals("국빱", StandardizePronunciation.standardize("국밥"));
                assertEquals("깍따", StandardizePronunciation.standardize("깎다"));
                assertEquals("넉빠지", StandardizePronunciation.standardize("넋받이"));
                assertEquals("삭똔", StandardizePronunciation.standardize("삯돈"));
            }
        }

        @Nested
        @DisplayName("24항")
        class Transform24thTest {

            @Test
            @DisplayName("어간 받침 \"ㄴ(ㄵ), ㅁ(ㄻ)\" 뒤에 결합되는 어미의 첫소리 \"ㄱ, ㄷ, ㅅ, ㅈ\"은 된소리로 발음한다")
            void tensification24() {
                assertEquals("신꼬", StandardizePronunciation.standardize("신고"));
                assertEquals("껴안따", StandardizePronunciation.standardize("껴안다"));
                assertEquals("안꼬", StandardizePronunciation.standardize("앉고"));
                assertEquals("언따", StandardizePronunciation.standardize("얹다"));
                assertEquals("삼꼬", StandardizePronunciation.standardize("삼고"));
                assertEquals("더듬찌", StandardizePronunciation.standardize("더듬지"));
                assertEquals("담꼬", StandardizePronunciation.standardize("닮고"));
                assertEquals("점찌", StandardizePronunciation.standardize("젊지"));
            }
        }

        @Nested
        @DisplayName("25항")
        class Transform25thTest {

            @Test
            @DisplayName("어간 받침 \"ㄼ, ㄾ\" 뒤에 결합되는 어미의 첫소리 \"ㄱ, ㄷ, ㅅ, ㅈ\"은 된소리로 발음한다")
            void tensification25() {
                assertEquals("널께", StandardizePronunciation.standardize("넓게"));
                assertEquals("할따", StandardizePronunciation.standardize("핥다"));
                assertEquals("훌쏘", StandardizePronunciation.standardize("훑소"));
                assertEquals("떨찌", StandardizePronunciation.standardize("떫지"));
            }
        }
    }

    @Nested
    @DisplayName("경음화 등의 된소리를 적용하지 않는다")
    class WithoutHardConversionTest {

        @Test
        @DisplayName("9항")
        void transform9th() {
            assertEquals("닥다", StandardizePronunciation.standardize("닦다", false));
        }

        @Test
        @DisplayName("10항")
        void transform10th() {
            assertEquals("안다", StandardizePronunciation.standardize("앉다", false));
        }

        @Test
        @DisplayName("11항")
        void transform11th() {
            assertEquals("막다", StandardizePronunciation.standardize("맑다", false));
        }

        @Test
        @DisplayName("17항")
        void transform17th() {
            assertEquals("고지듣다", StandardizePronunciation.standardize("곧이듣다", false));
        }

        @Test
        @DisplayName("23항")
        void transform23rd() {
            assertEquals("국밥", StandardizePronunciation.standardize("국밥", false));
        }

        @Test
        @DisplayName("24항")
        void transform24th() {
            assertEquals("신고", StandardizePronunciation.standardize("신고", false));
        }

        @Test
        @DisplayName("25항")
        void transform25th() {
            assertEquals("널게", StandardizePronunciation.standardize("넓게", false));
        }
    }

    @Nested
    @DisplayName("예외사항은 정의된 단어 모음에서 반환한다")
    class ExceptionWordsTest {

        @Test
        @DisplayName("사이시옷 예외사항 단어는 단어모음에서 찾아 반환한다")
        void saisiotException() {
            assertEquals("베갠닏", StandardizePronunciation.standardize("베갯잇"));
            assertEquals("깬닙", StandardizePronunciation.standardize("깻잎"));
            assertEquals("나문닙", StandardizePronunciation.standardize("나뭇잎"));
            assertEquals("도리깬녈", StandardizePronunciation.standardize("도리깻열"));
            assertEquals("뒨뉻", StandardizePronunciation.standardize("뒷윷"));
        }

        @Test
        @DisplayName("파생어/합성어 예외사항 단어는 단어모음에서 찾아 반환한다")
        void derivedWordException() {
            assertEquals("저녁", StandardizePronunciation.standardize("전역"));
        }
    }
}
