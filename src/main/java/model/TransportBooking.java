package model;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "TransportBookings")
public class TransportBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransportBookings_ID", nullable = false)
    private Integer id;

    @Column(name = "TotalMoney")
    private float totalMoney;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Users_ID", nullable = false)
    private User users;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "Transportations_ID")
    private Transportation transportation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Schedules_ID")
    private Schedule schedules;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Flight_ID")
    private Flight plane;

    @OneToMany(mappedBy = "bookings")
    private Set<PassengerInformation> passengerInformations = new LinkedHashSet<>();

    public Set<PassengerInformation> getPassengerInformations() {
        return passengerInformations;
    }

    public void setPassengerInformations(Set<PassengerInformation> passengerInformations) {
        this.passengerInformations = passengerInformations;
    }

    public Flight getPlane() {
        return plane;
    }

    public void setPlane(Flight plane) {
        this.plane = plane;
    }

    public Schedule getSchedules() {
        return schedules;
    }

    public void setSchedules(Schedule schedules) {
        this.schedules = schedules;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUsers() {
        return users;
    }


    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }
}