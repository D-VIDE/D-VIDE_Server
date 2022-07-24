package com.divide.post;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Point {
    private int x;
    private int y;

    protected Point() { //JPA 스펙상 리플렉션, 프록시 기술 적용하기 위함
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


