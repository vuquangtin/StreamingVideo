package com.org.videomonitor.models;

public class ComboBoxItemInfor {
	private int id;
	private String key;
	private String name;

	public ComboBoxItemInfor(int id, String name) {
		this.setId(id);
		this.setName(name);

	}

	public ComboBoxItemInfor(int id, String name, String key) {
		this.setId(id);
		this.setName(name);
		this.setKey(key);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}