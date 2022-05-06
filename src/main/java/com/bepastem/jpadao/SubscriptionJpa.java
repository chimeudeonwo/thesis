package com.bepastem.jpadao;

import com.bepastem.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionJpa extends JpaRepository<Subscription, String> {
}