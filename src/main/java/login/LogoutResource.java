package login;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("logout")
public class LogoutResource {
    private final UserSessionService userSessionService;

    public LogoutResource(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @POST
    public Response logout(@CookieParam("UTENTE") String idSession){
        userSessionService.logout(idSession);

        return Response.seeOther(URI.create("/login"))
                .cookie(new NewCookie.Builder("UTENTE").value(null).build())
                .build();
    }
}
