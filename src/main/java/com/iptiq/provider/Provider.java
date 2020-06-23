package com.iptiq.provider;

import java.util.Random;
import java.util.UUID;

public class Provider {

	String id;
	int requestCapacity;

	//creates providers with details on initialisation
	public Provider() {
		;
		this.id = UUID.randomUUID().toString();
		this.requestCapacity = 10;
	}

	public Provider(String id, int requestCapacity) {
		this.id = id;
		this.requestCapacity = requestCapacity;
	}

	public String get() throws InterruptedException {
		//add some delay time for the request
		Thread.sleep(100);
		return this.id;
	}

	public boolean check() {
		Random rnd = new Random();
		return rnd.nextBoolean();
	}

	public int getRequestCapacity() {
		return this.requestCapacity;
	}

	//for printing logs usage
	public String getName(){
		return this.id;
	}
}
