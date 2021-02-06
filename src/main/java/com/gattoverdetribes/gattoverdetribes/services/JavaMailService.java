package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class JavaMailService {

  @Autowired
  private JavaMailSender javaMailSender;

  public void sendConfirmationMail(String recepient, String token) throws Exception {

    MimeMessage msg = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, false);
    helper.setTo(recepient);
    helper.setSubject("GattoVerde Tribes Confirm Registration");
    helper.setText("<h1> Thanks for signing up with GattoVerde Tribes!\n"
            + "You must follow this link to activate your account: </h1> <br/> <h2><a href=\""
            + System.getenv("HOST_URL") + System.getenv("SERVER_PORT") + ""
            + "/confirm-account?token=" + token + "\">Activate!<a/></h2> <br/>"
            + "<h3><b> Have fun, and don't hesitate to contact us "
            + "with your feedback.</b></h3>  <br/><br/>"
            + "<h3><strong>The GattoVerde Team\n <strong></h3>"
            + "<h2><a href=\"https://gattoverde-tribes.herokuapp.com/\">"
            + "https://gattoverde-tribes.herokuapp.com/<a/><h2>",
        true);
    javaMailSender.send(msg);
  }

  public void sendKingdomAttackedMail(String recepient, Kingdom kingdom) throws Exception {

    MimeMessage msg = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, false);
    helper.setTo(recepient);
    helper.setSubject("GattoVerde Tribes - Kingdom Attcked!");
    helper.setText("<h1> Someone attacked your kingdom!</h1> <br/> <h2> A kingdom "
        + "named " + kingdom.getName()
        + " has attacked your kingdom. Please login to your account and "
        + "take the necessary steps my lord! You can just click on the link below.</h2> <br/>"
        + "<h3><b> Have fun, and don't hesitate to contact us "
        + "with your feedback.</b></h3>  <br/><br/>"
        + "<h3><strong>The GattoVerde Team\n <strong></h3>"
        + "<h2><a href=\"https://gattoverde-tribes.herokuapp.com/\">"
        + "https://gattoverde-tribes.herokuapp.com/<a/><h2>", true);
    javaMailSender.send(msg);
  }
}