package com.iptiq.loadbalancer;

import com.iptiq.exceptions.NoAvailableProvidersException;
import com.iptiq.provider.Provider;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer extends LoadBalancer {

	private final AtomicInteger position;

	public RoundRobinLoadBalancer() {
		super();
		this.position = new AtomicInteger(0);
	}

	@Override
	public Future<String> get() throws NoAvailableProvidersException {
		List<Provider> providerList = getActiveProviders();
		if (providerList.isEmpty()) {
			throw new NoAvailableProvidersException();
		}
		Provider target;

		synchronized (this.position) {
			if (this.position.get() > providerList.size() - 1) {
				this.position.set(0);
			}
			target = providerList.get(this.position.get());
			this.position.incrementAndGet();
		}
		return super.executeProvider(target);
	}
}

