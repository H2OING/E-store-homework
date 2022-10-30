package com.example.demo.Service;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Product;
import com.example.demo.Model.Role;
import com.example.demo.Model.Web_User;
import com.example.demo.Repository.Cart_Repository;
import com.example.demo.Repository.Web_User_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collection;

import java.math.BigDecimal;
import java.util.*;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

@Service
public class Web_User_Service implements UserDetailsService {
    @Autowired
    Web_User_Repository webUserRepository;
    @Autowired
    Cart_Repository cartRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static boolean isLoggedIn = false;
/*
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

 */

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public List<Web_User> getAllWebUsers(){
        return webUserRepository.findAll();
    }

    public Web_User getWebUserById(long id){
        Optional<Web_User> webUser = webUserRepository.findById(id);
        if(webUser.isPresent()){
            return webUser.get();
        } else{
            throw new EntityNotFoundException();
        }
    }

    public Web_User getLoggedInWebUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return webUserRepository.findByEmail(user.getUsername());
    }

    public Boolean isLoggedIn(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication && !("anonymousUser").equals(authentication.getName());
    }
    public Boolean isCustomer(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            return true;
        }
        return false;
    }

    public Web_User createWebUser(Web_User webUser){
        webUser.setRole(Role.ROLE_CUSTOMER);
        webUser.setPassword(passwordEncoder.encode(webUser.getPassword()));
        cartRepository.save(new Cart(BigDecimal.ZERO, true, webUser, new ArrayList<Product>()));
        return webUserRepository.save(webUser);
    }

    public boolean updateWebUser(Long id, Web_User webUser){
        Optional<Web_User> optionalWebUser = webUserRepository.findById(id);
        if(optionalWebUser.isPresent()){
            Web_User existingWebUser = optionalWebUser.get();
            existingWebUser.setName(webUser.getName());
            existingWebUser.setSurname(webUser.getSurname());
            existingWebUser.setPhoneNumber(webUser.getPhoneNumber());
            existingWebUser.setEmail(webUser.getEmail());
            webUserRepository.save(existingWebUser);
            return true;
        } 
        return false;
    }

    public boolean deleteWebUser(Long id){
        Optional<Web_User> optionalWebUser= webUserRepository.findById(id);
        if(optionalWebUser.isPresent()){
			webUserRepository.deleteById(id);
			return true;
		}
			return false;		
	}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Web_User webUser = webUserRepository.findByEmail(username);
        if(webUser == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new User(webUser.getEmail(), webUser.getPassword(), mapRolesToAuthorities(List.of(webUser.getRole().name())));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<String> roles){
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    public boolean getWebUserByEmailAndPassword(@NotNull String email, @NotNull String password) {
        if(webUserRepository.existsByEmail(email)){
            Web_User wUser = webUserRepository.findByEmail(email);
            if(wUser.checkPassword(password)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
