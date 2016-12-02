package tool;

import android.util.DisplayMetrics;

public class ScreenInfo {
	
	public double density;
	
	public int widthDp;
	
	public int heightDp;
	
	public int widthPixel;
	
	public int heightPixel;
	
	private ScreenInfo() {
		obtainInfo();
	}
	
	private void obtainInfo() {
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		UI.TopActivity.getWindowManager()
				.getDefaultDisplay().getMetrics(displayMetrics);
		
		density = displayMetrics.density;
		widthPixel = displayMetrics.widthPixels;
		heightPixel = displayMetrics.heightPixels;
		widthDp = (int)(displayMetrics.widthPixels / displayMetrics.density);
		heightDp = (int)(displayMetrics.heightPixels / displayMetrics.density);
	}
	

	private static ScreenInfo instance;

	public static ScreenInfo get() {
		if (instance == null) {
			instance = new ScreenInfo();
		}
		return instance;
	}
}
