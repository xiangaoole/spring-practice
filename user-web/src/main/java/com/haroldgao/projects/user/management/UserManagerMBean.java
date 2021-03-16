package com.haroldgao.projects.user.management;

public interface UserManagerMBean {
    public Long getId();

    public void setId(Long id);

    public String getName();

    public void setName(String name);

    public String getPassword();

    public void setPassword(String password);

    public String getEmail();

    public void setEmail(String email);

    public String getPhoneNumber();

    public void setPhoneNumber(String phoneNumber);
}
