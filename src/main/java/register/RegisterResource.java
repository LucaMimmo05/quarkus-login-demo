package register;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import login.UserSessionService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

@Path("register")
public class RegisterResource {
    private final Template register;


    public RegisterResource(Template register, UserSessionService userSessionService) {
        this.register = register;
    }

    @GET
    public TemplateInstance drawRegister() {
        return register.instance();
    }

    @POST
    public Response processRegister(@FormParam("email") String email, @FormParam("password") String password){
        System.out.println(email + " " + password);

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email e password sono obbligatorie.")
                    .build();
        }

        // * Password criptata
        /*String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());*/


        try(FileWriter fileWriter = new FileWriter("user.txt",true)){
                fileWriter.write(email + "," + password + "\n");
                return Response.seeOther(URI.create("/login")).build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
