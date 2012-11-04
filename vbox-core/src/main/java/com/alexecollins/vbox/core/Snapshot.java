package com.alexecollins.vbox.core;

/**
 * @author alex.e.c@gmail.com
 */
public class Snapshot {
	public static final Snapshot POST_CREATION = new Snapshot("post-creation");
	public static final Snapshot POST_PROVISIONING = new Snapshot("post-provisioning");
	private final String name;

	private Snapshot(String name) {
		if (name == null) {throw new IllegalArgumentException();}
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Snapshot snapshot = (Snapshot) o;

		if (!name.equals(snapshot.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public static Snapshot valueOf(String name) {
		return new Snapshot(name)                ;
	}
}
