package br.com.giovanna.todolist.filter;


import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.giovanna.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.equals("/tasks/")) {
            // Verificar a autenticação:
            var authorization = request.getHeader("Authorization");

            // Excluir a palavra Basic do número da Authorization em base 64:
            var authEncoded = authorization.substring("Basic".length()).trim();

            // Decode do base 64:
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            // Conversão em String:

            var authString = new String(authDecode);

            // Criar divisão na String para a visualização do usuário e senha:
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            // Validar o usuário e a senha:
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuário sem autorização!");
            } else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Senha inválida");
                }
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}



