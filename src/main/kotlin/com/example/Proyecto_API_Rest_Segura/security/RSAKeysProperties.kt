package com.example.Proyecto_API_Rest_Segura.security

import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@ConfigurationProperties(prefix = "rsa")
data class RSAKeysProperties(
    var publicKey : RSAPublicKey,
    var privateKey: RSAPrivateKey
)