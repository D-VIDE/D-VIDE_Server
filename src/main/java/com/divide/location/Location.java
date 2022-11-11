package com.divide.location;

import com.divide.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private Double latitude;
    private Double longitude;
}
