package com.softtek;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExecutorServicesNumberOfThreads { 
	
	public static void main(String[] args) throws InterruptedException {

		Log(Runtime.getRuntime().availableProcessors());
		
		Log(ForkJoinPool.commonPool().getParallelism());
		
		List<ExecutorService> executorServices = Arrays.asList( 
			Executors.newCachedThreadPool(),
			Executors.newFixedThreadPool(3),
			Executors.newSingleThreadExecutor(),
			Executors.newWorkStealingPool(),
			Executors.newScheduledThreadPool(5),
			ForkJoinPool.commonPool());
		
		List<Callable<Object>> tareas= 
			Stream.generate(ExecutorServicesNumberOfThreads::getTareaSleepUnSegundo)
				.limit(40)
				.collect(Collectors.toList());
		
		for(ExecutorService executorService: executorServices) {
			Instant inicio= Instant.now();
			executorService.invokeAll(tareas);
			Log(Duration.between(inicio, Instant.now()));
		}
		
		executorServices.stream().forEach(ExecutorService::shutdown);
	}
	
	private static Callable<Object> getTareaSleepUnSegundo() {
		return Executors.callable(()->{ 
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
	
	private static void Log(Object mensaje) {
		System.out.println(String.format("%s", mensaje.toString()));
	}
}