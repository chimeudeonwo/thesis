package com.bepastem.dao;

import com.bepastem.models.Victim;
import com.bepastem.models.VictimEmResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntityFactoryService {

    @Autowired
    EntityManagerFactory emf;

    public EntityManager getEmf(){
        return emf.createEntityManager();
    }

    public Victim getUserByUsernameAndPassword(String username, String password){
        Victim user = null;
        try{
            EntityManager em = getEmf();
            em.getTransaction().begin();
            TypedQuery<Victim> hql = em.createQuery("SELECT u FROM USERS u WHERE u.username = ?1 AND u.password = ?2", Victim.class);   //insert into user (id, name) values (?, ?)
            hql.setParameter(1, username);
            hql.setParameter(2, password);
            user = (Victim) hql.getSingleResult();
            em.getTransaction().commit();
        } catch(EntityExistsException e) {
            e.printStackTrace();
        }
        return user;
    }

    public VictimEmResponse[] getVictimEmResponseByAgencyIdAndCurrentStatus(String currentStatus, long agencyId){
        List<VictimEmResponse> victimEmResponseList = new ArrayList<VictimEmResponse>();
        try{
            EntityManager em = getEmf();
            em.getTransaction().begin();
            TypedQuery<VictimEmResponse> hql = em.createQuery("SELECT response FROM VICTIMEMRESPONSE response WHERE response.currentStatus = ?1 " +
                    "AND response.agencyAlerted_agencyId = ?2", VictimEmResponse.class);   //insert into user (id, name) values (?, ?)
            hql.setParameter(1, currentStatus);
            hql.setParameter(2, agencyId);
            victimEmResponseList = hql.getResultList();
            em.getTransaction().commit();
        } catch(EntityExistsException e) {
            e.printStackTrace();
        }
        //convert to array
        VictimEmResponse[] victimEmResponsesArr = new VictimEmResponse[victimEmResponseList.size()];
        return victimEmResponseList.toArray(victimEmResponsesArr);

    }

    public Victim getUserByUsername(String username){
        Victim user = null;
        try{
            EntityManager em = getEmf();
            em.getTransaction().begin();
            TypedQuery<Victim> hql = em.createQuery("SELECT u FROM USERS u WHERE u.username = ?1", Victim.class);   //insert into user (id, name) values (?, ?)
            hql.setParameter(1, username);
            user = (Victim) hql.getSingleResult();
            em.getTransaction().commit();
        } catch(EntityExistsException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<Object[]> getUserByRoleCategoryWithNativeQuery(String roleName){
        List<Object[]> user = new ArrayList<>();
        try{
            EntityManager em = getEmf();
            em.getTransaction().begin();
            Query hql = em.createNativeQuery("SELECT  u.userId, u.firstname, u.lastname, u.dob, u.username, u.password, u.email, u.street, u.lga, u.state, " +
                    "u.country, u.phonenumber, uinfo.videoId, uinfo.imgId, uinfo.bookReserveId, r.tableId, r.roleId, r.name, u_role.USERINFO_userId, " +
                    "u_role.roles_tableId FROM user u INNER JOIN userinfo uinfo ON uinfo.userId = u.userId INNER JOIN userinfo_roles u_role " +
                    "ON u_role.USERINFO_userId = uinfo.userId INNER JOIN role r ON r.tableId = u_role.roles_tableId WHERE r.name = ?1");   //insert into user (id, name) values (?, ?)
            hql.setParameter(1, roleName);
            user =  hql.getResultList();
            em.getTransaction().commit();
        } catch(EntityExistsException e) {
            e.printStackTrace();
        }

        return user;
    }
}
