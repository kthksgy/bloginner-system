package com.kthksgy.bloginnersystem.security;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import lombok.Getter;

public class MyLoginUser extends org.springframework.security.core.userdetails.User {
	/**
	 * 認証ユーザーのユーザー情報
	 */
	@Getter
    private com.kthksgy.bloginnersystem.entities.User user;

    public MyLoginUser(com.kthksgy.bloginnersystem.entities.User user) {
        super(user.getUsername(), user.getPassword(), makeAuthorities(user.getRestriction()));
        this.user = user;
    }

    /**
     * 制限レベルを基に権限情報を作成する。制限レベルが低い程、より高い権限を持つ。
     * @param restriction 制限レベル [0以上] (-1: 最低権限)
     * @return
     */
    private static List<GrantedAuthority> makeAuthorities(Integer restriction) {
    	List<String> roles = new LinkedList<>();
    	/* hasRole(String)では自動的に"ROLE_"が付加されるので注意 */
    	switch(restriction == null ? -1 : restriction) {
    	case 0:
    		roles.add("ROLE_ADMINISTRATOR");
    	default:
    		roles.add("ROLE_CONTRIBUTOR");
    		break;
    	}
 
    	return AuthorityUtils.createAuthorityList(roles.toArray(new String[roles.size()]));
    }
 
    public boolean hasRole(String role) {
    	return hasAuthority("ROLE_" + role);
    }
    
    public boolean hasAuthority(String authority) {
    	return this.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authority));
    }
    
    public String toJsonString() {
    	StringJoiner sj = new StringJoiner(",");
		getAuthorities().stream().forEach(a -> sj.add(String.format("\"%s\"", a.toString())));
		
		return String.format("{\"username\":\"%s\",\"roles\":[%s]}", user.getUsername(), sj.toString());
    }
    
    public Set<String> getAuthorityStrings() {
    	return getAuthorities().stream().map(a -> a.toString()).collect(Collectors.toSet());
    }
}
