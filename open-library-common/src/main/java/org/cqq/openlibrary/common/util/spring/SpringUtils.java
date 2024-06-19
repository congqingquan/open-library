package org.cqq.openlibrary.common.util.spring;

import org.cqq.openlibrary.common.util.ArrayUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Spring utils
 *
 * @author Qingquan.Cong
 */
public class SpringUtils implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    
    private static ApplicationContext applicationContext;

    private static Environment environment;

    private static BeanDefinitionRegistry beanDefinitionRegistry;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        SpringUtils.applicationContext = applicationContext;
        SpringUtils.environment = applicationContext.getEnvironment();
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        SpringUtils.beanDefinitionRegistry = beanDefinitionRegistry;
    }
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Do nothing
    }
    
    /**
     * ==================== Registry function ====================
     */

    public static <T> T createBean(String beanName, Class<T> beanClass, Object... constructorArgumentValues) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
        for (Object constructorArgumentValue : constructorArgumentValues) {
            argumentValues.addGenericArgumentValue(constructorArgumentValue);
        }
        beanDefinition.setConstructorArgumentValues(argumentValues);
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
        return getBean(beanClass);
    }
    
    /**
     * ==================== Basic function ====================
     */


    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }


    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static boolean isPrototype(String name) {
        return applicationContext.isPrototype(name);
    }

    public static Resource getResource(String location) {
        return applicationContext.getResource(location);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationClass) {
        return applicationContext.getBeansWithAnnotation(annotationClass);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> typeClass) {
        return applicationContext.getBeansOfType(typeClass);
    }

    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }
    
    public static <T> T getCurrentProxy(Class<T> proxyBeanClass) {
        try {
            Object currentProxy = AopContext.currentProxy();
            if (proxyBeanClass.isAssignableFrom(currentProxy.getClass())) {
                return proxyBeanClass.cast(currentProxy);
            }
        } catch (IllegalStateException ignored) {
        }
        return null;
    }
    
    /**
     * ==================== Environment function ====================
     */

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static <T> T getProperty(String key, Class<T> typeClass) {
        return environment.getProperty(key, typeClass);
    }

    public static String currentEnvironment() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (ArrayUtils.isEmpty(activeProfiles)) {
            throw new RuntimeException("No environment set");
        }
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }

    public static boolean inEnvironment(String environment) {
        return currentEnvironment().equals(environment);
    }

}
