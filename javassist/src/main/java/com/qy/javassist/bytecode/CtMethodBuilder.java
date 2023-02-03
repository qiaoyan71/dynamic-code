/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.qy.javassist.bytecode;

import com.qy.javassist.utils.JavassistUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import org.apache.commons.lang3.builder.Builder;

public class CtMethodBuilder implements Builder<CtMethod>{

	private CtMethod method;

	/**
	 * ct方法构建器
	 * @param modifiers  修饰符 {@link javassist.bytecode.AccessFlag or javassist.Modifier}
	 * @param returnType 返回类型
	 * @param mname      mname
	 * @param parameters 参数
	 * @param exceptions 异常
	 * @param body       身体
	 * @param declaring  声明
	 * @throws CannotCompileException 不能编译例外
	 */
	private CtMethodBuilder(int modifiers, CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, String body, CtClass declaring) throws CannotCompileException {
		try {
			CtMethod cm = new CtMethod(returnType, mname, parameters, declaring);
			cm.setModifiers(modifiers);
			cm.setExceptionTypes(exceptions);
			cm.setBody(body);
			this.method = cm;
		} catch (NotFoundException e) {
			throw new CannotCompileException(e);
		}
	}

	private CtMethodBuilder(CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, CtClass declaring) throws CannotCompileException {
		try {
			CtMethod cm = new CtMethod(returnType, mname, parameters, declaring);
			cm.setExceptionTypes(exceptions);
			this.method = cm;
		} catch (NotFoundException e) {
			throw new CannotCompileException(e);
		}
	}

	public static CtMethodBuilder create(CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, String body,CtClass declaring) throws CannotCompileException {
		return new CtMethodBuilder(Modifier.PUBLIC,returnType,mname,parameters,exceptions,body,declaring);
	}

	public static CtMethodBuilder create(int modifier,CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, String body,CtClass declaring) throws CannotCompileException {
		return new CtMethodBuilder(modifier,returnType,mname,parameters,exceptions,body,declaring);
	}

	public static CtMethodBuilder abstractCreate(CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, String body, CtClass declaring) throws CannotCompileException {
		return new CtMethodBuilder(returnType, mname, parameters, exceptions, declaring);
	}

	public CtMethodBuilder markClass(CtClass clazz) throws CannotCompileException {
		JavassistUtils.addClassMethod(clazz,this.method);
		return this;
	}


	@Override
	public CtMethod build() {
		return method;
	}
	
}
