package it.polito.ai.laboratorio1;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegistrationManager extends ConcurrentHashMap<String, RegistrationDetails> {

}
