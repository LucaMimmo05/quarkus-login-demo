package login;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("login")
public class LoginResource {
    private final Template login;
    private final UserSessionService userSessionService;

    public LoginResource(Template login, UserSessionService userSessionService) {
        this.login = login;
        this.userSessionService = userSessionService;
    }

    @GET
    public TemplateInstance drawLogin(){
        return login.instance();
    }

    @POST
    public Response processLogin(@FormParam("email") String email, @FormParam("password") String password){
        System.out.println(email + " " + password);

        String idSession = userSessionService.login(email, password);

        if(idSession != null){
            return Response.seeOther(URI.create("/profile"))
                    .cookie(new NewCookie.Builder("UTENTE").value(idSession).build())
                    .build();
        } else {
            return Response.status(401).entity(login.instance()).build();
        }

    }
}
