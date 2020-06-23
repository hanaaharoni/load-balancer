package com.iptiq.loadbalancer;

import com.iptiq.exceptions.NoAvailableProvidersException;
import com.iptiq.provider.Provider;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

public class RoundRobinLoadBalancerTest {

	@Test(dataProvider = "RoundRobinLoadBalancerInit", dataProviderClass = DataProviderClass.class)
	public void shouldGetRoundRobinProvider(LoadBalancer lb) throws NoAvailableProvidersException, InterruptedException, ExecutionException {
		List<Future<String>> targetList = new ArrayList<>();
		List<String> targetNameList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Future<String> target = lb.get();
			Assert.assertNotNull(target);
			targetList.add(target);
		}
		Iterator<Future<String>> it = targetList.iterator();
		Future<String> element;
		while (it.hasNext()) {
			element = it.next();
			targetNameList.add(element.get());
			it.remove();
		}
		Assert.assertEquals(targetNameList.size(), 10);
		Assert.assertEquals(targetNameList.get(0), targetNameList.get(5));
		Assert.assertEquals(targetNameList.get(1), targetNameList.get(6));
		Assert.assertEquals(targetNameList.get(2), targetNameList.get(7));
	}

	@Test(dataProvider = "RoundRobinLoadBalancerInit", dataProviderClass = DataProviderClass.class, expectedExceptions = NoAvailableProvidersException.class)
	public void shouldThrowNoAvailableProvidersException(LoadBalancer lb) throws NoAvailableProvidersException, InterruptedException {
		for (Provider p : lb.getActiveProviders())
			lb.excludeProvider(p);
		lb.get();
	}
}
