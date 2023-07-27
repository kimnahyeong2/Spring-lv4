package com.sparta.springlv4.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="category")
    private Long category;
    @Column(name = "cateId")
    private Long cateId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Like(User user, Long id, Long category) {
        this.user = user;
        this.cateId = id;
        this.category = category;
    }
}
