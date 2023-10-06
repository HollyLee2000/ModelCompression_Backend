package org.zjuvipa.util;

//import org.zjuvipa.entity.Problem;
//import org.zjuvipa.info.ProblemInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyBeanUtils extends BeanUtils {
	public static void copyProperties(Object source, Object target) throws BeansException {
//		Assert.notNull(source, "Source must not be null");
//		Class<?> sourceClass = source.getClass();
//		PropertyDescriptor[] sourcePds = getPropertyDescriptors(sourceClass);
//
//		Assert.notNull(target, "Target must not be null");
//		Class<?> targetClass = target.getClass();
//		PropertyDescriptor[] targetPds = getPropertyDescriptors(targetClass);
//
//		//获取相同的name
//		List<PropertyDescriptor> Pds = new ArrayList<PropertyDescriptor>();
//		for (PropertyDescriptor pd1 : sourcePds) {
//			for (PropertyDescriptor pd2 : targetPds) {
//				if (pd1.getName().equals(pd2.getName())) {
//					Pds.add(pd1);
//					break;
//				}
//			}
//		}
//
//		for (PropertyDescriptor targetPd : Pds) {
//			if (targetPd.getWriteMethod() != null) {
//				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
//				if (sourcePd != null && sourcePd.getReadMethod() != null) {
//					try {
//						Method readMethod = sourcePd.getReadMethod();
//						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
//							readMethod.setAccessible(true);
//						}
//						Object value = readMethod.invoke(source);
//						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
//						if (value != null) {
//							Method writeMethod = targetPd.getWriteMethod();
//							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
//								writeMethod.setAccessible(true);
//							}
//							writeMethod.invoke(target, value);
//						}
//					} catch (Throwable ex) {
//						throw new FatalBeanException("Could not copy properties from source to target", ex);
//					}
//				}
//			}
//		}

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if (value != null) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}

//	public static void main(String[] args) {
//		Problem problem = new Problem();
//		problem.setProblemId("0001");
//		problem.setProblemInfo("这里什么都没有");
//		problem.setProblemLevel(1);;
//		problem.setProblemPanes("000006010004012007519070026140069750750000089092150034930080245400720900020900000");
//
//		ProblemInfo problemInfo = new ProblemInfo();
//		MyBeanUtils.copyProperties(problem,problemInfo);
//		System.out.println(problemInfo.toString());
//	}
}