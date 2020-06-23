package com.iptiq;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.iptiq.config.BasicModule;
import com.iptiq.exceptions.MaxNumberOfProvidersReachedException;
import com.iptiq.exceptions.NoAvailableProvidersException;
import com.iptiq.loadbalancer.LoadBalancer;
import com.iptiq.provider.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class LoadBalancerApp {

	private final static Logger logger = LogManager.getLogger(LoadBalancerApp.class);
	@Inject
	private static LoadBalancer loadBalancer;

	public static void main(String... args) throws ExecutionException, InterruptedException {
		Injector injector = Guice.createInjector(new BasicModule());
		loadBalancer = injector.getInstance(LoadBalancer.class);

		for (int i = 0; i < 5; i++)
			try {
				loadBalancer.registerProvider(new Provider());
			} catch (MaxNumberOfProvidersReachedException e) {
				logger.info(e.getLocalizedMessage());
			}

		ConcurrentLinkedQueue<Future<String>> futures = new ConcurrentLinkedQueue<>();

		while (true) {
			Thread.sleep(100);
			try {
				logger.info("Getting a new request!");
				Future<String> s = loadBalancer.get();
				futures.add(s);
			} catch (RejectedExecutionException | NoAvailableProvidersException ex) {
				logger.warn(ex.getMessage());
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
				logger.info(String.format("Routed to: %s", element.get()));
				it.remove();
			}
		}
	}
}
