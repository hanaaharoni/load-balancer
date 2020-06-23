package com.iptiq.loadbalancer;

import com.iptiq.exceptions.NoAvailableProvidersException;
import com.iptiq.provider.Provider;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

public class RandomLoadBalancer extends LoadBalancer {

	private Random random;

	public RandomLoadBalancer() {
		super();
		this.random = new Random();
	}

	@Override
	public Future<String> get() throws NoAvailableProvidersException {
		List<Provider> providerList = getActiveProviders();
		if (providerList.isEmpty())
			throw new NoAvailableProvidersException();
		Provider provider = providerList.get(this.random.nextInt(providerList.size()));
		return super.executeProvider(provider);
	}
}

