package com.app.swiftcodesproject.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "swift_codes")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCode {

    @Id
    private String swiftCode;
    private String bankName;
    private String address;
    private String countryISO2;
    private String countryName;
    @Column(name = "is_headquarter")
    private boolean isHeadquarter;

    @ManyToOne(optional = true)
    @JoinColumn(name = "headquarter_id")
    private SwiftCode headquarter;

    public void setHeadquarterBranch(SwiftCode headquarter) {
        this.headquarter = headquarter;
    }
}
