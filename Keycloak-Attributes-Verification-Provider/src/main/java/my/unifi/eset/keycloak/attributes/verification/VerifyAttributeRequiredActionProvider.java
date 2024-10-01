package my.unifi.eset.keycloak.attributes.verification;


import org.hibernate.query.Query;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.UserModel;
import org.keycloak.models.jpa.entities.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import my.unifi.eset.keycloak.attributes.verification.jpa.UAVerificationEntity;
import my.unifi.eset.keycloak.attributes.verification.jpa.UAVerificationStatus;

import java.util.List;

public class VerifyAttributeRequiredActionProvider implements RequiredActionProvider {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void close() {
        // No-op, as we're using a container-managed EntityManager
    }

    @Override
    public void evaluateTriggers(RequiredActionContext context) {
        if (getPendingVerificationEntity(context.getUser()) != null) {
            context.getUser().addRequiredAction(context.getAction());
        } else {
            context.getUser().removeRequiredAction(context.getAction());   
        }
    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        UAVerificationEntity uav = getPendingVerificationEntity(context.getUser());
        if (uav != null) {
        } else {
            throw new UnsupportedOperationException("No pending verification entity found");
        }
    }

    @Override
    public void processAction(RequiredActionContext context) {
    UAVerificationEntity uav = getPendingVerificationEntity(context.getUser());
    if (uav != null) {
        // Update the verification entity status to "COMPLETED"
        uav.setStatus(UAVerificationStatus.COMPLETED); // or new UAVerificationStatus("COMPLETED")
        entityManager.merge(uav);
        entityManager.flush();
        context.getUser().removeRequiredAction(context.getAction());
        } else {
            throw new UnsupportedOperationException("No pending verification entity found");
        }
    }

    UAVerificationEntity getPendingVerificationEntity(UserModel userModel) {
        String id = userModel.getId();
        UserEntity user = entityManager.find(UserEntity.class, id);
        if (user != null) {
            @SuppressWarnings("rawtypes")
            Query query = (Query) entityManager.createQuery("SELECT uav FROM UAVerificationEntity uav WHERE uav.user = :user AND uav.STATUS_VALUE <> 'COMPLETED' ORDER BY uav.id");
            query.setParameter("user", user);
            @SuppressWarnings("unchecked")
            List<UAVerificationEntity> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } else {
            return null;
        }
    }
}