/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebitaapiodoo2;

import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author AlumnadoTarde
 */
public class PruebitaApiOdoo2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException {
        final XmlRpcClient client = new XmlRpcClient();
        final String url = "http://192.168.103.53:8069",
                db = "DBRellena",
                username = "carloshernandezarencibia@alumno.ieselrincon.es",
                password = "1234";

        final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
        common_config.setServerURL(
                new URL(String.format("%s/xmlrpc/2/common", url)));
        try {
            System.out.println(client.execute(common_config, "version", emptyList()));
            int uid = (int) client.execute(
                    common_config, "authenticate", asList(
                            db, username, password, emptyMap()));
            System.out.println(uid);

            final XmlRpcClient models = new XmlRpcClient() {
                {
                    setConfig(new XmlRpcClientConfigImpl() {
                        {
                            setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
                        }
                    });
                }
            };

            System.out.println(models.execute("execute_kw", asList(
                    db, uid, password,
                    "x_municipios", "check_access_rights",
                    asList("read"),
                    new HashMap() {
                {
                    put("raise_exception", false);
                }
            }
            )));

            System.out.println(asList((Object[]) models.execute("execute_kw", asList(
                    db, uid, password,
                    "x_municipios", "search_read",
                    asList(asList(
                            
                            asList("x_name", "=", "Gato"))),
                    new HashMap() {
                {
                    put("fields", asList("x_name","x_superficie", "x_habitantes"));
                    
                }
            }
            ))));

        } catch (XmlRpcException ex) {
            Logger.getLogger(PruebitaApiOdoo2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
