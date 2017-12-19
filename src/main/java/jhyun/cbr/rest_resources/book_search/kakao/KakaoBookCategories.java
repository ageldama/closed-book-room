package jhyun.cbr.rest_resources.book_search.kakao;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static jhyun.cbr.rest_resources.book_search.kakao.KakaoBookCategory.of;

public final class KakaoBookCategories {

    // https://developers.kakao.com/docs/restapi/search#%EC%B1%85-%EA%B2%80%EC%83%89
    // perl -ne '/^(.+?)\t(.+?)\t(.+?)$/; print "of(\"$1\", \"$2\", $3),\n";'
    public static final List<KakaoBookCategory> CATEGORIES = ImmutableList.of(
            of("국내도서", "소설", 1),
            of("국내도서", "시/에세이", 3),
            of("국내도서", "인문", 5),
            of("국내도서", "가정/생활", 7),
            of("국내도서", "요리", 8),
            of("국내도서", "건강", 9),
            of("국내도서", "취미/스포츠", 11),
            of("국내도서", "경제/경영", 13),
            of("국내도서", "자기계발", 15),
            of("국내도서", "정치/사회", 17),
            of("국내도서", "정부간행물", 18),
            of("국내도서", "역사/문화", 19),
            of("국내도서", "종교", 21),
            of("국내도서", "예술/대중문화", 23),
            of("국내도서", "중/고등학습", 25),
            of("국내도서", "기술/공학", 26),
            of("국내도서", "외국어", 27),
            of("국내도서", "과학", 29),
            of("국내도서", "취업/수험서", 31),
            of("국내도서", "여행/기행", 32),
            of("국내도서", "컴퓨터/IT", 33),
            of("국내도서", "잡지", 35),
            of("국내도서", "사전", 37),
            of("국내도서", "청소년", 38),
            of("국내도서", "초등참고서", 39),
            of("국내도서", "유아", 41),
            of("국내도서", "아동", 42),
            of("국내도서", "어린이영어", 45),
            of("국내도서", "만화", 47),
            of("국내도서", "대학교재", 50),
            of("국내도서", "어린이전집", 51),
            of("국내도서", "한국소개도서", 53),
            of("e북", "소설", 901),
            of("e북", "장르소설", 902),
            of("e북", "시/에세이", 903),
            of("e북", "경제/경영", 904),
            of("e북", "자기계발", 905),
            of("e북", "인문", 906),
            of("e북", "정치/사회", 907),
            of("e북", "로맨스/무협/판타지", 908),
            of("e북", "종교", 909),
            of("e북", "예술/대중문화", 910),
            of("e북", "가정/생활", 911),
            of("e북", "건강", 912),
            of("e북", "여행/취미", 913),
            of("e북", "청소년", 914),
            of("e북", "학습/수험서", 915),
            of("e북", "유아", 916),
            of("e북", "아동", 917),
            of("e북", "외국어/사전", 918),
            of("e북", "과학", 919),
            of("e북", "컴퓨터/IT", 920),
            of("e북", "잡지", 921),
            of("e북", "만화", 922),
            of("e북", "외국도서", 923),
            of("e북", "무료eBook", 924),
            of("e북", "개인출판", 925),
            of("e북", "오디오북", 926),
            of("e북", "연재", 951),
            of("e북", "eReader Free", 953),
            of("영미도서", "문학", 101),
            of("영미도서", "취미/실용/여행", 103),
            of("영미도서", "생활/요리/건강", 105),
            of("영미도서", "예술/건축", 107),
            of("영미도서", "인문/사회", 109),
            of("영미도서", "경제/경영", 111),
            of("영미도서", "과학/기술", 113),
            of("영미도서", "아동", 115),
            of("영미도서", "한국관련도서", 117),
            of("영미도서", "NON_BOOK", 119),
            of("영미도서", "UMI", 120),
            of("영미도서", "ELT/영어교재", 181),
            of("영미도서", "어린이영어", 183),
            of("영미도서", "대학교재", 191),
            of("영미도서", "중국관련도서", 194),
            of("일본도서", "일서메인", 239),
            of("일본도서", "잡지", 241),
            of("일본도서", "엔터테인먼트", 243),
            of("일본도서", "만화", 245),
            of("일본도서", "문학", 247),
            of("일본도서", "라이트노벨", 249),
            of("일본도서", "문고(포켓북)", 251),
            of("일본도서", "신서(포켓북)", 253),
            of("일본도서", "아동", 255),
            of("일본도서", "실용서/예술", 257),
            of("일본도서", "인문/사회", 259),
            of("일본도서", "자연/기술과학", 261),
            of("일본도서", "어학/학습/사전", 263),
            of("일본도서", "문구/멀티/기타", 264),
            of("일본도서", "중국관련도서", 267),
            of("프랑스도서", "프랑스종합", 486),
            of("독일도서", "독일종합", 588),
            of("스페인도서", "스페인종합", 690),
            of("미분류", "미분류", 0)
            );

}