package com.tap.dao;

import com.tap.model.DeliveryAgent;
import java.util.List;

public interface DeliveryAgentDAO {
    boolean registerAgent(DeliveryAgent agent);
    DeliveryAgent getAgentByEmail(String email);
    DeliveryAgent getAgentById(int agentId);
    List<DeliveryAgent> getAllAgents();
    List<DeliveryAgent> getAvailableAgents();
    boolean updateAvailability(int agentId, boolean available);
    boolean markVerified(String email);
    boolean updatePassword(String email, String newHashedPassword);
    boolean updateAgent(DeliveryAgent agent);
    boolean deleteAgent(int agentId);
    boolean emailExists(String email);
}
