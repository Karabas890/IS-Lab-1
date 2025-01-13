package controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Named
@SessionScoped
@Getter
@Setter
public class ProductController implements Serializable {
    public String figma() {
        System.out.println("figma moment: ");return null;
    }
}

