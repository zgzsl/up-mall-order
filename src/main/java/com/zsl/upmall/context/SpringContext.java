package com.zsl.upmall.context;

import org.springframework.context.ApplicationContext;

public class SpringContext {

	private static ApplicationContext applicationContext = null;
	
	public static void setApplicationContext(ApplicationContext context){
		if(null == SpringContext.applicationContext){
			SpringContext.applicationContext = context;
		}
		System.out.println("=========SpringContext 上下文配置成功==========");
	}
	
	/**
	 * 
	 * getContext:(获取spring 上下文). <br/>
	 * @return
	 * @since
	 */
	public static ApplicationContext getContext() {
		return applicationContext;
	}
	
	/**
	 * 
	 * getBean:(根据name获取Bean). <br/>
	 * @param beanName
	 * @return
	 * @since
	 */
	public static Object getBean(String beanName){
		return applicationContext.getBean(beanName);
	}
	
	/**
	 * 
	 * getBean:(根据class获取bean). <br/>
	 * @param clazz
	 * @return
	 * @since
	 */
	public static <T> T getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}
	
	/**
	 * 
	 * getBean:(通过name,以及class获取指定的Bean). <br/>
	 * @param name
	 * @param clazz
	 * @return
	 * @since
	 */
	public static <T> T getBean(String name,Class<T> clazz){
		return applicationContext.getBean(name, clazz);
	}
}

