package com.glue.struct;


public enum Category {
	MUSIC("#3a87ad"), PERFORMING_ART("#b94a48"), SPORT("#468847"), CONFERENCE("#c09853"), EXHIBITION("#ef7c00"), OTHER(
			"#bd1789");

	private final String color;

	Category(String color) {
		this.color = color;
	}

	/**
	 * For EL access.
	 * 
	 * @return
	 */
	public String getName() {
		return name();
	}

	/**
	 * Returns the hexadecimal representation of the color associated with this
	 * category.
	 * 
	 * @return
	 */
	public String getColor() {
		return color;
	}
}
