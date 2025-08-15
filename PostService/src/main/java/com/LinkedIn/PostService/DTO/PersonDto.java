package com.LinkedIn.PostService.DTO;

public class PersonDto {
    private Long userId;
    private String name;

    public PersonDto() {
    }

    public PersonDto(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
