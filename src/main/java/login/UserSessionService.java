package login;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class UserSessionService {
    private final Map<String,String> sessionMap = new HashMap<>();
    private String idSessionGenerated = null;


    public UserSessionService() {
        loadSessions();
    }

    /***
     * Esegue operazione di login
     * @param email email inserita dall'utente
     * @param password password inserita dall'utente
     * @return ritorna idSessionGenerated se il login va a buon fine, altrimenti null
     */
    public String login(String email, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("user.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }

                String fileEmail = parts[0].trim();
                String filePassword = parts[1].trim();

                if (fileEmail.equals(email) && filePassword.equals(password)) {
                    idSessionGenerated = UUID.randomUUID().toString();
                    if (!sessionMap.containsKey(idSessionGenerated)) {
                        sessionMap.put(idSessionGenerated, email);
                        saveSessions();
                    }
                    return idSessionGenerated;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /***
     * Recupera l'utente dalla sessione
     * @param session ID sessione
     * @return email dell'utente se la sessione è valida, altrimenti null
     */
    public String getUserFromSession(String session) {
        if (isSessionValid(session)) {
            return sessionMap.get(session);
        }
        return null;
    }

    /***
     * Effettua il logout
     * @param idSession ID della sessione da invalidare
     */
    public void logout(String idSession) {
        if (isSessionValid(idSession)) {
            sessionMap.remove(idSession);
            saveSessions();
        }
    }

    /***
     * Controlla se la sessione è valida
     * @param idSession ID della sessione
     * @return true se la sessione è valida, false altrimenti
     */
    public boolean isSessionValid(String sessionId) {
        return sessionMap.containsKey(sessionId);
    }


    /***
     * Salva la sessione all'interno del file
    * */
    private void saveSessions() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("session.txt"))) {
            for (Map.Entry<String, String> entry : sessionMap.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Carica le sessioni
     */
    private void loadSessions() {
        File file = new File("session.txt");
        if (!file.exists()){
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }

                sessionMap.put(parts[0].trim(), parts[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
