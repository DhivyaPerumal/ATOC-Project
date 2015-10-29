package com.bpa.qaproduct.springAOP;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointCutDefinition {
	
	@Pointcut("within(com.bpa.qaproduct.service.ExecutionService..*)")
	public void pointCutMethod()
	{
		System.out.println("PointCut Method Running");
	}
}
