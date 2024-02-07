//package com.digiboridev.rxpg.service
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.mail.SimpleMailMessage
//import org.springframework.mail.javamail.JavaMailSender
//import org.springframework.stereotype.Service
//
//@Service
//class MailingService(
//    val javaMailSender: JavaMailSender,
//    @Value("\${spring.mail.sender.email}")
//    val senderEmail: String,
//    @Value("\${spring.mail.sender.text}")
//    val senderText: String,
//) {
//    fun sendEmail(email: String, subject: String, message: String) {
//        println("Sending email to $email with subject: $subject and message: $message")
//
//        val mail = SimpleMailMessage()
//
//        mail.setTo(email)
//        mail.from = senderEmail
//        mail.subject = subject
//        mail.text = "$senderText\n\n$message"
//
//        javaMailSender.send(mail)
//        println("Email sent to $email")
//    }
//}