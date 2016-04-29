package com.kimdew.lib;

import java.util.HashMap;

public class HanGul {
	
	// 한글 음절 : http://www.unicode.org/charts/PDF/UAC00.pdf
	// 한글 호환성 자모 : http://www.unicode.org/charts/PDF/U3130.pdf
	// 한글 자모 : http://www.unicode.org/charts/PDF/U1100.pdf
	
	// 한글 자모 (Range : 1100-11FF)
	private static final int FIRST_JAMO = 0x1100;
	private static final int LAST_JAMO = 0x11FF;
	
	// 한글 호환성 자모 (Range : 3130-318F)
	private static final int FIRST_COMPATIBILITY_JAMO = 0x3130;
	private static final int LAST_COMPATIBILITY_JAMO = 0x318F;

//	// 한글 호환성 초성, 종성 범위 (Range : 3131-314E)
//	private static final int FIRST_COMPATIBILITY_CHOSEONG_JONGSEONG = 0x3131;
//	private static final int LAST_COMPATIBILITY_CHOSEONG_JONGSEONG = 0x314E;
	
	private static final HashMap<Integer, String> compatibilityChoseongs = new HashMap<Integer, String>() {

		{
			put(Integer.valueOf(0x3131), "ㄱ");
			put(Integer.valueOf(0x3132), "ㄲ");
			put(Integer.valueOf(0x3134), "ㄴ");
			put(Integer.valueOf(0x3137), "ㄷ");
			put(Integer.valueOf(0x3138), "ㄸ");
			put(Integer.valueOf(0x3139), "ㄹ");
			put(Integer.valueOf(0x3141), "ㅁ");
			put(Integer.valueOf(0x3142), "ㅂ");
			put(Integer.valueOf(0x3143), "ㅃ");
			put(Integer.valueOf(0x3145), "ㅅ");
			put(Integer.valueOf(0x3146), "ㅆ");
			put(Integer.valueOf(0x3147), "ㅇ");
			put(Integer.valueOf(0x3148), "ㅈ");
			put(Integer.valueOf(0x3149), "ㅉ");
			put(Integer.valueOf(0x314A), "ㅊ");
			put(Integer.valueOf(0x314B), "ㅋ");
			put(Integer.valueOf(0x314C), "ㅌ");
			put(Integer.valueOf(0x314D), "ㅍ");
			put(Integer.valueOf(0x314E), "ㅎ");
		}
		
	};
	
	// Syllable : (Range : AC00-D7AF)
	private static final int FIRST_SYLLABLE = 0xAC00;
	private static final int LAST_SYLLABLE = 0xD7AF;
	
	
	
    /**
     * 초성 (Initial consonants) : 19
     */
	private static final int CHOSEONGS_COUNT = 19;
    private static final String[] CHOSEONGS = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
    
    /**
     * 중성 (Medial vowels) : 21
     */
    private static final int JUNGSEONGS_COUNT = 21;
    private static final String[] JUNGSEONGS = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};
    
    /**
     * 종성 (Final consonants) : 28 (공백 포함)
     */
    private static final int JONGSEONGS_COUNT = 28;
    private static final String[] JONGSEONGS = {"", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
    
    
    
    /**
     * Constructor
     */
    public HanGul() {
    	
    }
    
    

    /**
     * @param c
     * @return 한글 자모인지 아닌지 여부
     */
    public static boolean isJamo(char c) {
    	if (c >= FIRST_JAMO && c <= LAST_JAMO) {
    		return true;
    	}

    	return false;
    
    }
    
    /**
     * @param c
     * @return 한글 호환성 자모인지 아닌지 여부
     */
    public static boolean isCompatibilityJamo(char c) {
    	if (c >= FIRST_COMPATIBILITY_JAMO && c <= LAST_COMPATIBILITY_JAMO) {
    		return true;
    	}

    	return false;
    
    }
    
    /**
     * @param c
     * @return 한글 호환성 초성인지 아닌지 여부
     */
    public static boolean isCompatibilityChoseong (char c) {
    	// 한글 호환성 초성
		if (compatibilityChoseongs.containsKey(Integer.valueOf((int) c))) {
			return true;
		}

		return false;
    	
    }
    
    /**
     * @param c
     * @return 한글 호환성 자모인지 아닌지, 초성인지 아닌지 여부
     */
    public static boolean isCompatibilityJamoAndChoseong (char c) {
    	
    	// 한글 호환성 자모
    	if (isCompatibilityJamo(c)) {
    		// 한글 호환성 초성
    		if (isCompatibilityChoseong(c)) {
    			return true;
    		}
    	}
    	
    	return false;
    	
    }

    /**
     * @param c
     * @return 한글 음절인지 아닌지 여부
     */
    public static boolean isSyllable(char c) {
    	if (c >= FIRST_SYLLABLE && c <= LAST_SYLLABLE) {
    		return true;
    	}

    	return false;
    }
    
    public static String getSplitWord(int[] indexes) {
    	
    	String splitWord = "" + CHOSEONGS[indexes[0]] + JUNGSEONGS[indexes[1]] + JONGSEONGS[indexes[2]];
    	
    	return splitWord;
    	
    }
    
    public static String getSplitFirstWord(int[] indexes) {
    	
    	String splitFirstWord = "" + CHOSEONGS[indexes[0]];
    	
    	return splitFirstWord;
    	
    }

    public static int[] getIndexes(char syllable) {
    	
    	int[] indexes = new int[3];

    	// 초성
    	indexes[0] = (syllable - FIRST_SYLLABLE) / (JUNGSEONGS_COUNT * JONGSEONGS_COUNT);
    	// 중성
    	indexes[1] = (syllable - FIRST_SYLLABLE) % (JUNGSEONGS_COUNT * JONGSEONGS_COUNT) / JONGSEONGS_COUNT;
    	// 종성
    	indexes[2] = (syllable - FIRST_SYLLABLE) % (JUNGSEONGS_COUNT * JONGSEONGS_COUNT) % JONGSEONGS_COUNT;
    	
    	return indexes;
    }
    
    public static char combine(int indexes[]) {
    	
    	int choseongIndex = 0;
    	int jungseongIndex = 0;
    	int jongseongIndex = 0;
    	
    	try {
    		choseongIndex = indexes[0];
    	} catch (IndexOutOfBoundsException e) {
    	}
    	
    	try {
    		jungseongIndex = indexes[1];
    	} catch (IndexOutOfBoundsException e) {
    	}

    	try {
    		jongseongIndex = indexes[2];
    	} catch (IndexOutOfBoundsException e) {
    	}
    	
    	char syllable = (char) (FIRST_SYLLABLE + (choseongIndex * JUNGSEONGS_COUNT + jungseongIndex) * JONGSEONGS_COUNT + jongseongIndex);

    	return syllable;
    	
    }
}
