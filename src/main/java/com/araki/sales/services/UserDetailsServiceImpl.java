package com.araki.sales.services;

import com.araki.sales.model.Cliente;
import com.araki.sales.repositories.ClienteRepository;
import com.araki.sales.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(username);

        if(cliente == null){
            throw new UsernameNotFoundException(username);
        }
        return new UserSS(cliente.getId(),cliente.getEmail(), cliente.getPassword(), cliente.getPerfis());
    }
}
