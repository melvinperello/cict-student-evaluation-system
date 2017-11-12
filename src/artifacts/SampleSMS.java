/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artifacts;

import com.jhmvin.Mono;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;

/**
 *
 * @author Jhon Melvin
 */
public class SampleSMS {

    public static void main(String[] args) {
        //Non Blocking
        SMSWrapper.send("+639368955866", "Hellow World", WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()), response -> {
            System.out.println("RESPONSE: " + response);
        });

        // Blocking When Used in Loops
        String res = SMSWrapper.send("+639368955866", "Hellow World", WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()));
        System.out.println(res);

        System.out.println("Sa Huli");
    }
}
