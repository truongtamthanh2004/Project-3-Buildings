package com.javaweb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "assignmentbuilding")
public class AssignmentBuildingEntity {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;

    @ManyToOne
    @JoinColumn(name="staffid")
    private UserEntity staff;

    @ManyToOne
    @JoinColumn(name="buildingid")
    private BuildingEntity building;

    @Column(name="createddate")
    private Date createdDate;

    @Column(name="modifieddate")
    private Date modifiedDate;

    @Column(name="createdby")
    private String createdBy;

    @Column(name="modifiedby")
    private String modifiedBy;
}
