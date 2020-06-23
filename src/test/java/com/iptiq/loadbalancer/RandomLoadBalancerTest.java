package com.iptiq.loadbalancer;

import com.iptiq.exceptions.NoAvailableProvidersException;
import com.iptiq.provider.Provider;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RandomLoadBalancerTest {

	@Test(dataProvider = "RandomBalancerInit", dataProviderClass = DataProviderClass.class)
	public void shouldGetRandomProvider(LoadBalancer lb) throws NoAvailableProvidersException, InterruptedException, ExecutionException {
		Future<String> target = lb.get();
		Assert.assertNotNull(target.get());
	}

	@Test(dataProvider = "RandomBalancerInit", dataProviderClass = DataProviderClass.class, expectedExceptions = NoAvailableProvidersException.class)
	public void shouldThrowNoAvailableProvidersException(LoadBalancer lb) throws NoAvailableProvidersException, InterruptedException, ExecutionException {
		for (Provider p : lb.getActiveProviders())
			lb.excludeProvider(p);
		Future<String> target = lb.get();
		Assert.assertNull(target.get());
	}
}
