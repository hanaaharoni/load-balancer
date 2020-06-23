package com.iptiq.loadbalancer;

import com.iptiq.exceptions.MaxNumberOfProvidersReachedException;
import com.iptiq.provider.Provider;
import org.mockito.Mockito;
import org.testng.annotations.DataProvider;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

public class DataProviderClass {

	@DataProvider(name = "RoundRobinLoadBalancerInit")
	public Object[][] initializeRoundRobin() throws MaxNumberOfProvidersReachedException {
		LoadBalancer lb = spy(new RoundRobinLoadBalancer());
		Mockito.doNothing().when(lb).heartBeatCheck();
		for (int i = 0; i < 5; i++) {
			lb.registerProvider(new Provider("Provider" + i, 3));
		}
		return new Object[][]{{lb}};
	}

	@DataProvider(name = "RandomBalancerInit")
	public Object[][] initializeRandom() throws MaxNumberOfProvidersReachedException {
		LoadBalancer lb = new RandomLoadBalancer();
		lb = spy(lb);
		doNothing().when(lb).heartBeatCheck();
		for (int i = 0; i < 5; i++) {
			lb.registerProvider(new Provider("Provider" + i, 3));
		}
		return new Object[][]{{lb}};
	}
}
