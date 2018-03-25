package com.booking.common;

public enum RoomType {
	SINGLE(1), DOUBLE(2);

	private int value;

	private RoomType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static RoomType getRoomType(int value) {
		for (RoomType type : RoomType.values()) {
			if (type.getValue() == value)
				return type;
		}
		return null;
	}

}
