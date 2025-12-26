package kr.sharenshare.utils.hangul;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RomanizeTest {

    @Nested
    @DisplayName("romanize 테스트")
    class RomanizeBasicTest {

        @Test
        @DisplayName("자음 사이에서 동화 작용이 일어나는 경우")
        void assimilation() {
            assertEquals("baengma", Romanize.romanize("백마"));
            assertEquals("jongno", Romanize.romanize("종로"));
            assertEquals("wangsimni", Romanize.romanize("왕십리"));
            assertEquals("byeollae", Romanize.romanize("별래"));
            assertEquals("silla", Romanize.romanize("신라"));
        }

        @Test
        @DisplayName("ㄴ, ㄹ이 덧나는 경우")
        void nlAddition() {
            assertEquals("hangnyeoul", Romanize.romanize("학여울"));
            assertEquals("allyak", Romanize.romanize("알약"));
            assertEquals("horangi", Romanize.romanize("호랑이"));
            assertEquals("ppalgansaegieyo", Romanize.romanize("빨간색이에요"));
        }

        @Test
        @DisplayName("구개음화가 되는 경우")
        void palatalization() {
            assertEquals("haedoji", Romanize.romanize("해돋이"));
            assertEquals("gachi", Romanize.romanize("같이"));
            assertEquals("guchida", Romanize.romanize("굳히다"));
        }

        @Test
        @DisplayName("ㄱ, ㄷ, ㅂ, ㅈ이 ㅎ과 합하여 거센소리로 소리 나는 경우")
        void aspiration() {
            assertEquals("joko", Romanize.romanize("좋고"));
            assertEquals("nota", Romanize.romanize("놓다"));
            assertEquals("japyeo", Romanize.romanize("잡혀"));
            assertEquals("nachi", Romanize.romanize("낳지"));
        }

        @Test
        @DisplayName("된소리되기는 표기에 반영하지 않는다")
        void noTensification() {
            assertEquals("apgujeong", Romanize.romanize("압구정"));
            assertEquals("nakdonggang", Romanize.romanize("낙동강"));
            assertEquals("jukbyeon", Romanize.romanize("죽변"));
            assertEquals("nakseongdae", Romanize.romanize("낙성대"));
            assertEquals("hapjeong", Romanize.romanize("합정"));
            assertEquals("paldang", Romanize.romanize("팔당"));
            assertEquals("saetbyeol", Romanize.romanize("샛별"));
            assertEquals("ulsan", Romanize.romanize("울산"));
        }

        @Test
        @DisplayName("ㄱ, ㄷ, ㅂ은 모음 앞에서는 g, d, b로, 자음 앞이나 어말에서는 k, t, p로 적는다")
        void consonantPosition() {
            assertEquals("gumi", Romanize.romanize("구미"));
            assertEquals("yeongdong", Romanize.romanize("영동"));
            assertEquals("baegam", Romanize.romanize("백암"));
            assertEquals("okcheon", Romanize.romanize("옥천"));
            assertEquals("hapdeok", Romanize.romanize("합덕"));
            assertEquals("hobeop", Romanize.romanize("호법"));
            assertEquals("wolgot", Romanize.romanize("월곶"));
            assertEquals("beotkkot", Romanize.romanize("벚꽃"));
            assertEquals("hanbat", Romanize.romanize("한밭"));
        }

        @Test
        @DisplayName("ㄹ은 모음 앞에서는 r로, 자음 앞이나 어말에서는 l로 적는다. 단, ㄹㄹ은 ll로 적는다")
        void rieulRomanization() {
            assertEquals("guri", Romanize.romanize("구리"));
            assertEquals("seorak", Romanize.romanize("설악"));
            assertEquals("chilgok", Romanize.romanize("칠곡"));
            assertEquals("imsil", Romanize.romanize("임실"));
            assertEquals("ulleung", Romanize.romanize("울릉"));
            assertEquals("daegwallyeong", Romanize.romanize("대관령"));
        }

        @Test
        @DisplayName("완성된 음절이 아닌 경우에는 그대로 반환한다")
        void incompleteCharacter() {
            assertEquals("g", Romanize.romanize("ㄱ"));
            assertEquals("ganadarambs", Romanize.romanize("가나다라ㅁㅂㅅㅇ"));
            assertEquals("a", Romanize.romanize("ㅏ"));
            assertEquals("wa", Romanize.romanize("ㅘ"));
        }

        @Test
        @DisplayName("특수문자는 로마자 표기로 변경하지 않는다")
        void specialCharacters() {
            assertEquals("annyeonghaseyo.", Romanize.romanize("안녕하세요."));
            assertEquals("hangugeo!", Romanize.romanize("한국어!"));
            assertEquals("", Romanize.romanize(""));
            assertEquals("!?/", Romanize.romanize("!?/"));
        }

        @Test
        @DisplayName("한글과 영어가 혼합된 경우에는 영어는 그대로 반환된다")
        void mixedKoreanEnglish() {
            assertEquals("annyeonghaseyo es-hangul", Romanize.romanize("안녕하세요 es-hangul"));
            assertEquals("hangugeunkorea", Romanize.romanize("한국은korea"));
            assertEquals("goyangineuncat", Romanize.romanize("고양이는cat"));
        }
    }
}

