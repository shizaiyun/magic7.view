package org.magic7.view.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.magic7.view.module.ResultBean;

public class ControllerAOP {
	
	public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
		long startTime = System.currentTimeMillis();
		ResultBean<?> result;
		try {
			result = (ResultBean<?>) pjp.proceed();
			System.out.println(pjp.getSignature() + " use time:" + (System.currentTimeMillis() - startTime));
		} catch (Throwable e) {
			result = handlerException(pjp, e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
		ResultBean<?> result = new ResultBean();
		System.out.println(pjp.getSignature() + " error ");
		e.printStackTrace();
		result.setMsg(e.getMessage());
		result.setCode(ResultBean.FAIL);
		return result;
	}
}
