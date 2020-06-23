package com.iptiq.config;

import com.google.inject.AbstractModule;

import com.iptiq.loadbalancer.LoadBalancer;
import com.iptiq.loadbalancer.RandomLoadBalancer;


public class BasicModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LoadBalancer.class).to(RandomLoadBalancer.class);
	}
}
