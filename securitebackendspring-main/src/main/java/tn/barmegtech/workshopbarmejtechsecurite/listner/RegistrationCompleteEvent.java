package tn.barmegtech.workshopbarmejtechsecurite.listner;

import lombok.Getter;
import lombok.Setter;
import tn.barmegtech.workshopbarmejtechsecurite.entites.User;

import org.springframework.context.ApplicationEvent;




@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
   private User user;
   private String applicationUrl;


   public RegistrationCompleteEvent(User user, String applicationUrl) {
       super(user);
       this.user = user;
       this.applicationUrl = applicationUrl;
   }
}

