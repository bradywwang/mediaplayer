package com.brady.mediaplayer.utils;

public class ConvertUtils {
	public static String formatTime(long time) {
		String min = String.valueOf(time / (1000 * 60));
		if (min.length() < 2)
			min = "0" + min;
		String sec = String.valueOf(time % (1000 * 60));
		if (sec.length() >= 4) {
			sec = "0" + sec;
		} else if (sec.length() == 3) {
			sec = "00";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}
	
	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;

	public static String convertToStringRepresentation(final long value){
	    final long[] dividers = new long[] { T, G, M, K, 1 };
	    final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
	    if(value < 1)
	        throw new IllegalArgumentException("Invalid file size: " + value);
	    String result = null;
	    for(int i = 0; i < dividers.length; i++){
	        final long divider = dividers[i];
	        if(value >= divider){
	            result = format(value, divider, units[i]);
	            break;
	        }
	    }
	    return result;
	}

	private static String format(final long value,
	    final long divider,
	    final String unit){
	    final double result =
	        divider > 1 ? (double) value / (double) divider : (double) value;
	    return String.format("%.1f %s", Double.valueOf(result), unit);
	}
}
