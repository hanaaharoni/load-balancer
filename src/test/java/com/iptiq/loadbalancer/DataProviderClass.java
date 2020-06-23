package com.iptiq.loadbalancer;

import com.iptiq.exceptions.MaxNumberOfProvidersReachedException;
import com.iptiq.provider.Provider;
import org.mockito.Mockito;
import org.testng.annotations.DataProvider;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

public class DataProviderClass {

	@DataProvider(name = "RoundRobinLoadBalancerInit")
	public Object[][] initializeRoundRobin() throws MaxNumberOfProvidersReachedException, InterruptedException {
		LoadBalancer lb = spy(new RoundRobinLoadBalancer());
		Mockito.doNothing().when(lb).heartBeatCheck();
		for (int i = 0; i < 5; i++) {
			Provider p = Mockito.mock(Provider.class);
			Mockito.when(p.get()).thenReturn("Provider" + i);
			Mockito.when(p.getName()).thenReturn("Provider" + i);
			lb.registerProvider(p);
		}
		return new Object[][]{{lb}};
	}

	@DataProvider(name = "RandomBalancerInit")
	public Object[][] initializeRandom() throws MaxNumberOfProvidersReachedException {
		LoadBalancer lb = new RandomLoadBalancer();
		lb = spy(lb);
		doNothing().when(lb).heartBeatCheck();
		return new Object[][]{{lb}};
	}
}
