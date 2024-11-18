package org.cqq.openlibrary.common.util.spring;

import org.cqq.openlibrary.common.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;
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
 * @author Qingquan
 */
public class SpringUtils implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    
    private ApplicationContext applicationContext;

    private Environment environment;

    private BeanDefinitionRegistry beanDefinitionRegistry;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(@NotNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }
    
    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Do nothing
    }
    
    // ======================================== Registry function ========================================

    public <T> T createBean(String beanName, Class<T> beanClass, Object... constructorArgumentValues) {
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
    
    //  ======================================== Basic function ========================================
    
    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }


    public <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public boolean isPrototype(String name) {
        return applicationContext.isPrototype(name);
    }

    public Resource getResource(String location) {
        return applicationContext.getResource(location);
    }

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationClass) {
        return applicationContext.getBeansWithAnnotation(annotationClass);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> typeClass) {
        return applicationContext.getBeansOfType(typeClass);
    }

    public Class<?> getType(String name) {
        return applicationContext.getType(name);
    }
    
    public <T> T getCurrentProxy(Class<T> proxyBeanClass) {
        try {
            Object currentProxy = AopContext.currentProxy();
            if (proxyBeanClass.isAssignableFrom(currentProxy.getClass())) {
                return proxyBeanClass.cast(currentProxy);
            }
        } catch (IllegalStateException ignored) {
        }
        return null;
    }
    
    // ======================================== Environment function ========================================

    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    public <T> T getProperty(String key, Class<T> typeClass) {
        return environment.getProperty(key, typeClass);
    }

    public String currentEnvironment() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (ArrayUtils.isEmpty(activeProfiles)) {
            throw new RuntimeException("No environment set");
        }
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }

    public boolean inEnvironment(String environment) {
        return currentEnvironment().equals(environment);
    }

}
