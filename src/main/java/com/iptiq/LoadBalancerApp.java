package com.iptiq;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.iptiq.config.BasicModule;
import com.iptiq.exceptions.MaxNumberOfProvidersReachedException;
import com.iptiq.exceptions.NoAvailableProvidersException;
import com.iptiq.loadbalancer.LoadBalancer;
import com.iptiq.provider.Provider;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class LoadBalancerApp {

	@Inject
	private static LoadBalancer loadBalancer;

	public static void main(String... args) throws ExecutionException, InterruptedException {
		Injector injector = Guice.createInjector(new BasicModule());
		loadBalancer = injector.getInstance(LoadBalancer.class);

		for (int i = 0; i < 5; i++)
			try {
				loadBalancer.registerProvider(new Provider("Provider" + i, 5));
			} catch (MaxNumberOfProvidersReachedException e) {
				System.out.println(e.getLocalizedMessage());
			}

		ConcurrentLinkedQueue<Future<String>> futures = new ConcurrentLinkedQueue<>();

		while (true) {
			Thread.sleep(100);
			try {
				System.out.println("Getting a new request!");
				Future<String> s = loadBalancer.get();
				futures.add(s);
			} catch (RejectedExecutionException ex) {
				System.out.println(ex.getMessage());
			} catch (NoAvailableProvidersException e) {
				System.out.println(e.getMessage());
			}
			checkFutures(futures);
		}
	}

	private static void checkFutures(ConcurrentLinkedQueue<Future<String>> futureTargets) throws ExecutionException, InterruptedException {
		Iterator<Future<String>> it = futureTargets.iterator();
		Future<String> element;
		while (it.hasNext()) {
			element = it.next();
			if (element.isDone()) {
				System.out.println("Routed to: " + element.get());
				it.remove();
			}
		}
	}
}
