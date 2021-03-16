package com.haroldgao.projects.user.management;

/**
 *
 */
public interface AuthorMXBean {

    public String getName();

    public void setName(String name);

    public String getEmail();

    public void setEmail(String email);

    public String getGithub();

    public void setGithub(String github);

    public Author.Hobbies getHobbies();

    public void setHobbies(Author.Hobbies hobbies);
}
