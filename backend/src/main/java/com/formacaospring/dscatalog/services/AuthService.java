package com.formacaospring.dscatalog.services;

import com.formacaospring.dscatalog.dto.EmailDTO;
import com.formacaospring.dscatalog.dto.NewPasswordDTO;
import com.formacaospring.dscatalog.entities.PasswordRecover;
import com.formacaospring.dscatalog.entities.User;
import com.formacaospring.dscatalog.repositories.PasswordRecoverRepository;
import com.formacaospring.dscatalog.repositories.UserRepository;
import com.formacaospring.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;


    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Transactional
    public void saveNewPassword(NewPasswordDTO body) {
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(body.getToken(), Instant.now());
        if(result.size() == 0){
            throw new ResourceNotFoundException("Token inválido");
        }

        User user = userRepository.findByEmail(result.get(0).getEmail());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user = userRepository.save(user);
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail(username);
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

}
