# 1 工程简介
动态执行代码,生成字节码等

## 1.1 javassit
动态生成字节码,
修改字节码(未被加载前)

## 1.2 groovy
执行一段java字符串代码
类似的还有 QLExpression 解析表达式
* 

## 1.3 javapoet
动态生成java代码
区别于javassist
* javapoet是定义的java类信息，不过生成的是java代码，然后利用javac进行编译，转为字节码;
* javassist则是直接操作字节码，只是为了代码简便，提供了类似java代码的方式定义字节码，
  即生成的是已经编译后的javac代码，直接运行在jvm里面，和你的java代码不相干，所以看起来甚至可以修改类的代码，
  因为本身就是字节码，就是生成和修改字节码的，不会修改你的java代码，如果修改你的java代码必须重新编译才会生效
* 参考 https://blog.csdn.net/baiaihan/article/details/88126874



