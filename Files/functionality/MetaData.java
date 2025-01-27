package functionality;

import java.io.Serializable;

class MetaData implements Serializable{
	private int fontSize;
	private String fontName="";
	private boolean wordWrap;
	
	public MetaData(int fontSize, String fontName, boolean wordWrap) {
		this.fontSize = fontSize;
		this.fontName = fontName;
		this.wordWrap = wordWrap;
	}

	public int getFontSize() {
		return fontSize;
	}

	public String getFontName() {
		return fontName;
	}

	public boolean isWordWrap() {
		return wordWrap;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public void setWordWrap(boolean wordWrap) {
		this.wordWrap = wordWrap;
	}
}