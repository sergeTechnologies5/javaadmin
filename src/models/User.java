/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author oXCToo
 */
public class User {
    public User(String id, String email, String role, String username, String lastname, String firstname) {
        this.email = email;
        this.id = id;
        this.role = role;
        this.username = username;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    private String id;

    private String email;

    private String role;

    private String username;

    private String lastname;

    private String firstname;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getLastname ()
    {
        return lastname;
    }

    public void setLastname (String lastname)
    {
        this.lastname = lastname;
    }

    public String getFirstname ()
    {
        return firstname;
    }

    public void setFirstname (String firstname)
    {
        this.firstname = firstname;
    }

    @Override
    public String toString()
    {
        return "id = "+id+", email = "+email+", role = "+ role +", username = "+ username +", lastname = "+lastname+", firstname = "+firstname+"";
    }
}