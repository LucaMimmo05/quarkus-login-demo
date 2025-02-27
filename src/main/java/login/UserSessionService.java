package login;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserSessionService {
    private String idSessionGenerated = null;

    public String login(String email, String password){
        if ("vlad@gmail.com".equals(email) && "password".equals(password)){
            double idSession = Math.random() * 3000.0;
            idSessionGenerated = String.valueOf(idSession);
            return idSessionGenerated;
        } else {
            return null;
        }
    }

    public String getUserFromSession(String session){
        if (isSessionValid(session)) {
            return "vlad@gmail.com";
        } else {
            return null;
        }
    }

    public void logout(String idSession){
        if (isSessionValid(idSession)) {
            idSessionGenerated = null;
        }
    }

    private boolean isSessionValid(String idSession) {
        return idSession != null && !idSession.isEmpty() && idSession.equals(idSessionGenerated);
    }
}
