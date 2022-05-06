package com.bepastem.jpadao;

import com.bepastem.models.Geolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeolocationJpa extends JpaRepository<Geolocation, String> {

    @Query("Select currentLocation from GEOLOCATION currentLocation " +
            "where currentLocation.time = (select max(geo.time) from GEOLOCATION geo where geo.geolocationId_userId = ?1)" )
    Geolocation getCurrentVictimLocation(long userId);

    /** returns list of top matched near location agencies id's.*/
    @Query("SELECT geo FROM GEOLOCATION geo WHERE geo.longitude >= ?1 and geo.longitude <= ?1 ORDER BY ?2")
    List<Geolocation> findSecAgencyGeolocationByVictimLocation(String victimLocationPlus100, String victimLocation);
}
