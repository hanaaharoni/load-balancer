package com.iptiq.loadbalancer;

import com.iptiq.exceptions.MaxNumberOfProvidersReachedException;
import com.iptiq.exceptions.NoAvailableProvidersException;
import com.iptiq.provider.Provider;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RandomLoadBalancerTest {

	@Test(dataProvider = "RandomBalancerInit", dataProviderClass = DataProviderClass.class)
	public void shouldGetRandomProvider(LoadBalancer lb) throws NoAvailableProvidersException, InterruptedException, ExecutionException, MaxNumberOfProvidersReachedException {
		Provider p = Mockito.mock(Provider.class);
		Mockito.when(p.get()).thenReturn("hello");
		lb.registerProvider(p);
		Future<String> target = lb.get();
		Assert.assertNotNull(target.get());
		verify(p, times(1)).get();
	}

	@Test(dataProvider = "RandomBalancerInit", dataProviderClass = DataProviderClass.class, expectedExceptions = NoAvailableProvidersException.class)
	public void shouldThrowNoAvailableProvidersException(LoadBalancer lb) throws NoAvailableProvidersException, InterruptedException, ExecutionException, MaxNumberOfProvidersReachedException {
		List<Provider> providers = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Provider p = Mockito.mock(Provider.class);
			lb.registerProvider(p);
			providers.add(p);
		}
		for (Provider p : lb.getActiveProviders())
			lb.excludeProvider(p);
		Future<String> target = lb.get();
		Assert.assertNull(target.get());
		for (Provider p : providers)
			verify(p, times(0)).get();
	}
}
