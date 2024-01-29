package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.stereotype.Component
import java.util.*


interface PolyBean {
    fun foo()
}

class PolyBeanImpl1 : PolyBean {
    override fun foo() = println("PolyBeanImpl1")
}

class PolyBeanImpl2 : PolyBean {
    override fun foo() = println("PolyBeanImpl2")
}

@Component
class BeanConsumer( private val polyBean: PolyBean ) {
    @Autowired
    lateinit var testBean: String
    @Autowired
    @Qualifier("testBean2")
    lateinit var testBeanRenamed: String

    fun doSomething() {
        println("PolyBeanConsumer doSomething")
        polyBean.foo()
    }
}


@SpringBootApplication
@EnableMongoAuditing
class DemoApplication {
    @Bean("testBean")
    fun testBean(): String = "123"
    @Bean("testBean2")
    fun testBean2(): String = "456"
    @Bean
    fun polyBean(): PolyBean = PolyBeanImpl1()


}

fun main(args: Array<String>) {
    val context = runApplication<DemoApplication>(*args)
    println("DemoApplication Started")

    val beanConsumer = context.getBean(BeanConsumer::class.java)
    beanConsumer.doSomething()
}
