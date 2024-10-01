package my.unifi.eset.keycloak.attributes.verification;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class VerifyAttributeRequiredActionProviderFactory implements RequiredActionFactory {

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        // TODO Auto-generated method stub
        return new VerifyAttributeRequiredActionProvider();
    }

    @Override
    public void init(Scope config) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return "Verify";
    }

    @Override
    public String getDisplayText() {
        // TODO Auto-generated method stub
        return "Meow";
    }
    
}
