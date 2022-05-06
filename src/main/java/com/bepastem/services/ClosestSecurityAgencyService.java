package com.bepastem.services;

import com.bepastem.jpadao.GeolocationJpa;
import com.bepastem.jpadao.SecurityAgencyJpa;
import com.bepastem.models.Geolocation;
import com.bepastem.models.SecurityAgency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ClosestSecurityAgencyService {
    @Autowired
    private GeolocationJpa geolocationJpa;

    @Autowired
    private SecurityAgencyJpa securityAgencyJpa;

    public ClosestSecurityAgencyService() {
    }

    /**
     * . Calculating the distance between the victims location and the security agencies in the same city.
     * @return top three security agencies closest to the vitim location.
     */
    public List<SecurityAgency> topThreeClosestSecAgencies(Geolocation victimLocation) {

        double victimLat = Double.parseDouble(victimLocation.getLatitude());
        double victimLng = Double.parseDouble(victimLocation.getLongitude());

        var listOfAllSecAgenciesGeolocationInACity =
                getSecurityAgenciesGeolocationById(getAllSecurityAgenciesInACity(victimLocation.getCity()));

        //TODO: if no agency is found by city, check get user lon and lat and compare it to the closest lat and lon with sec agency lat and lon
        // the method will come here

        // map holding security agency to its location distance relative to the user location.
        HashMap<Long, Double> computedDistance = new HashMap<>();

        // run through the listOfGeolocationOfEachAgency, compute their respective locations with victim location and save them to a map
        for(Geolocation secLoc : listOfAllSecAgenciesGeolocationInACity) {
            computedDistance.put(secLoc.getGeolocationId_userId(),
                    calculateDistance(victimLat, victimLng, Double.parseDouble(secLoc.getLatitude()), Double.parseDouble(secLoc.getLongitude())));
        }

        // sort the agencies according the location and return top three nearest location
        return getTopThreeSecurityAgenciesGeolocation(computedDistance);
    }

    private SecurityAgency[] getAllSecurityAgenciesInACity(String cityName) {
        return securityAgencyJpa.getAgencyWithTown(cityName);
    }

    private List<Geolocation> getSecurityAgenciesGeolocationById(SecurityAgency[] securityAgencies) {
        List<Geolocation> list = new ArrayList<>();
        for(SecurityAgency sec : securityAgencies){
            list.add(geolocationJpa.getById(sec.getGeolocationId()));
        }
        return  list;
    }

    /** List of top three objects of the sorted map.
     * @param distanceComputed - the sorted hashmap of security agency's userId and their distances relative to the user location.
     * @return list of top three security agencies closest to the victim's location.
     */
    private List<SecurityAgency> getTopThreeSecurityAgenciesGeolocation(HashMap<Long, Double> distanceComputed) {
        var sortedDistance = sortMapByComputedDistance(distanceComputed);

        List<SecurityAgency> topThree = new ArrayList<>();
        int count = 0;

        for(Map.Entry<Long, Double> entry : sortedDistance.entrySet()){
            if(count < 3) {
                topThree.add(securityAgencyJpa.getAgencyByAgencyId_userId(entry.getKey()));
                count++;
            }
            else break;
        }
        return  topThree;
    }

    /** Sorts hashmap
     * @param distanceComputed - the hashmap of security agency's userId and their distances relative to the user location.
     * @return sorted map.
     */
    private HashMap<Long, Double> sortMapByComputedDistance(HashMap<Long, Double> distanceComputed) {
        var res = distanceComputed.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return (HashMap<Long, Double>) res;
    }

    /**
     * calculates the distance between two locations in Kilometer.
     *
     */
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; // in km, change to 3958.75 for miles output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sin_dLat = Math.sin(dLat / 2);
        double sin_dLng = Math.sin(dLng / 2);

        double a = Math.pow(sin_dLat, 2) + Math.pow(sin_dLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c; // output distance, in KILOMETER
    }

}
