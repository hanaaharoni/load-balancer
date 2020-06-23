package com.iptiq.loadbalancer;

import com.iptiq.exceptions.MaxNumberOfProvidersReachedException;
import com.iptiq.exceptions.NoAvailableProvidersException;
import com.iptiq.provider.Provider;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.mockito.Mockito.mock;

public class LoadBalancerTest {


	@Test(dataProvider = "RoundRobinLoadBalancerInit", dataProviderClass = DataProviderClass.class)
	public void shouldReturnActiveProvidersOnly(LoadBalancer lb) {
		Assert.assertEquals(lb.getActiveProviders().size(), 5);
	}

	@Test(dataProvider = "RoundRobinLoadBalancerInit", dataProviderClass = DataProviderClass.class)
	public void shouldIncludeProvider(LoadBalancer lb) throws MaxNumberOfProvidersReachedException {
		Provider p = new Provider();
		lb.registerProvider(p);
		lb.includeProvider(p);
		Assert.assertTrue(lb.getActiveProviders().contains(p));
	}

	@Test(dataProvider = "RoundRobinLoadBalancerInit", dataProviderClass = DataProviderClass.class)
	public void shouldExcludeProvider(LoadBalancer lb) {
		Provider p = lb.getActiveProviders().get(0);
		lb.excludeProvider(p);
		Assert.assertFalse(lb.getActiveProviders().contains(p));
	}

	@Test(dataProvider = "RoundRobinLoadBalancerInit", dataProviderClass = DataProviderClass.class)
	public void shouldRegisterNewProvider(LoadBalancer lb) throws MaxNumberOfProvidersReachedException {
		Provider p = new Provider("testProvider", 3);
		lb.registerProvider(p);
		Assert.assertTrue(lb.getActiveProviders().contains(p));
	}

	@Test(dataProvider = "RoundRobinLoadBalancerInit", dataProviderClass = DataProviderClass.class, expectedExceptions = MaxNumberOfProvidersReachedException.class)
	public void shouldThrowMaxNumberOfProvidersReachedException(LoadBalancer lb) throws MaxNumberOfProvidersReachedException {
		for (int i = 0; i < 10; i++) {
			lb.registerProvider(new Provider());
		}
	}

	@Test(dataProvider = "RoundRobinLoadBalancerInit", dataProviderClass = DataProviderClass.class)
	public void shouldNotThrowNoAvailableProvidersException(LoadBalancer lb) throws NoAvailableProvidersException, InterruptedException {
		int capacity = lb.getActiveProviders().size() * 3;
		List<Future<String>> futureTargets = new ArrayList<>();
		for (int i = 0; i < capacity; i++) {
			futureTargets.add(lb.get());
		}
		Assert.assertEquals(futureTargets.size(), capacity);
	}
}
