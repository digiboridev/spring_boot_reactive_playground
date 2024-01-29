package com.example.demo

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


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

    @PostConstruct
    fun postConstruct() {
        println("testBean: $testBean")
        println("testBeanRenamed: $testBeanRenamed")
        polyBean.foo()
    }
}

@Configuration
class BeanTestConfig {
    @Bean("testBean")
    fun testBean(): String = "123"
    @Bean("testBean2")
    fun testBean2(): String = "456"
    @Bean
    fun polyBean(): PolyBean = PolyBeanImpl1()
}
