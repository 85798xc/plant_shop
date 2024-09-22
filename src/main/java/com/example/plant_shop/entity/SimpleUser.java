package com.example.plant_shop.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SU")
public class SimpleUser extends User{

}
