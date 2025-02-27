package login;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/profile")
public class ProfileResource {
    private final Template profile;
    private final UserSessionService userSessionService;

    public ProfileResource(Template profile, UserSessionService userSessionService) {
        this.profile = profile;
        this.userSessionService = userSessionService;
    }

    @GET
    public Response drawProfile(@CookieParam("UTENTE") String idSession){
        String email = userSessionService.getUserFromSession(idSession);

        if (email == null) {
            return Response.status(401).build();
        }

        return Response.ok(profile.data("utente", email)).build();
    }
}
