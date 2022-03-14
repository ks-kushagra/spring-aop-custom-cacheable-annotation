package com.airtel.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CacheableWithRefreshScopeAdvice {
	
	Logger log=LoggerFactory.getLogger(CacheableWithRefreshScopeAdvice.class);

	private ConcurrentHashMap<Object , Object> hashMap ;

	public CacheableWithRefreshScopeAdvice(){
		hashMap = new ConcurrentHashMap<>();
	}

	public CacheableWithRefreshScopeAdvice(ConcurrentHashMap<Object, Object> hashMap) {
		this.hashMap = hashMap;
	}

	@Around("@annotation(com.airtel.advice.CacheableWithRefreshScope)")
	public Object cacheOperation(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
		String methodName = pjp.getSignature().getName();
		String className = pjp.getTarget().toString();
		Object[] arguments = pjp.getArgs();
		log.info("Performing Cache Operation for method : " + className +"." + methodName + " args : " +
				Arrays.toString(arguments));
		Object key = SimpleKeyGenerator.generateKey(arguments);
		if(hashMap.containsKey(key)){
			log.info("Time taken if key found : {}ms" ,(System.currentTimeMillis()-startTime));
			return hashMap.get(key);
		}else{
			Object response = pjp.proceed();
			hashMap.put(key,response);
			log.info("Time taken if key not found : {}ms" ,(System.currentTimeMillis()-startTime));
			return response;
		}
	}

}
