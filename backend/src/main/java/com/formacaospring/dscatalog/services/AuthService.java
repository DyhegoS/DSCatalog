package com.formacaospring.dscatalog.services;

import com.formacaospring.dscatalog.dto.EmailDTO;
import com.formacaospring.dscatalog.entities.PasswordRecover;
import com.formacaospring.dscatalog.entities.User;
import com.formacaospring.dscatalog.repositories.PasswordRecoverRepository;
import com.formacaospring.dscatalog.repositories.UserRepository;
import com.formacaospring.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;


    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createRecoverToken(EmailDTO body) {
        User user = userRepository.findByEmail(body.getEmail());
        if(user == null){
            throw new ResourceNotFoundException("e-mail não encontrado");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity = passwordRecoverRepository.save(entity);

        String bodyMessage = "Acesse o link para definir a nova senha\n\n"
                + recoverUri + token + ". Validade de " + tokenMinutes + " minutos";

        emailService.sendEmail(body.getEmail(), "Recuperação de Senha", bodyMessage);
    }
}
